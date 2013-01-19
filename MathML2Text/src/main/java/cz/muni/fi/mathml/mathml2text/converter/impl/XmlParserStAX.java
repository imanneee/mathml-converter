package cz.muni.fi.mathml.mathml2text.converter.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.MathMLElement;
import cz.muni.fi.mathml.mathml2text.converter.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.XmlAttribute;

/**
 * This parser is responsible for reading input file or string and invoking
 * {@link MathMLConverter converter} for every occurence of MathML code block
 * in the input.
 * Resulting output format depends on the type of input:
 * <ul>
 *  <li>XML file &rarr; output is an XML file in which every MathML node is substituted
 *      with converted value,</li>
 *  <li>string &rarr; 
 *      <ol>
 *          <li>if input string is just MathML element (parent node of XML is 
 *              <code>&lt;math&gt;</code>), output will consist only of the 
 *              converted string without any XML tags
 *          </li>
 *          <li>otherwise result will be similar to the case of input XML file,
 *              but instead of to file, the result will be written as string
 *              and outputted.
 *          </li>
 * </ul>
 * 
 * @todo extract interface
 * 
 * @author Maros Kucbel Oct 31, 2012, 19:07:12 PM
 */
public final class XmlParserStAX {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(XmlParserStAX.class);
    /**
     * Responsible for converting MathML tree to string.
     */
    private MathMLConverter converter;
    /**
     * Selected language of transformation.
     */
    private Locale language;
    /**
     * Input factory for creating {@link XMLStreamReader} instance.
     */
    private XMLInputFactory xmlInputFactory;
    /**
     * Output factory for creating {@link XMLStreamWriter} instance.
     */
    private XMLOutputFactory xmlOutputFactory;
    
    /**
     * Constructor.
     * Delelegates to {@link #XmlParserStAX(java.util.Locale) } with parameter
     * value {@link Locale#ENGLISH}.
     */
    public XmlParserStAX() {
        this(Locale.ENGLISH);
    }
    
    /**
     * Constructor.
     * {@link XMLInputFactory} is configured with values:
     * <ul>
     *  <li>{@link XMLInputFactory#IS_REPLACING_ENTITY_REFERENCES} &rarr <code>true</code></li>
     *  <li>{@link XMLInputFactory#IS_SUPPORTING_EXTERNAL_ENTITIES} &rarr <code>true</code></li>
     *  <li>{@link XMLInputFactory#IS_COALESCING} &rarr <code>true</code></li>
     *  <li>{@link XMLInputFactory#IS_VALIDATING} &rarr <code>false</code></li>
     *  <li>{@link XMLInputFactory#SUPPORT_DTD} &rarr <code>true</code></li>
     * </ul>
     * @param language Language of transformation.
     */
    public XmlParserStAX(Locale language) {
        this.converter = new MathMLConverter();
        this.language = language;
        this.xmlInputFactory = XMLInputFactory.newInstance();
        this.xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
        this.xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
        this.xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
        this.xmlInputFactory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        this.xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, true);
        this.xmlInputFactory.setProperty(XMLInputFactory.RESOLVER, new XMLResolver() {
            @Override
            public Object resolveEntity(String publicID, String systemID, String baseURI, String namespace) throws XMLStreamException {
                if (systemID.endsWith("dtd")) {
                        return XmlParserStAX.class.getResourceAsStream("xhtml-math11-f.dtd");
                }
                return null;
            }
        });
        this.xmlOutputFactory = XMLOutputFactory.newInstance();
    }
    
    /**
     * Returns logger for this class.
     */
    private Logger getLogger() {
        return this.logger;
    }

    /**
     * Parses single file. For every occurrence of math element inside input 
     * XML file new {@link MathMLNode} tree is builded and subsequently converted
     * to string.
     * 
     * @param file Input XML file.
     * @return A file with every occurence of <code>&lt;math&gt;</code> tag replaced
     *  with converted string inside <code>&lt;mathconv&gt;</code> tag.
     */
    public File parse(@Nonnull final File file) {
        Validate.isTrue(file != null, "File for transformation should not be null.");
        
        XMLStreamReader reader;
        XMLStreamWriter writer;
        String filePath = file.getPath();
        String outputFilePath = filePath.substring(0, filePath.lastIndexOf('.'));
        File outputFile = new File(outputFilePath + "-transformed.xml");
        try {
            outputFile.createNewFile();
        } catch (IOException ex) {
            this.getLogger().warn("File [{}] already exists.", outputFile.getPath());
        }
        
        /** root node */
        MathMLNode tree = null;
        /** current node of reader */
        MathMLNode currentNode = null;
        /** parent node of current node */
        MathMLNode parentNode = null;
        /** type of current element */
        MathMLElement currentElement = null;
        
        try {
            // create stream reader from input file
            reader = this.xmlInputFactory.createXMLStreamReader(new FileInputStream(file), "UTF-8");
            OutputStream output = new FileOutputStream(outputFile);
            // create stream writer
            writer = this.xmlOutputFactory.createXMLStreamWriter(output, "UTF-8");
//            writer = this.xmlOutputFactory.createXMLStreamWriter(System.out, "UTF-8");
            writer.writeStartDocument(reader.getEncoding(), reader.getVersion());
            
            // indicates whether we are inside a math element
            boolean processingMathMLElement = false;
            while (reader.hasNext()) {
                // pull next event code
                final int eventCode = reader.next();
                // determine type of event
                final XmlStreamConstant constant = XmlStreamConstant.forEventCode(eventCode);

                // check whether we are inside math element, if not and current event is not
                // START_ELEMENT continue to the next event
                if (!processingMathMLElement && !XmlStreamConstant.START_ELEMENT.equals(constant)) {
                    switch (constant) {
                        case END_ELEMENT: {
                            writer.writeEndElement();
                            break;
                        }
                        case CHARACTERS: {
                            writer.writeCharacters(reader.getText());
                            break;
                        }
                        case DTD: {
                            writer.writeDTD(reader.getText());
                        }
                        default:
                            break;
                    }
                    continue;
                }

                switch (constant) {
                    case START_ELEMENT: {
                        // retrieve element name
                        final String elementName = reader.getLocalName();
                        // if we are not inside math element continue to the next event
                        if (!processingMathMLElement && !MathMLElement.MATH.getElementName().equals(elementName)) {
                            writer.writeStartElement(reader.getName().getPrefix(), reader.getLocalName(), reader.getName().getNamespaceURI());
                            for (int index = 0; index < reader.getAttributeCount(); ++index) {
                                writer.writeAttribute(reader.getAttributeLocalName(index), reader.getAttributeValue(index));
                            }
                            for (int index = 0; index < reader.getNamespaceCount(); ++index) {
                                writer.writeNamespace(reader.getNamespacePrefix(index), reader.getNamespaceURI(index));
                            }
                            continue;
                        }
                        
                        // now we are surely inside math element, we can determine type of math element
                        currentElement = MathMLElement.forElementName(elementName);
                        processingMathMLElement = true;
                        // set new current node
                        currentNode = new MathMLNode();
                        // set its name
                        currentNode.setType(currentElement);
                        // set its attributes if there are any
                        for (int index = 0; index < reader.getAttributeCount(); ++index) {
                            currentNode.getAttributes().add(
                                    new XmlAttribute(reader.getAttributeLocalName(index), 
                                                     reader.getAttributeValue(index)));
                        }
                        // if parent node was set, set it to current node
                        if (parentNode != null) {
                            currentNode.setParent(parentNode);
                            parentNode.getChildren().add(currentNode);
                        }

                        // if we are just at math element initialize new tree
                        if (tree == null) {
                            tree = currentNode;
                        }

                        parentNode = currentNode;
                        break;
                    }
                    case END_ELEMENT: {
                        // we are going "one level up" inside the tree
                        parentNode = currentNode.getParent();
                        currentNode = parentNode;

                        //@todo it should be enough to check whether currentNode is not null
                        final String elementName = reader.getLocalName();
                        final MathMLElement element = MathMLElement.forElementName(elementName);
                        switch (element) {
                            case MATH: {
                                // transform created tree and write to output
                                String converted = this.converter.convert(tree, this.language);
                                writer.writeStartElement("mathconv");
                                writer.writeCharacters(converted);
                                writer.writeEndElement();
                                processingMathMLElement = false;
                                currentElement = null;
                                tree = null;
                                currentNode = null;
                                parentNode = null;
                                break;
                            }
                            default:
                                break;
                        }

                        break;
                    }
                    case CHARACTERS: {
                        final String value = reader.getText();
                        if (currentElement != null && StringUtils.isNotBlank(value)) {
                            currentNode.setValue(value);
                        }
                        break;
                    }  
                    case ENTITY_REFERENCE: {
                        final String value = reader.getLocalName();
                        if (currentElement != null && StringUtils.isNotBlank(value)) {
                            currentNode.setValue(value);
                        }
                        break;
                    }
                    case COMMENT: {
                        // comment in input file is ignored, no comment event is received...
                        writer.writeComment(reader.getText());
                        break;
                    }    
                    case DTD: {
                        writer.writeDTD(reader.getText());
                    }
                    default:
                        break;
                }
            }
            reader.close();
            writer.flush();
            writer.close();
            output.flush();
            output.close();
            
        } catch (final IOException ex) {
            this.getLogger().error("Cannot load input file.", ex);
        } catch (final XMLStreamException ex) {
            this.getLogger().error("Cannot open xml file for reading.", ex);
        }
        return outputFile;
    }
    
}
