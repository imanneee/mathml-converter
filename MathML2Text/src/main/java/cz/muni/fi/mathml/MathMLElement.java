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
     * Integer division operator.
     */
    QUOTIENT("quotient", MathMLType.CONTENT),
    /**
     * Factorial.
     */
    FACTORIAL("factorial", MathMLType.CONTENT_GROUP),
    /**
     * Division
     */
    DIVIDE("divide", MathMLType.CONTENT_MIDDLE),
    /**
     * Maximum.
     */
    MAX("max", MathMLType.CONTENT_GROUP),
    /**
     * Minumum
     */
    MIN("min", MathMLType.CONTENT_GROUP),
    /**
     * With one argument it's unary operator (-x), with two it's subtraction.
     */
    MINUS("minus", MathMLType.CONTENT),
    /**
     * Addition.
     */
    PLUS("plus", MathMLType.CONTENT_MIDDLE),
    /**
     * Exponentiation.
     */
    POWER("power", MathMLType.CONTENT),
    /**
     * Reminder.
     */
    REM("rem", MathMLType.CONTENT),
    /**
     * Multiplication.
     */
    TIMES("times", MathMLType.CONTENT_MIDDLE),
    /**
     * Root.
     */
    ROOT("root", MathMLType.CONTENT),
    /**
     * Degree of the root.
     */
    DEGREE("degree", MathMLType.CONTENT),
    /**
     * Greatest common divisor.
     */
    GCD("gcd", MathMLType.CONTENT_GROUP),
    /**
     * Logical and.
     */
    AND("and", MathMLType.CONTENT_MIDDLE),
    /**
     * Logical or.
     */
    OR("or", MathMLType.CONTENT_MIDDLE),
    /**
     * Logical xor.
     */
    XOR("xor", MathMLType.CONTENT_MIDDLE),
    /**
     * Logical not.
     */
    NOT("not", MathMLType.CONTENT_BEFORE),
    /**
     * Logical implication.
     */
    IMPLIES("implies", MathMLType.CONTENT_MIDDLE),
    /**
     * For all quantifier.
     */
    FORALL("forall", MathMLType.CONTENT),
    /**
     * Exists quantifier.
     */
    EXISTS("exists", MathMLType.CONTENT),
    /**
     * Absolute value.
     */
    ABS("abs", MathMLType.CONTENT_BEFORE),
    /**
     * Function defined over the complex numbers with returns 
     * the complex conjugate of its argument.
     */
    CONJUGATE("conjugate", MathMLType.CONTENT),
    /**
     * Unary function which returns the angular argument of a complex number, 
     * namely the angle which a straight line drawn from the number to zero 
     * makes with the real line (measured anti-clockwise).
     */
    ARG("arg", MathMLType.CONTENT),
    /**
     * Unary operator used to construct an expression representing 
     * the "real" part of a complex number.
     */
    REAL("real", MathMLType.CONTENT),
    /**
     * Unary operator used to construct an expression representing 
     * the "imaginary" part of a complex number.
     */
    IMAGINARY("imaginary", MathMLType.CONTENT),
    /**
     * Lowest common multiple.
     */
    LCM("lcm", MathMLType.CONTENT_GROUP),
    /**
     * Rounds down to the nearest integer.
     */
    FLOOR("floor", MathMLType.CONTENT_BEFORE),
    /**
     * Rounds up to the nearest integer.
     */
    CEILING("ceiling", MathMLType.CONTENT_BEFORE),
    /**
     * Equal to.
     */
    EQ("eq", MathMLType.CONTENT_MIDDLE),
    /**
     * Not equal to.
     */
    NEQ("neq", MathMLType.CONTENT_MIDDLE),
    /**
     * Greater than.
     */
    GT("gt", MathMLType.CONTENT_MIDDLE),
    /**
     * Lower than.
     */
    LT("lt", MathMLType.CONTENT_MIDDLE),
    /**
     * Greater than or equal to.
     */
    GEQ("geq", MathMLType.CONTENT_MIDDLE),
    /**
     * Lower than or equal to.
     */
    LEQ("leq", MathMLType.CONTENT_MIDDLE),
    /**
     * Equivalence.
     */
    EQUIVALENT("equivalent", MathMLType.CONTENT_MIDDLE),
    /**
     * Approximation.
     */
    APPROX("approx", MathMLType.CONTENT_MIDDLE),
    /**
     * Factor of.
     */
    FACTOROF("factorof", MathMLType.CONTENT_MIDDLE),
    /**
     * Integral.
     */
    INT("int", MathMLType.CONTENT),
    /**
     * Differentiation.
     */
    DIFF("diff", MathMLType.CONTENT),
    /**
     * Partial differentiation over several variables.
     */
    PARTIALDIFF("partialdiff", MathMLType.CONTENT),
    /**
     * Divergence.
     */
    DIVERGENCE("divergence", MathMLType.CONTENT),
    /**
     * Gradient.
     */
    GRAD("grad", MathMLType.CONTENT),
    /**
     * Curl.
     */
    CURL("curl", MathMLType.CONTENT),
    /**
     * Laplacian.
     */
    LAPLACIAN("laplacian", MathMLType.CONTENT),
    /**
     * Set.
     */
    SET("set", MathMLType.CONTENT_GROUP),
    /**
     * List.
     */
    LIST("list", MathMLType.CONTENT_GROUP),
    /**
     * Union of sets.
     */
    UNION("union", MathMLType.CONTENT_MIDDLE),
    /**
     * Intersection of sets.
     */
    INTERSECT("intersect", MathMLType.CONTENT_MIDDLE),
    /**
     * Set inclusion.
     */
    IN("in", MathMLType.CONTENT_MIDDLE),
    /**
     * Negated set inclusion.
     */
    NOTIN("notin", MathMLType.CONTENT_MIDDLE),
    /**
     * Subset.
     */
    SUBSET("subset", MathMLType.CONTENT_MIDDLE),
    /**
     * Proper subset.
     */
    PRSUBSET("prsubset", MathMLType.CONTENT_MIDDLE),
    /**
     * Negated subset.
     */
    NOTSUBSET("notsubset", MathMLType.CONTENT_MIDDLE),
    /**
     * Negated proper subset.
     */
    NOTPRSUBSET("notprsubset", MathMLType.CONTENT_MIDDLE),
    /**
     * Set difference.
     */
    SETDIFF("setdiff", MathMLType.CONTENT_MIDDLE),
    /**
     * Set cardinality.
     */
    CARD("card", MathMLType.CONTENT),
    /**
     * Cartesian product.
     */
    CARTESIANPRODUCT("cartesianproduct", MathMLType.CONTENT),
    /**
     * Summation.
     */
    SUM("sum", MathMLType.CONTENT),
    /**
     * Product.
     */
    PRODUCT("product", MathMLType.CONTENT),
    /**
     * Limit.
     */
    LIMIT("limit", MathMLType.CONTENT),
    /**
     * Relation that a quantity is tending to a specified value (x -> 0);
     */
    TENDSTO("tendsto", MathMLType.CONTENT),
    /**
     * Sine.
     */
    SIN("sin", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Cosine.
     */
    COS("cos", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Tangent
     */
    TAN("tan", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Secant.
     */
    SEC("sec", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Cosecant.
     */
    CSC("csc", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Cotangent.
     */
    COT("cot", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Hyperbolic sine.
     */
    SINH("sinh", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Hyperbolic cosine.
     */
    COSH("cosh", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Hyperbolic tangent.
     */
    TANH("tanh", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Hyperbolic secant.
     */
    SECH("sech", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Hyperbolic cosecant.
     */
    CSCH("csch", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Hyperbolic cotangent.
     */
    COTH("coth", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Arcsine.
     */
    ARCSIN("arcsin", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Arccosine.
     */
    ARCCOS("arccos", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Arctangent.
     */
    ARCTAN("arctan", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Inverse hyperbolic cosine.
     */
    ARCCOSH("arccosh", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Arccotangent.
     */
    ARCCOT("arccot", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Inverse hyperbolic tangent.
     */
    ARCCOTH("arccoth", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Inverse cosecant.
     */
    ARCCSC("arccsc", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Inverse hyperbolic cosecant.
     */
    ARCCSCH("arccsch", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Inverse secant.
     */
    ARCSEC("arcsec", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Inverse hyperbolic secant.
     */
    ARCSECH("arcsech", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Inverse hyperbolic sine.
     */
    ARCSINH("arcsinh", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Inverse hyperbolic tangent.
     */
    ARCTANH("arctanh", MathMLType.CONTENT_TRIGONOMETRY),
    /**
     * Exponentiation.
     */
    EXP("exp", MathMLType.CONTENT_BEFORE),
    /**
     * Natural logarithm.
     */
    LN("ln", MathMLType.CONTENT_BEFORE),
    /**
     * Logarithm.
     */
    LOG("log", MathMLType.CONTENT),
    /**
     * Base of logarithm.
     */
    LOGBASE("logbase", MathMLType.CONTENT),
    /**
     * Set of integers.
     */
    INTEGERS("integers", MathMLType.CONTENT),
    /**
     * Set of real numbers.
     */
    REALS("reals", MathMLType.CONTENT),
    /**
     * Set of rational numbers.
     */
    RATIONALS("rationals", MathMLType.CONTENT),
    /**
     * Set of natural numbers (including zero).
     */
    NATURALNUMBERS("naturalnumbers", MathMLType.CONTENT),
    /**
     * Set of complex numbers.
     */
    COMPLEXES("complexes", MathMLType.CONTENT),
    /**
     * Set of prime numbers.
     */
    PRIMES("primes", MathMLType.CONTENT),
    /**
     * Base of natural logarithm (Euler's number).
     */
    EXPONENTIALE("exponentiale", MathMLType.CONTENT),
    /**
     * Imaginary number, root of -1.
     */
    IMAGINARYI("imaginaryi", MathMLType.CONTENT),
    /**
     * Not of not-a-number.
     */
    NOTANUMBER("notanumber", MathMLType.CONTENT),
    /**
     * Boolean value true.
     */
    TRUE("true", MathMLType.CONTENT),
    /**
     * Boolean value false.
     */
    FALSE("false", MathMLType.CONTENT),
    /**
     * Empty set.
     */
    EMPTYSET("emptyset", MathMLType.CONTENT),
    /**
     * Pi.
     */
    PI("pi", MathMLType.CONTENT),
    /**
     * Gamma constant, approx 0.5772.
     */
    EULERGAMMA("eulergamma", MathMLType.CONTENT),
    /**
     * Infinity
     */
    INFINITY("infinity", MathMLType.CONTENT),
    
    //@todo vectors and statistics
    
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
