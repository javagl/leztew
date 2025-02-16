/*
 * www.javagl.de - leztew
 *
 * Copyright 2025 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.leztew;

import java.util.ArrayList;
import java.util.List;

/**
 * A plain old Java object ("bean") representing a category of nodes
 * 
 * This is mainly used for serialization to JSON.
 */
@SuppressWarnings("javadoc")
public class Category
{
    private String name;
    private List<Node> nodes = new ArrayList<Node>();
    private List<Category> children = new ArrayList<Category>();

    public Category()
    {
        // Default constructor
    }

    public Category(String name)
    {
        this.name = name;
    }

    public Category(String name, List<Node> nodes, List<Category> children)
    {
        this.name = name;
        this.nodes = nodes;
        this.children = children;
    }

    public Category(Category that)
    {
        this.name = that.name;
        for (Node e : that.getNodes())
        {
            this.nodes.add(new Node(e));
        }
        for (Category e : that.getChildren())
        {
            this.children.add(new Category(e));
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Node> getNodes()
    {
        return nodes;
    }

    public void setNodes(List<Node> nodes)
    {
        this.nodes = nodes;
    }

    public void addNode(Node node)
    {
        this.nodes.add(node);
    }

    public List<Category> getChildren()
    {
        return children;
    }

    public void setChildren(List<Category> children)
    {
        this.children = children;
    }

    public void addChild(Category child)
    {
        this.children.add(child);
    }

    @Override
    public String toString()
    {
        return "Category [name=" + name + ", nodes=" + nodes + ", children="
            + children.size() + "]";
    }

}
