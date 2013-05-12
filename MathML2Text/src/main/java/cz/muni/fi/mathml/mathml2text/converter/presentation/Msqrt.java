package cz.muni.fi.mathml.mathml2text.converter.presentation;

import cz.muni.fi.mathml.mathml2text.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.Node;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;

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
        builder.append(Node.process(node.getChildren().get(0), settings));
        return builder.toString();
    }
}