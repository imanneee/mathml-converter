package cz.muni.fi.mathml.mathml2text.converter;

import cz.muni.fi.mathml.mathml2text.converter.content.Apply;
import cz.muni.fi.mathml.mathml2text.converter.content.Ci;
import cz.muni.fi.mathml.mathml2text.converter.content.Cn;
import cz.muni.fi.mathml.mathml2text.converter.presentation.Mfenced;
import cz.muni.fi.mathml.mathml2text.converter.presentation.Mfrac;
import cz.muni.fi.mathml.mathml2text.converter.presentation.Mi;
import cz.muni.fi.mathml.mathml2text.converter.presentation.Mn;
import cz.muni.fi.mathml.mathml2text.converter.presentation.Mo;
import cz.muni.fi.mathml.mathml2text.converter.presentation.Mover;
import cz.muni.fi.mathml.mathml2text.converter.presentation.Mroot;
import cz.muni.fi.mathml.mathml2text.converter.presentation.Msqrt;
import cz.muni.fi.mathml.mathml2text.converter.presentation.Msub;
import cz.muni.fi.mathml.mathml2text.converter.presentation.Msup;
import cz.muni.fi.mathml.mathml2text.converter.presentation.Munder;
import cz.muni.fi.mathml.mathml2text.converter.presentation.Munderover;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLType;

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
            case ANNOTATION_XML: {
                // if we encounter annotation-xml element, then we are processing
                // Presentation MathML and it is time to stop
                break;
            }
            case MROW: {
                // mrow acts as a grouping element
                //@todo Make some kind of mark to divide long formulae.
                final boolean enclose = node.getChildren().size() > 1 
                        && node.getParent() != null 
                        && MathMLType.PRESENTATION.equals(node.getParent().getType().getType());
                if (enclose) {
                    builder.append(settings.getProperty("open_braces"));
                }
                for (final MathMLNode child : node.getChildren()) {
                    builder.append(Node.process(child, settings));
                }
                if (enclose) {
                    builder.append(settings.getProperty("close_braces"));
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
            case MOVER: {
                builder.append(Mover.process(node, settings));
                break;
            }
            case MUNDEROVER: case MSUBSUP: {
                builder.append(Munderover.process(node, settings));
                break;
            }
            case MFENCED: {
                builder.append(Mfenced.process(node, settings));
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