package cz.muni.fi.mathml.mathml2text.converter.impl.converter.content;

import org.apache.commons.lang3.StringEscapeUtils;

import cz.muni.fi.mathml.mathml2text.Strings;
import cz.muni.fi.mathml.mathml2text.converter.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.impl.Operation;
import cz.muni.fi.mathml.mathml2text.converter.impl.converter.ConverterSettings;

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
        return operation != null ? settings.getProperty(operation.getKey()) : identifier + Strings.SPACE;
    }
}