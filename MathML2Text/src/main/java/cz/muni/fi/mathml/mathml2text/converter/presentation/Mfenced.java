package cz.muni.fi.mathml.mathml2text.converter.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.Node;
import cz.muni.fi.mathml.mathml2text.converter.operation.Symbol;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.tree.XmlAttribute;

/**
 * Specific implementation of <code>&lt;mfenced&gt;</code> node.
 * 
 * @author Maros Kucbel
 */
public final class Mfenced {

    private static final Logger logger = LoggerFactory.getLogger(Mfenced.class);
    
    /**
     * Processes input node.
     * @param node Input node.
     * @param settings Converter settings.
     * @return Content of the input node converted to string.
     */
    public static String process(final MathMLNode node, final ConverterSettings settings) {
        final StringBuilder builder = new StringBuilder();
        String openBraces = settings.getProperty("open_braces");
        String closeBraces = settings.getProperty("close_braces");
        for (final XmlAttribute attr : node.getAttributes()) {
            if ("open".equals(attr.getKey())) {
                final Symbol symbol = Symbol.forValue(attr.getValue());
                if (symbol == null) {
                    logger.warn("Uknown braces [{}].", attr.getValue());
                } else {
                    openBraces = settings.getProperty(symbol.getKey());
                }
            }
            if ("close".equals(attr.getKey())) {
                final Symbol symbol = Symbol.forValue(attr.getValue());
                if (symbol == null) {
                    logger.warn("Uknown braces [{}].", attr.getValue());
                } else if (Symbol.ABS.equals(symbol)) { 
                    closeBraces = settings.getProperty("abs_close");
                } else {
                    closeBraces = settings.getProperty(symbol.getKey());
                }
            }
        }
        builder.append(openBraces);
        for (int index = 0; index < node.getChildren().size(); ++index) {
            final MathMLNode child = node.getChildren().get(index);
            builder.append(Node.process(child, settings));
            if (index < node.getChildren().size() - 1) {
                builder.append(settings.getProperty("comma"));
            }
        }
        builder.append(closeBraces);
        return builder.toString();
    }
}