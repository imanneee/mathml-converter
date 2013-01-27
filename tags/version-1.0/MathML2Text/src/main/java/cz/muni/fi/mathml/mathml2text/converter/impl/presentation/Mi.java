package cz.muni.fi.mathml.mathml2text.converter.impl.presentation;

import org.apache.commons.lang3.StringEscapeUtils;

import cz.muni.fi.mathml.mathml2text.Strings;
import cz.muni.fi.mathml.mathml2text.converter.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.impl.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.impl.Operation;
import cz.muni.fi.mathml.mathml2text.converter.impl.Symbol;

/**
 * Specific implementation of <code>&lt;mi&gt;</code> node.
 * 
 * @author Maros Kucbel
 * @date 2012-12-14T17:14:36+0100
 */
public final class Mi {

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