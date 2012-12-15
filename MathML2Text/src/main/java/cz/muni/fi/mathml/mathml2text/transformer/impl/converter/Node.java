package cz.muni.fi.mathml.mathml2text.transformer.impl.converter;


import cz.muni.fi.mathml.mathml2text.Strings;
import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.content.*;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.presentation.*;

/**
 *
 * @author Maros Kucbel
 * @date 2012-12-15T09:58:02+0100
 */
public final class Node {
    
    public static String process(final MathMLNode node, 
                                 final ConverterSettings settings) {
        if (node.isProcessed()) {
            return Strings.EMPTY;
        }
        final StringBuilder builder = new StringBuilder();
        
        switch (node.getType()) {
            case MATH: case SEMANTICS: {
                // root element, iterate through children
                for (final MathMLNode child : node.getChildren()) {
                    builder.append(Node.process(child, settings));
                }
                break;
            }
            case MROW: {
                // mrow acts as a grouping element
                //@todo Make some kind of mark to divide long formulae.
                for (final MathMLNode child : node.getChildren()) {
                    builder.append(Node.process(child, settings));
                }
                break;
            }
            case MI: {
                builder.append(Mi.process(node, settings));
                break;
            }
            case MO: {
                builder.append(Mo.process(node, settings));
                break;
            }
            case MN: {
                builder.append(Mn.process(node, settings));
                break;
            }
            case MFRAC: {    
                builder.append(Mfrac.process(node, settings));
                break;
            }
            case MSQRT: {
                builder.append(Msqrt.process(node, settings));
                break;
            }
            case MROOT: {
                builder.append(Mroot.process(node, settings));
                break;
            }    
            case MSUB: {
                builder.append(Msub.process(node, settings));
                break;
            }
            case MSUP: {
                builder.append(Msup.process(node, settings));
                break;
            }
            case MUNDER: {
                builder.append(Munder.process(node, settings));
                break;
            } 
            case MUNDEROVER: case MSUBSUP: {
                builder.append(Munderover.process(node, settings));
                break;
            }
            case CN: {
                builder.append(Cn.process(node, settings));
                break;
            }
            case CI: {
                builder.append(Ci.process(node, settings));
                break;
            }
            case APPLY: {
                builder.append(Apply.process(node, settings));
                break;
            }
            default: {
                if (node.getChildren().isEmpty()) {
                    builder.append(node.getValue());
                } else {
                    for (final MathMLNode child : node.getChildren()) {
                        builder.append(Node.process(child, settings));
                    }
                }
                break;
            }
        }
        node.setProcessed();
        return builder.toString();
    }
}