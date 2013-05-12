package cz.muni.fi.mathml.mathml2text.input;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.converter.ConverterSettings;
import cz.muni.fi.mathml.mathml2text.converter.MathMLConverter;
import cz.muni.fi.mathml.mathml2text.converter.Strings;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLElement;
import cz.muni.fi.mathml.mathml2text.converter.tree.MathMLNode;
import cz.muni.fi.mathml.mathml2text.converter.tree.XmlAttribute;
import cz.muni.fi.mir.mathmlcanonicalization.MathMLCanonizer;
import cz.muni.fi.mir.mathmlcanonicalization.modules.ElementMinimizer;
import cz.muni.fi.mir.mathmlcanonicalization.modules.MfencedReplacer;
import cz.muni.fi.mir.mathmlcanonicalization.modules.MrowNormalizer;
import cz.muni.fi.mir.mathmlcanonicalization.modules.OperatorNormalizer;

/**
 * This parser is responsible for reading input file or string and invoking
 * {@link MathMLConverter converter} for every occurence of MathML code block
 * in the input.
 * Resulting output format depends on the type of input:
 * <ul>
 *  <li>XML file &rarr; output is an XML file in which every MathML node is substituted
 *      with converted value,</li>
 *  <li>string &rarr; 
 *      <ol>
 *          <li>if input string is just MathML element (parent node of XML is 
 *              <code>&lt;math&gt;</code>), output will consist only of the 
 *              converted string without any XML tags
 *          </li>
 *          <li>otherwise result will be similar to the case of input XML file,
 *              but instead of to file, the result will be written as string
 *              and outputted.
 *          </li>
 *      </ol>
 *  </li>
 * </ul>
 * 
 * @todo extract interface
 * 
 * @author Maros Kucbel Oct 31, 2012, 19:07:12 PM
 */
public final class XmlParserStAX implements XmlParser {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(XmlParserStAX.class);
    /**
     * Responsible for converting MathML tree to string.
     */
    private final MathMLConverter converter;
    /**
     * Input factory for creating {@link XMLStreamReader} instance.
     */
    private final XMLInputFactory xmlInputFactory;
    /**
     * Output factory for creating {@link XMLStreamWriter} instance.
     */
    private final XMLOutputFactory xmlOutputFactory;
    
    private static final String CONVERTER_NAMESPACE_URI = "http://code.google.com/p/mathml-converter/";
    
    private static final String CONVERTER_NAMESPACE_PREFIX = "conv";
    
    private static final String CONVERTER_ELEMENT_NAME = "math";
    
    private final MathMLCanonizer canonicalizer;
    
    private final Set<File> originalInputFiles = new HashSet<File>();
    
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    
    /**
     * Constructor.
     * Delegates to {@link #XmlParserStAX(boolean) } with parameter value <code>true</false>.
     */
    public XmlParserStAX() {
        this(true);
    }
    
    /**
     * Constructor.
     * {@link XMLInputFactory} is configured with values:
     * <ul>
     *  <li>{@link XMLInputFactory#IS_REPLACING_ENTITY_REFERENCES} &rarr <code>true</code></li>
     *  <li>{@link XMLInputFactory#IS_SUPPORTING_EXTERNAL_ENTITIES} &rarr <code>true</code></li>
     *  <li>{@link XMLInputFactory#IS_COALESCING} &rarr <code>true</code></li>
     *  <li>{@link XMLInputFactory#IS_VALIDATING} &rarr <code>false</code></li>
     *  <li>{@link XMLInputFactory#SUPPORT_DTD} &rarr <code>true</code></li>
     * </ul>
     * 
     * @param supportDTD Enable DTD processing.
     */
    public XmlParserStAX(final boolean supportDTD) {
        this.converter = new MathMLConverter();
        this.xmlInputFactory = XMLInputFactory.newInstance();
        this.xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
        this.xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
        this.xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
        this.xmlInputFactory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        this.xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, supportDTD);
        this.xmlInputFactory.setProperty(XMLInputFactory.RESOLVER, new XMLResolver() {
            @Override
            public Object resolveEntity(String publicID, String systemID, String baseURI, String namespace) throws XMLStreamException {
                if (systemID.endsWith("dtd")) { 
                    return XmlParserStAX.class.getResourceAsStream("xhtml-math11-f.dtd");
                }
                return null;
            }
        });
        this.xmlOutputFactory = XMLOutputFactory.newInstance();
        this.canonicalizer = new MathMLCanonizer()//
                .addModule(new ElementMinimizer())//
                .addModule(new MfencedReplacer())//
                .addModule(new MrowNormalizer())//
                .addModule(new OperatorNormalizer());
    }
    
    /**
     * Returns logger for this class.
     */
    private Logger getLogger() {
        return this.logger;
    }

    /**
     * Parses input string and converts every occurence of math element into 
     * plain text. Returned string is a concatenation of every such converted
     * element.
     * @param inputString Input.
     * @param language Language of conversion.
     * @return Input converted to plain text.
     * @throws UnsupportedLanguageException
     */
    @Override
    public String parse(@Nonnull final String inputString, final Locale language)
            throws UnsupportedLanguageException {
        Validate.isTrue(StringUtils.isNotBlank(inputString));
        this.checkSupportedLanguages(language);
        
        XMLStreamReader reader;
        
        /** root node */
        MathMLNode tree = null;
        /** current node of reader */
        MathMLNode currentNode = null;
        /** parent node of current node */
        MathMLNode parentNode = null;
        /** type of current element */
        MathMLElement currentElement = null;
        
        try {
            // canonicalize
            InputStream inputStream = ConverterSettings.getInstance().isCanonicalize()
                                      ? this.canonicalize(new ByteArrayInputStream(inputString.getBytes()))
                                      : new ByteArrayInputStream(inputString.getBytes());
            // create stream reader from input file
            reader = this.xmlInputFactory.createXMLStreamReader(inputStream, "UTF-8");
            StringBuilder output = new StringBuilder();
            // indicates whether we are inside a math element
            boolean processingMathMLElement = false;
            while (reader.hasNext()) {
                // pull next event code
                final int eventCode = reader.next();
                // determine type of event
                final XmlStreamConstant constant = XmlStreamConstant.forEventCode(eventCode);

                // check whether we are inside math element, if not and current event is not
                // START_ELEMENT continue to the next event
                if (!processingMathMLElement && !XmlStreamConstant.START_ELEMENT.equals(constant)) {
                    continue;
                }

                switch (constant) {
                    case START_ELEMENT: {
                        // retrieve element name
                        final String elementName = reader.getLocalName();
                        // if we are not inside math element continue to the next event
                        if (!processingMathMLElement && !MathMLElement.MATH.getElementName().equals(elementName)) {
                            continue;
                        }
                        
                        // now we are surely inside math element, we can determine type of math element
                        currentElement = MathMLElement.forElementName(elementName);
                        processingMathMLElement = true;
                        // set new current node
                        currentNode = new MathMLNode();
                        // set its name
                        currentNode.setType(currentElement);
                        // set its attributes if there are any
                        for (int index = 0; index < reader.getAttributeCount(); ++index) {
                            currentNode.getAttributes().add(
                                    new XmlAttribute(reader.getAttributeLocalName(index), 
                                                     reader.getAttributeValue(index)));
                        }
                        // if parent node was set, set it to current node
                        if (parentNode != null) {
                            currentNode.setParent(parentNode);
                            parentNode.getChildren().add(currentNode);
                        }

                        // if we are just at math element initialize new tree
                        if (tree == null) {
                            tree = currentNode;
                        }

                        parentNode = currentNode;
                        break;
                    }
                    case END_ELEMENT: {
                        // we are going "one level up" inside the tree
                        parentNode = currentNode.getParent();
                        currentNode = parentNode;

                        //@todo it should be enough to check whether currentNode is not null
                        final String elementName = reader.getLocalName();
                        final MathMLElement element = MathMLElement.forElementName(elementName);
                        switch (element) {
                            case MATH: {
                                // transform created tree and write to output
                                String converted = this.converter.convert(tree, language);
                                output.append(converted);
                                processingMathMLElement = false;
                                currentElement = null;
                                tree = null;
                                currentNode = null;
                                parentNode = null;
                                break;
                            }
                            default:
                                break;
                        }

                        break;
                    }
                    case CHARACTERS: {
                        final String value = reader.getText();
                        if (currentElement != null && StringUtils.isNotBlank(value)) {
                            currentNode.setValue(value);
                        }
                        break;
                    }  
                    case ENTITY_REFERENCE: {
                        final String value = reader.getLocalName();
                        if (currentElement != null && StringUtils.isNotBlank(value)) {
                            currentNode.setValue(value);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
            reader.close();
            return output.toString();
        } catch (final XMLStreamException ex) {
            this.getLogger().error("Cannot open xml file for reading.", ex);
        }
        return Strings.EMPTY;
    }
    
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
                    return XmlParserStAX.this.parse(file, language);
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
    
    @Override
    public File parse(@Nonnull final File file, final Locale language) throws UnsupportedLanguageException {
        Validate.isTrue(file != null, "File for transformation should not be null.");
        this.checkSupportedLanguages(language);
        
        int executionNumber = this.atomicInteger.incrementAndGet();
        logger.debug("Processing file [" + executionNumber + "] [" + file.getPath() + "].");
        
        XMLStreamReader reader;
        XMLStreamWriter writer;
        String outputFilePath;
        if (StringUtils.isBlank(ConverterSettings.getInstance().getOutputDirectory())) {
            String filePath = file.getPath();
            outputFilePath = filePath.substring(0, filePath.lastIndexOf('.')) + "-transformed.xml";
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
            this.getLogger().warn("File [{}] already exists.", outputFile.getPath());
        }
    
        /** root node */
        MathMLNode tree = null;
        /** current node of reader */
        MathMLNode currentNode = null;
        /** parent node of current node */
        MathMLNode parentNode = null;
        /** type of current element */
        MathMLElement currentElement = null;
        
        try {
            InputStream inputStream;
            if (file.getName().endsWith("zip")) {
                inputStream = Unzipper.unzip(file);
            } else {
                inputStream = new FileInputStream(file);
            }
            // canonicalize
            inputStream = ConverterSettings.getInstance().isCanonicalize()
                          ? this.canonicalize(inputStream)
                          : inputStream;
            
            // create stream reader from input file
            reader = this.xmlInputFactory.createXMLStreamReader(inputStream, "UTF-8");
            OutputStream output = new FileOutputStream(outputFile);
            // create stream writer
            writer = this.xmlOutputFactory.createXMLStreamWriter(output, "UTF-8");
            writer.writeStartDocument(reader.getEncoding(), reader.getVersion());
            
            // is this the root element
            boolean isRoot = true;
            // indicates whether we are inside a math element
            boolean processingMathMLElement = false;
            while (reader.hasNext()) {
                // pull next event code
                final int eventCode = reader.next();
                // determine type of event
                final XmlStreamConstant constant = XmlStreamConstant.forEventCode(eventCode);

                // check whether we are inside math element, if not and current event is not
                // START_ELEMENT continue to the next event
                if (!processingMathMLElement && !XmlStreamConstant.START_ELEMENT.equals(constant)) {
                    switch (constant) {
                        case END_ELEMENT: {
                            writer.writeEndElement();
                            break;
                        }
                        case CHARACTERS: {
                            writer.writeCharacters(reader.getText());
                            break;
                        }
                        case DTD: {
                            writer.writeDTD(reader.getText());
                            break;
                        }
                        default:
                            break;
                    }
                    continue;
                }

                switch (constant) {
                    case START_ELEMENT: {
                        // retrieve element name
                        final String elementName = reader.getLocalName();
                        // if we are not inside math element continue to the next event
                        if (!processingMathMLElement && !MathMLElement.MATH.getElementName().equals(elementName)) {
                            writer.writeStartElement(reader.getName().getPrefix(), reader.getLocalName(), reader.getName().getNamespaceURI());
                            for (int index = 0; index < reader.getAttributeCount(); ++index) {
                                writer.writeAttribute(reader.getAttributeLocalName(index), reader.getAttributeValue(index));
                            }
                            for (int index = 0; index < reader.getNamespaceCount(); ++index) {
                                writer.writeNamespace(reader.getNamespacePrefix(index), reader.getNamespaceURI(index));
                            }
                            if (isRoot) {
                                writer.writeNamespace(CONVERTER_NAMESPACE_PREFIX, CONVERTER_NAMESPACE_URI);
                                isRoot = false;
                            }
                            continue;
                        }
                        
                        // now we are surely inside math element, we can determine type of math element
                        currentElement = MathMLElement.forElementName(elementName);
                        processingMathMLElement = true;
                        // set new current node
                        currentNode = new MathMLNode();
                        // set its name
                        currentNode.setType(currentElement);
                        // set its attributes if there are any
                        for (int index = 0; index < reader.getAttributeCount(); ++index) {
                            currentNode.getAttributes().add(
                                    new XmlAttribute(reader.getAttributeLocalName(index), 
                                                     reader.getAttributeValue(index)));
                        }
                        // if parent node was set, set it to current node
                        if (parentNode != null) {
                            currentNode.setParent(parentNode);
                            parentNode.getChildren().add(currentNode);
                        }

                        // if we are just at math element initialize new tree
                        if (tree == null) {
                            tree = currentNode;
                        }

                        parentNode = currentNode;
                        break;
                    }
                    case END_ELEMENT: {
                        // we are going "one level up" inside the tree
                        parentNode = currentNode.getParent();
                        currentNode = parentNode;

                        //@todo it should be enough to check whether currentNode is not null
                        final String elementName = reader.getLocalName();
                        final MathMLElement element = MathMLElement.forElementName(elementName);
                        switch (element) {
                            case MATH: {
                                // transform created tree and write to output
                                String converted = this.converter.convert(tree, language);
                                if (isRoot) {
                                    writer.writeStartElement(CONVERTER_ELEMENT_NAME);
                                    writer.writeDefaultNamespace(CONVERTER_NAMESPACE_URI);
                                    isRoot = false;
                                } else {
                                    writer.writeStartElement(CONVERTER_NAMESPACE_URI, CONVERTER_ELEMENT_NAME);
                                }
                                writer.writeCharacters(converted);
                                writer.writeEndElement();
                                processingMathMLElement = false;
                                currentElement = null;
                                tree = null;
                                currentNode = null;
                                parentNode = null;
                                break;
                            }
                            default:
                                break;
                        }

                        break;
                    }
                    case CHARACTERS: {
                        final String value = reader.getText();
                        if (currentElement != null && StringUtils.isNotBlank(value)) {
                            currentNode.setValue(value);
                        }
                        break;
                    }  
                    case ENTITY_REFERENCE: {
                        final String value = reader.getLocalName();
                        if (currentElement != null && StringUtils.isNotBlank(value)) {
                            currentNode.setValue(value);
                        }
                        break;
                    }
                    case COMMENT: {
                        // comment in input file is ignored, no comment event is received...
                        writer.writeComment(reader.getText());
                        break;
                    }    
                    case DTD: {
                        writer.writeDTD(reader.getText());
                    }
                    default:
                        break;
                }
            }
            inputStream.close();
            reader.close();
            writer.flush();
            writer.close();
            output.flush();
            output.close();
            logger.debug("Finished conversion of file [" + executionNumber + "] [" + file.getPath() + "]");
        } catch (final IOException ex) {
            this.getLogger().error("Exception while working with input/output file.", ex);
        } catch (final XMLStreamException ex) {
            this.getLogger().error("Cannot open xml file for reading.", ex);
        }
        return outputFile;
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
     * Uses {@link MathMLCanonizer} to canonicalize input data.
     * Returns new {@link InputStream} so that it can be used as input 
     * for conversion.
     * @param input Input data stream.
     * @return {@link InputStream} instance that contains canonicalized data.
     */
    private InputStream canonicalize(final InputStream input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            logger.trace("Starting canonicalization.");
            this.canonicalizer.canonicalize(input, output);
            logger.trace("Canonicalization completed.");
        } catch (JDOMException ex) {
            logger.error("Error while performing canonicalization.", ex);
        } catch (FileNotFoundException ex) {
            logger.error("Input file not found.", ex);
        } catch (IOException ex) {
            logger.error("General IO exception.", ex);
        }
        final InputStream result = new ByteArrayInputStream(output.toByteArray());
        return result;
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
    
}
