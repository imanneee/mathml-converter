package cz.muni.fi.mathml.mathml2text.transformer.impl.converter.content;

import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.Node;

/**
 *
 * @author Maros Kucbel
 * @date 2012-12-15T11:45:41+0100
 */
public final class Apply {

    public static String process(final MathMLNode node, final ConverterSettings settings) {
        final StringBuilder builder = new StringBuilder();
        int childrenCount = node.getChildren().size();
        if (childrenCount < 1) {
            
        } else if (childrenCount < 2) {
            builder.append(settings.getProperty("function"));
            builder.append(Node.process(node.getChildren().get(0), settings));
            builder.append(settings.getProperty("applied_to"));
            builder.append(Node.process(node.getChildren().get(1), settings));
        } else {
            String function = Node.process(node.getChildren().get(0), settings);
            for (int index = 1; index < childrenCount; ++index) {
                builder.append(Node.process(node.getChildren().get(index), settings));
                if (index < childrenCount - 1) {
                    builder.append(function);
                }
            }
        }
        return builder.toString();
    }
}