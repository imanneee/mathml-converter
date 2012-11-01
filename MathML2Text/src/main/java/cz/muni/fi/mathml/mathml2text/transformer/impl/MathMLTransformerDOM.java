package cz.muni.fi.mathml.mathml2text.transformer.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.Validate;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.MathMLElement;
import cz.muni.fi.mathml.mathml2text.Strings;
import cz.muni.fi.mathml.mathml2text.transformer.IMathMLTransformer;
import cz.muni.fi.mathml.mathml2text.transformer.numbers.NumberTransformer;

/**
 *
 * @author Maros Kucbel
 * @date 2012-10-22T19:43:14+0200
 */
public final class MathMLTransformerDOM implements IMathMLTransformer {

    private final Logger logger = LoggerFactory.getLogger(MathMLTransformerDOM.class);
    
    /**
     * Localization properties for locales.
     */
    @Nonnull
    private Map<Locale, Properties> localizationMap = new HashMap<Locale, Properties>();
    
    /**
     * Returns logger for this class.
     */
    private Logger getLogger() {
        return this.logger;
    }
    
    /**
     * Returns localization properties for given locale.
     */
    private Properties getLocalization(final Locale locale) {
        if (this.localizationMap.get(locale) == null) {
            final Properties properties = new Properties();
            final InputStream resourceAsStream = this.getClass().getResourceAsStream(String.format("%1$s.xml", locale.getLanguage()));
            try {
                properties.loadFromXML(resourceAsStream);
                this.localizationMap.put(locale, properties);
            } catch (final IOException ex) {
                this.getLogger().error(String.format("Cannot load properties for language [%1$s].", locale.getLanguage()), ex);
            }
        }
        return this.localizationMap.get(locale);
    }
    
    @Override
    public String transformMathML(Source input, String language) throws TransformerConfigurationException, TransformerException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void transformMathML(File input, File targetDirectory, String language) throws TransformerConfigurationException, TransformerException, IllegalAccessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void transform(final Collection<File> filesToTransform, final Locale language) {
        Validate.isTrue(filesToTransform != null, "List of files for transformation should not be null.");
        Validate.notEmpty(filesToTransform, "List of files for transformation should not be empty.");
        Validate.noNullElements(filesToTransform, "List of files for transformation should not contain null elements.");
        Validate.isTrue(language != null, "Language of transformation should not be null.");
        
        for (final File file : filesToTransform) {
            try {
                this.transform0(file, language);
            } catch (final IOException ex) {
                this.getLogger().error("Input file cannot be opened.", ex);
            } catch (final JDOMException ex) {
                this.getLogger().error("Error while parsing input file.", ex);
            }
        }
    }
    
    private void transform0(final File file, final Locale language) throws IOException, JDOMException {
        final SAXBuilder documentBuilder = new SAXBuilder();
        final Document document = documentBuilder.build(file);
        final Element rootElement = document.getRootElement();
        final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        this.process(rootElement, language);
        final String result = outputter.outputString(document);
        System.out.println(result);
        //        final NumberTransformer numberTransformer = new NumberTransformer(language);
        //        for (final Element math : mathElements) {
        //            final StringBuilder builder = new StringBuilder();
        //            builder.append("=======================\n");
        //            this.processElement(math, numberTransformer, builder);
        //            builder.append("\n=======================\n");
        //            System.out.println(builder.toString());
        //        }
    }
    
    private void process(final Element element, final Locale language) {
        if (MathMLElement.MATH.getElementName().equals(element.getName())) {
            // process math element
            final StringBuilder builder = new StringBuilder();
            final NumberTransformer numberTransformer = new NumberTransformer(language);
            this.processElement(element, numberTransformer, builder);
            element.setName("mathText");
            element.removeContent();
            element.setText(builder.toString());
        } else {
            for (final Element child : element.getChildren()) {
                this.process(child, language);
            }
        }
    }
    
    /**
     * Finds all <code>math</code> element descendants (children, grandchildren, etc.) 
     * of root element (or any other).
     * It's assumed that there cannot be a <code>math</code> element inside another
     * <code>math</code> element.
     * 
     * @param root Element whose descendants with name <code>math</code> we want to find.
     * @return List of all descendants of given element with name <code>math</code>.
     */
    private List<Element> findMathElements(final Element root) {
        final List<Element> maths = new ArrayList<Element>();
        if (MathMLElement.MATH.getElementName().equals(root.getName())) {
            maths.add(root);
            return maths;
        }
        for (final Element child : root.getChildren()) {
            maths.addAll(this.findMathElements(child));
        }
        return maths;
    }
    
    private void processElement(final Element element, final NumberTransformer numberTransformer, final StringBuilder builder) {
        for (final Element child : element.getChildren()) {
            final MathMLElement m = MathMLElement.forElementName(child.getName());
            switch (m) {
                case MROW: case MATH: {
                    this.processElement(child, numberTransformer, builder);
                    break;
                }
                case MI: {
                    builder.append(child.getValue());
                    builder.append(Strings.SPACE);
                    break;
                }
                case MN: {
                    final String number = numberTransformer.transformNumber(child.getValue());
                    builder.append(number);
                    builder.append(Strings.SPACE);
                    break;
                }    
                case MO: {
                    //@todo process operation (function)
                    final String op = StringEscapeUtils.escapeHtml4(child.getValue());
                    this.getLogger().debug(String.format("[%1$s]", op));
                    final Properties localization = this.getLocalization(numberTransformer.getLanguage());
                    final Operation operator = Operation.forSymbol(op);
                    if (operator != null) {
                        builder.append(localization.getProperty(operator.getKey()));
                        builder.append(Strings.SPACE);
                    } else {
                        if (this.getLogger().isWarnEnabled()) {
                            this.getLogger().warn("Operation [{}] not supported.", op);
                        }
                    }
                    break;
                }
                default: break;
            }
        }
    }
    
}