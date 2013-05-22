package cz.muni.fi.mathml.mathml2text.input;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cz.muni.fi.mathml.mathml2text.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.MathMLConverter;
import cz.muni.fi.mathml.mathml2text.converter.Strings;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLElement;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.tree.XmlAttribute;

/**
 * An {@link XmlParser} implementation using JDOM2.
 * 
 * @author Maros Kucbel
 * @date 2013-05-12T21:53:03+0100
 */
public final class XmlParserDOM implements XmlParser {

    private static final Logger logger = LoggerFactory.getLogger(XmlParserDOM.class);
    
    private final Set<File> originalInputFiles = new HashSet<File>();
    
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    
    final XMLOutputter outputter;
    /**
     * Responsible for converting MathML tree to string.
     */
    private final MathMLConverter converter;
    
    private static final String CONVERTER_NAMESPACE_URI = "http://code.google.com/p/mathml-converter/";
    
    private static final String CONVERTER_NAMESPACE_PREFIX = "conv";
    
    private static final String CONVERTER_ELEMENT_NAME = "math";
    
    public XmlParserDOM() {
        this.converter = new MathMLConverter();
        this.outputter = new XMLOutputter();
        this.outputter.setFormat(Format.getPrettyFormat());
    }
    
    private SAXBuilder createSAXBuilder() {
        final SAXBuilder saxBuilder = new SAXBuilder();
        saxBuilder.setXMLReaderFactory(XMLReaders.NONVALIDATING);
        saxBuilder.setFeature("http://xml.org/sax/features/validation", false);
        saxBuilder.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", true);
        saxBuilder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
        saxBuilder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                if (systemId.endsWith("dtd")) { 
                    return new InputSource(XmlParserDOM.class.getResourceAsStream("xhtml-math11-f.dtd"));
                }
                return null;
            }
        });
        return saxBuilder;
    }
    
    /**
     * Concurrently converts all input files.
     * For every file delegates to method {@link #parse(java.io.File, java.util.Locale) }.
     * The number of possible concurrent conversion is determined with parameter
     * {@link ConverterSettings#getThreadCount()}. 
     * @param files List of input files.
     * @param language Language of conversion.
     * @return List of converted files.
     * @throws UnsupportedLanguageException
     */
    @Override
    public List<File> parse(@Nonnull final List<File> files, final Locale language) throws UnsupportedLanguageException {
        final List<File> inputFiles = new ArrayList<File>();
        for (final File file : files) {
            this.originalInputFiles.add(file);
            inputFiles.addAll(this.findFiles(file));
        }
        final List<File> outputFiles = new ArrayList<File>(inputFiles.size());
        final ExecutorService executorService = Executors.newFixedThreadPool(ConverterSettings.getInstance().getThreadCount());
        final Collection<Callable<File>> callables = new ArrayList<Callable<File>>(inputFiles.size());
        for (final File file : inputFiles) {
            callables.add(new Callable<File>() {

                @Override
                public File call() throws Exception {
                    return XmlParserDOM.this.parse(file, language);
                }
            });
        }
        try {
            final List<Future<File>> futures = executorService.invokeAll(callables);
            for (final Future<File> future : futures) {
                final File result = future.get();
//                outputFiles.add(result);
            }
            logger.debug("Finished converting all files.");
        } catch (final ExecutionException ex) {
            logger.warn("The execution of a single callable resulted in an exception.", ex);
        } catch (final InterruptedException ex) {
            logger.warn("The execution was interrupted.", ex);
        } catch (final CancellationException ex) {
            logger.warn("The execution was cancelled.", ex);
        } finally {
            for (final File file : files) {
                this.originalInputFiles.remove(file);
            }
            try {
                executorService.shutdownNow();
            } catch (final Exception ex) {
                logger.warn("Exception while shutting down the executor service.", ex);
                executorService.shutdownNow();
            }
        }
//        return outputFiles;
        return null;
    }
    
    /**
     * Parses single file. For every occurrence of math element inside input 
     * XML file new {@link MathMLNode} tree is builded and subsequently converted
     * to string.
     * 
     * @param file Input XML file.
     * @param language Language of conversion.
     * @return A file with every occurence of <code>&lt;math&gt;</code> tag replaced
     *  with converted string inside <code>&lt;mathconv&gt;</code> tag.
     * @throws UnsupportedLanguageException
     */
    @Override
    public File parse(@Nonnull final File file, final Locale language) throws UnsupportedLanguageException {
        Validate.isTrue(file != null, "File for transformation should not be null.");
        this.checkSupportedLanguages(language);
        
        int executionNumber = this.atomicInteger.incrementAndGet();
        logger.debug("Processing file [" + executionNumber + "] [" + file.getPath() + "].");
        
        String outputFilePath;
        if (StringUtils.isBlank(ConverterSettings.getInstance().getOutputDirectory())) {
            String filePath = file.getPath();
            outputFilePath = filePath.substring(0, filePath.lastIndexOf('.')) + "-converted.xml";
        } else {
            String suffix = "";
            for (final File f : this.originalInputFiles) {
                if (file.getPath().startsWith(f.getPath())) {
                    suffix = StringUtils.difference(f.getPath(), file.getPath());
                    break;
                }
            }
            if (StringUtils.isBlank(suffix)) {
                suffix = file.getName();
            }
            if (!suffix.startsWith(System.getProperty("file.separator"))) {
                suffix = System.getProperty("file.separator") + suffix;
            }
            outputFilePath = ConverterSettings.getInstance().getOutputDirectory() + suffix;
            if (outputFilePath.endsWith("zip")) {
                outputFilePath = outputFilePath.substring(0, outputFilePath.lastIndexOf("."));
            }
        }
        
        File outputFile = new File(outputFilePath);
        outputFile.getParentFile().mkdirs();
        try {
            outputFile.createNewFile();
        } catch (IOException ex) {
            logger.warn("File [{}] already exists.", outputFile.getPath());
        }
    
        try {
            InputStream inputStream;
            if (file.getName().endsWith("zip")) {
                inputStream = Unzipper.unzip(file);
            } else {
                inputStream = new FileInputStream(file);
            }
            final Document document = this.createSAXBuilder().build(inputStream);
            final Element root = document.getRootElement();
            root.addNamespaceDeclaration(Namespace.getNamespace(CONVERTER_NAMESPACE_PREFIX, CONVERTER_NAMESPACE_URI));
            this.processMath(root, language);
            final OutputStream output = new FileOutputStream(outputFile);
            this.outputter.output(document, output);
            output.flush();
            output.close();
            inputStream.close();
        } catch (final FileNotFoundException ex) {
            logger.error("Input file not found.", ex);
        } catch (final JDOMException ex) {
            logger.error("Error while creating DOM document", ex);
        } catch (final IOException ex) {
            logger.error("IO error.", ex);
        }
        return null;
    }
    
    private void processMath(final Element element, final Locale language) {
        if ("math".equals(element.getName())) {
            final MathMLNode tree = this.createTree(element);
            element.setNamespace(Namespace.getNamespace(CONVERTER_NAMESPACE_PREFIX, CONVERTER_NAMESPACE_URI));
            element.setName(CONVERTER_ELEMENT_NAME);
            final String converted = this.converter.convert(tree, language);
            element.removeContent();
            element.setText(converted);
        } else {
            for (final Element child : element.getChildren()) {
                this.processMath(child, language);
            }
        }
    }
    
    private MathMLNode createTree(final Element element) {
        final MathMLNode node = new MathMLNode();
        node.setType(MathMLElement.forElementName(element.getName()));
        final String value = element.getText().trim();
        if (StringUtils.isNotBlank(value)) {
            node.setValue(value);
        }
        for (final Attribute attr : element.getAttributes()) {
            node.getAttributes().add(new XmlAttribute(attr.getName(), attr.getValue()));
        }
        for (final Element child : element.getChildren()) {
            final MathMLNode childTree = this.createTree(child);
            childTree.setParent(node);
            node.getChildren().add(childTree);
        }
        return node;
    }
    
    /**
     * Checks whether the required language is supported by the converter.
     * @param language Required language.
     * @throws UnsupportedLanguageException If the required language is not supported. 
     */
    private void checkSupportedLanguages(final Locale language) throws UnsupportedLanguageException {
        if (!ConverterSettings.getInstance().getSupportedLanguages().contains(language.getLanguage())) {
            throw new UnsupportedLanguageException(String.format("[%1$s] is not supported.", language.getLanguage()));
        }
    }
    
    /**
     * Find all child files of a given file.
     * @param file Input file.
     * @return Always returns nonnull list, if given file is a file (not a directory)
     *  returns a list that contains only this file.
     */
    private List<File> findFiles(final File file) {
        final List<File> files = new ArrayList<File>();
        if (file.canRead()) {
            if (file.isFile()) {
                files.add(file);
                return files;
            } else {
                for (final File child : file.listFiles()) {
                    files.addAll(this.findFiles(child));
                }
            }
        }
        return files;
    }

    @Override
    public String parse(String inputString, Locale language) throws UnsupportedLanguageException {
        Validate.isTrue(StringUtils.isNotBlank(inputString), "Input string cant be null nor blank.");
        try {
            InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
            final Document document = this.createSAXBuilder().build(inputStream);
            final Element root = document.getRootElement();
            root.addNamespaceDeclaration(Namespace.getNamespace(CONVERTER_NAMESPACE_PREFIX, CONVERTER_NAMESPACE_URI));
            String converted = this.processMathAsString(root, language);
            return converted;
        } catch (final FileNotFoundException ex) {
            logger.error("Input file not found.", ex);
        } catch (final JDOMException ex) {
            logger.error("Error while creating DOM document", ex);
        } catch (final IOException ex) {
            logger.error("IO error.", ex);
        }
        return null;
    }
    
    private String processMathAsString(final Element element, final Locale language) {
        final StringBuilder builder = new StringBuilder();
        if ("math".equals(element.getName())) {
            final MathMLNode tree = this.createTree(element);
            final String converted = this.converter.convert(tree, language);
            builder.append(converted).append(Strings.SPACE);
        } else {
            for (final Element child : element.getChildren()) {
                builder.append(processMathAsString(child, language));
            }
        }
        return builder.toString();
    }
}