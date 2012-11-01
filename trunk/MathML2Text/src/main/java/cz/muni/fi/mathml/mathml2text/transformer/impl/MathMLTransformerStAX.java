package cz.muni.fi.mathml.mathml2text.transformer.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.MathMLElement;
import cz.muni.fi.mathml.mathml2text.Strings;
import cz.muni.fi.mathml.mathml2text.transformer.IMathMLTransformer;
import cz.muni.fi.mathml.mathml2text.transformer.numbers.NumberTransformer;

/**
 * MathML transformer implementation using StAX API.
 *
 * @author Maros Kucbel Sep 15, 2012, 10:07:12 PM
 */
public final class MathMLTransformerStAX implements IMathMLTransformer {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(MathMLTransformerStAX.class);
    /**
     * Localization properties for locales.
     */
    @Nonnull
    private Map<Locale, Properties> localizationMap = new HashMap<Locale, Properties>();
    
    private Properties currentLocalization;
    private MathMLElement currentElement;
    private NumberTransformer numberTransformer;
    private Locale currentLocale;
    private XMLStreamReader reader;
    private XMLStreamWriter writer;
    private MathMLNode tree;
    private MathMLNode currentNode;
    private MathMLNode parentNode;
    
    /**
     * Returns logger for this class.
     */
    private Logger getLogger() {
        return this.logger;
    }

    /**
     * Returns localization properties for given locale.
     */
    private Properties getLocalization(final Locale locale) {
        if (this.localizationMap.get(locale) == null) {
            final Properties properties = new Properties();
            final InputStream resourceAsStream = this.getClass().getResourceAsStream(String.format("%1$s.xml", locale.getLanguage()));
            try {
                properties.loadFromXML(resourceAsStream);
                this.localizationMap.put(locale, properties);
            } catch (final IOException ex) {
                this.getLogger().error(String.format("Cannot load properties for language [%1$s].", locale.getLanguage()), ex);
            }
        }
        return this.localizationMap.get(locale);
    }

    @Override
    public String transformMathML(Source input, String language) throws TransformerConfigurationException, TransformerException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void transformMathML(File input, File targetDirectory, String language) throws TransformerConfigurationException, TransformerException, IllegalAccessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void transform(@Nonnull final Collection<File> filesToTransform,
            @Nonnull final Locale language) {
        Validate.isTrue(filesToTransform != null, "List of files for transformation should not be null.");
        Validate.notEmpty(filesToTransform, "List of files for transformation should not be empty.");
        Validate.noNullElements(filesToTransform, "List of files for transformation should not contain null elements.");
        Validate.isTrue(language != null, "Language of transformation should not be null.");

        for (final File file : filesToTransform) {
            this.transform0(file, language);
        }

    }

    private void init0(final Locale language) {
        this.currentElement = null;
        this.currentLocale = language;
        this.numberTransformer = new NumberTransformer(language);
        this.currentLocalization = this.getLocalization(language);
    }
    
    private void transform0(@Nonnull final File file,
            @Nonnull final Locale language) {
        Validate.isTrue(file != null, "File for transformation should not be null.");
        Validate.isTrue(language != null, "Language of transformation should not be null.");
        
        this.init0(language);
        
        try {
            final FileInputStream input = new FileInputStream(file);
            this.reader = XMLInputFactory.newInstance().createXMLStreamReader(input);
            this.writer = XMLOutputFactory.newInstance().createXMLStreamWriter(System.out);

            StringBuilder builder = new StringBuilder();

            // indicates whether we are inside a math element
            boolean processingMathMLElement = false;
            while (this.reader.hasNext()) {
                // pull next event code
                final int eventCode = this.reader.next();
                // determine type of event
                final XmlStreamConstant constant = XmlStreamConstant.forEventCode(eventCode);

                // check whether we are inside math element
                // if we are outside math element and this event type is not START_ELEMENT
                // simply copy element content
                if (!processingMathMLElement && !XmlStreamConstant.START_ELEMENT.equals(constant)) {
                    switch (constant) {
                        case END_ELEMENT: {
                            this.writer.writeEndElement();
                            break;
                        }
                        case CHARACTERS: {
                            this.writer.writeCharacters(this.reader.getText());
                            break;
                        }
                        default:
                            break;
                    }
                    continue;
                }

                // if we progressed here, that means we are inside math element
                // or we are outside math element and event type is START_ELEMENT
                switch (constant) {
                    case START_ELEMENT: {
                        if (this.getLogger().isDebugEnabled()) {
                            this.getLogger().debug(String.format("Start of element [%1$s].", this.reader.getLocalName()));
                        }
                        // retrieve element name
                        final String elementName = this.reader.getLocalName();
                        // if we are outside math element this element's name is not math
                        // we will copy element with its attributes
                        if (!processingMathMLElement && !MathMLElement.MATH.getElementName().equals(elementName)) {
                            this.writer.writeStartElement(this.reader.getLocalName());
                            for (int index = 0; index < this.reader.getAttributeCount(); ++index) {
                                this.writer.writeAttribute(this.reader.getAttributeLocalName(index), this.reader.getAttributeValue(index));
                            }
                            continue;
                        }
                        // at this point we are inside math element or at math element
                        // translate element name to constant for easier use
                        this.currentElement = MathMLElement.forElementName(elementName);
                        processingMathMLElement = true;
                        this.processStartElement();
                        break;
                    }
                    case END_ELEMENT: {
                        if (this.getLogger().isDebugEnabled()) {
                            this.getLogger().debug(String.format("End of element [%1$s].", this.reader.getLocalName()));
                        }
                        
                        this.parentNode = this.currentNode.getParent();
                        this.currentNode = this.parentNode;
                        
                        final String elementName = this.reader.getLocalName();
                        final MathMLElement element = MathMLElement.forElementName(elementName);
                        switch (element) {
                            case MATH: {
                                System.out.println(this.tree);
                                this.writer.writeCharacters(builder.toString());
                                this.writer.writeEndElement();
                                processingMathMLElement = false;
                                this.currentElement = null;
                                this.getLogger().info(builder.toString());
                                builder = new StringBuilder();
                                break;
                            }
                            default:
                                break;
                        }

                        break;
                    }
                    case CHARACTERS: {
                        if (this.getLogger().isDebugEnabled() && StringUtils.isNotBlank(this.reader.getText())) {
                            this.getLogger().debug(String.format("Characters [%1$s].", this.reader.getText()));
                        }
                        final String text = this.reader.getText();
                        final String result = this.processCharacters(text);
                        builder.append(result);
                        break;
                    }
                    case ATTRIBUTE: {
                        if (this.getLogger().isDebugEnabled()) {
                            this.getLogger().debug(String.format("Attribute [%1$s].", this.reader.getTextCharacters()));
                        }
                        break;
                    }
                    case ENTITY_REFERENCE: {
                        if (this.getLogger().isDebugEnabled()) {
                            this.getLogger().debug("Entity reference [%1$s].", this.reader.getLocalName());
                        }
                    }
                    default:
                        break;
                }
            }
            this.reader.close();

            this.writer.flush();
            this.writer.close();
        } catch (final IOException ex) {
            this.getLogger().error("Cannot load input file.", ex);
        } catch (final XMLStreamException ex) {
            this.getLogger().error("Cannot open xml file for reading.", ex);
        }
    }

    /**
     * This method processes event type {@link XmlStreamConstant#START_ELEMENT}.
     * 
     * @return
     * @throws XMLStreamException 
     */
    private String processStartElement() throws XMLStreamException {
        // set new current node
        this.currentNode = new MathMLNode();
        // set its name
        this.currentNode.setType(this.currentElement);
        // if parent node was set, set it to current node
        if (this.parentNode != null) {
            this.currentNode.setParent(this.parentNode);
            this.parentNode.getChildren().add(this.currentNode);
        }
        
        // if we are just at math element initialize new tree
        if (this.tree == null) {
            this.tree = this.currentNode;
            this.parentNode = this.currentNode;
        }
        
        this.parentNode = this.currentNode;
        
        final StringBuilder builder = new StringBuilder();
        switch (this.currentElement) {
            case MATH: {
                this.writer.writeStartElement("mathText");
                break;
            }
            case MROW: {
                //@todo some kind of mark that will be used to divide long formulae into shorter parts
                break;
            } 
            case MFRAC: {
                
                break;
            }
            default:
                break;
        }
        return builder.toString();
    }
    
    private String processCharacters(final String value) {
        final StringBuilder builder = new StringBuilder();
        if (this.currentElement != null && StringUtils.isNotBlank(value)) {
            
            this.currentNode.setValue(value);
            
            switch (this.currentElement) {
                case MN: {
                    try {
                        final String number = this.numberTransformer.transformNumber(value);
                        builder.append(number);
                        builder.append(Strings.SPACE);
                    } catch (final NumberFormatException ex) {
                        this.getLogger().warn(String.format("Cannot transform string [%1$s] to number.", value), ex);
                    }
                    break;
                }
                case MI: {
                    //@todo check whether this value is funtion name or just an identifier
                    builder.append(value);
                    builder.append(Strings.SPACE);
                    break;
                }
                case MO: {
                    //@todo process operation (function)
                    final String op = StringEscapeUtils.escapeHtml4(value);
                    this.getLogger().debug(String.format("[%1$s]", op));
                    final Operation operator = Operation.forSymbol(op);
                    if (operator != null) {
                        builder.append(this.currentLocalization.getProperty(operator.getKey()));
                        builder.append(Strings.SPACE);
                    } else {
                        if (this.getLogger().isWarnEnabled()) {
                            this.getLogger().warn("Operation [{}] not supported.", op);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
        return builder.toString();
    }
    
    private String processMfrac() {
        final StringBuilder builder = new StringBuilder();
        boolean isFraction = true;
        for (int i = 0; i < this.reader.getAttributeCount(); ++i) {
            if ("linethickness".equals(this.reader.getAttributeLocalName(i).toLowerCase())
                    && "0".equals(this.reader.getAttributeValue(i))) {
                isFraction = false;
                break;
            }
        }
        if (isFraction) {
            builder.append(this.currentLocalization.getProperty("fraction"));
            builder.append(Strings.SPACE);
        } else {
            
        }
        return builder.toString();
    }
}
