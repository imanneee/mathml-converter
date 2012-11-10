package cz.muni.fi.mathml.mathml2text.transformer.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.mathml.mathml2text.transformer.IMathMLTransformer;
import cz.muni.fi.mathml.mathml2text.transformer.input.InputParser;
import cz.muni.fi.mathml.mathml2text.transformer.numbers.NumberTransformer;

/**
 * Implementation of MathML to plain text transformer.
 * 
 * @deprecated 
 * @author Maros Kucbel Sep 13, 2012, 9:18:12 PM
 */
public final class MathMLTransformer implements IMathMLTransformer {
    /** Logger. */
    private final Logger logger = LoggerFactory.getLogger(MathMLTransformer.class);
    private ResourceBundle bundle;

    public MathMLTransformer() {
        this(Locale.getDefault());
    }
    
    public MathMLTransformer(final Locale locale) {
        this.bundle = ResourceBundle.getBundle("cz.muni.fi.mathml.mathml2text.transformer.impl.gui", locale);
    }

    /**
     * Returns logger for this class.
     */
    private Logger getLogger() {
        return this.logger;
    }
    
    /** 
     * Returns resource bundle with localized messages.
     */
    private ResourceBundle getBundle() {
        return this.bundle;
    }

    @Override
    public void transform(Collection<File> filesToTransform, Locale language) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    @Nullable
    public String transformMathML(@Nonnull final Source input, @Nonnull final String language) 
            throws TransformerConfigurationException, TransformerException, IllegalArgumentException {
        Validate.isTrue(input != null, "Input source should not be null.");
        Validate.isTrue(StringUtils.isNotBlank(language), "Transformation language should not be null or empty string.");
        
        
        final Transformer optimusPrime = this.createTransformer();
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Transforming ...");
        }
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            optimusPrime.setParameter("lang", language);
            optimusPrime.transform(input, new StreamResult(bos));

            final String error = testErrors(bos.toString());
            final String ret;
            if (error == null) {
                this.getLogger().info(bos.toString());
                final NumberTransformer nt = new NumberTransformer(new Locale(language));
                ret = nt.transformNumbers(bos);
            } else {
                ret = this.getBundle().getString("TRANSFORMATION_INVALID") + " " + error;
            }
            return ret;

        } catch (final TransformerException ex) {
            throw new TransformerException(ex);
        } catch (final Exception ex) {
            this.getLogger().error("General error occured.", ex);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (final IOException ex) {
                this.getLogger().error("Cannot close ByteArrayOutputStream.", ex);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void transformMathML(@Nonnull final File input, 
                                @Nonnull final File targetDirectory, 
                                @Nonnull final String language) 
            throws TransformerConfigurationException, TransformerException, IllegalArgumentException {
        Validate.isTrue(input != null, "Input file should not be null.");
        Validate.isTrue(targetDirectory != null, "Target directory should not be null.");
        Validate.isTrue(StringUtils.isNotBlank(language), "Language code should not be null or empty string.");
        
        final Transformer optimusPrime = createTransformer();
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Transforming ...");
        }
        final List<File> inputList = new ArrayList<File>();
        try {
            inputList.addAll(InputParser.parseInput(input));

        } catch (final TransformerException tex) {
            throw tex;
        }
        for (final File f : inputList) {
            this.transformMathML0(optimusPrime, f, targetDirectory, language);
            f.delete();
        }

    }
    
    private void transformMathML0(@Nonnull final Transformer optimusPrime, 
                                  @Nonnull final File input, 
                                  @Nonnull final File targetDirectory, 
                                  @Nonnull final String language) throws TransformerException {
        Validate.isTrue(optimusPrime != null, "Transformer should not be null.");
        Validate.isTrue(input != null, "Input file should not be null.");
        Validate.isTrue(targetDirectory != null, "Target directory should not be null.");
        Validate.isTrue(StringUtils.isNotBlank(language), "Language code should not be null or empty string.");
        
        String strippedSource = input.getName();
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("\tTransforming " + input + " ...");
        }
        // temporary files are created with specific name that appends some random string to file name
        final int extIndex = strippedSource.lastIndexOf('_');
        if (extIndex > 0) {
            strippedSource = strippedSource.substring(0, extIndex + 2);
        }

        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            optimusPrime.setParameter("lang", language);

            optimusPrime.transform(new StreamSource(input), new StreamResult(bos));

            final NumberTransformer nt = new NumberTransformer(new Locale(language));
            final File target = new File(targetDirectory.getPath() + "\\" + strippedSource + ".txt");
            this.saveTransformationResult(nt.transformNumbers(bos).getBytes(), target);

        } catch (final TransformerException ex) {
            this.getLogger().error("Exception occured during transformation.", ex);
            throw new TransformerException(ex);
        } catch (final Exception ex) {
            this.getLogger().error("General error occured. ", ex);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (final IOException ex) {
                this.getLogger().error("Cannot close ByteArrayOutputStream", ex);
            }
        }
    }

    /**
     * Creates an instance of transformer that uses file mathml2text.xsl as transformation source.
     */
    private Transformer createTransformer() throws TransformerConfigurationException, TransformerException {
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Creating TransformerFactory ...");
        }
        final TransformerFactory allSpark = TransformerFactory.newInstance();

        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Loading Transformer ...");
        }
        final InputStream template = this.getClass().getResourceAsStream("mathml2text.xsl");
        final Transformer optimusPrime = allSpark.newTransformer(new StreamSource(template));

        if (optimusPrime == null) {
            this.getLogger().error("Transformer not found!");
            throw new TransformerException("Transformer not found!");
        }
        return optimusPrime;
    }

    /**
     * Saves given byte array as new file with specified parameters.
     */
    private void saveTransformationResult(final byte[] bytes, @Nonnull final File target) throws TransformerException {
        Validate.isTrue(target != null, "Target file should not be null.");
        
        if (bytes == null) {
            throw new TransformerException("No data specified");
        }
        FileOutputStream fos = null;
        try {
            final String re = testErrors(new String(bytes));
            fos = new FileOutputStream(target);
            if (re == null) {
                fos.write(bytes);
                fos.flush();
            } else {
                String error = this.getBundle().getString("TRANSFORMATION_INVALID") + " " + re;
                fos.write(error.getBytes());
                fos.flush();
                throw new TransformerException(this.getBundle().getString("TRANSFORMATION_INVALID") + " " + re);
            }
        } catch (final IOException ex) {
            throw new TransformerException("Transformation failed. Unable to open target file for writing", ex);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (final IOException ex) {
                throw new TransformerException("Failed to close output stream", ex);
            }
        }
    }

    /**
     * Tests given source for errors.
     * Error is any sequence of characters enclosed by expression: ERROR:(.*?):
     */
    private String testErrors(final String source) {
        final Matcher m = Pattern.compile("ERROR:(.*?):").matcher(source);
        if (m.find()) {
            if (this.getLogger().isDebugEnabled()) {
                this.getLogger().debug("MATCH " + m.group(1));
            }
            return m.group(1);
        } else {
            if (this.getLogger().isDebugEnabled()) {
                this.getLogger().debug("OUTPUT OK");
            }
            return null;
        }
    }
}
