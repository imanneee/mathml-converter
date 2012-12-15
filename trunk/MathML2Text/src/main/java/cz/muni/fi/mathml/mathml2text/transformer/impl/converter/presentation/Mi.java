package cz.muni.fi.mathml.mathml2text.transformer.impl.converter.presentation;

import org.apache.commons.lang3.StringEscapeUtils;

import cz.muni.fi.mathml.mathml2text.Strings;
import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.ConverterSettings;

/**
 *
 * @author Maros Kucbel
 * @date 2012-12-14T17:14:36+0100
 */
public final class Mi {

    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (!node.getChildren().isEmpty()) {
            throw new IllegalStateException("[mi] node should not have children.");
        }
        final String identifier = StringEscapeUtils.escapeHtml4(node.getValue());
        return identifier + Strings.SPACE;
    }
}