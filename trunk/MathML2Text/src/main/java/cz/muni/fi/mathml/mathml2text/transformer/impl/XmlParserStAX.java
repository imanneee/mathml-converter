package cz.muni.fi.mathml.mathml2text.transformer.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctc.wstx.stax.WstxInputFactory;

import cz.muni.fi.mathml.MathMLElement;

/**
 *
 * @author Maros Kucbel Oct 31, 2012, 19:07:12 PM
 */
public final class XmlParserStAX {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(XmlParserStAX.class);
    
    private WstxInputFactory xmlInputFactory;
    

    public XmlParserStAX() {
        this.xmlInputFactory = (WstxInputFactory) XMLInputFactory.newInstance();
//        this.xmlInputFactory.setProperty("javax.xml.stream.isValidating", false);
        this.xmlInputFactory.setProperty("javax.xml.stream.isReplacingEntityReferences", false);
        this.xmlInputFactory.getConfig().doSupportDTDs(false);
        this.xmlInputFactory.getConfig().doCacheDTDs(true);
        this.xmlInputFactory.getConfig().doReplaceEntityRefs(false);
        //        this.xmlInputFactory.getConfig().doSupportDTDPP(false);
    }
    
    /**
     * Returns logger for this class.
     */
    private Logger getLogger() {
        return this.logger;
    }

    public List<MathMLNode> parse(@Nonnull final Collection<File> filesToTransform) {
        Validate.isTrue(filesToTransform != null, "List of files for transformation should not be null.");
        Validate.notEmpty(filesToTransform, "List of files for transformation should not be empty.");
        Validate.noNullElements(filesToTransform, "List of files for transformation should not contain null elements.");

        final List<MathMLNode> nodeList = new ArrayList<MathMLNode>();
        
        for (final File file : filesToTransform) {
            nodeList.addAll(this.parse0(file));
        }
        return nodeList;
    }

    private List<MathMLNode> parse0(@Nonnull final File file) {
        Validate.isTrue(file != null, "File for transformation should not be null.");

        final List<MathMLNode> nodeList = new ArrayList<MathMLNode>();
        
        XMLStreamReader reader;
        
        MathMLNode tree = null;
        MathMLNode currentNode = null;
        MathMLNode parentNode = null;
        MathMLElement currentElement = null;
        
        try {
            final FileReader input = new FileReader(file);
            reader = this.xmlInputFactory.createXMLStreamReader(input);

            // indicates whether we are inside a math element
            boolean processingMathMLElement = false;
            while (reader.hasNext()) {
                // pull next event code
                final int eventCode = reader.next();
                // determine type of event
                final XmlStreamConstant constant = XmlStreamConstant.forEventCode(eventCode);

                // check whether we are inside math element
                if (!processingMathMLElement && !XmlStreamConstant.START_ELEMENT.equals(constant)) {
                    continue;
                }

                switch (constant) {
                    case START_ELEMENT: {
                        // retrieve element name
                        final String elementName = reader.getLocalName();
                        
                        if (!processingMathMLElement && !MathMLElement.MATH.getElementName().equals(elementName)) {
                            continue;
                        }
                        
                        // if we are outside math element this element's name is not math
                        // we will copy element with its attributes
                        currentElement = MathMLElement.forElementName(elementName);
//                        if (currentElement == MathMLElement.UNKNOWN) {
//                            this.getLogger().debug(String.format("Unknown math element [%1$s]", elementName));
//                        }
//                        this.getLogger().debug("Starting to parse math element.");
                        processingMathMLElement = true;
                        // set new current node
                        currentNode = new MathMLNode();
                        // set its name
                        currentNode.setType(currentElement);
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
                        parentNode = currentNode.getParent();
                        currentNode = parentNode;

                        final String elementName = reader.getLocalName();
                        final MathMLElement element = MathMLElement.forElementName(elementName);
//                        if (currentElement == MathMLElement.UNKNOWN) {
//                            this.getLogger().debug(String.format("Unknown math element [%1$s]", elementName));
//                        }
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
