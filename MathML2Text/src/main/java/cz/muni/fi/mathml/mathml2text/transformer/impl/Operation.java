package cz.muni.fi.mathml.mathml2text.transformer.impl;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

/**
 *
 * @author Maros Kucbel Sep 16, 2012, 4:54:09 PM
 */
public enum Operation {
    /**
     * Addition operator.
     */
    ADD("plus", "+"),
    /**
     * Subtraction operator.
     */
    SUBTRACT("minus", "-", "&minus;"),
    /**
     * Multiplication operator.
     */
    MULTIPLY("times", "&times;"),
    /**
     * Division operator.
     */
    DIVIDE("divide", "/", "&divide;");
    
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
