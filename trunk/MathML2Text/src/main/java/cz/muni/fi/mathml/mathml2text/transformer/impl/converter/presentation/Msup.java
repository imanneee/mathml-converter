package cz.muni.fi.mathml.mathml2text.transformer.impl.converter.presentation;

import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.transformer.impl.Operation;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.Node;

/**
 *
 * @author Maros Kucbel
 * @date 2012-12-15T10:18:41+0100
 */
public final class Msup {

    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (node.getChildren().size() != 2) {
            throw new IllegalArgumentException("[msup] should have two children.");
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(Node.process(node.getChildren().get(0), settings));
        if (Operation.SQUARE.getSymbols().contains(node.getChildren().get(1).getValue())) {
            builder.append(settings.getProperty("squared"));
        } else {
            builder.append(settings.getProperty("superscript"));
            builder.append(Node.process(node.getChildren().get(1), settings));
        }
        return builder.toString();
    }
}