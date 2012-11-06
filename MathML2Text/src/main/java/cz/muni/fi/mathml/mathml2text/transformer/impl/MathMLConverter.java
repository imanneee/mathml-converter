package cz.muni.fi.mathml.mathml2text.transformer.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.MathMLElement;
import cz.muni.fi.mathml.mathml2text.Strings;
import cz.muni.fi.mathml.mathml2text.transformer.numbers.NumberFormat;
import cz.muni.fi.mathml.mathml2text.transformer.numbers.NumberTransformer;

/**
 *
 * @author Maros Kucbel
 * @date 2012-11-03T18:55:09+0100
 */
public final class MathMLConverter {
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
     * If other than {@link NumberFormat#CARDINAL} is used, it should be set 
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
    private String getProperty(final String key) {
        return this.currentLocalization.getProperty(key);
    }
    
    /**
     * Converts a list of math nodes.
     * 
     * @param nodeList 
     */
    public List<String> convert(final List<MathMLNode> nodeList, final Locale language) {
        final List<MathMLNode> checked = new ArrayList<MathMLNode>();
        for (final MathMLNode node : nodeList) {
            if (MathMLElement.MATH.equals(node.getType())) {
                checked.add(node);
            } else {
                logger.debug(String.format("Expected [math] node, but got [%1$s].", node.getType().getElementName()));
            }
        }
        this.numberTransformer = new NumberTransformer(language);
        this.currentLocalization = this.getLocalization(language);
        final List<String> converted = new ArrayList<String>(checked.size());
        for (final MathMLNode root : checked) {
            converted.add(this.processNode(root));
        }
        return converted;
    }
    
    /**
     * Converts a single math node.
     * @param root 
     */
    private String processNode(final MathMLNode node) {
        
        final StringBuilder builder = new StringBuilder();
        
        switch (node.getType()) {
            case MATH: {
                // root element, iterate through children
                for (final MathMLNode child : node.getChildren()) {
                    builder.append(this.processNode(child));
                }
                break;
            }
            case MROW: {
                // mrow acts as a grouping element
                //@todo Make some kind of mark to divide long formulae.
                for (final MathMLNode child : node.getChildren()) {
                    builder.append(this.processNode(child));
                }
                break;
            }
            case MI: {
                builder.append(this.processMi(node));
                break;
            }
            case MO: {
                builder.append(this.processMo(node));
                break;
            }
            case MN: {
                builder.append(this.processMn(node));
                break;
            }
            case MFRAC: {    
                builder.append(this.processMfrac(node));
                break;
            }
            case MSQRT: {
                builder.append(this.processMsqrt(node));
                break;
            }
            case MROOT: {
                builder.append(this.processMroot(node));
                break;
            }    
            default: {
                if (node.getChildren().isEmpty()) {
                    builder.append(node.getValue());
                } else {
                    for (final MathMLNode child : node.getChildren()) {
                        builder.append(this.processNode(child));
                    }
                }
                break;
            }
        }
        return builder.toString();
    } 
    
    /**
     * Processes mi node.
     */
    private String processMi(final MathMLNode node) {
        // this node is an identifier
        // it has no child elements
        if (!node.getChildren().isEmpty()) {
            throw new IllegalStateException("[mi] node should not have children.");
        }
        final String identifier = StringEscapeUtils.escapeHtml4(node.getValue());
        return identifier + Strings.SPACE;
    }
    
    /**
     * Processes mn node.
     */
    private String processMn(final MathMLNode node) {
        // this node is a number
        // it should have its value set and has no child elements
        if (!node.getChildren().isEmpty()) {
            throw new IllegalStateException("[mi] node should not have children.");
        }
        if (node.getValue() == null) {
            throw new IllegalStateException("[mi] node should have its value set.");
        }
        try {
            final String number;
            switch (this.numberFormat) {
                case ORDINAL: {
                    number = this.numberTransformer.transformOrdinalNumber(node.getValue());
                    break;
                }
                case CARDINAL: {
                    number = this.numberTransformer.transformNumber(node.getValue());
                    break;
                }
                default: {
                    number = Strings.EMPTY;
                    break;
                }
            }
            return number + Strings.SPACE;
        } catch (final NumberFormatException ex) {
            logger.warn(String.format("Cannot transform string [%1$s] to number.", node.getValue()), ex);
        }
        return Strings.EMPTY;
    }
    
    /**
     * Processes mo node.
     */
    private String processMo(final MathMLNode node) {
        // this node is an operation
        // it should have its value set and has no child elements
        if (!node.getChildren().isEmpty()) {
            throw new IllegalStateException("[mo] node should not have children.");
        }
        if (node.getValue() == null) {
            throw new IllegalStateException("[mo] node should have its value set.");
        }
        final String op = StringEscapeUtils.escapeHtml4(node.getValue());
//                logger.debug(String.format("[%1$s]", op));
        final Operation operator = Operation.forSymbol(op);
        if (operator != null) {
            return this.getProperty(operator.getKey()) + Strings.SPACE;
        } else {
            logger.warn("Operation [{}] not supported.", op);
        }
        return Strings.EMPTY;
    }
    
    /**
     * Processes mfrac node. If linethickness attribute is set to value 0 (zero),
     * this node is treated as binomial number, otherwise as fraction.
     */
    private String processMfrac(final MathMLNode node) {
        if (node.getChildren().size() != 2) {
            throw new IllegalStateException("[mfrac] should have two children.");
        }
        final StringBuilder builder = new StringBuilder();
        boolean isFraction = true;
        for (final XmlAttribute attr : node.getAttributes()) {
            if ("linethickness".equals(attr.getKey()) && "0".equals(attr.getValue())) {
                isFraction = false;
                break;
            }
        }
        if (isFraction) {
            builder.append(this.getProperty("fraction"));
            builder.append(Strings.SPACE);
            boolean braces = false;
            if (node.getChildren().get(0).getChildren().size() > 1) {
                builder.append(this.getProperty("open_braces"));
                builder.append(Strings.SPACE);
                braces = true;
            }
            builder.append(this.processNode(node.getChildren().get(0)));
            if (braces) {
                builder.append(this.getProperty("close_braces"));
                builder.append(Strings.SPACE);
            }
            builder.append(this.getProperty("divided_by"));
            builder.append(Strings.SPACE);
            braces = false;
            if (node.getChildren().get(0).getChildren().size() > 1) {
                builder.append(this.getProperty("open_braces"));
                builder.append(Strings.SPACE);
                braces = true;
            }
            builder.append(this.processNode(node.getChildren().get(1)));
            if (braces) {
                builder.append(this.getProperty("close_braces"));
                builder.append(Strings.SPACE);
            }
            builder.append(this.getProperty("end_fraction"));
            builder.append(Strings.SPACE);
        } else {
            builder.append(this.getProperty("binomial_coefficient"));
            builder.append(Strings.SPACE);
            builder.append(this.processNode(node.getChildren().get(0)));
            builder.append(this.getProperty("choose"));
            builder.append(Strings.SPACE);
            builder.append(this.processNode(node.getChildren().get(1)));
            builder.append(this.getProperty("end_binomial_coefficient"));
            builder.append(Strings.SPACE);
        }
        return builder.toString();
    }
    
    /**
     * Processes msqrt node.
     */
    private String processMsqrt(final MathMLNode node) {
        if (node.getChildren().size() != 1) {
            throw new IllegalStateException("[msqrt] should have one child.");
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getProperty("square_root"));
        builder.append(Strings.SPACE);
        boolean braces = false;
        if (node.getChildren().get(0).getChildren().size() > 1) {
            braces = true;
        }
        if (braces) {
            builder.append(this.getProperty("open_braces"));
            builder.append(Strings.SPACE);
        }
        builder.append(this.processNode(node.getChildren().get(0)));
        if (braces) {
            builder.append(this.getProperty("close_braces"));
            builder.append(Strings.SPACE);
        }
        return builder.toString();
    }
    
    /**
     * Processes mroot node.
     */
    private String processMroot(final MathMLNode node) {
        if (node.getChildren().size() != 2) {
            throw new IllegalArgumentException("[mroot] should have two children.");
        }
        final StringBuilder builder = new StringBuilder();
        this.numberFormat = NumberFormat.ORDINAL;
        boolean braces = false;
        if (node.getChildren().get(1).getChildren().size() > 1) {
            braces = true;
        }
        if (braces) {
            builder.append(this.getProperty("open_braces"));
            builder.append(Strings.SPACE);
        }
        builder.append(this.processNode(node.getChildren().get(1)));
        if (braces) {
            builder.append(this.getProperty("close_braces"));
            builder.append(Strings.SPACE);
        }
        this.numberFormat = NumberFormat.CARDINAL;
        builder.append(this.getProperty("root"));
        builder.append(Strings.SPACE);
        braces = false;
        if (node.getChildren().get(0).getChildren().size() > 1) {
            braces = true;
        }
        if (braces) {
            builder.append(this.getProperty("open_braces"));
            builder.append(Strings.SPACE);
        }
        builder.append(this.processNode(node.getChildren().get(0)));
        if (braces) {
            builder.append(this.getProperty("close_braces"));
            builder.append(Strings.SPACE);
        }
        
        return builder.toString();
    }
}