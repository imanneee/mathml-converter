package cz.muni.fi.mathml.mathml2text.transformer.impl.converter;

import java.util.Properties;

import cz.muni.fi.mathml.mathml2text.Strings;
import cz.muni.fi.mathml.mathml2text.transformer.numbers.NumberFormat;
import cz.muni.fi.mathml.mathml2text.transformer.numbers.NumberTransformer;

/**
 *
 * @author Maros Kucbel
 * @date 2012-12-15T10:21:57+0100
 */
public final class ConverterSettings {

    private Properties localization;
    
    private NumberTransformer numberTransformer;

    private boolean useContentMathML = false;
    
    public Properties getLocalization() {
        return localization;
    }

    public void setLocalization(Properties localization) {
        this.localization = localization;
    }

    public NumberTransformer getNumberTransformer() {
        return numberTransformer;
    }

    public void setNumberTransformer(NumberTransformer numberTransformer) {
        this.numberTransformer = numberTransformer;
    }

    public NumberFormat getNumberFormat() {
        return this.getNumberTransformer().getNumberFormat();
    }
    
    public void setNumberFormat(final NumberFormat numberFormat) {
        this.getNumberTransformer().setNumberFormat(numberFormat);
    }
    
    public String getProperty(final String key) {
        return this.getLocalization().getProperty(key) + Strings.SPACE;
    }

    public boolean isUseContentMathML() {
        return useContentMathML;
    }

    public void setUseContentMathML(boolean useContentMathML) {
        this.useContentMathML = useContentMathML;
    }
    
}