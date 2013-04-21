package cz.muni.fi.mathml.mathml2text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.converter.impl.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.impl.XmlParserStAX;

/**
 * Class containing runnable main method.
 */
public class App {
    /** Logger. */ 
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    /**
     * Set of supported languages
     */
    private static final Set<String> SUPPORTED_LANGUAGES;

    static {
        SUPPORTED_LANGUAGES = new HashSet<String>(3);
        SUPPORTED_LANGUAGES.add("en");
        SUPPORTED_LANGUAGES.add("sk");
        SUPPORTED_LANGUAGES.add("cs");
    }

    public static void main(final String[] args) {
        if (args.length < 1) {
            System.out.println("Please specify input file path. Use '-h' option to print help.");
            System.exit(1);
        }
        // default language is english
        String language = "en";
        if ("--h".equals(args[0]) || "-h".equals(args[0]) || "-help".equals(args[0]) || "--help".equals(args[0])) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("converter [options] <file...>", createOptions());
            System.exit(0);
        }
        
        final CommandLineParser commandLineParser = new BasicParser();
        try {
            CommandLine line = commandLineParser.parse(createOptions(), args);
            if (line.hasOption("language")) {
                language = line.getOptionValue("language");
                if (!SUPPORTED_LANGUAGES.contains(language)) {
                    System.err.println("Unsupported language.");
                    System.exit(1);
                }
            }
            if (line.hasOption("c")) {
                ConverterSettings.getInstance().setCanonicalize(true);
            }
            if (line.hasOption("r")) {
                ConverterSettings.getInstance().setReplaceSpaces(true);
            }
            if (line.hasOption("threads")) {
                String optionValue = line.getOptionValue("threads");
                try {
                    Integer value = Integer.valueOf(optionValue); 
                    ConverterSettings.getInstance().setThreadCount(value);
                } catch (final NumberFormatException ex) {
                    logger.warn("Could not convert thread count [" + optionValue + "] to integer.");
                }
            }
            String[] fileNames = line.getArgs();
            final List<File> inputFiles = new ArrayList<File>(fileNames.length);
            for (final String fileName : fileNames) {
                inputFiles.add(new File(fileName));
            }
            final Instant start = Instant.now();

            final XmlParserStAX parser = new XmlParserStAX();

            List<File> parse = parser.parse(inputFiles, new Locale(language));
            
            final Instant end = Instant.now();
            final Duration duration = new Duration(start, end);
            System.out.println("\n" + duration.getMillis() + " ms");
        } catch (ParseException ex) {
            System.err.println("Error while parsing command line arguments: " + ex.getMessage());
            System.exit(1);
        }
        /**
         * ***********************************************************
         */
        /*  You can instantiate parser from inside application and run the parse() methods.  */
//        String out = parser.parse("<math xmlns=\"http://www.w3.org/1998/Math/MathML\"><mrow><mfrac><mrow><mi>x</mi><mo>&#x2297;</mo><mn>5</mn></mrow><mrow><mi>x</mi><mo>*</mo><mn>3</mn></mrow></mfrac></mrow></math>", new Locale(language));
//        System.out.println(out);
    }

    /**
     * Create an {@link Options} instance that defines possible input parameters.
     */
    private static Options createOptions() {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("language")
                .withDescription("specify language (defaults to en)")
                .hasArg()
                .withArgName("LANGUAGE")
                .create());
        options.addOption(OptionBuilder.withLongOpt("threads")//
                .withDescription("specify number of threads for parallel conversion (defaults to 1)")//
                .hasArg()//
                .withArgName("NUMBER")//
                .create());
        options.addOption("c", "canonicalize", false, "canonicalize input");
        options.addOption("r", "replace-spaces", false, "replace spaces with underscores");
        return options;
    }
}
