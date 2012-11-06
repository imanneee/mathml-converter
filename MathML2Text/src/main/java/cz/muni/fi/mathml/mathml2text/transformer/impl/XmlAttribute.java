package cz.muni.fi.mathml.mathml2text.transformer.impl;

/**
 * Single attribute of a node.
 * 
 * @author Maros Kucbel
 * @date 2012-11-02T21:06:27+0100
 */
public final class XmlAttribute {
    
    private final String key;
    
    private final String value;
    
    public XmlAttribute(final String key,
                        final String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

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