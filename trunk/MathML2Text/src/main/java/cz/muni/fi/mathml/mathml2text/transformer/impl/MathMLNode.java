package cz.muni.fi.mathml.mathml2text.transformer.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cz.muni.fi.mathml.MathMLElement;

/**
 * DOM-like representation of MathML document tree.
 * Root element (the only node without a parent)
 * should always be of type {@link MathMLElement#MATH}.
 * There's no checking whether this structure is actually a valid MathML tree.
 * 
 * @author Maros Kucbel
 * @date 2012-10-30T20:00:48+0100
 */
public final class MathMLNode {
    /**
     * Name of this node.
     */
    private MathMLElement type;
    /**
     * List of all children of this node. Empty if there are none. In this case value must be set.
     */
    private List<MathMLNode> children = new ArrayList<MathMLNode>();
    /**
     * Text value of this node. {@code null} if there are some child nodes.
     */
    private String value;
    /**
     * Parent node.
     */
    private MathMLNode parent;
    /**
     * Set of attributes.
     */
    @Nonnull
    private Set<XmlAttribute> attributes = new HashSet<XmlAttribute>();

    /**
     * Returns parent node of this node.
     * @return Parent node.
     */
    public MathMLNode getParent() {
        return parent;
    }

    /**
     * Sets parent node of this node.
     * @param parent Parent node.
     */
    public void setParent(MathMLNode parent) {
        this.parent = parent;
    }
    
    /**
     * Return type of this node.
     * @return Type.
     */
    public MathMLElement getType() {
        return type;
    }

    /**
     * Sets type of this node.
     * @param type Type.
     */
    public void setType(MathMLElement type) {
        this.type = type;
    }

    /**
     * Returns a list of child nodes of this node.
     * Never returns <code>null</node>. If there are no children, returns 
     * empty list.
     * @return 
     */
    @Nonnull
    public List<MathMLNode> getChildren() {
        return children;
    }

    /**
     * Returns text value of this node. Can be <code>null</code>
     * @return Text value.
     */
    @Nullable
    public String getValue() {
        return value;
    }

    /**
     * Sets text value of this node.
     * @param value Text value.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns set of attributes of this node.
     * Never returns <code>null</node>. If there are no children, returns 
     * empty set.
     * @return 
     */
    @Nonnull
    public Set<XmlAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.type.getElementName());
        for (final XmlAttribute attr : this.attributes) {
            builder.append("\t");
            builder.append(attr.toString());
        }
        if (!this.attributes.isEmpty()) {
            builder.append("\n");
        }
        if (this.value != null) {
            builder.append("\t");
            builder.append("value=");
            builder.append(this.value);
        } else {
            for (final MathMLNode node : this.children) {
                builder.append("\n");
                builder.append("\t");
                builder.append(node.toString());
                builder.append("\n");
            }
        }
        return builder.toString();
    }
    
}