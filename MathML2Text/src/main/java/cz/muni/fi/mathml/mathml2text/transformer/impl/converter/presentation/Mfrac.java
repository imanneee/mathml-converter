package cz.muni.fi.mathml.mathml2text.transformer.impl.converter.presentation;

import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.transformer.impl.XmlAttribute;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.Node;

/**
 *
 * @author Maros Kucbel
 * @date 2012-12-14T17:18:44+0100
 */
public final class Mfrac {

    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (node.getChildren().size() != 2) {
            throw new IllegalStateException("[mfrac] should have two children.");
        }
        final StringBuilder builder = new StringBuilder();
        boolean isFraction = true;
        for (final XmlAttribute attr : node.getAttributes()) {
            if ("linethickness".equals(attr.getKey()) && "0".equals(attr.getValue())) {
                isFraction = false;
                break;
            }
        }
        if (isFraction) {
            builder.append(settings.getProperty("fraction"));
            boolean braces = false;
            if (node.getChildren().get(0).getChildren().size() > 1) {
                builder.append(settings.getProperty("open_braces"));
                braces = true;
            }
            builder.append(Node.process(node.getChildren().get(0), settings));
            if (braces) {
                builder.append(settings.getProperty("close_braces"));
            }
            builder.append(settings.getProperty("divided_by"));
            braces = false;
            if (node.getChildren().get(0).getChildren().size() > 1) {
                builder.append(settings.getProperty("open_braces"));
                braces = true;
            }
            builder.append(Node.process(node.getChildren().get(1), settings));
            if (braces) {
                builder.append(settings.getProperty("close_braces"));
            }
            builder.append(settings.getProperty("end_fraction"));
        } else {
            builder.append(settings.getProperty("binomial_coefficient"));
            builder.append(Node.process(node.getChildren().get(0), settings));
            builder.append(settings.getProperty("choose"));
            builder.append(Node.process(node.getChildren().get(1), settings));
            builder.append(settings.getProperty("end_binomial_coefficient"));
        }
        return builder.toString();
    }
    
}