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
        final List<String> converted = new ArrayList<String>(checked.size());
        for (final MathMLNode root : checked) {
            converted.add(this.processNode(root));
        }
        return converted;
    }
    
    public String convert(final MathMLNode node, final Locale language) {
        if (!MathMLElement.MATH.equals(node.getType())) {
            throw new IllegalStateException(String.format("Expected [math] node, but got [%1$s].", node.getType().getElementName()));
        }
        this.numberTransformer = new NumberTransformer(language);
        this.currentLocalization = this.getLocalization(language);
        return this.processNode(node);
    }
    
    /**
     * Converts a single math node.
     * @param node Node. 
     */
    private String processNode(final MathMLNode node) {
        if (node.isProcessed()) {
            return Strings.EMPTY;
        }
        final StringBuilder builder = new StringBuilder();
        
        switch (node.getType()) {
            case MATH: case SEMANTICS: {
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
            case MSUB: {
                builder.append(this.processMsub(node));
                break;
            }
            case MSUP: {
                builder.append(this.processMsup(node));
                break;
            }
            case MUNDER: {
                builder.append(this.processMunder(node));
                break;
            } 
            case MUNDEROVER: case MSUBSUP: {
                builder.append(this.processMunderover(node));
                break;
            }
            case CN: {
                builder.append(this.processCn(node));
                break;
            }
            case CI: {
                builder.append(this.processCi(node));
                break;
            }
            case APPLY: {
                builder.append(this.processApply(node));
                break;
            }
            case CSYMBOL: {
                builder.append(this.processCsymbol(node));
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
        node.setProcessed();
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
            switch (this.getNumberFormat()) {
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
        final Operation operator = Operation.forSymbol(op);
        if (operator != null) {
            return this.getProperty(operator.getKey());
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
            boolean braces = false;
            if (node.getChildren().get(0).getChildren().size() > 1) {
                builder.append(this.getProperty("open_braces"));
                braces = true;
            }
            builder.append(this.processNode(node.getChildren().get(0)));
            if (braces) {
                builder.append(this.getProperty("close_braces"));
            }
            builder.append(this.getProperty("divided_by"));
            braces = false;
            if (node.getChildren().get(0).getChildren().size() > 1) {
                builder.append(this.getProperty("open_braces"));
                braces = true;
            }
            builder.append(this.processNode(node.getChildren().get(1)));
            if (braces) {
                builder.append(this.getProperty("close_braces"));
            }
            builder.append(this.getProperty("end_fraction"));
        } else {
            builder.append(this.getProperty("binomial_coefficient"));
            builder.append(this.processNode(node.getChildren().get(0)));
            builder.append(this.getProperty("choose"));
            builder.append(this.processNode(node.getChildren().get(1)));
            builder.append(this.getProperty("end_binomial_coefficient"));
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
        boolean braces = false;
        if (node.getChildren().get(0).getChildren().size() > 1) {
            braces = true;
        }
        if (braces) {
            builder.append(this.getProperty("open_braces"));
        }
        builder.append(this.processNode(node.getChildren().get(0)));
        if (braces) {
            builder.append(this.getProperty("close_braces"));
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
        this.setNumberFormat(NumberFormat.ORDINAL);
        boolean braces = false;
        if (node.getChildren().get(1).getChildren().size() > 1) {
            braces = true;
        }
        if (braces) {
            builder.append(this.getProperty("open_braces"));
        }
        builder.append(this.processNode(node.getChildren().get(1)));
        if (braces) {
            builder.append(this.getProperty("close_braces"));
        }
        this.setNumberFormat(NumberFormat.CARDINAL);
        builder.append(this.getProperty("root"));
        braces = false;
        if (node.getChildren().get(0).getChildren().size() > 1) {
            braces = true;
        }
        if (braces) {
            builder.append(this.getProperty("open_braces"));
        }
        builder.append(this.processNode(node.getChildren().get(0)));
        if (braces) {
            builder.append(this.getProperty("close_braces"));
        }
        
        return builder.toString();
    }
    
    /**
     * Processes msub node.
     */
    private String processMsub(final MathMLNode node) {
        if (node.getChildren().size() != 2) {
            throw new IllegalArgumentException("[msub] should have two children.");
        }
        final StringBuilder builder = new StringBuilder();
        // if first child is operation logarithm
        if (Operation.LOGARITHM.getSymbols().contains(node.getChildren().get(0).getValue())) {
            builder.append(this.getProperty("logarithm_base"));
            builder.append(this.processNode(node.getChildren().get(1)));
            builder.append(this.getProperty("logarithm_from"));
        } else {
            builder.append(this.processNode(node.getChildren().get(0)));
            builder.append(this.processNode(node.getChildren().get(1)));
        }
        return builder.toString();
    }
    
    /**
     * Processes msup node.
     */
    private String processMsup(final MathMLNode node) {
        if (node.getChildren().size() != 2) {
            throw new IllegalArgumentException("[msup] should have two children.");
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(this.processNode(node.getChildren().get(0)));
        if (Operation.SQUARE.getSymbols().contains(node.getChildren().get(1).getValue())) {
            builder.append(this.getProperty("squared"));
        } else {
            builder.append(this.getProperty("superscript"));
            builder.append(this.processNode(node.getChildren().get(1)));
        }
        return builder.toString();
    }
    
    private String processMunder(final MathMLNode node) {
        if (node.getChildren().size() != 2) {
            throw new IllegalArgumentException("[munder] should have two children.");
        }
        final StringBuilder builder = new StringBuilder();
        if (Operation.LIMIT.getSymbols().contains(node.getChildren().get(0).getValue())) {
            builder.append(this.processNode(node.getChildren().get(0)));
            MathMLNode firstSibling = node.getParent().getChildren().get(1);
            if (firstSibling != null) {
                builder.append(this.processNode(firstSibling));
            }
            builder.append(this.getProperty("limit_as"));
            builder.append(this.processNode(node.getChildren().get(1)));
        } else {
            builder.append(this.processNode(node.getChildren().get(0)));
            builder.append(this.getProperty("subscript"));
            builder.append(this.processNode(node.getChildren().get(1)));
        }
        return builder.toString();
    }
    
    private String processMunderover(final MathMLNode node) {
        if (node.getChildren().size() != 3) {
            throw new IllegalArgumentException("[munderover] should have three children.");
        }
        final StringBuilder builder = new StringBuilder();
        final String possibleOperation = node.getChildren().get(0).getValue();
        if (Operation.INTEGRAL.getSymbols().contains(possibleOperation)) {
            builder.append(this.getProperty("integral_definite"));
            builder.append(this.processNode(node.getChildren().get(0)));
            builder.append(this.getProperty("from"));
            builder.append(this.processNode(node.getChildren().get(1)));
            builder.append(this.getProperty("to"));
            builder.append(this.processNode(node.getChildren().get(2)));
            builder.append(this.getProperty("of"));
            MathMLNode firstSibling = node.getParent().getChildren().get(1);
            if (firstSibling != null) {
                builder.append(this.processNode(firstSibling));
            }
        } else if (Operation.SUMMATION.getSymbols().contains(possibleOperation)
                || Operation.PRODUCT.getSymbols().contains(possibleOperation)) {
            builder.append(this.processNode(node.getChildren().get(0)));
            MathMLNode identifier = !node.getChildren().get(1).getChildren().isEmpty() 
                    ? node.getChildren().get(1).getChildren().get(0)
                    : null;
            if (identifier != null && MathMLElement.MI.equals(identifier.getType())) {
                builder.append(this.getProperty("over"));
                builder.append(this.processNode(identifier));
                builder.append(this.getProperty("from"));
                this.processNode(node.getChildren().get(1).getChildren().get(1)); // so that the node will be processed
                builder.append(this.processNode(node.getChildren().get(1)));
                builder.append(this.getProperty("to"));
                builder.append(this.processNode(node.getChildren().get(2)));
                builder.append(this.getProperty("of"));
                MathMLNode firstSibling = node.getParent().getChildren().get(1);
                if (firstSibling != null) {
                    builder.append(this.processNode(firstSibling));
                }
            } else {
                builder.append(this.getProperty("from"));
                builder.append(this.processNode(node.getChildren().get(1)));
                builder.append(this.getProperty("to"));
                builder.append(this.processNode(node.getChildren().get(2)));
                builder.append(this.getProperty("of"));
                MathMLNode firstSibling = node.getParent().getChildren().get(1);
                if (firstSibling != null) {
                    builder.append(this.processNode(firstSibling));
                }
            }
        }
        return builder.toString();
    }
    
    private String processCn(final MathMLNode node) {
        if (node.getValue() == null) {
            throw new IllegalStateException("[mi] node should have its value set.");
        }
        final StringBuilder builder = new StringBuilder();
        for (final XmlAttribute attr : node.getAttributes()) {
            if (("base".equals(attr.getKey()) && !"10".equals(attr.getValue()))
                    || ("type".equals(attr.getKey()) && "hexdouble".equals(attr.getValue()))) {
                builder.append(node.getValue());
                builder.append(Strings.SPACE);
                return builder.toString();
            }
        }
        try {
            final String number;
            switch (this.getNumberFormat()) {
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
    
    private String processCi(final MathMLNode node) {
        // this node is an identifier
        // it has no child elements
        if (!node.getChildren().isEmpty()) {
            throw new IllegalStateException("[mi] node should not have children.");
        }
        final String identifier = StringEscapeUtils.escapeHtml4(node.getValue());
        return identifier + Strings.SPACE;
    }
    
    private String processApply(final MathMLNode node) {
        final StringBuilder builder = new StringBuilder();
        int childrenCount = node.getChildren().size();
        if (childrenCount < 1) {
            
        } else if (childrenCount < 2) {
            builder.append(this.getProperty("function"));
            builder.append(this.processNode(node.getChildren().get(0)));
            builder.append(this.getProperty("applied_to"));
            builder.append(this.processNode(node.getChildren().get(1)));
        } else {
            String function = this.processNode(node.getChildren().get(0));
            for (int index = 1; index < childrenCount; ++index) {
                builder.append(this.processNode(node.getChildren().get(index)));
                if (index < childrenCount - 1) {
                    builder.append(function);
                }
            }
        }
        return builder.toString();
    }
    
    private String processCsymbol(final MathMLNode node) {
        if (node.getValue() == null) {
            throw new IllegalStateException("[csymbol] should have some value.");
        }
        return Strings.EMPTY;
    }
}