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
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.input.XmlParser;
import cz.muni.fi.mathml.mathml2text.input.XmlParserDOM;
import cz.muni.fi.mathml.mathml2text.input.XmlParserStAX;

/**
 * Class containing runnable main method.
 * 
 * @author Maros Kucbel
 * @date 2012-11-03T18:57:09+0100
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
        // default parser implementation
        String parserImplementation = "woodstox";
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
            if (line.hasOption("canonicalize")) {
                ConverterSettings.getInstance().setCanonicalize(true);
            }
            if (line.hasOption("replace-spaces")) {
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
            if (line.hasOption("output")) {
                String outputDirectoryPath = line.getOptionValue("output");
                if (outputDirectoryPath != null) {
                    outputDirectoryPath = StringUtils.stripEnd(outputDirectoryPath, System.getProperty("file.separator"));
                }
                ConverterSettings.getInstance().setOutputDirectory(outputDirectoryPath);
            }
            if (line.hasOption("transform-numbers")) {
                ConverterSettings.getInstance().setTransformNumbers(true);
            }
            if (line.hasOption("parser")) {
                parserImplementation = line.getOptionValue("parser");
            }
            if (line.hasOption("content-markup")) {
                ConverterSettings.getInstance().setUseContentMarkup(true);
            }
            String[] fileNames = line.getArgs();
            final List<File> inputFiles = new ArrayList<File>(fileNames.length);
            for (final String fileName : fileNames) {
                inputFiles.add(new File(fileName));
            }
            final Instant start = Instant.now();

            final XmlParser parser;
            if ("stax".equals(parserImplementation)) {
                setUpForStAX();
                parser = new XmlParserStAX(); 
            } else if ("dom".equals(parserImplementation)) {
                setUpForDOM();
                parser = new XmlParserDOM();
            } else if ("aalto".equals(parserImplementation)) {
                setUpForAalto();
                parser = new XmlParserStAX();
            } else if ("woodstox".equals(parserImplementation)) {
                setUpForWoodstox();
                parser = new XmlParserStAX(false);
            } else {
                System.err.println("Unknown parser implementation");
                parser = null;
                System.exit(1);
            }

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
                .create("l"));
        options.addOption(OptionBuilder.withLongOpt("threads")//
                .withDescription("specify number of threads for parallel conversion (defaults to 1)")//
                .hasArg()//
                .withArgName("NUMBER")//
                .create("t"));
        options.addOption(OptionBuilder.withLongOpt("output")//
                .withDescription("output directory path")//
                .hasArg()//
                .withArgName("PATH")//
                .create("o"));
        options.addOption(OptionBuilder.withLongOpt("parser")//
                .withDescription("choose parser implementation [dom|stax|aalto|woodstox], defaults to woodstox")//
                .hasArg()//
                .withArgName("PARSER")//
                .create("p"));
        options.addOption("c", "canonicalize", false, "canonicalize input");
        options.addOption("r", "replace-spaces", false, "replace spaces with underscores");
        options.addOption("n", "transform-numbers", false, "transform all numbers to strings");
        options.addOption("cm", "content-markup", false, "use content markup for conversion");
        return options;
    }
    
    private static void setUpForDOM() {
        System.setProperty("javax.xml.stream.XMLInputFactory", "com.sun.xml.internal.stream.XMLInputFactoryImpl");
        System.setProperty("javax.xml.stream.XMLOutputFactory", "com.sun.xml.internal.stream.XMLOutputFactoryImpl");
        System.setProperty("javax.xml.stream.XMLEventFactory", "com.sun.xml.internal.stream.events.XMLEventFactoryImpl");
        System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
    }
    
    private static void setUpForStAX() {
        System.setProperty("javax.xml.stream.XMLInputFactory", "com.sun.xml.internal.stream.XMLInputFactoryImpl");
        System.setProperty("javax.xml.stream.XMLOutputFactory", "com.sun.xml.internal.stream.XMLOutputFactoryImpl");
        System.setProperty("javax.xml.stream.XMLEventFactory", "com.sun.xml.internal.stream.events.XMLEventFactoryImpl");
        System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
    }
    
    private static void setUpForAalto() {
        System.setProperty("javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        System.setProperty("javax.xml.parsers.SAXParserFactory", "com.fasterxml.aalto.sax.SAXParserFactoryImpl");
    }
    
    private static void setUpForWoodstox() {
        System.setProperty("javax.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory");
        System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
        System.setProperty("javax.xml.stream.XMLEventFactory", "com.ctc.wstx.stax.WstxEventFactory");
        System.setProperty("javax.xml.parsers.SAXParserFactory", "com.ctc.wstx.sax.WstxSAXParserFactory");
    }
}
