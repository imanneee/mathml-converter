package cz.muni.fi.mathml.mathml2text.converter.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.Node;
import cz.muni.fi.mathml.mathml2text.converter.Strings;
import cz.muni.fi.mathml.mathml2text.converter.operation.Operation;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLElement;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;
import cz.muni.fi.mathml.mathml2text.numbers.NumberFormat;

/**
 * Specific implementation of <code>&lt;apply&gt;</code> node.
 * Based on the type of mathematic operation, different approach to conversion
 * is taken.
 * 
 * @author Maros Kucbel
 * @date 2012-12-15T11:45:41+0100
 */
public final class Apply {

    //@todo sum, product
    
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
            //@todo maybe do not return but process other children (can there be any?)
            builder.append(Node.process(firstChild, settings));
            builder.append(settings.getProperty("applied_to"));
            for (int index = 1; index < node.getChildren().size(); ++index) {
                builder.append(Node.process(node.getChildren().get(index), settings));
            }
            return builder.toString();
        } else if (MathMLElement.CI.equals(firstChild.getType()) && node.getChildren().size() > 1) {
            // function can be defined in ci
            function = firstChild.getValue();
        } else if (MathMLElement.MTEXT.equals(firstChild.getType())) {
            // ignore
            return Strings.EMPTY;
        } else {
            // if nothing else holds true, we will assume that first child is function definition
            function = firstChild.getType().getElementName();
        }
        // mark first child as processed to prevent its conversion at later time
        firstChild.setProcessed();
        
        // if the function is defined by cn or ci element but has no arguments
        // return just the function (this is probably due to the not-so-correct
        // conversion of LaTeX files to MathML)
        if ((MathMLElement.CN.getElementName().equals(function) 
                || MathMLElement.CI.getElementName().equals(function)) 
                && node.getChildren().size() == 1) {
            //@todo some apply elements have only one child - cn/ci - what to do?
            builder.append(Node.process(firstChild, settings));
            return builder.toString();
        }
        
        // find operation for given function, if no function was defined or 
        // operation is not defined in enumeration return current content in
        // StringBuilder
        Operation operation = Operation.forSymbol(function);
        if (operation == null) {
            if (function == null) {
                logger.debug("Function is null. First child: [{}]", firstChild.getType().getElementName());
                return Strings.EMPTY;
            }
            List<Integer> chars = new ArrayList<Integer>();
            for (int i = 0; i < function.length(); ++i) {
                chars.add((int)function.charAt(i));
            }
            String join = StringUtils.join(chars, ",");
            logger.info("Unknown operation [{}] [{}]", function, join);
            
            // treat this function as user defined (not a well known mathematical operation)
            // all other child elements of current apply element are considered parameters of this function
            builder.append(function);
            builder.append(Strings.SPACE);
            builder.append(settings.getProperty("with_parameters"));
            for (int index = 1; index < node.getChildren().size(); ++index) {
                builder.append(Node.process(node.getChildren().get(index), settings));
            }
            
            return builder.toString();
        }
         
        String functionName = settings.getProperty(operation.getKey());
        if (StringUtils.isBlank(functionName)) {
            logger.info("Unknown function [{}]", function);
        }
        
        if ((Operation.ADD.equals(operation) || Operation.SUBTRACT.equals(operation)) 
                && node.getChildren().size() == 1) {
            // standalone plus or minus sign
            builder.append(functionName);
            return builder.toString();
        }
        
        // process operation based on its type
        switch (operation.getType()) {
            case INFIX: {
                if (node.getChildren().size() == 1) {
                    logger.debug("Infix operator [{}] has no arguments.", operation.getKey());
                    builder.append(functionName);
                    return builder.toString();
                }
                builder.append(Node.process(node.getChildren().get(1), settings));
                int count = node.getChildren().size();
                for (int index = 2; index < count; ++index) {
                    builder.append(functionName);
                    builder.append(Node.process(node.getChildren().get(index), settings));
                }
                return builder.toString();
            }
            case PREFIX: {
                builder.append(functionName);
                builder.append(Node.process(node.getChildren().get(1), settings));
                return builder.toString();
            }
            case PREFIX_MULTI: {
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
            case EVERY_ARGUMENT: {
                for (int index = 1; index < node.getChildren().size(); ++index) {
                    builder.append(Node.process(node.getChildren().get(index), settings));
                    builder.append(settings.getProperty("with"));
                    builder.append(functionName);
                }
            }
            case INFIX_OR_PREFIX: {
                // based on the number of children the operation is either in prefix form (number == 2)
                // or in infix form (number > 2)
                if (node.getChildren().size() == 2) {
                    builder.append(functionName);
                    builder.append(Node.process(node.getChildren().get(1), settings));
                    return builder.toString();
                } else {
                    builder.append(Node.process(node.getChildren().get(1), settings));
                    int count = node.getChildren().size();
                    for (int index = 2; index < count; ++index) {
                        builder.append(functionName);
                        builder.append(Node.process(node.getChildren().get(index), settings));
                    }
                    return builder.toString();
                }
            }
            case WITH_INTERVAL: {
                final MathMLNode secondChild = node.getChildren().get(1);
                if (node.getChildren().size() == 2) {
                    builder.append(functionName);
                    builder.append(settings.getProperty("of"));
                    builder.append(Node.process(secondChild, settings));
                    return builder.toString();
                }
                
                if (MathMLElement.DOMAIN_OF_APPLICATION.equals(secondChild.getType())) {
                    builder.append(functionName);
                    builder.append(settings.getProperty("over"));
                    builder.append(settings.getProperty("domain"));
                    builder.append(Node.process(secondChild, settings)); // domain
                    builder.append(settings.getProperty("of"));
                    builder.append(Node.process(node.getChildren().get(2), settings)); // function
                } else if (MathMLElement.BVAR.equals(secondChild.getType())) {
                    if (MathMLElement.LOWLIMIT.equals(node.getChildren().get(2).getType())) {
                        builder.append(functionName);
                        builder.append(settings.getProperty("over"));
                        builder.append(Node.process(secondChild, settings));
                        builder.append(settings.getProperty("from"));
                        builder.append(Node.process(node.getChildren().get(2), settings)); // lowlimit
                        builder.append(settings.getProperty("to"));
                        builder.append(Node.process(node.getChildren().get(3), settings)); // uplimit
                        builder.append(settings.getProperty("of"));
                        builder.append(Node.process(node.getChildren().get(4), settings)); // function
                    } else if (MathMLElement.CONDITION.equals(node.getChildren().get(2).getType())) {
                        builder.append(functionName);
                        builder.append(settings.getProperty("over"));
                        builder.append(Node.process(secondChild, settings));
                        builder.append(settings.getProperty("where"));
                        builder.append(Node.process(node.getChildren().get(2), settings)); // condition
                        builder.append(settings.getProperty("of"));
                        builder.append(Node.process(node.getChildren().get(3), settings)); // function
                    } else {
                        logger.warn("Bound variable has no limits.");
                    }
                } else if (MathMLElement.INTERVAL.equals(secondChild.getType())) {
                     builder.append(functionName);
                     builder.append(settings.getProperty("from"));
                     builder.append(Node.process(secondChild.getChildren().get(0), settings));
                     builder.append(settings.getProperty("to"));
                     builder.append(Node.process(secondChild.getChildren().get(1), settings));
                     builder.append(settings.getProperty("of"));
                     builder.append(Node.process(node.getChildren().get(2), settings)); // function
                     
                } else {
                    logger.warn("Unknown variable definition [{}] of [{}].", 
                            secondChild.getType().getElementName(), operation.getKey());
                }
                return builder.toString();
            }
            default: break;
        }
        
        // all other operations have to be processed individually
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
            case INTERVAL: {
                builder.append(functionName);
                builder.append(settings.getProperty("from"));
                builder.append(Node.process(node.getChildren().get(1), settings));
                builder.append(settings.getProperty("to"));
                builder.append(Node.process(node.getChildren().get(2), settings));
                return builder.toString();
            }
            case COMPOSE: {
                if (node.getChildren().size() < 2) {
                    // nothing to compose
                    return builder.toString();
                }
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
            case UPWARDS_ARROW: case DOWNWARDS_ARROW: {
                for (int index = 1; index < node.getChildren().size(); ++index) {
                    builder.append(Node.process(node.getChildren().get(index), settings));
                    builder.append(functionName);
                }
                break;
            }
            case FUNCTION_APPLICATION: {
                // I don't know what function application means in Content MathML
                for (int index = 1; index < node.getChildren().size(); ++index) {
                    builder.append(Node.process(node.getChildren().get(index), settings));
                }
                break;
            }
            default: {
                logger.debug("Operation [{}] was not processed.", operation.getKey());
                break;
            }
        }
        
        // try to corresponding element to given function, if none is found
        // try a look up for operation
        // this is done to simplify number of cases needed for processing, 
        // as elements can be sorted into groups
        
        return builder.toString();
    }
}