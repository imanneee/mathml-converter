package cz.muni.fi.mathml.mathml2text.transformer.impl;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.mathml.MathMLElement;

/**
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

    public MathMLNode getParent() {
        return parent;
    }

    public void setParent(MathMLNode parent) {
        this.parent = parent;
    }
    
    public MathMLElement getType() {
        return type;
    }

    public void setType(MathMLElement type) {
        this.type = type;
    }

    public List<MathMLNode> getChildren() {
        return children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.type.getElementName());
        if (this.value != null) {
            builder.append("\t");
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