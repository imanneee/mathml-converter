package cz.muni.fi.mathml.mathml2text.converter.content;

import org.apache.commons.lang3.StringEscapeUtils;

import cz.muni.fi.mathml.mathml2text.converter.Strings;
import cz.muni.fi.mathml.mathml2text.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.operation.Operation;
import cz.muni.fi.mathml.mathml2text.converter.operation.Symbol;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;

/**
 * Specific implementation of <code>&lt;ci&gt;</code> node.
 * 
 * @author Maros Kucbel
 * @date 2012-12-15T11:45:01+0100
 */
public final class Ci {

    /**
     * Processes input node.
     * @param node Input node.
     * @param settings Converter settings.
     * @return Content of the input node converted to string.
     */
    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (!node.getChildren().isEmpty()) {
            throw new IllegalStateException("[mi] node should not have children.");
        }
        final String identifier = StringEscapeUtils.escapeHtml4(node.getValue());
        Operation operation = Operation.forSymbol(identifier);
        if (operation != null) {
            return settings.getProperty(operation.getKey());
        }
        // it's not operation let's try symbol
        Symbol symbol = Symbol.forValue(identifier);
        if (symbol != null) {
            return settings.getProperty(symbol.getKey());
        }
        return identifier + Strings.SPACE;
    }
}