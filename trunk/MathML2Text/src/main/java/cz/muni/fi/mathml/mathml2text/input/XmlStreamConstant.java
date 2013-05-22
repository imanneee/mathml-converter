package cz.muni.fi.mathml.mathml2text.input;

import javax.xml.stream.XMLStreamConstants;

/**
 * This class is a copy of {@link XMLStreamConstants} interface. 
 * Makes working with XML stream events more understandable and readable.
 * 
 * @author Maros Kucbel Sep 15, 2012, 10:21:50 PM
 * @date 2012-09-15T10:21:50+0100
 */
public enum XmlStreamConstant {
    /**
     * Indicates an event is a start element
     *
     * @see javax.xml.stream.events.StartElement
     */
    START_ELEMENT(1),
    /**
     * Indicates an event is an end element
     *
     * @see javax.xml.stream.events.EndElement
     */
    END_ELEMENT(2),
    /**
     * Indicates an event is a processing instruction
     *
     * @see javax.xml.stream.events.ProcessingInstruction
     */
    PROCESSING_INSTRUCTION(3),
    /**
     * Indicates an event is characters
     *
     * @see javax.xml.stream.events.Characters
     */
    CHARACTERS(4),
    /**
     * Indicates an event is a comment
     *
     * @see javax.xml.stream.events.Comment
     */
    COMMENT(5),
    /**
     * The characters are white space (see [XML], 2.10 "White Space Handling").
     * Events are only reported as SPACE if they are ignorable white space.
     * Otherwise they are reported as CHARACTERS.
     *
     * @see javax.xml.stream.events.Characters
     */
    SPACE(6),
    /**
     * Indicates an event is a start document
     *
     * @see javax.xml.stream.events.StartDocument
     */
    START_DOCUMENT(7),
    /**
     * Indicates an event is an end document
     *
     * @see javax.xml.stream.events.EndDocument
     */
    END_DOCUMENT(8),
    /**
     * Indicates an event is an entity reference
     *
     * @see javax.xml.stream.events.EntityReference
     */
    ENTITY_REFERENCE(9),
    /**
     * Indicates an event is an attribute
     *
     * @see javax.xml.stream.events.Attribute
     */
    ATTRIBUTE(10),
    /**
     * Indicates an event is a DTD
     *
     * @see javax.xml.stream.events.DTD
     */
    DTD(11),
    /**
     * Indicates an event is a CDATA section
     *
     * @see javax.xml.stream.events.Characters
     */
    CDATA(12),
    /**
     * Indicates the event is a namespace declaration
     *
     * @see javax.xml.stream.events.Namespace
     */
    NAMESPACE(13),
    /**
     * Indicates a Notation
     *
     * @see javax.xml.stream.events.NotationDeclaration
     */
    NOTATION_DECLARATION(14),
    /**
     * Indicates a Entity Declaration
     *
     * @see javax.xml.stream.events.NotationDeclaration
     */
    ENTITY_DECLARATION(15);
    
    /**
     * Event code for constant.
     */
    private final int eventCode;
    
    /**
     * Constructor.
     * @param eventCode Event code for constant. 
     */
    private XmlStreamConstant(final int eventCode) {
        this.eventCode = eventCode;
    }
    
    /**
     * Returns event code of this constant.
     * @return Event code.
     */
    public int getEventCode() {
        return this.eventCode;
    }
    
    /**
     * Returns a constant representing given event code or {@code null},
     * if theres is no constant for event code.
     * 
     * @param eventCode Event code.
     * @return Constant equivalent to given event code.
     */
    public static XmlStreamConstant forEventCode(final int eventCode) {
        for (final XmlStreamConstant constant : XmlStreamConstant.values()) {
            if (constant.getEventCode() == eventCode) {
                return constant;
            }
        }
        return null;
    }
}
