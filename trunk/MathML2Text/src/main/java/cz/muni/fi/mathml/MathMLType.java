package cz.muni.fi.mathml;

/**
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
     * Elements that don't strictly belong to presentation or content elements 
     * (semantics, annotation, ...).
     */
    OTHER;
}
