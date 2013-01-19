package cz.muni.fi.mathml.mathml2text.converter.impl.converter;

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
        return this.getLocalization().getProperty(key) + Strings.SPACE;
    }
}