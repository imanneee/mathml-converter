package cz.muni.fi.mathml.mathml2text.transformer;

import java.io.File;
import java.util.Collection;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Maros Kucbel Sep 13, 2012, 8:52:52 PM
 */
public interface IMathMLTransformer {

    /**
     * @deprecated This method is used only for backward compatibility.
     * Use {@link IMathMLTransformer#transform(java.util.Collection, java.util.Locale) }.
     * Transforms MathML input to String.
     *
     * @param input input Source that contains only MathML.
     * @param language Language that will be used in the transformation (en, sk, etc.).
     * @return String representation of given input
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws IllegalArgumentException If any of the given parameters contain {@code null} value.
     */
    String transformMathML(@Nonnull final Source input, 
                           @Nonnull final String language) 
            throws TransformerConfigurationException, TransformerException, IllegalArgumentException;

    /**
     * @deprecated This method is used only for backward compatibility.
     * Use {@link IMathMLTransformer#transform(java.util.Collection, java.util.Locale) }.
     * Transforms MathML input
     *
     * @param input input XML file that contains only MathML
     * @param targetDirectory Directory to save output file to
     * @param language Language that will be used in the transformation (en, sk, etc.)
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws IllegalArgumentException If any of the given parameters contain {@code null} value.
     */
    void transformMathML(@Nonnull final File input, 
                         @Nonnull final File targetDirectory, 
                         @Nonnull final String language) 
            throws TransformerConfigurationException, TransformerException, IllegalAccessException;
    
    /**
     * Transforms collection of XML files containing MathML markup.
     * 
     * @param filesToTransform Collection of files that will be transformed.
     * @param language Language of transformation.
     */
    void transform(@Nonnull final Collection<File> filesToTransform,
                   @Nonnull final Locale language);
}
