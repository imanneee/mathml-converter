package cz.muni.fi.mathml.mathml2text.converter.presentation;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.Node;
import cz.muni.fi.mathml.mathml2text.converter.operation.Operation;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;

/**
 * Specific implementation of <code>&lt;munderover&gt;</code> 
 * and <code>&lt;msubsup&gt;</code> node.
 * 
 * @author Maros Kucbel
 * @date 2012-12-15T11:19:57+0100
 */
public final class Munderover {

    /**
     * Processes input node.
     * @param node Input node.
     * @param settings Converter settings.
     * @return Content of the input node converted to string.
     */
    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (node.getChildren().size() != 3) {
            throw new IllegalArgumentException("[munderover] should have three children.");
        }
        final StringBuilder builder = new StringBuilder();
        final String possibleOperation = StringEscapeUtils.escapeHtml4(node.getChildren().get(0).getValue());
        if (Operation.INTEGRAL.getSymbols().contains(possibleOperation)) {
            builder.append(settings.getProperty("integral_definite"));
            builder.append(Node.process(node.getChildren().get(0), settings));
            builder.append(settings.getProperty("from"));
            builder.append(Node.process(node.getChildren().get(1), settings));
            builder.append(settings.getProperty("to"));
            builder.append(Node.process(node.getChildren().get(2), settings));
            builder.append(settings.getProperty("of"));
            MathMLNode firstSibling = findFirstSibling(node);
            if (firstSibling != null) {
                builder.append(Node.process(firstSibling, settings));
            }
        } else if (Operation.SUMMATION.getSymbols().contains(possibleOperation)
                || Operation.PRODUCT.getSymbols().contains(possibleOperation)) {
            builder.append(Node.process(node.getChildren().get(0), settings));
            builder.append(settings.getProperty("from"));
            builder.append(Node.process(node.getChildren().get(1), settings));
            builder.append(settings.getProperty("to"));
            builder.append(Node.process(node.getChildren().get(2), settings));
            builder.append(settings.getProperty("of"));
            MathMLNode firstSibling = findFirstSibling(node);
            if (firstSibling != null) {
                builder.append(Node.process(firstSibling, settings));
            }
        } else {
            builder.append(Node.process(node.getChildren().get(0), settings));
            builder.append(settings.getProperty("subscript"));
            builder.append(Node.process(node.getChildren().get(1), settings));
            builder.append(settings.getProperty("superscript"));
            builder.append(Node.process(node.getChildren().get(2), settings));
        }
        return builder.toString();
    }
    
    /**
     * Finds first sibling of this node.
     * @param node Node
     * @return First sibling or {@code null} if none exists.
     */
    private static MathMLNode findFirstSibling(final MathMLNode node) {
        MathMLNode firstSibling = null;
        for (int index = 0; index < node.getParent().getChildren().size(); ++index) {
            if (node.equals(node.getParent().getChildren().get(index))) {
                try {
                    firstSibling = node.getParent().getChildren().get(index + 1);
                } catch (IndexOutOfBoundsException ex) {
                    LoggerFactory.getLogger(Munder.class).warn("No next sibling for [munder].");
                }
            }
        }
        return firstSibling;
    }
}