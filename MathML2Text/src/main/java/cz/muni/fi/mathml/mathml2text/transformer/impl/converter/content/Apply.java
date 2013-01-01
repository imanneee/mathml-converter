package cz.muni.fi.mathml.mathml2text.transformer.impl.converter.content;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.MathMLElement;
import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.transformer.impl.Operation;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.Node;
import cz.muni.fi.mathml.mathml2text.transformer.numbers.NumberFormat;

/**
 *
 * @author Maros Kucbel
 * @date 2012-12-15T11:45:41+0100
 */
public final class Apply {

    public static String process(final MathMLNode node, final ConverterSettings settings) {
        final StringBuilder builder = new StringBuilder();
        // first element is function
        MathMLElement function = node.getChildren().get(0).getType();
        Operation operation = Operation.forSymbol(function.getElementName());
        String functionName = settings.getProperty(operation.getKey());
        if (StringUtils.isBlank(functionName)) {
            LoggerFactory.getLogger(Apply.class).warn("Unknown function [{}]", function.getElementName());
        }
        
        switch (operation) {
            case EXPONENTIATION: {
                builder.append(Node.process(node.getChildren().get(1), settings));
                builder.append(functionName);
                builder.append(Node.process(node.getChildren().get(2), settings));
                return builder.toString();
            }
            case ROOT: {
                settings.setNumberFormat(NumberFormat.ORDINAL);
                MathMLNode degree = node.getChildren().get(1);
                MathMLNode from;
                if (MathMLElement.DEGREE.equals(degree.getType())) {
                    degree = degree.getChildren().get(0);
                    from = node.getChildren().get(2);
                } else {
                    degree = node.getChildren().get(2);
                    from = node.getChildren().get(1);
                }
                builder.append(Node.process(degree, settings));
                settings.setNumberFormat(NumberFormat.CARDINAL);
                builder.append(functionName);
                builder.append(Node.process(from, settings));
                return builder.toString();
            }
            case LOGARITHM: {
                builder.append(functionName);
                MathMLNode base;
                if (node.getChildren().size() == 2) {
                    builder.append(settings.getNumberTransformer().transform("10"));
                    builder.append(settings.getProperty("logarithm_from"));
                    builder.append(Node.process(node.getChildren().get(1), settings));
                } else {
                    if (MathMLElement.LOGBASE.equals(node.getChildren().get(1).getType())) {
                        base = node.getChildren().get(1).getChildren().get(0);
                    } else {
                        base = node.getChildren().get(1);
                    }
                    builder.append(Node.process(base, settings));
                    builder.append(settings.getProperty("logarithm_from"));
                    builder.append(Node.process(node.getChildren().get(2), settings));
                }
                return builder.toString();
            }
            case SUBTRACT: {
                if (node.getChildren().size() == 2) {
                    // unary function (minus one, etc)
                    builder.append(functionName);
                    builder.append(Node.process(node.getChildren().get(1), settings));
                    return builder.toString();
                } else {
                    break;
                }
            }
            default: // do nothing
        }
        
        if (Operation.SUBTRACT.equals(operation) && node.getChildren().size() == 2) {
            // unary function (minus one, etc)
            builder.append(functionName);
            builder.append(Node.process(node.getChildren().get(1), settings));
            return builder.toString();
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
            case CONTENT_BEFORE_MIDDLE: {
                builder.append(functionName);
                builder.append(Node.process(node.getChildren().get(1), settings));
                builder.append(settings.getProperty(Operation.DIVIDE.getKey()));
                builder.append(Node.process(node.getChildren().get(2), settings));
                return builder.toString();
            }
            default: // do nothing
        }
        
        
        
        return builder.toString();
    }
}