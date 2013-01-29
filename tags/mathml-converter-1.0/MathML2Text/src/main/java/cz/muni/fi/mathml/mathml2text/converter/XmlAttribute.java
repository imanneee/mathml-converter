package cz.muni.fi.mathml.mathml2text.converter;

/**
 * Immutable attribute of a node.
 * 
 * @author Maros Kucbel
 * @date 2012-11-02T21:06:27+0100
 */
public final class XmlAttribute {
    /**
     * Attribute identifier.
     */
    private final String key;
    /**
     * Attribute value.
     */
    private final String value;
    
    /**
     * Constructor.
     * @param key Identifier.
     * @param value Value.
     */
    public XmlAttribute(final String key,
                        final String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns identifier of this attribute.
     * @return Identifier.
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns value of this attribute.
     * @return Value.
     */
    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + (this.key != null ? this.key.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof XmlAttribute)) {
            return false;
        }
        final XmlAttribute other = (XmlAttribute) obj;
        if ((this.key == null) || (other.key == null)) {
            return false;
        }
        return this.key.equals(other.key);
    }

    @Override
    public String toString() {
        return "attr: " + this.key + "=" + this.value;
    }

}