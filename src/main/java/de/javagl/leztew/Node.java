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
 * A plain old Java object ("bean") representing "node" that was extracted from
 * the specification.
 * 
 * This is mainly used for serialization to JSON.
 */
@SuppressWarnings("javadoc")
public class Node
{
    private String title;
    private String name;
    private String description;

    private List<ConfigurationElement> configuration =
        new ArrayList<ConfigurationElement>();

    private List<Socket> inputFlowSockets = new ArrayList<Socket>();
    private List<Socket> inputValueSockets = new ArrayList<Socket>();
    private List<Socket> outputFlowSockets = new ArrayList<Socket>();
    private List<Socket> outputValueSockets = new ArrayList<Socket>();

    public Node()
    {
        // Default constructor
    }

    public Node(String title, String name, String description,
        List<ConfigurationElement> configuration, List<Socket> inputFlowSockets,
        List<Socket> inputValueSockets, List<Socket> outputFlowSockets,
        List<Socket> outputValueSockets)
    {
        this.title = title;
        this.name = name;
        this.description = description;
        this.configuration = configuration;
        this.inputFlowSockets = inputFlowSockets;
        this.inputValueSockets = inputValueSockets;
        this.outputFlowSockets = outputFlowSockets;
        this.outputValueSockets = outputValueSockets;
    }

    public Node(Node that)
    {
        this.title = that.title;
        this.name = that.name;
        this.description = that.description;
        for (ConfigurationElement e : that.getConfiguration())
        {
            this.configuration.add(new ConfigurationElement(e));
        }
        for (Socket e : that.getInputFlowSockets())
        {
            this.inputFlowSockets.add(new Socket(e));
        }
        for (Socket e : that.getInputValueSockets())
        {
            this.inputValueSockets.add(new Socket(e));
        }
        for (Socket e : that.getOutputFlowSockets())
        {
            this.outputFlowSockets.add(new Socket(e));
        }
        for (Socket e : that.getOutputValueSockets())
        {
            this.outputValueSockets.add(new Socket(e));
        }
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<ConfigurationElement> getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration(List<ConfigurationElement> configuration)
    {
        this.configuration = configuration;
    }

    public List<Socket> getInputFlowSockets()
    {
        return inputFlowSockets;
    }

    public void setInputFlowSockets(List<Socket> inputFlowSockets)
    {
        this.inputFlowSockets = inputFlowSockets;
    }

    public List<Socket> getInputValueSockets()
    {
        return inputValueSockets;
    }

    public void setInputValueSockets(List<Socket> inputValueSockets)
    {
        this.inputValueSockets = inputValueSockets;
    }

    public List<Socket> getOutputFlowSockets()
    {
        return outputFlowSockets;
    }

    public void setOutputFlowSockets(List<Socket> outputFlowSockets)
    {
        this.outputFlowSockets = outputFlowSockets;
    }

    public List<Socket> getOutputValueSockets()
    {
        return outputValueSockets;
    }

    public void setOutputValueSockets(List<Socket> outputValueSockets)
    {
        this.outputValueSockets = outputValueSockets;
    }

    @Override
    public String toString()
    {
        return "Node [title=" + title + ", name=" + name + ", description="
            + description + ", configuration=" + configuration
            + ", inputFlowSockets=" + inputFlowSockets + ", inputValueSockets="
            + inputValueSockets + ", outputFlowSockets=" + outputFlowSockets
            + ", outputValueSockets=" + outputValueSockets + "]";
    }

}