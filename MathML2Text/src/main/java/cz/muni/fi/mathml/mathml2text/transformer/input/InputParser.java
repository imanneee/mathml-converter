package cz.muni.fi.mathml.mathml2text.transformer.input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @deprecated 
 * Prepares input file(s) for transformation.
 * Most notably searches input file for any occurence of {@code math} element, 
 * then takes this whole element and creates separate file. Also prepends XML declaration
 * and MathML DTD.
 * 
 * @author Maros Kucbel Sep 14, 2012, 6:56:12 PM
 */
public final class InputParser {
    /** Logger. */
    private static Logger LOGGER = LoggerFactory.getLogger(InputParser.class);

    /**
     * Parses given XML file. For each occurence of {@code math} tag creates new XML file 
     * that contains only this element (and its descendants). This new file is valid 
     * XML file and also contains MathML DTD.
     *
     * @param file Input file
     * @return list of files that were created
     * @throws TransformerException If some unexpected error occurs. All exceptions are enclosed in {@link TransformerException}.
     */
    public static List<File> parseInput(@Nonnull final File file) throws TransformerException {
        Validate.isTrue(file != null, "Input file should not be null.");
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Parsing input file...");
        }
        final List<File> returnList = new ArrayList<File>();

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            final DocumentBuilder db = dbf.newDocumentBuilder();
            final Document doc = db.parse(file);
            doc.normalize();
            final NodeList mathList = doc.getElementsByTagNameNS("http://www.w3.org/1998/Math/MathML", "math");
            final Transformer transformer = transformerFactory.newTransformer(new StreamSource("resources/inputParser.xsl"));

            final String strippedFileName = file.getName().substring(0, file.getName().indexOf("."));

            for (int i = 0; i < mathList.getLength(); ++i) {
                final Document document = db.newDocument();
                final Node node = document.importNode(mathList.item(i), true);
                document.appendChild(node);

                final DOMSource source = new DOMSource(document);
                final File newFile = File.createTempFile(strippedFileName + "_" + (i + 1), ".xml");

                final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile)));
                out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
                        + "<!DOCTYPE math PUBLIC \"-//W3C//DTD MathML 2.0//EN\"\n"
                        + "\"http://www.w3.org/Math/DTD/mathml2/mathml2.dtd\">\n\n");

                final StreamResult result = new StreamResult(out);

                transformer.transform(source, result);
                returnList.add(newFile);
            }
        } catch (final SAXException sax) {
            LOGGER.error("Error working with DOM tree.", sax);
            throw new TransformerException(sax);
        } catch (final ParserConfigurationException ex) {
            LOGGER.error("Parser configuration error.", ex);
            throw new TransformerException(ex);
        } catch (final TransformerException tex) {
            LOGGER.error("Error during transformation occured.", tex);
            throw new TransformerException(tex);
        } catch (final IOException ioe) {
            LOGGER.error("Error working with file(s).", ioe);
            throw new TransformerException(ioe);
        } catch (final Exception ex) {
            LOGGER.error("Unexpected exception was thrown.", ex);
            throw new TransformerException(ex);
        }

        return returnList;
    }
}
