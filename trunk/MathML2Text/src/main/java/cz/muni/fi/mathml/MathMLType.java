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
     * Elements that don't strictly belong to presentation or content elements 
     * (semantics, annotation, ...).
     */
    OTHER;
}
