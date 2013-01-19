package cz.muni.fi.mathml.mathml2text.converter.impl.converter.presentation;

import cz.muni.fi.mathml.mathml2text.converter.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.impl.Operation;
import cz.muni.fi.mathml.mathml2text.converter.impl.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.impl.converter.Node;

/**
 * Specific implementation of <code>&lt;munder&gt;</code> node.
 * 
 * @author Maros Kucbel
 * @date 2012-12-15T10:19:59+0100
 */
public final class Munder {

    /**
     * Processes input node.
     * @param node Input node.
     * @param settings Converter settings.
     * @return Content of the input node converted to string.
     */
    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (node.getChildren().size() != 2) {
            throw new IllegalArgumentException("[munder] should have two children.");
        }
        final StringBuilder builder = new StringBuilder();
        if (Operation.LIMIT.getSymbols().contains(node.getChildren().get(0).getValue())) {
            builder.append(Node.process(node.getChildren().get(0), settings));
            MathMLNode firstSibling = node.getParent().getChildren().get(1);
            if (firstSibling != null) {
                builder.append(Node.process(firstSibling, settings));
            }
            builder.append(settings.getProperty("limit_as"));
            builder.append(Node.process(node.getChildren().get(1), settings));
        } else {
            builder.append(Node.process(node.getChildren().get(0), settings));
            builder.append(settings.getProperty("subscript"));
            builder.append(Node.process(node.getChildren().get(1), settings));
        }
        return builder.toString();
    }
}