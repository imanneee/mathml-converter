package cz.muni.fi.mathml.mathml2text.transformer.impl.converter.content;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.MathMLElement;
import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.transformer.impl.Operation;
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
        Operation operation = Operation.forSymbol(function.getElementName());
        String functionName = settings.getProperty(operation.getKey());
        if (StringUtils.isBlank(functionName)) {
            LoggerFactory.getLogger(Apply.class).warn("Unknown function [{}]", function.getElementName());
        }
        switch (function.getType()) {
            case CONTENT_TRIGONOMETRY: {
                builder.append(functionName);
                builder.append(settings.getProperty("of"));
                builder.append(Node.process(node.getChildren().get(1), settings));
                return builder.toString();
            }
            case CONTENT_GROUP: {
                builder.append(functionName);
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
            case CONTENT_MIDDLE: {
                builder.append(Node.process(node.getChildren().get(1), settings));
                int count = node.getChildren().size();
                for (int index = 2; index < count; ++index) {
                    builder.append(functionName);
                    builder.append(Node.process(node.getChildren().get(index), settings));
                }
                return builder.toString();
            }
            case CONTENT_BEFORE: {
                builder.append(functionName);
                builder.append(Node.process(node.getChildren().get(1), settings));
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