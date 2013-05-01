package cz.muni.fi.mathml.mathml2text.converter.impl;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableList;


/**
 * Enumeration of supported mathematical operations and their possible 
 * representation in presentation MathML (<code>&lt;mo&gt;</code>, <code>&lt;mi&gt;</code>) 
 * as well as in content MathML (<code>&lt;csymbol&gt;</code>, <code>&lt;ci&gt;</code>).
 * 
 * @author Maros Kucbel Sep 16, 2012, 4:54:09 PM
 */
public enum Operation {
    /**
     * Addition operator.
     */
    ADD("plus", OperationType.INFIX_OR_PREFIX, "+", "&plus;", "plus"),
    /**
     * Subtraction operator.
     */
    SUBTRACT("minus", OperationType.INFIX_OR_PREFIX, "-", "&minus;", "minus"),
    /**
     * Multiplication operator.
     */
    MULTIPLY("times", OperationType.INFIX, "&times;", "*", "times", "&sdot;", "&#8901;", "&#x22C5;", "⋅", "&#8290;", "&#x2062;", "⁢"),
    /**
     * Division operator.
     */
    DIVIDE("divide", OperationType.INFIX, "/", "&divide;", "divide", ":"),
    /**
     * Equals.
     */
    EQUALS("equals", OperationType.INFIX, "=", "eq"),
    /**
     * Logarithm.
     */
    LOGARITHM("logarithm", OperationType.SPECIAL, "log"),
    /**
     * Natural logarithm.
     */
    NATURAL_LOGARITHM("naturalLogarithm", OperationType.SPECIAL, "ln"),
    /**
     * Square. (to the power of 2)
     */
    SQUARE("square", OperationType.SPECIAL, "2"),
    /**
     * Limit.
     */
    LIMIT("limit", OperationType.SPECIAL, "lim"),
    /**
     * A variable approaches a value.
     */
    APPROACHES("approaches", OperationType.INFIX, "&rarr;", "rarr", "→"),
    /**
     * Integral.
     */
    INTEGRAL("integral", OperationType.SPECIAL, "int", "&int;"),
    /**
     * Differential of integral.
     */
    DIFFERENTIAL("differential", OperationType.SPECIAL, "dd", "&dd;"),
    /**
     * Summation.
     */
    SUMMATION("summation", OperationType.SPECIAL, "sum", "&sum;"),
    /**
     * Product.
     */
    PRODUCT("product", OperationType.SPECIAL, "prod", "&prod;", "∏", "&amp;prod;"),
    /**
     * Sine.
     */
    SIN("sin", OperationType.PREFIX, "sin"),
    /**
     * Cosine.
     */
    COS("cos", OperationType.PREFIX, "cos"),
    /**
     * Tangent
     */
    TAN("tan", OperationType.PREFIX, "tan"),
    /**
     * Secant.
     */
    SEC("sec", OperationType.PREFIX, "sec"),
    /**
     * Cosecant.
     */
    CSC("csc", OperationType.PREFIX, "csc"),
    /**
     * Cotangent.
     */
    COT("cot", OperationType.PREFIX, "cot"),
    /**
     * Hyperbolic sine.
     */
    SINH("sinh", OperationType.PREFIX, "sinh"),
    /**
     * Hyperbolic cosine.
     */
    COSH("cosh", OperationType.PREFIX, "cosh"),
    /**
     * Hyperbolic tangent.
     */
    TANH("tanh", OperationType.PREFIX, "tanh"),
    /**
     * Hyperbolic secant.
     */
    SECH("sech", OperationType.PREFIX, "sech"),
    /**
     * Hyperbolic cosecant.
     */
    CSCH("csch", OperationType.PREFIX, "csch"),
    /**
     * Hyperbolic cotangent.
     */
    COTH("coth", OperationType.PREFIX, "coth"),
    /**
     * Arcsine.
     */
    ARCSIN("arcsin", OperationType.PREFIX, "arcsin"),
    /**
     * Arccosine.
     */
    ARCCOS("arccos", OperationType.PREFIX, "arccos"),
    /**
     * Arctangent.
     */
    ARCTAN("arctan", OperationType.PREFIX, "arctan"),
    /**
     * Inverse hyperbolic cosine.
     */
    ARCCOSH("arccosh", OperationType.PREFIX, "arccosh"),
    /**
     * Arccotangent.
     */
    ARCCOT("arccot", OperationType.PREFIX, "arccot"),
    /**
     * Inverse hyperbolic tangent.
     */
    ARCCOTH("arccoth", OperationType.PREFIX, "arccoth"),
    /**
     * Inverse cosecant.
     */
    ARCCSC("arccsc", OperationType.PREFIX, "arccsc"),
    /**
     * Inverse hyperbolic cosecant.
     */
    ARCCSCH("arccsch", OperationType.PREFIX, "arccsch"),
    /**
     * Inverse secant.
     */
    ARCSEC("arcsec", OperationType.PREFIX, "arcsec"),
    /**
     * Inverse hyperbolic secant.
     */
    ARCSECH("arcsech", OperationType.PREFIX, "arcsech"),
    /**
     * Inverse hyperbolic sine.
     */
    ARCSINH("arcsinh", OperationType.PREFIX, "arcsinh"),
    /**
     * Inverse hyperbolic tangent.
     */
    ARCTANH("arctanh", OperationType.PREFIX, "arctanh"),
    /**
     * Not equal.
     */
    NOT_EQUAL_TO("neq", OperationType.INFIX, "neq", "&neq;"),
    /**
     * Greater than.
     */
    GREATER_THAN("gt", OperationType.INFIX, "gt", "&gt;"),
    /**
     * Lower than.
     */
    LOWER_THAN("lt", OperationType.INFIX, "lt", "&lt;"),
    /**
     * Greater than or equal to.
     */
    GREATER_THAN_OR_EQUAL_TO("geq", OperationType.INFIX, "geq", "&geq;"),
    /**
     * Lower than or equal to.
     */
    LOWER_THAN_OR_EQUAL_TO("leq", OperationType.INFIX, "leq", "&leq;"),
    /**
     * Equivalence.
     */
    EQUIVALENT_TO("equivalent", OperationType.INFIX, "equivalent", "&hArr;", "⇔"),
    /**
     * Approximation.
     */
    APPROXIMATELY("approx", OperationType.INFIX, "approx", "≈"),
    /**
     * Logical and.
     */
    AND("and", OperationType.INFIX, "and"),
    /**
     * Logical or.
     */
    OR("or", OperationType.INFIX, "or"),
    /**
     * Logical xor.
     */
    XOR("xor", OperationType.INFIX, "xor"),
    /**
     * Logical implication.
     */
    IMPLIES("implies", OperationType.INFIX, "implies"),
    /**
     * Factor of.
     */
    FACTOR_OF("factorof", OperationType.INFIX, "factorof"),
    /**
     * Union of sets.
     */
    UNION("union", OperationType.INFIX, "union"),
    /**
     * Intersection of sets.
     */
    INTERSECT("intersect", OperationType.INFIX, "intersect"),
    /**
     * Set inclusion.
     */
    IN("in", OperationType.INFIX, "in", "∈", "&isin;"),
    /**
     * Negated set inclusion.
     */
    NOTIN("notin", OperationType.INFIX, "notin"),
    /**
     * Subset.
     */
    SUBSET("subset", OperationType.INFIX, "subset", "⊂", "&sub;"),
    /**
     * Proper subset.
     */
    PRSUBSET("prsubset", OperationType.INFIX, "prsubset"),
    /**
     * Negated subset.
     */
    NOTSUBSET("notsubset", OperationType.INFIX, "notsubset"),
    /**
     * Negated proper subset.
     */
    NOTPRSUBSET("notprsubset", OperationType.INFIX, "notprsubset"),
    /**
     * Set difference.
     */
    SETDIFF("setdiff", OperationType.INFIX, "setdiff"),
    /**
     * Logical negation.
     */
    NOT("not", OperationType.PREFIX, "not"),
    /**
     * Absolute value.
     */
    ABSOLUTE_VALUE("abs", OperationType.PREFIX, "abs"),
    /**
     * Function that rounds down to the nearest integer.
     */
    FLOOR("floor", OperationType.PREFIX, "floor"),
    /**
     * Function that rounds up to the nearest integer.
     */
    CEILING("ceiling", OperationType.PREFIX, "ceiling"),
    /**
     * Exponentiation with base <i>e</i>.
     */
    EXPONENTIAL("exp", OperationType.SPECIAL, "exp"),
    /**
     * Set cardinality.
     */
    CARDINALITY("card", OperationType.SPECIAL, "card"),
    /**
     * Remainder.
     */
    REMAINDER("rem", OperationType.SPECIAL, "rem"),
    /**
     * Integer division operator.
     */
    QUOTIENT("quotient", OperationType.SPECIAL, "quotient"),
    /**
     * Exponentiation.
     */
    EXPONENTIATION("power", OperationType.SPECIAL, "power"),
    /**
     * Root extraction.
     */
    ROOT("root", OperationType.SPECIAL, "root"),
    /**
     * Open parentheses.
     */
    OPEN_BRACES("open_braces", OperationType.SPECIAL, "("),
    /**
     * Close parentheses.
     */
    CLOSE_BRACES("close_braces", OperationType.SPECIAL, ")"),
    /**
     * Open angle braces.
     */
    LEFT_ANGLE_BRACKET("left_angle_bracket", OperationType.SPECIAL, "⟨", "&#10216;", "&#x27E8;"),
    /**
     * Close angle braces.
     */
    RIGHT_ANGLE_BRACKET("right_angle_bracket", OperationType.SPECIAL, "⟩", "&#10217;", "&#x27E9;"),
    /**
     * Open square braces.
     */
    LEFT_SQUARE_BRACKET("left_square_bracket", OperationType.SPECIAL, "[", "&#91;", "&#x005B;"),
    /**
     * Close square braces.
     */
    RIGHT_SQUARE_BRACKET("right_square_bracket", OperationType.SPECIAL, "]", "&#93;", "&#x005D;"),
    /**
     * Open curly braces.
     */
    LEFT_CURLY_BRACKET("left_curly_bracket", OperationType.SPECIAL, "{", "&#125;", "&#x007D;"),
    /**
     * Close curly braces.
     */
    RIGHT_CURLY_BRACKET("right_curly_bracket", OperationType.SPECIAL, "}", "&#102177;", "&#x27E9;"),
    /**
     * Superscript.
     */
    SUPERSCRIPT("superscript", OperationType.SPECIAL, "superscript"),
    /**
     * Subscript.
     */
    SUBSCRIPT("subscript", OperationType.SPECIAL, "subscript"),
    /**
     * Some files use ci instead of csymbol to express function.
     */
    CI("ci", OperationType.SPECIAL),
    /**
     * Assign value to variable.
     */
    ASSIGN("assign", OperationType.SPECIAL, ":="),
    /**
     * Tilde above a letter.
     */
    TILDE("tilde", OperationType.EVERY_ARGUMENT, "~", "&#126;", "&#x007E;", "&#x7E;"),
    /**
     * Dash above a letter.
     */
    DASHED("dashed", OperationType.EVERY_ARGUMENT, "¯"),
    /**
     * Interval.
     */
    INTERVAL("interval", OperationType.SPECIAL, "interval"),
    /**
     * Function composition.
     */
    COMPOSE("compose", OperationType.SPECIAL, "compose"),
    /**
     * Vector (a matrix with one column).
     */
    VECTOR("vector", OperationType.SPECIAL, "vector"),
    /**
     * Direct sum.
     */
    DIRECT_SUM("direct_sum", OperationType.SPECIAL, "&oplus;", "⊕"),
    /**
     * Plus minus.
     */
    PLUS_MINUS("plus_minus", OperationType.INFIX, "&plusmn;", "±"),
    /**
     * Minus plus
     */
    MINUS_PLUS("minus_plus", OperationType.INFIX, "$#8723;", "&#x2213;", "∓"),
    /**
     * Almost equal.
     */
    SIMEQ("simeq", OperationType.INFIX, "≃"),
    /**
     * Roughly similar.
     */
    SIM("sim", OperationType.INFIX, "∼", "&#8764;", "&#x223C;", "&sim;"),
    /**
     * Far greater than.
     */
    FAR_GT("far_gt", OperationType.INFIX, "≫"),
    /**
     * Far lower than.
     */
    FAR_LT("far_lt", OperationType.INFIX, "≪"),
    /**
     * Comma.
     */
    COMMA("comma", OperationType.INFIX, ",", "&#44;"),
    /**
     * Dot.
     */
    DOT("dot", OperationType.INFIX, ".", "&#46;"),
    /**
     * Proportional to.
     */
    PROPORTIONAL_TO("proportional_to", OperationType.INFIX, "∝", "&#8733;", "&#x221D;", "&prop;", "prop"),
    /**
     * Less than or equivalent to.
     */
    LT_EQUIV("lt_equiv", OperationType.INFIX, "≲", "&#8818;", "&#x2272;"),
    /**
     * Greater than or equivalent to.
     */
    GT_EQUIV("gt_equiv", OperationType.INFIX, "≳", "&#8819;", "&#x2273;"),
    /**
     * Identical to.
     */
    IDENTICAL("identical_to", OperationType.INFIX, "≡", "&#8801;", "&#x2261;"),
    /**
     * Set.
     */
    SET("set", OperationType.PREFIX_MULTI, "set"),
    /**
     * List.
     */
    LIST("list", OperationType.PREFIX_MULTI, "list"),
    /**
     * Prime - minutes, feet.
     */
    PRIME("prime", OperationType.INFIX, "&#8242;", "&#x2032;", "&prime;"),
    /**
     * Double prime - seconds, inches.
     */
    DOUBLE_PRIME("double_prime", OperationType.INFIX, "&#8243;", "&#x2033;", "&Prime;"),
    /**
     * A dot above a letter.
     */
    DOT_ABOVE("dot_above", OperationType.EVERY_ARGUMENT, "&#729;", "&#x2D9;", "˙"),
    /**
     * An up pointing arrowhead above a letter.
     */
    UP_ARROWHEAD("up_arrowhead", OperationType.EVERY_ARGUMENT, "&#8963;", "&#x2303;", "⌃"),
    /**
     * Orthogonal to.
     */
    PERPENDICULAR("perpendicular", OperationType.INFIX, "&#10178;", "&#x27C2;", "⟂"),
    /**
     * Diaeresis above a letter.
     */
    DIAERESIS("diaeresis", OperationType.EVERY_ARGUMENT, "¨", "&#168;", "&#x00A8;"),
    /**
     * Partial differentiation.
     */
    PARTIALDIFF("partialdiff", OperationType.PREFIX, "partialdiff"),
    /**
     * Factorial.
     */
    FACTORIAL("factorial", OperationType.PREFIX, "factorial"),
    /**
     * Empty or {@code null} function.
     */
    EMPTY("empty", OperationType.INFIX);
    
    /**
     * Localization key for this operation.
     */
    private final String key;
    /**
     * Type of the operation.
     */
    private final OperationType type;
    /**
     * Possible symbols for this operation.
     */
    private final Collection<String> symbols; 
    /**
     * Constructor.
     * @param key Localization key.
     * @param symbols Possible symbols.
     */
    private Operation(final String key, final OperationType type, final String... symbols) {
        this.key = key;
        this.type = type;
        final ImmutableList.Builder<String> builder = new ImmutableList.Builder<String>();
        builder.add(symbols);
        this.symbols = builder.build();
    }
    
    /**
     * Returns localization key for this operation.
     * @return Localization key for this operation.
     */
    public String getKey() {
        return this.key;
    }
    
    /**
     * Returns the type of the operation.
     * @return Type of the operation.
     */
    public OperationType getType() {
        return this.type;
    }
    
    /**
     * Returns symbols for this operation.
     * @return Symbols for this operation.
     */
    public Collection<String> getSymbols() {
        return this.symbols;
    }
    
    /**
     * Finds operation for given symbol. Returns {@code null} if there isn't one.
     * @param symbol Symbol.
     * @return Operation for symbol.
     */
    public static Operation forSymbol(final String symbol) {
        if (StringUtils.isBlank(symbol)) {
            return Operation.EMPTY;
        }
        for (final Operation operation : Operation.values()) {
            if (operation.getSymbols().contains(symbol)) {
                return operation;
            }
        }
        return null;
    }
    
}
