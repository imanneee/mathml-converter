package cz.muni.fi.mathml.mathml2text.converter.impl.content;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.MathMLElement;
import cz.muni.fi.mathml.mathml2text.converter.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.impl.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.impl.Node;
import cz.muni.fi.mathml.mathml2text.converter.impl.Operation;
import cz.muni.fi.mathml.mathml2text.converter.numbers.NumberFormat;

/**
 * Specific implementation of <code>&lt;apply&gt;</code> node.
 * Based on the type of mathematic operation, different approach to conversion
 * is taken.
 * 
 * @author Maros Kucbel
 * @date 2012-12-15T11:45:41+0100
 */
public final class Apply {

    private static final Logger logger = LoggerFactory.getLogger(Apply.class);
    
    public static String process(final MathMLNode node, final ConverterSettings settings) {
        final StringBuilder builder = new StringBuilder();
        // first element is function
        
        String function;
        MathMLNode firstChild = node.getChildren().get(0);
        if (MathMLElement.CSYMBOL.equals(firstChild.getType())) {
            // function can be defined in csymbol
            function = firstChild.getValue();
        } else if (MathMLElement.APPLY.equals(firstChild.getType())) {
            // or another function can be used in apply
            builder.append(Node.process(firstChild, settings));
            builder.append(settings.getProperty("applied_to"));
            for (int index = 1; index < node.getChildren().size(); ++index) {
                builder.append(Node.process(node.getChildren().get(index), settings));
            }
            return builder.toString();
        } else if (MathMLElement.CI.equals(firstChild.getType()) && node.getChildren().size() > 1) {
            // function can be defined in ci
            function = firstChild.getValue();
        } else {
            // if nothing else holds true, we will assume that first child is function definition
            function = firstChild.getType().getElementName();
        }
        // mark first child as processed to prevent its conversion at later time
        firstChild.setProcessed();
        // find operation for given function, if no function was defined or 
        // operation is not defined in enumeration return current content in
        // StringBuilder
        Operation operation = Operation.forSymbol(function);
        if (operation == null) {
            logger.info("Unknown operation [{}]", function);
            return builder.toString();
        }
         
        String functionName = settings.getProperty(operation.getKey());
        if (StringUtils.isBlank(functionName)) {
            logger.info("Unknown function [{}]", function);
        }
        
        // process based on operation
        switch (operation) {
            case SUPERSCRIPT: case SUBSCRIPT: case APPROACHES: {
                builder.append(Node.process(node.getChildren().get(1), settings));
                builder.append(functionName);
                builder.append(Node.process(node.getChildren().get(2), settings));
                return builder.toString();
            }
            case ASSIGN: {
                builder.append(functionName);
                builder.append(Node.process(node.getChildren().get(1), settings));
                builder.append(settings.getProperty("value"));
                builder.append(Node.process(node.getChildren().get(2), settings));
                return builder.toString();
            }
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
            case TILDE: case DASHED: {
                builder.append(Node.process(node.getChildren().get(1), settings));
                builder.append(functionName);
                return builder.toString();
            }
            case INTERVAL: {
                builder.append(functionName);
                builder.append(settings.getProperty("from"));
                builder.append(Node.process(node.getChildren().get(1), settings));
                builder.append(settings.getProperty("to"));
                builder.append(Node.process(node.getChildren().get(2), settings));
                return builder.toString();
            }
            case COMPOSE: {
                builder.append(functionName);
                builder.append(Node.process(node.getChildren().get(1), settings));
                for (int index = 2; index < node.getChildren().size(); ++index) {
                    if (index + 1 == node.getChildren().size()) {
                        builder.append(settings.getProperty("and"));
                    } else {
                        builder.append(", ");
                    }
                    builder.append(Node.process(node.getChildren().get(index), settings));
                }
                return builder.toString();
            }
            case VECTOR: {
                builder.append(functionName);
                builder.append(settings.getProperty("first_value"));
                builder.append(Node.process(node.getChildren().get(1), settings));
                for (int index = 2; index < node.getChildren().size(); ++index) {
                    builder.append(settings.getProperty("next_value"));
                    builder.append(Node.process(node.getChildren().get(index), settings));
                }
                builder.append(settings.getProperty("vector_end"));
                return builder.toString();
            }
            default: // do nothing
        }
        
        if (Operation.SUBTRACT.equals(operation) && node.getChildren().size() == 2) {
            // unary function (minus one, etc)
            builder.append(functionName);
            builder.append(Node.process(node.getChildren().get(1), settings));
            return builder.toString();
        }

        // try to corresponding element to given function, if none is found
        // try a look up for operation
        // this is done to simplify number of cases needed for processing, 
        // as elements can be sorted into groups
        MathMLElement functionElement = MathMLElement.forElementName(function);
        if (MathMLElement.UNKNOWN.equals(functionElement)) {
            functionElement = MathMLElement.forElementName(operation.getKey());
        }
        switch (functionElement.getType()) {
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