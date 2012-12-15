package cz.muni.fi.mathml.mathml2text.transformer.impl.converter.presentation;

import cz.muni.fi.mathml.MathMLElement;
import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.transformer.impl.Operation;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.Node;

/**
 *
 * @author Maros Kucbel
 * @date 2012-12-15T11:19:57+0100
 */
public final class Munderover {

    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (node.getChildren().size() != 3) {
            throw new IllegalArgumentException("[munderover] should have three children.");
        }
        final StringBuilder builder = new StringBuilder();
        final String possibleOperation = node.getChildren().get(0).getValue();
        if (Operation.INTEGRAL.getSymbols().contains(possibleOperation)) {
            builder.append(settings.getProperty("integral_definite"));
            builder.append(Node.process(node.getChildren().get(0), settings));
            builder.append(settings.getProperty("from"));
            builder.append(Node.process(node.getChildren().get(1), settings));
            builder.append(settings.getProperty("to"));
            builder.append(Node.process(node.getChildren().get(2), settings));
            builder.append(settings.getProperty("of"));
            MathMLNode firstSibling = node.getParent().getChildren().get(1);
            if (firstSibling != null) {
                builder.append(Node.process(firstSibling, settings));
            }
        } else if (Operation.SUMMATION.getSymbols().contains(possibleOperation)
                || Operation.PRODUCT.getSymbols().contains(possibleOperation)) {
            builder.append(Node.process(node.getChildren().get(0), settings));
            MathMLNode identifier = !node.getChildren().get(1).getChildren().isEmpty() 
                    ? node.getChildren().get(1).getChildren().get(0)
                    : null;
            if (identifier != null && MathMLElement.MI.equals(identifier.getType())) {
                builder.append(settings.getProperty("over"));
                builder.append(Node.process(identifier, settings));
                builder.append(settings.getProperty("from"));
                node.getChildren().get(1).getChildren().get(1).setProcessed();
                builder.append(Node.process(node.getChildren().get(1), settings));
                builder.append(settings.getProperty("to"));
                builder.append(Node.process(node.getChildren().get(2), settings));
                builder.append(settings.getProperty("of"));
                MathMLNode firstSibling = node.getParent().getChildren().get(1);
                if (firstSibling != null) {
                    builder.append(Node.process(firstSibling, settings));
                }
            } else {
                builder.append(settings.getProperty("from"));
                builder.append(Node.process(node.getChildren().get(1), settings));
                builder.append(settings.getProperty("to"));
                builder.append(Node.process(node.getChildren().get(2), settings));
                builder.append(settings.getProperty("of"));
                MathMLNode firstSibling = node.getParent().getChildren().get(1);
                if (firstSibling != null) {
                    builder.append(Node.process(firstSibling, settings));
                }
            }
        }
        return builder.toString();
    }
}