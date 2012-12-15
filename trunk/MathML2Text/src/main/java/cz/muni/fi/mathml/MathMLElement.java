package cz.muni.fi.mathml;

/**
 * Enumeration of MathML elements.
 * @todo Add content MathML elements.
 * 
 * @author Maros Kucbel Sep 16, 2012, 11:58:46 AM
 */
public enum MathMLElement {
    /************** PRESENTATION MATHML *************/
    /** 
     * Root element of MathML.
     */
    MATH("math", MathMLType.PRESENTATION),
    
    /*** Presentation MathML elements. ***/
    /**
     * Grouping element.
     */
    MROW("mrow", MathMLType.PRESENTATION),
    /**
     * Number.
     */
    MN("mn", MathMLType.PRESENTATION),
    /**
     * Identifier.
     */
    MI("mi", MathMLType.PRESENTATION),
    /**
     * Operation.
     */
    MO("mo", MathMLType.PRESENTATION),
    /**
     * Fraction or binomial number.
     */
    MFRAC("mfrac", MathMLType.PRESENTATION),
    /**
     * Subscript.
     */
    MSUB("msub", MathMLType.PRESENTATION),
    /**
     * Superscript.
     */
    MSUP("msup", MathMLType.PRESENTATION),
    /**
     * Subscript-superscript.
     */
    MSUBSUP("msubsup", MathMLType.PRESENTATION),
    /**
     * Underscript.
     */
    MUNDER("munder", MathMLType.PRESENTATION),
    /**
     * Overscript.
     */
    MOVER("mover", MathMLType.PRESENTATION),
    /**
     * Underscript-overscript.
     */
    MUNDEROVER("munderover", MathMLType.PRESENTATION),
    /**
     * Square root.
     */
    MSQRT("msqrt", MathMLType.PRESENTATION),
    /**
     * Radical with index.
     */
    MROOT("mroot", MathMLType.PRESENTATION),
    /**
     * Parentheses.
     */
    MFENCED("mfenced", MathMLType.PRESENTATION),
    /**
     * String literal.
     */
    MS("ms", MathMLType.PRESENTATION),
    /**
     * Text.
     */
    MTEXT("mtext", MathMLType.PRESENTATION),
    /**
     * Space.
     */
    MSPACE("mspace", MathMLType.PRESENTATION),
    /**
     * Table or matrix.
     */
    MTABLE("mtable", MathMLType.PRESENTATION),
    /**
     * Row in a table or a matrix.
     */
    MTR("mtr", MathMLType.PRESENTATION),
    /**
     * Cell in a table or a matrix.
     */
    MTD("mtd", MathMLType.PRESENTATION),
    /**
     * Container for annotations.
     */
    SEMANTICS("semantics", MathMLType.OTHER),
    /**
     * Different kind of MathML notation (for example content). 
     * Attribute <code>encoding</code> is used to ascertain exact type.
     */
    ANNOTATION_XML("annotation-xml", MathMLType.OTHER),
    /************** CONTENT MATHML *************/
    /**
     * A number.
     */
    CN("cn", MathMLType.CONTENT),
    /**
     * An identifier.
     */
    CI("ci", MathMLType.CONTENT),
    /**
     * Symbol.
     */
    CSYMBOL("csymbol", MathMLType.CONTENT),
    /**
     * Function application.
     */
    APPLY("apply", MathMLType.CONTENT),
    /**
     * Unknown element.
     */
    UNKNOWN("unknown", MathMLType.OTHER);
    
    /** Name of this element (name of XML tag). */
    private final String elementName;
    
    private final MathMLType type;
    
    private MathMLElement(final String elementName, final MathMLType type) {
        this.elementName = elementName;
        this.type = type;
    }
    
    /**
     * Returns the name of this element (name of XML tag).
     */
    public String getElementName() {
        return this.elementName;
    }
    
    public MathMLType getType() {
        return this.type;
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
