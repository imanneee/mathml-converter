package cz.muni.fi.mathml.mathml2text.transformer.impl;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import cz.muni.fi.mathml.MathMLType;

/**
 * Enumeration of supported mathematical operations and their possible 
 * representation in presentation MathML (mo element).
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
    MULTIPLY("times", "&times;", "*", "times"),
    /**
     * Division operator.
     */
    DIVIDE("divide", "/", "&divide;", "divide"),
    /**
     * Equals.
     */
    EQUALS("equals", "="),
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
    
    APPROACHES("approaches", "&rarr;", "rarr"),
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
    PRODUCT("product", "prod", "&prod;"),
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
    ARCTANH("arctanh", "arctanh");
    
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
