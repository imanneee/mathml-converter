package cz.muni.fi.mathml.mathml2text.converter.impl;

import java.util.Properties;
import java.util.Set;

import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import cz.muni.fi.mathml.mathml2text.converter.Strings;
import cz.muni.fi.mathml.mathml2text.converter.numbers.NumberFormat;
import cz.muni.fi.mathml.mathml2text.converter.numbers.NumberTransformer;

/**
 * Settings for {@link MathMLConverter}. 
 * Instance of this class is passed between processing classes and methods 
 * and offers:
 * <ul>
 *  <li>pulling of localized messages,</li>
 *  <li>converting numbers.</li>
 * </ul>
 * 
 * Default settings for conversion:
 * <ul>
 *  <li>replacing spaces with underscores - <code>false</code>,</li>
 *  <li>input canonicalization - <code>true</code>.</li>
 * </ul>
 * 
 * @author Maros Kucbel
 * @date 2012-12-15T10:21:57+0100
 */
public final class ConverterSettings {
    /**
     * {@link Properties} file currently used to access localized messages.
     */
    private Properties localization;
    /**
     * {@link NumberTransformer} instance.
     */
    private NumberTransformer numberTransformer;
    /**
     * Indicates whether spaces in strings received from localization file should
     * be replaced with underscores. (open braces -> open_braces)
     */
    private boolean replaceSpaces;
    /**
     * Indicates whether input date should be canonicalized.
     */
    private boolean canonicalize;
    /**
     * Number of threads that will be available for parallel conversion.
     */
    private int threadCount;
    /**
     * Output directory path.
     */
    private String outputDirectory;
    /**
     * Singleton instance.
     */
    private static final ConverterSettings INSTANCE = new ConverterSettings();
    
    /**
     * Constructor.
     * Inicializes this values:
     * <ul>
     *  <li>{@link #replaceSpaces} to <code>false</code></li>
     *  <li>{@link #canonicalize} to <code>false</code></li>
     *  <li>{@link #threadCount} to <code>1</code></li>
     * </ul>
     */
    private ConverterSettings() {
        this.replaceSpaces = false;
        this.canonicalize = false;
        this.threadCount = 1;
    }
    
    /**
     * Instance of this singleton class.
     * @return Instance of this singleton class.
     */
    public static ConverterSettings getInstance() {
        return INSTANCE;
    }
    
    /**
     * Returns current {@link Properties} file used to access localized messages.
     * @return Current {@link Properties} file used to access localized messages.
     */
    public Properties getLocalization() {
        return localization;
    }

    /**
     * Set {@link Properties} file used to access localized messages.
     * @param localization New {@link Properties} file used to access localized messages.
     */
    public void setLocalization(Properties localization) {
        this.localization = localization;
    }

    /**
     * Return {@link NumberTransformer} instance.
     * @return {@link NumberTransformer} instance.
     */
    public NumberTransformer getNumberTransformer() {
        return numberTransformer;
    }

    /**
     * Sets {@link NumberTransformer} instance.
     * @param numberTransformer {@link NumberTransformer} instance.
     */
    public void setNumberTransformer(NumberTransformer numberTransformer) {
        this.numberTransformer = numberTransformer;
    }

    /**
     * Returns current number format. Internally calls {@link NumberTransformer#getNumberFormat() }
     * method.
     * @return Returns current number format. 
     */
    public NumberFormat getNumberFormat() {
        return this.getNumberTransformer().getNumberFormat();
    }
    
    /**
     * Sets current number format. Internally calls 
     * {@link NumberTransformer#setNumberFormat(cz.muni.fi.mathml.mathml2text.transformer.numbers.NumberFormat)  }
     * method.
     * @param numberFormat New {@link NumberFormat number format}.
     */
    public void setNumberFormat(final NumberFormat numberFormat) {
        this.getNumberTransformer().setNumberFormat(numberFormat);
    }
    
    /**
     * Returns localized message retrieved from current localization settings 
     * {@link #getLocalization() }.
     * @param key Message key.
     * @return Localized message.
     */
    public String getProperty(final String key) {
        String result = this.getLocalization().getProperty(key);
        if (this.isReplaceSpaces()) {
            result = result.replace(" ", "_");
        }
        return result + Strings.SPACE;
    }

    /**
     * Indicates whether spaces in strings received from localization file should
     * be replaced with underscores. (open braces -> open_braces)
     * @return {@code true} if spaces should be replaced, {@code false} otherwise.
     * @see #replaceSpaces
     */
    public boolean isReplaceSpaces() {
        return this.replaceSpaces;
    }

    /**
     * Sets the value that indicates whether spaces should be replaced or not.
     * @param replaceSpaces Indicator whether spaces should be replaced or not.
     * @see #replaceSpaces
     */
    public void setReplaceSpaces(final boolean replaceSpaces) {
        this.replaceSpaces = replaceSpaces;
    }

    /**
     * Indicates whether canonicalizion should be applied on input data.
     * @return {@code true}, if canonicalizion should be applied, {@code false} otherwise.
     */
    public boolean isCanonicalize() {
        return this.canonicalize;
    }

    /**
     * Sets the value that indicates whether canonicalization should be applied.
     * @param canonicalize Indicator whether canonicalization should be applied.
     */
    public void setCanonicalize(boolean canonicalize) {
        this.canonicalize = canonicalize;
    }

    /**
     * Returns the number of threads that will be available for parallel conversion.
     * Minimal value is 1.
     * @return The number of threads that will be available for parallel conversion.
     */
    public int getThreadCount() {
        return threadCount;
    }

    /**
     * Sets the number of threads that will be available for parallel conversion.
     * Minimal value is 1.
     * @param threadCount The number of threads that will be available for parallel conversion.
     */
    public void setThreadCount(int threadCount) {
        if (threadCount < 1) {
            LoggerFactory.getLogger(ConverterSettings.class).warn(
                    "The number of threads must be at least 1. Setting to 1.");
            this.threadCount = 1;
        } else {
            this.threadCount = threadCount;
        }
    }

    /**
     * Returns the output directory path.
     * If the value is {@code null}, the output will be written to the input directory.
     * @return Output directory path.
     */
    public String getOutputDirectory() {
        return this.outputDirectory;
    }

    /**
     * Sets the output directory path.
     * @param outputDirectory Output directory path.
     */
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
    
    /**
     * Returns set of language codes, that are supported by the converter.
     * @return Set of language codes, that are supported by the converter. 
     */
    public Set<String> getSupportedLanguages() {
        return Sets.newHashSet("en", "cs", "sk");
    }
}