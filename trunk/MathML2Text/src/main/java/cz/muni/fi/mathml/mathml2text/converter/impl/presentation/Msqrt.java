package cz.muni.fi.mathml.mathml2text.converter.impl.presentation;

import cz.muni.fi.mathml.mathml2text.converter.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.impl.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.impl.Node;

/**
 * Specific implementation of <code>&lt;msqrt&gt;</code> node.
 * 
 * @author Maros Kucbel
 * @date 2012-12-15T10:10:20+0100
 */
public final class Msqrt {

    /**
     * Processes input node.
     * @param node Input node.
     * @param settings Converter settings.
     * @return Content of the input node converted to string.
     */
    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (node.getChildren().size() != 1) {
            throw new IllegalStateException("[msqrt] should have one child.");
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(settings.getProperty("square_root"));
        boolean braces = false;
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