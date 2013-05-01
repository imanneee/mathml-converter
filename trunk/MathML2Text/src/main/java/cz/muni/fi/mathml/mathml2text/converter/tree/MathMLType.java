package cz.muni.fi.mathml.mathml2text.converter.tree;

/**
 * Enumeration of values used to sort MathML elements into groups based on
 * the way they are processed by converter.
 * 
 * @author Maros Kucbel
 * @date 2012-12-13T18:23:57+0100
 */
public enum MathMLType {
    /**
     * Presentation MathML.
     */
    PRESENTATION,
    /**
     * Content MathML.
     */
    CONTENT,
    /**
     * Trigonometric functions in content MathML.
     */
    CONTENT_TRIGONOMETRY,
    /**
     * Content MathML elements that form a "group" (max, min, set, etc).
     */
    CONTENT_GROUP,
    /**
     * Content MathML elements that are used in infix form (plus, times, equals, etc).
     */
    CONTENT_MIDDLE,
    /**
     * Content MathML elements that are used in prefix form (not, abs, floor, etc).
     */
    CONTENT_BEFORE,
    /**
     * Content MathML elements that are used before formulae and in the middle as well (quotient, remainder).
     */
    CONTENT_BEFORE_MIDDLE,
    /**
     * Elements that don't strictly belong to presentation or content elements 
     * (semantics, annotation, ...).
     */
    OTHER;
}
