package cz.muni.fi.mathml.mathml2text;

import java.io.File;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.converter.impl.XmlParserStAX;

/**
 * Hello world!
 *
 */
public class App {
    
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    
    public static void main(final String[] args) {
        if (args.length != 1) {
            System.out.println("Please specify input file path.");
//            System.exit(0);
        }
//        File input = new File(args[0]);
        
        final Instant start = Instant.now();

        final XmlParserStAX parser = new XmlParserStAX();
        
//        File input = new File("d:\\Projects\\math.0001002.xhtml");
        File input = new File("d:\\Projects\\ex3.xml");
        File parse = parser.parse(input);
        
        final Instant end = Instant.now();
        final Duration duration = new Duration(start, end);
        System.out.println("\n" + duration.getMillis() + " ms");
    }
}
