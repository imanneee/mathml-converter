package cz.muni.fi.mathml.mathml2text.converter.presentation;

import cz.muni.fi.mathml.mathml2text.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.Node;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;

/**
 * Specific implementation of <code>&lt;mover&gt;</code> node.
 * 
 * @author Maros Kucbel
 * @date 2013-05-08T19:35:40+0100
 */
public final class Mover {

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
        builder.append(Node.process(node.getChildren().get(0), settings));
        builder.append(settings.getProperty("with"));
        builder.append(Node.process(node.getChildren().get(1), settings));
        builder.append(settings.getProperty("above"));
        return builder.toString();
    }
}