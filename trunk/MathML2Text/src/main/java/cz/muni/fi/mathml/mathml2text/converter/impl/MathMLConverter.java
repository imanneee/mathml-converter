package cz.muni.fi.mathml.mathml2text.converter.impl;

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

import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLElement;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.tree.XmlAttribute;
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
     * Converts a list of MathML nodes into a list of corresponding list of strings.
     * If there was an error during conversion of some input node, a {@code null}
     * will be inserted to the list at the index of erroneous input node.
     * 
     * @param nodeList Input list of MathML nodes.
     * @return List of converted strings.
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
        ConverterSettings settings = ConverterSettings.getInstance();
        if (settings.getLocalization() == null) {
            settings.setLocalization(this.currentLocalization);
        }
        if (settings.getNumberTransformer() == null) {
            settings.setNumberTransformer(this.numberTransformer);
        }
        final List<String> converted = new ArrayList<String>(checked.size());
        for (final MathMLNode root : checked) {
            MathMLNode nodeToProcess = this.getNodeForProcessing(root);
            String result = null;
            try {
                result = Node.process(nodeToProcess, settings);
            } catch (Throwable ex) {
                logger.error("Error while processing input tree.", ex);
            }
            converted.add(result);
        }
        return converted;
    }
    
    /**
     * Converts a MathML node into string.
     * If there was an error during conversion, {@code null} will be returned.
     * 
     * @param node Input node.
     * @param language Language of conversion.
     * @return Node converted to string.
     */
    public String convert(final MathMLNode node, final Locale language) {
        if (!MathMLElement.MATH.equals(node.getType())) {
            throw new IllegalStateException(String.format("Expected [math] node, but got [%1$s].", node.getType().getElementName()));
        }
        this.numberTransformer = new NumberTransformer(language);
        this.currentLocalization = this.getLocalization(language);
        ConverterSettings settings = ConverterSettings.getInstance();
        if (settings.getLocalization() == null) {
            settings.setLocalization(this.currentLocalization);
        }
        if (settings.getNumberTransformer() == null) {
            settings.setNumberTransformer(this.numberTransformer);
        }
        MathMLNode nodeToProcess = this.getNodeForProcessing(node);
        String result = null;
        try {
            result = Node.process(nodeToProcess, settings);
        } catch (Throwable ex) {
            logger.error("Error while processing input tree.", ex);
        }
        return result;
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