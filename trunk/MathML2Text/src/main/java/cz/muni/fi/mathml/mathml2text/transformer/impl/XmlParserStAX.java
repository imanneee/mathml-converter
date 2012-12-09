package cz.muni.fi.mathml.mathml2text.transformer.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;

import cz.muni.fi.mathml.MathMLElement;

/**
 * Parser for creating {@link MathMLNode} trees from input XML files.
 * Input files are read via Woodstox ({@linkplain http://woodstox.codehaus.org})
 * implementation of StAX API.
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
    
    private MathMLConverter converter;
    private Locale language;
    
    private WstxInputFactory xmlInputFactory;
    private WstxOutputFactory xmlOutputFactory;
    
    public XmlParserStAX() {
        this(Locale.ENGLISH);
    }
    
    public XmlParserStAX(Locale language) {
        this.converter = new MathMLConverter();
        this.language = language;
        this.xmlInputFactory = (WstxInputFactory) XMLInputFactory.newInstance();
        this.xmlInputFactory.getConfig().doSupportDTDs(false);
        this.xmlInputFactory.getConfig().doCacheDTDs(true);
        this.xmlInputFactory.getConfig().doReplaceEntityRefs(false);
        this.xmlOutputFactory = (WstxOutputFactory) XMLOutputFactory.newInstance();
        this.xmlOutputFactory.getConfig().doAddSpaceAfterEmptyElem(true);
    }
    
    /**
     * Returns logger for this class.
     */
    private Logger getLogger() {
        return this.logger;
    }

    /**
     * Parses collection of files into the list of {@link MathMLNode} trees. 
     * Every occurrence of math element inside any file is parsed as new instance
     * of {@link MathMLNode} tree.
     * 
     * @todo change return type, maybe accept only single file. As it is now
     * the information about from which file given tree comes is lost.
     * 
     * @param filesToTransform
     * @return 
     */
    public List<MathMLNode> parse(@Nonnull final Collection<File> filesToTransform) {
        Validate.isTrue(filesToTransform != null, "List of files for transformation should not be null.");
        Validate.notEmpty(filesToTransform, "List of files for transformation should not be empty.");
        Validate.noNullElements(filesToTransform, "List of files for transformation should not contain null elements.");

        final List<MathMLNode> nodeList = new ArrayList<MathMLNode>();
        
        // process every file
        for (final File file : filesToTransform) {
            nodeList.addAll(this.parse0(file));
        }
        return nodeList;
    }

    /**
     * Parses single file. For every occurrence of math element inside input 
     * XML file new {@link MathMLNode} tree is builded. 
     * 
     * @param file Input XML file.
     * @return List of {@link MathMLNode} trees.
     */
    public File parse(@Nonnull final File file) {
        Validate.isTrue(file != null, "File for transformation should not be null.");
        
        XMLStreamReader reader;
        XMLStreamWriter writer;
        String filePath = file.getPath();
        String outputFilePath = filePath.substring(0, filePath.lastIndexOf('.'));
        File outputFile = new File(outputFilePath + "-transformed.xml");
        try {
            System.out.println(outputFile.createNewFile());
        } catch (IOException ex) {
            System.out.println("File already exists.");
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
            final FileReader input = new FileReader(file);
            reader = this.xmlInputFactory.createXMLStreamReader(input);
            FileWriter output = new FileWriter(outputFile);
//            writer = this.xmlOutputFactory.createXMLStreamWriter(output);
            writer = this.xmlOutputFactory.createXMLStreamWriter(System.out);

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
                            writer.writeStartElement(reader.getLocalName());
                            for (int index = 0; index < reader.getAttributeCount(); ++index) {
                                writer.writeAttribute(reader.getAttributeLocalName(index), reader.getAttributeValue(index));
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
    
    /**
     * Parses single file. For every occurrence of math element inside input 
     * XML file new {@link MathMLNode} tree is builded. 
     * 
     * @param file Input XML file.
     * @return List of {@link MathMLNode} trees.
     */
    private List<MathMLNode> parse0(@Nonnull final File file) {
        Validate.isTrue(file != null, "File for transformation should not be null.");

        final List<MathMLNode> nodeList = new ArrayList<MathMLNode>();
        
        XMLStreamReader reader;
        
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
            final FileReader input = new FileReader(file);
            reader = this.xmlInputFactory.createXMLStreamReader(input);

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
                    continue;
                }

                switch (constant) {
                    case START_ELEMENT: {
                        // retrieve element name
                        final String elementName = reader.getLocalName();
                        // if we are not inside math element continue to the next event
                        if (!processingMathMLElement && !MathMLElement.MATH.getElementName().equals(elementName)) {
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
                                processingMathMLElement = false;
                                currentElement = null;
                                nodeList.add(tree);
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
                    default:
                        break;
                }
            }
            reader.close();

        } catch (final IOException ex) {
            this.getLogger().error("Cannot load input file.", ex);
        } catch (final XMLStreamException ex) {
            this.getLogger().error("Cannot open xml file for reading.", ex);
        }
        
        return nodeList;
    }
    
}
