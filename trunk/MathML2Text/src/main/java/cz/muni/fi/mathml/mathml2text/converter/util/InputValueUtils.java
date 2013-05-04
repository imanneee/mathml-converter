package cz.muni.fi.mathml.mathml2text.converter.util;

/**
 * Helper methods for processing input values.
 * 
 * @author Maros Kucbel
 * @date 2013-03-03T15:55:19+0100
 */
public final class InputValueUtils {

    /**
     * Replaces special characters (non-ASCII) with their equivalent HTML 
     * number entities.
     * Code taken from <a href="http://shankarpshetty.blogspot.cz/2009/11/java-function-to-convert-string-to-html.html">this blog</a>.
     * @param input Input string.
     * @return String with replaced special characters
     */
    public static String buildHtmlEntityCode(String input) {
        StringBuilder output = new StringBuilder(input.length() * 2);

        int len = input.length();
        int code, code1, code2, code3, code4;
        char ch;

        for (int i = 0; i < len;) {
            code1 = input.codePointAt(i);
            if (code1 >> 3 == 30) {
                code2 = input.codePointAt(i + 1);
                code3 = input.codePointAt(i + 2);
                code4 = input.codePointAt(i + 3);
                code = ((code1 & 7) << 18) | ((code2 & 63) << 12) | ((code3 & 63) << 6) | (code4 & 63);
                i += 4;
                output.append("&#").append(code).append(";");
            } else if (code1 >> 4 == 14) {
                code2 = input.codePointAt(i + 1);
                code3 = input.codePointAt(i + 2);
                code = ((code1 & 15) << 12) | ((code2 & 63) << 6) | (code3 & 63);
                i += 3;
                output.append("&#").append(code).append(";");
            } else if (code1 >> 5 == 6) {
                code2 = input.codePointAt(i + 1);
                code = ((code1 & 31) << 6) | (code2 & 63);
                i += 2;
                output.append("&#").append(code).append(";");
            } else {
                code = code1;
                i += 1;

                ch = (char) code;
                if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9') {
                    output.append(ch);
                } else {
                    output.append("&#").append(code).append(";");
                }
            }
        }

        return output.toString();
    }
}