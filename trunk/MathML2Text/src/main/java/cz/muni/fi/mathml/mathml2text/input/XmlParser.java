package cz.muni.fi.mathml.mathml2text.input;

import java.io.File;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import cz.muni.fi.mathml.mathml2text.converter.MathMLConverter;

/**
 * XML parser implementation is responsible for processing input files (be it a file or a string)
 * and replacing all occurrenes of <code>&lt;math&gt;</code> element with a value returned
 * from {@link MathMLConverter converter}.
 * 
 * @author Maros Kucbel
 */
public interface XmlParser {
    /**
     * Parses input string and converts every occurence of math element into 
     * plain text. Returned string is a concatenation of every such converted
     * element.
     * @param inputString Input. Can not be <code>null</code> or blank (consisting of only whitespace characters).
     * @param language Language of conversion.
     * @return Input converted to plain text.
     * @throws UnsupportedLanguageException
     */
    String parse(@Nonnull String inputString, Locale language) throws UnsupportedLanguageException;
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
    List<File> parse(@Nonnull List<File> files, Locale language) throws UnsupportedLanguageException;
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
    File parse(@Nonnull File file, Locale language) throws UnsupportedLanguageException;
}
