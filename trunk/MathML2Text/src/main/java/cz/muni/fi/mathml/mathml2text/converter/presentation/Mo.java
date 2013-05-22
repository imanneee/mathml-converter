package cz.muni.fi.mathml.mathml2text.converter.presentation;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.Strings;
import cz.muni.fi.mathml.mathml2text.converter.operation.Operation;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.util.InputValueUtils;

/**
 * Specific implementation of <code>&lt;mo&gt;</code> node.
 * 
 * @author Maros Kucbel
 * @date 2012-12-14T17:16:51+0100
 */
public final class Mo {

    /**
     * Processes input node.
     * @param node Input node.
     * @param settings Converter settings.
     * @return Content of the input node converted to string.
     */
    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (!node.getChildren().isEmpty()) {
            throw new IllegalStateException("[mo] node should not have children.");
        }
        if (node.getValue() == null) {
            throw new IllegalStateException("[mo] node should have its value set.");
        }
        final String op = StringEscapeUtils.escapeHtml4(node.getValue());
        final Operation operator = Operation.forSymbol(op);
        if (operator != null) {
            if (Operation.ABSOLUTE_VALUE.equals(operator)) {
                int absCounter = 0;
                for (MathMLNode child : node.getParent().getChildren()) {
                    if (child.equals(node)) {
                        break;
                    }
                    if (Operation.ABSOLUTE_VALUE.getSymbols().contains(child.getValue())) {
                        absCounter++;
                    }
                }
                if (absCounter % 2 == 1) {
                    return settings.getProperty("abs_close");
                }
            }
            
            return settings.getProperty(operator.getKey());
        } else {
            final String htmlEntity = InputValueUtils.buildHtmlEntityCode(node.getValue());
            LoggerFactory.getLogger(Mo.class).warn("Operation [{}] not supported. Original operator [{}].", htmlEntity, op);
            return htmlEntity + Strings.SPACE;
        }
    }
}