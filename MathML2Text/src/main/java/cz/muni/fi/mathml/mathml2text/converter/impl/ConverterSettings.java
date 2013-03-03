package cz.muni.fi.mathml.mathml2text.converter.impl;

import java.util.Properties;

import cz.muni.fi.mathml.mathml2text.Strings;
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
     * Singleton instance.
     */
    private static final ConverterSettings INSTANCE = new ConverterSettings();
    
    /**
     * Constructor.
     * Inicializes this values:
     * <ul>
     *  <li>{@link #replaceSpaces} to false</li>
     * </ul>
     */
    private ConverterSettings() {
        this.replaceSpaces = false;
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
    
}