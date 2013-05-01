package cz.muni.fi.mathml.mathml2text.converter.impl;

import cz.muni.fi.mathml.mathml2text.converter.Strings;
import cz.muni.fi.mathml.mathml2text.converter.impl.content.Apply;
import cz.muni.fi.mathml.mathml2text.converter.impl.content.Ci;
import cz.muni.fi.mathml.mathml2text.converter.impl.content.Cn;
import cz.muni.fi.mathml.mathml2text.converter.impl.presentation.Mfrac;
import cz.muni.fi.mathml.mathml2text.converter.impl.presentation.Mi;
import cz.muni.fi.mathml.mathml2text.converter.impl.presentation.Mn;
import cz.muni.fi.mathml.mathml2text.converter.impl.presentation.Mo;
import cz.muni.fi.mathml.mathml2text.converter.impl.presentation.Mroot;
import cz.muni.fi.mathml.mathml2text.converter.impl.presentation.Msqrt;
import cz.muni.fi.mathml.mathml2text.converter.impl.presentation.Msub;
import cz.muni.fi.mathml.mathml2text.converter.impl.presentation.Msup;
import cz.muni.fi.mathml.mathml2text.converter.impl.presentation.Munder;
import cz.muni.fi.mathml.mathml2text.converter.impl.presentation.Munderover;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;

/**
 * General MathML node.
 * 
 * @author Maros Kucbel
 * @date 2012-12-15T09:58:02+0100
 */
public final class Node {
    
    /**
     * Processes given node.
     * Based on type of input node calls specific node implementations.
     * @param node Input node.
     * @param settings Converter settings.
     * @return Content of the input node converted to string.
     */
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