package cz.muni.fi.mathml.mathml2text.converter.operation;

/**
 * Type of an operation based on the word order this operation uses.
 * 
 * @author Maros Kucbel
 * @date 2012-09-16T16:58:40+0100
 */
public enum OperationType {
    /**
     * Operation is used between every two consecutive arguments.
     */
    INFIX,
    /**
     * Operation is used only at the beginning of expression.
     */
    PREFIX,
    /**
     * Operation can be used both in {@link #PREFIX} or {@link #INFIX} form.
     */
    INFIX_OR_PREFIX,
    /**
     * Operation is used only at the beginning of expression, but there are mulpiple 
     * arguments that should be divided by some delimiter.
     */
    PREFIX_MULTI,
    /**
     * Operation is used on every argument separately.
     */
    EVERY_ARGUMENT,
    /**
     * Operation is defined on an interval (sum, integral, ...).
     */
    WITH_INTERVAL,
    /**
     * Operation can not be assigned to any of above defined groups.
     */
    SPECIAL;
}
