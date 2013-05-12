package cz.muni.fi.mathml.mathml2text.converter.presentation;

import cz.muni.fi.mathml.mathml2text.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.Node;
import cz.muni.fi.mathml.mathml2text.numbers.NumberFormat;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;

/**
 * Specific implementation of <code>&lt;mroot&gt;</code> node.
 * 
 * @author Maros Kucbel
 * @date 2012-12-15T10:14:32+0100
 */
public final class Mroot {

    /**
     * Processes input node.
     * @param node Input node.
     * @param settings Converter settings.
     * @return Content of the input node converted to string.
     */
    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (node.getChildren().size() != 2) {
            throw new IllegalArgumentException("[mroot] should have two children.");
        }
        final StringBuilder builder = new StringBuilder();
        settings.setNumberFormat(NumberFormat.ORDINAL);
        builder.append(Node.process(node.getChildren().get(1), settings));
        settings.setNumberFormat(NumberFormat.CARDINAL);
        builder.append(settings.getProperty("root"));
        builder.append(Node.process(node.getChildren().get(0), settings));
        
        return builder.toString();
    }
}