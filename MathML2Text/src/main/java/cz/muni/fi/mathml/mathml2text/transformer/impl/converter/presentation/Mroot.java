package cz.muni.fi.mathml.mathml2text.transformer.impl.converter.presentation;

import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.Node;
import cz.muni.fi.mathml.mathml2text.transformer.numbers.NumberFormat;

/**
 *
 * @author Maros Kucbel
 * @date 2012-12-15T10:14:32+0100
 */
public final class Mroot {

    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (node.getChildren().size() != 2) {
            throw new IllegalArgumentException("[mroot] should have two children.");
        }
        final StringBuilder builder = new StringBuilder();
        settings.setNumberFormat(NumberFormat.ORDINAL);
        boolean braces = false;
        if (node.getChildren().get(1).getChildren().size() > 1) {
            braces = true;
        }
        if (braces) {
            builder.append(settings.getProperty("open_braces"));
        }
        builder.append(Node.process(node.getChildren().get(1), settings));
        if (braces) {
            builder.append(settings.getProperty("close_braces"));
        }
        settings.setNumberFormat(NumberFormat.CARDINAL);
        builder.append(settings.getProperty("root"));
        braces = false;
        if (node.getChildren().get(0).getChildren().size() > 1) {
            braces = true;
        }
        if (braces) {
            builder.append(settings.getProperty("open_braces"));
        }
        builder.append(Node.process(node.getChildren().get(0), settings));
        if (braces) {
            builder.append(settings.getProperty("close_braces"));
        }
        
        return builder.toString();
    }
}