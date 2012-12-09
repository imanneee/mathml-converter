package cz.muni.fi.mathml;

/**
 * Enumeration of MathML elements.
 * @todo Add content MathML elements.
 * 
 * @author Maros Kucbel Sep 16, 2012, 11:58:46 AM
 */
public enum MathMLElement {
    /** 
     * Root element of MathML.
     */
    MATH("math"),
    
    /*** Presentation MathML elements. ***/
    /**
     * Grouping element.
     */
    MROW("mrow"),
    /**
     * Number.
     */
    MN("mn"),
    /**
     * Identifier.
     */
    MI("mi"),
    /**
     * Operation.
     */
    MO("mo"),
    /**
     * Fraction or binomial number.
     */
    MFRAC("mfrac"),
    /**
     * Subscript.
     */
    MSUB("msub"),
    /**
     * Superscript.
     */
    MSUP("msup"),
    /**
     * Subscript-superscript.
     */
    MSUBSUP("msubsup"),
    /**
     * Underscript.
     */
    MUNDER("munder"),
    /**
     * Overscript.
     */
    MOVER("mover"),
    /**
     * Underscript-overscript.
     */
    MUNDEROVER("munderover"),
    /**
     * Square root.
     */
    MSQRT("msqrt"),
    /**
     * Radical with index.
     */
    MROOT("mroot"),
    /**
     * Parentheses.
     */
    MFENCED("mfenced"),
    /**
     * String literal.
     */
    MS("ms"),
    /**
     * Text.
     */
    MTEXT("mtext"),
    /**
     * Space.
     */
    MSPACE("mspace"),
    /**
     * Table or matrix.
     */
    MTABLE("mtable"),
    /**
     * Row in a table or a matrix.
     */
    MTR("mtr"),
    /**
     * Cell in a table or a matrix.
     */
    MTD("mtd"),
    /**
     * Container for annotations.
     */
    SEMANTICS("semantics"),
    /**
     * Different kind of MathML notation (for example content). 
     * Attribute <code>encoding</code> is used to ascertain exact type.
     */
    ANNOTAION_XML("annotation-xml"),
    /**
     * Unknown element.
     */
    UNKNOWN("unknown");
    
    /** Name of this element (name of XML tag). */
    private final String elementName;
    
    private MathMLElement(final String elementName) {
        this.elementName = elementName;
    }
    
    /**
     * Returns the name of this element (name of XML tag).
     */
    public String getElementName() {
        return this.elementName;
    }
    
    /**
     * Returns MathML element of given name or {@code null} if there isn't one.
     * 
     * @param elementName Name of the MathML element.
     * @return MathML element of given name or {@code null}.
     */
    public static MathMLElement forElementName(final String elementName) {
        for (final MathMLElement element : values()) {
            if (element.getElementName().equals(elementName)) {
                return element;
            }
        }
        return UNKNOWN;
    }
}
