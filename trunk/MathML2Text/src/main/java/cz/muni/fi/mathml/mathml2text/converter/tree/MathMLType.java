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
     * Elements that don't strictly belong to presentation or content elements 
     * (semantics, annotation, ...).
     */
    OTHER;
}
