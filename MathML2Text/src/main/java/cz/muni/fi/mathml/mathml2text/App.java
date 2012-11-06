package cz.muni.fi.mathml.mathml2text;

import java.io.File;
import java.util.List;
import java.util.Locale;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLConverter;
import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLNode;
import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLTransformerDOM;
import cz.muni.fi.mathml.mathml2text.transformer.impl.MathMLTransformerStAX;
import cz.muni.fi.mathml.mathml2text.transformer.impl.XmlParserStAX;

/**
 * Hello world!
 *
 */
public class App {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    
    public static void main(final String[] args) {
//        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        final Instant start = Instant.now();

//        final Locale locale = new Locale("en");
//        final IMathMLTransformer transformer = new MathMLTransformer(locale);
//        final Source source = new StreamSource(new File("d:\\Projects\\ex4.xml"));
//        try {
//            final String transformMathML = transformer.transformMathML(source, locale.getLanguage());
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info(transformMathML);
//            }
//        } catch (TransformerConfigurationException ex) {
//            LOGGER.error("Cannot load transformer.", ex);
//        } catch (TransformerException ex) {
//            LOGGER.error("Error during transformartion.", ex);
//        } catch (IllegalArgumentException ex) {
//            LOGGER.error("Invalid method parameters.", ex);
//        }
        
//        final MathMLTransformerStAX transformer = new MathMLTransformerStAX();
//        transformer.parse(Lists.<File>newArrayList(new File("d:\\Projects\\math.0001002.xhtml")), Locale.ENGLISH);
        
//        final MathMLTransformerDOM transformer = new MathMLTransformerDOM();
//        transformer.parse(Lists.<File>newArrayList(new File("d:\\Projects\\ex2.xml")), Locale.ENGLISH);
        
        final XmlParserStAX parser = new XmlParserStAX();
//        final List<MathMLNode> nodeList = parser.parse(Lists.<File>newArrayList(new File("d:\\Projects\\math.0001002.xhtml")));
        
        final List<MathMLNode> nodeList = parser.parse(Lists.<File>newArrayList(new File("d:\\Projects\\ex3.xml")));
        final MathMLConverter converter = new MathMLConverter();
        final List<String> convert = converter.convert(nodeList, Locale.ENGLISH);
        for (final String s : convert) {
            System.out.println(s);
        }
        
//        System.out.println(nodeList);
        System.out.println(nodeList.size());
        
        final Instant end = Instant.now();
        final Duration duration = new Duration(start, end);
        System.out.println("\n\n" + duration.getMillis());
    }
}
