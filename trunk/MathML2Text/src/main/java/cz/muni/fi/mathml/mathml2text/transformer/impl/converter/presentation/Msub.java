package cz.muni.fi.mathml.mathml2text.transformer.impl.converter.presentation;

import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.transformer.impl.Operation;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.Node;

/**
 *
 * @author Maros Kucbel
 * @date 2012-12-15T10:17:25+0100
 */
public final class Msub {

    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (node.getChildren().size() != 2) {
            throw new IllegalArgumentException("[msub] should have two children.");
        }
        final StringBuilder builder = new StringBuilder();
        // if first child is operation logarithm
        if (Operation.LOGARITHM.getSymbols().contains(node.getChildren().get(0).getValue())) {
            builder.append(settings.getProperty("logarithm"));
            builder.append(Node.process(node.getChildren().get(1), settings));
            builder.append(settings.getProperty("logarithm_from"));
        } else {
            builder.append(Node.process(node.getChildren().get(0), settings));
            builder.append(Node.process(node.getChildren().get(1), settings));
        }
        return builder.toString();
    }
}