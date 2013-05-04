package cz.muni.fi.mathml.mathml2text.input;

/**
 * Thrown if language is not supported by this converter.
 * 
 * @author Maros Kucbel
 * @date 2013-01-27T23:02:28+0100
 */
public final class UnsupportedLanguageException extends RuntimeException {

    public UnsupportedLanguageException(String message) {
        super(message);
    }
}