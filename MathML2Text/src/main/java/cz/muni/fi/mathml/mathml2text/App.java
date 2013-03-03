package cz.muni.fi.mathml.mathml2text;

import java.io.File;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.converter.impl.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.impl.XmlParserStAX;

/**
 * App class containing main method.
 *
 */
public class App {
    
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    /** Set of supported languages */
    private static final Set<String> SUPPORTED_LANGUAGES;
    
    static {
        SUPPORTED_LANGUAGES = new HashSet<String>(3);
        SUPPORTED_LANGUAGES.add("en");
        SUPPORTED_LANGUAGES.add("sk");
        SUPPORTED_LANGUAGES.add("cs");
    }
    
    public static void main(final String[] args) {
        if (args.length < 1) {
            System.out.println("Please specify input file path.");
            System.exit(1);
        }
        int fileArg = 0;
        // default language is english
        String language = "en";
        if (args.length > 1) {
            // some parameters were specified, only support language option
            if (!"-l".equals(args[0])) {
                System.out.println("Unknow parameter [" + args[0] + "].");
                System.exit(1);
            }
            language = args[1];
            // check if given language is supported
            if (!SUPPORTED_LANGUAGES.contains(language)) {
                System.out.println("Unsupported language.");
                System.exit(1);
            }
            fileArg = 2;
        }
        
        File input = new File(args[fileArg]);
        final Instant start = Instant.now();

        final XmlParserStAX parser = new XmlParserStAX();
        ConverterSettings.getInstance().setReplaceSpaces(true);

        File parse = parser.parse(input, new Locale(language));
        
        /**************************************************************/
        /*  You can instantiate parser from inside application and run the parse() methods.  */
//        String out = parser.parse("<math xmlns=\"http://www.w3.org/1998/Math/MathML\"><mrow><mfrac><mrow><mi>x</mi><mo>&#x2297;</mo><mn>5</mn></mrow><mrow><mi>x</mi><mo>*</mo><mn>3</mn></mrow></mfrac></mrow></math>", new Locale(language));
//        System.out.println(out);
        
        final Instant end = Instant.now();
        final Duration duration = new Duration(start, end);
        System.out.println("\n" + duration.getMillis() + " ms");
    }
}
