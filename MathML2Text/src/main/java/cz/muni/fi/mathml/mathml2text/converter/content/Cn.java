package cz.muni.fi.mathml.mathml2text.converter.content;

import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.converter.Strings;
import cz.muni.fi.mathml.mathml2text.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.tree.XmlAttribute;

/**
 * Specific implementation of <code>&lt;cn&gt;</code> node.
 * 
 * @author Maros Kucbel
 * @date 2012-12-15T11:42:18+0100
 */
public final class Cn {

    /**
     * Processes input node.
     * @param node Input node.
     * @param settings Converter settings.
     * @return Content of the input node converted to string.
     */
    public static String process(final MathMLNode node, final ConverterSettings settings) {
        if (node.getValue() == null) {
            throw new IllegalStateException("[mi] node should have its value set.");
        }
        final StringBuilder builder = new StringBuilder();
        for (final XmlAttribute attr : node.getAttributes()) {
            if (("base".equals(attr.getKey()) && !"10".equals(attr.getValue()))
                    || ("type".equals(attr.getKey()) && "hexdouble".equals(attr.getValue()))) {
                builder.append(node.getValue());
                builder.append(Strings.SPACE);
                return builder.toString();
            }
        }
        try {
            String strippedValue = node.getValue().trim();
            for (final Character c : Strings.VALUE_EMPTY_CHARS) {
                strippedValue = strippedValue.replace(c.toString(), Strings.EMPTY);
            } 
            final String number = settings.isTransformNumbers() 
                                  ? settings.getNumberTransformer().transform(strippedValue) 
                                  : strippedValue;
            return number.trim() + Strings.SPACE;
        } catch (final NumberFormatException ex) {
            LoggerFactory.getLogger(Cn.class).warn(String.format("Cannot transform string [%1$s] to number.", node.getValue()), ex);
        } catch (final NullPointerException ex) {
            LoggerFactory.getLogger(Cn.class).warn(String.format("Cannot transform string [%1$s] to number.", node.getValue()), ex);
        }
        return Strings.EMPTY;
    }
}