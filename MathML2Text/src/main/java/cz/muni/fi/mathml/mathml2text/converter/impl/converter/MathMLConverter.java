package cz.muni.fi.mathml.mathml2text.converter.impl.converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.MathMLElement;
import cz.muni.fi.mathml.mathml2text.Strings;
import cz.muni.fi.mathml.mathml2text.converter.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.impl.XmlAttribute;
import cz.muni.fi.mathml.mathml2text.converter.numbers.NumberFormat;
import cz.muni.fi.mathml.mathml2text.converter.numbers.NumberTransformer;

/**
 * Transformer of {@link MathMLNode} trees into string representations.
 * @todo extract interface
 * 
 * @author Maros Kucbel
 * @date 2012-11-03T18:55:09+0100
 */
public class MathMLConverter {
    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(MathMLConverter.class);
    /**
     * Localization properties for locales. 
     */
    @Nonnull
    private Map<Locale, Properties> localizationMap = new HashMap<Locale, Properties>();
    /**
     * Transformation of numbers.
     */
    private NumberTransformer numberTransformer;
    /**
     * Helper property for access to properties file for current localization.
     */
    private Properties currentLocalization;
    /**
     * Indicates format of a number we are transforming.
     * Whenever other than {@link NumberFormat#CARDINAL} is used, it should be set 
     * back to {@link NumberFormat#CARDINAL} afterwards.
     */
    private NumberFormat numberFormat = NumberFormat.CARDINAL;
    
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
                logger.error(String.format("Cannot load properties for language [%1$s].", locale.getLanguage()), ex);
            }
        }
        return this.localizationMap.get(locale);
    }
    
    /**
     * Finds a property value for given key.
     * Returns <code>null</code> if there isn't one.
     */
    protected String getProperty(final String key) {
        return this.currentLocalization.getProperty(key) + Strings.SPACE;
    }
    
    protected NumberFormat getNumberFormat() {
        return this.numberFormat;
    }
    
    protected void setNumberFormat(final NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }
    
    protected NumberTransformer getNumberTransformer() {
        return this.numberTransformer;
    }
    
    /**
     * Converts a list of math nodes.
     * 
     * @param nodeList 
     */
    public List<String> convert(final List<MathMLNode> nodeList, final Locale language) {
        final List<MathMLNode> checked = new ArrayList<MathMLNode>();
        // check whether every root of tree is of type math
        //@todo for perfomance optimization, delete this check
        for (final MathMLNode node : nodeList) {
            if (MathMLElement.MATH.equals(node.getType())) {
                checked.add(node);
            } else {
                logger.debug(String.format("Expected [math] node, but got [%1$s].", node.getType().getElementName()));
            }
        }
        this.numberTransformer = new NumberTransformer(language);
        this.currentLocalization = this.getLocalization(language);
        ConverterSettings settings = new ConverterSettings();
        settings.setLocalization(this.currentLocalization);
        settings.setNumberTransformer(this.numberTransformer);
        final List<String> converted = new ArrayList<String>(checked.size());
        for (final MathMLNode root : checked) {
//            settings.setUseContentMathML(this.chooseMathMLTypeToUse(root));
            MathMLNode nodeToProcess = this.getNodeForProcessing(root);
            converted.add(Node.process(nodeToProcess, settings));
        }
        return converted;
    }
    
    public String convert(final MathMLNode node, final Locale language) {
        if (!MathMLElement.MATH.equals(node.getType())) {
            throw new IllegalStateException(String.format("Expected [math] node, but got [%1$s].", node.getType().getElementName()));
        }
        this.numberTransformer = new NumberTransformer(language);
        this.currentLocalization = this.getLocalization(language);
        ConverterSettings settings = new ConverterSettings();
        settings.setLocalization(this.currentLocalization);
        settings.setNumberTransformer(this.numberTransformer);
//        settings.setUseContentMathML(this.chooseMathMLTypeToUse(node));
        MathMLNode nodeToProcess = this.getNodeForProcessing(node);
        return Node.process(nodeToProcess, settings);
    }
    
    private boolean chooseMathMLTypeToUse(final MathMLNode node) {
        for (final MathMLNode child : node.getChildren()) {
            if (MathMLElement.SEMANTICS.equals(child.getType())) {
                for (final MathMLNode semanticsChild : child.getChildren()) {
                    if (MathMLElement.ANNOTATION_XML.equals(semanticsChild.getType())) {
                        for (final XmlAttribute attr : semanticsChild.getAttributes()) {
                            if ("encoding".equals(attr.getKey()) && "MathML-Content".equals(attr.getValue())) {
                                return true;
                            }
                        }
                    }
                }
            } else if (MathMLElement.ANNOTATION_XML.equals(child.getType())) {
                for (final XmlAttribute attr : child.getAttributes()) {
                    if ("encoding".equals(attr.getKey()) && "MathML-Content".equals(attr.getValue())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Returns a node that will be converted based on some input parameters.
     * @param node
     * @return 
     */
    private MathMLNode getNodeForProcessing(final MathMLNode node) {
        for (final MathMLNode child : node.getChildren()) {
            if (MathMLElement.SEMANTICS.equals(child.getType())) {
                for (final MathMLNode semanticsChild : child.getChildren()) {
                    if (MathMLElement.ANNOTATION_XML.equals(semanticsChild.getType())) {
                        for (final XmlAttribute attr : semanticsChild.getAttributes()) {
                            if ("encoding".equals(attr.getKey()) && "MathML-Content".equals(attr.getValue())) {
                                if (!semanticsChild.getChildren().isEmpty()) {
                                    return semanticsChild;
                                }
                            }
                        }
                    }
                }
            } else if (MathMLElement.ANNOTATION_XML.equals(child.getType())) {
                for (final XmlAttribute attr : child.getAttributes()) {
                    if ("encoding".equals(attr.getKey()) && "MathML-Content".equals(attr.getValue())) {
                        if (!child.getChildren().isEmpty()) {
                            return child;
                        }
                    }
                }
            }
        }
        return node;
    }
}