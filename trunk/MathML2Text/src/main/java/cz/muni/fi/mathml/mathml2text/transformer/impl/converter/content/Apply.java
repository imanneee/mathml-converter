package cz.muni.fi.mathml.mathml2text.transformer.impl.converter.content;

import cz.muni.fi.mathml.MathMLElement;
import cz.muni.fi.mathml.MathMLType;
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
        // first element is function
        MathMLElement function = node.getChildren().get(0).getType();
        switch (function.getType()) {
            case CONTENT_TRIGONOMETRY: {
                builder.append(settings.getProperty(function.getElementName()));
                builder.append(settings.getProperty("of"));
                builder.append(Node.process(node.getChildren().get(1), settings));
                return builder.toString();
            }
            case CONTENT_GROUP: {
                builder.append(settings.getProperty(function.getElementName()));
                builder.append(settings.getProperty("of"));
                builder.append(Node.process(node.getChildren().get(1), settings));
                int count = node.getChildren().size();
                for (int index = 2; index < count; ++index) {
                    if (index == count - 1) {
                        builder.append(settings.getProperty("and"));
                    } else {
                        builder.append(", ");
                    }
                    builder.append(Node.process(node.getChildren().get(index), settings));
                }
                return builder.toString();
            }
        }
        if (childrenCount < 1) {
            
        } else if (childrenCount < 2) {
            builder.append(settings.getProperty("function"));
            builder.append(Node.process(node.getChildren().get(0), settings));
            builder.append(settings.getProperty("applied_to"));
            builder.append(Node.process(node.getChildren().get(1), settings));
        } else {
            String funct = Node.process(node.getChildren().get(0), settings);
            for (int index = 1; index < childrenCount; ++index) {
                builder.append(Node.process(node.getChildren().get(index), settings));
                if (index < childrenCount - 1) {
                    builder.append(funct);
                }
            }
        }
        return builder.toString();
    }
}