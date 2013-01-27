package cz.muni.fi.mathml.mathml2text;

import java.io.File;
import java.util.Locale;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.converter.impl.XmlParserStAX;

/**
 * App class containing main method.
 *
 */
public class App {
    
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    
    public static void main(final String[] args) {
        if (args.length != 1) {
            System.out.println("Please specify input file path.");
            System.exit(1);
        }
        File input = new File(args[0]);
        
        final Instant start = Instant.now();

        final XmlParserStAX parser = new XmlParserStAX();
        
        File parse = parser.parse(input, Locale.ENGLISH);
        
        /**************************************************************/
        /*  You can instantiate parser from inside application and run the parse() methods.  */
//        String out = parser.parse("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1 plus MathML 2.0//EN\" \"http://www.w3.org/TR/MathML2/dtd/xhtml-math11-f.dtd\"><m:math xmlns:m=\"http://www.w3.org/1998/Math/MathML\"><m:mrow><m:mo>(</m:mo><m:mi>x</m:mi><m:mo>+</m:mo><m:mn>5.2</m:mn><m:mo>)</m:mo>	</m:mrow></m:math>");
//        System.out.println(out);
        
        final Instant end = Instant.now();
        final Duration duration = new Duration(start, end);
        System.out.println("\n" + duration.getMillis() + " ms");
    }
}
