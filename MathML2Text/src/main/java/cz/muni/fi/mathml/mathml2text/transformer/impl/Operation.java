package cz.muni.fi.mathml.mathml2text.transformer.impl;

import java.util.Collection;

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
    ADD("plus", "+", "&plus;", "plus"),
    /**
     * Subtraction operator.
     */
    SUBTRACT("minus", "-", "&minus;", "minus"),
    /**
     * Multiplication operator.
     */
    MULTIPLY("times", "&times;", "*", "times", "&sdot;"),
    /**
     * Division operator.
     */
    DIVIDE("divide", "/", "&divide;", "divide", ":"),
    /**
     * Equals.
     */
    EQUALS("equals", "=", "eq"),
    /**
     * Logarithm.
     */
    LOGARITHM("logarithm", "log"),
    /**
     * Natural logarithm.
     */
    NATURAL_LOGARITHM("naturalLogarithm", "ln"),
    /**
     * Square. (to the power of 2)
     */
    SQUARE("square", "2"),
    /**
     * Limit.
     */
    LIMIT("limit", "lim"),
    /**
     * A variable approaches a value.
     */
    APPROACHES("approaches", "&rarr;", "rarr", "→"),
    /**
     * Integral.
     */
    INTEGRAL("integral", "int", "&int;"),
    /**
     * Differential of integral.
     */
    DIFFERENTIAL("differential", "dd", "&dd;"),
    /**
     * Summation.
     */
    SUMMATION("summation", "sum", "&sum;"),
    /**
     * Product.
     */
    PRODUCT("product", "prod", "&prod;", "∏", "&amp;prod;"),
    /**
     * Sine.
     */
    SIN("sin", "sin"),
    /**
     * Cosine.
     */
    COS("cos", "cos"),
    /**
     * Tangent
     */
    TAN("tan", "tan"),
    /**
     * Secant.
     */
    SEC("sec", "sec"),
    /**
     * Cosecant.
     */
    CSC("csc", "csc"),
    /**
     * Cotangent.
     */
    COT("cot", "cot"),
    /**
     * Hyperbolic sine.
     */
    SINH("sinh", "sinh"),
    /**
     * Hyperbolic cosine.
     */
    COSH("cosh", "cosh"),
    /**
     * Hyperbolic tangent.
     */
    TANH("tanh", "tanh"),
    /**
     * Hyperbolic secant.
     */
    SECH("sech", "sech"),
    /**
     * Hyperbolic cosecant.
     */
    CSCH("csch", "csch"),
    /**
     * Hyperbolic cotangent.
     */
    COTH("coth", "coth"),
    /**
     * Arcsine.
     */
    ARCSIN("arcsin", "arcsin"),
    /**
     * Arccosine.
     */
    ARCCOS("arccos", "arccos"),
    /**
     * Arctangent.
     */
    ARCTAN("arctan", "arctan"),
    /**
     * Inverse hyperbolic cosine.
     */
    ARCCOSH("arccosh", "arccosh"),
    /**
     * Arccotangent.
     */
    ARCCOT("arccot", "arccot"),
    /**
     * Inverse hyperbolic tangent.
     */
    ARCCOTH("arccoth", "arccoth"),
    /**
     * Inverse cosecant.
     */
    ARCCSC("arccsc", "arccsc"),
    /**
     * Inverse hyperbolic cosecant.
     */
    ARCCSCH("arccsch", "arccsch"),
    /**
     * Inverse secant.
     */
    ARCSEC("arcsec", "arcsec"),
    /**
     * Inverse hyperbolic secant.
     */
    ARCSECH("arcsech", "arcsech"),
    /**
     * Inverse hyperbolic sine.
     */
    ARCSINH("arcsinh", "arcsinh"),
    /**
     * Inverse hyperbolic tangent.
     */
    ARCTANH("arctanh", "arctanh"),
    /**
     * Not equal.
     */
    NOT_EQUAL_TO("neq", "neq", "&neq;"),
    /**
     * Greater than.
     */
    GREATER_THAN("gt", "gt", "&gt;"),
    /**
     * Lower than.
     */
    LOWER_THAN("lt", "lt", "&lt;"),
    /**
     * Greater than or equal to.
     */
    GREATER_THAN_OR_EQUAL_TO("geq", "geq", "&geq;"),
    /**
     * Lower than or equal to.
     */
    LOWER_THAN_OR_EQUAL_TO("leq", "leq", "&leq;"),
    /**
     * Equivalence.
     */
    EQUIVALENT_TO("equivalent", "equivalent", "&hArr;"),
    /**
     * Approximation.
     */
    APPROXIMATELY("approx", "approx"),
    /**
     * Logical and.
     */
    AND("and", "and"),
    /**
     * Logical or.
     */
    OR("or", "or"),
    /**
     * Logical xor.
     */
    XOR("xor", "xor"),
    /**
     * Logical implication.
     */
    IMPLIES("implies", "implies"),
    /**
     * Factor of.
     */
    FACTOR_OF("factorof", "factorof"),
    /**
     * Union of sets.
     */
    UNION("union", "union"),
    /**
     * Intersection of sets.
     */
    INTERSECT("intersect", "intersect"),
    /**
     * Set inclusion.
     */
    IN("in", "in", "∈", "&isin;"),
    /**
     * Negated set inclusion.
     */
    NOTIN("notin", "notin"),
    /**
     * Subset.
     */
    SUBSET("subset", "subset", "⊂", "&sub;"),
    /**
     * Proper subset.
     */
    PRSUBSET("prsubset", "prsubset"),
    /**
     * Negated subset.
     */
    NOTSUBSET("notsubset", "notsubset"),
    /**
     * Negated proper subset.
     */
    NOTPRSUBSET("notprsubset", "notprsubset"),
    /**
     * Set difference.
     */
    SETDIFF("setdiff", "setdiff"),
    /**
     * Logical negation.
     */
    NOT("not", "not"),
    /**
     * Absolute value.
     */
    ABSOLUTE_VALUE("abs", "abs"),
    /**
     * Function that rounds down to the nearest integer.
     */
    FLOOR("floor", "floor"),
    /**
     * Function that rounds up to the nearest integer.
     */
    CEILING("ceiling", "ceiling"),
    /**
     * Exponentiation with base <i>e</i>.
     */
    EXPONENTIAL("exp", "exp"),
    /**
     * Set cardinality.
     */
    CARDINALITY("card", "card"),
    /**
     * Remainder.
     */
    REMAINDER("rem", "rem"),
    /**
     * Integer division operator.
     */
    QUOTIENT("quotient", "quotient"),
    /**
     * Exponentiation.
     */
    EXPONENTIATION("power", "power"),
    /**
     * Root extraction.
     */
    ROOT("root", "root"),
    /**
     * Open parentheses.
     */
    OPEN_BRACES("open_braces", "("),
    /**
     * Close parentheses.
     */
    CLOSE_BRACES("close_braces", ")"),
    /**
     * Superscript.
     */
    SUPERSCRIPT("superscript", "superscript"),
    /**
     * Subscript.
     */
    SUBSCRIPT("subscript", "subscript"),
    /**
     * Some files use ci instead of csymbol to express function.
     */
    CI("ci"),
    /**
     * Assign value to variable.
     */
    ASSIGN("assign", ":="),
    /**
     * Tilde above a letter.
     */
    TILDE("tilde", "~"),
    /**
     * Dash above a letter.
     */
    DASHED("dashed", "¯"),
    /**
     * Interval.
     */
    INTERVAL("interval", "interval"),
    /**
     * Function composition.
     */
    COMPOSE("compose", "compose"),
    /**
     * Vector (a matrix with one column).
     */
    VECTOR("vector", "vector"),
    /**
     * Direct sum.
     */
    DIRECT_SUM("direct_sum", "&oplus;", "⊕");
    
    /**
     * Localization key for this operation.
     */
    private final String key;
    /**
     * Possible symbols for this operation.
     */
    private final Collection<String> symbols; 
    /**
     * Constructor.
     * @param key Localization key.
     * @param symbols Possible symbols.
     */
    private Operation(final String key, final String... symbols) {
        this.key = key;
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
        for (final Operation operation : Operation.values()) {
            if (operation.getSymbols().contains(symbol)) {
                return operation;
            }
        }
        return null;
    }
    
}
