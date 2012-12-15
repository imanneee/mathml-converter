package cz.muni.fi.mathml.mathml2text.transformer.impl.converter.presentation;

import java.util.Properties;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.Strings;
import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.transformer.impl.Operation;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.transformer.impl.converter.MathMLConverter;

/**
 *
 * @author Maros Kucbel
 * @date 2012-12-14T17:16:51+0100
 */
public final class Mo {

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
            return settings.getProperty(operator.getKey());
        } else {
            LoggerFactory.getLogger(MathMLConverter.class).warn("Operation [{}] not supported.", op);
        }
        return Strings.EMPTY;
    }
}