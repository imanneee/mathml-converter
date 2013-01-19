package cz.muni.fi.mathml.mathml2text.converter.impl.presentation;

import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.Strings;
import cz.muni.fi.mathml.mathml2text.converter.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.impl.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.impl.MathMLConverter;

/**
 * Specific implementation of <code>&lt;mn&gt;</code> node.
 * 
 * @author Maros Kucbel
 * @date 2012-12-15T10:01:49+0100
 */
public final class Mn {

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
        if (node.getValue() == null) {
            throw new IllegalStateException("[mi] node should have its value set.");
        }
        try {
            final String number = settings.getNumberTransformer().transform(node.getValue());
            return number + Strings.SPACE;
        } catch (final NumberFormatException ex) {
            LoggerFactory.getLogger(MathMLConverter.class).warn(String.format("Cannot transform string [%1$s] to number.", node.getValue()), ex);
        }
        return Strings.EMPTY;
    }
}