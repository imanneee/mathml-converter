package cz.muni.fi.mathml.mathml2text.converter;

import java.util.HashSet;
import java.util.Set;

/**
 * String constants. For better readability of code.
 * 
 * @author Maros Kucbel
 * @date 2012-09-16T16:40:40+0100 
*/
public final class Strings {
    /**
     * Space.
     */
    public static final String SPACE = " ";
    /**
     * Empty string.
     */
    public static final String EMPTY = "";
    /**
     * Strings that are essentially empty, like thin space, wide space...
     */
    public static final Set<Character> VALUE_EMPTY_CHARS;
    
    static {
        VALUE_EMPTY_CHARS = new HashSet<Character>();
        VALUE_EMPTY_CHARS.add(Character.valueOf((char) 8201));
        VALUE_EMPTY_CHARS.add(Character.valueOf((char) 8194));
        VALUE_EMPTY_CHARS.add(Character.valueOf((char) 8195));
        VALUE_EMPTY_CHARS.add(Character.valueOf((char) 8196));
        VALUE_EMPTY_CHARS.add(Character.valueOf((char) 8197));
    }
}
