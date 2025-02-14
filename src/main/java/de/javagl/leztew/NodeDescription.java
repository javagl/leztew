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
 * A plain old Java object ("bean") representing the description of a "node"
 * that was extracted from the specification.
 * 
 * This is mainly used for serialization to JSON.
 */
@SuppressWarnings("javadoc")
public class NodeDescription
{
    private String title;
    private String name;
    private String description;

    private List<ConfigurationElementDescription> configuration =
        new ArrayList<ConfigurationElementDescription>();

    private List<SocketDescription> inputFlowSockets =
        new ArrayList<SocketDescription>();
    private List<SocketDescription> inputValueSockets =
        new ArrayList<SocketDescription>();
    private List<SocketDescription> outputFlowSockets =
        new ArrayList<SocketDescription>();
    private List<SocketDescription> outputValueSockets =
        new ArrayList<SocketDescription>();

    public NodeDescription() 
    {
        // Default constructor
    }

    public NodeDescription(String title, String name, String description,
        List<ConfigurationElementDescription> configuration,
        List<SocketDescription> inputFlowSockets,
        List<SocketDescription> inputValueSockets,
        List<SocketDescription> outputFlowSockets,
        List<SocketDescription> outputValueSockets)
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

    public NodeDescription(NodeDescription that)
    {
        this.title = that.title;
        this.name = that.name;
        this.description = that.description;
        for (ConfigurationElementDescription e : that.getConfiguration())
        {
            this.configuration.add(new ConfigurationElementDescription(e));
        }
        for (SocketDescription e : that.getInputFlowSockets())
        {
            this.inputFlowSockets.add(new SocketDescription(e));
        }
        for (SocketDescription e : that.getInputValueSockets())
        {
            this.inputValueSockets.add(new SocketDescription(e));
        }
        for (SocketDescription e : that.getOutputFlowSockets())
        {
            this.outputFlowSockets.add(new SocketDescription(e));
        }
        for (SocketDescription e : that.getOutputValueSockets())
        {
            this.outputValueSockets.add(new SocketDescription(e));
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

    public List<ConfigurationElementDescription> getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration(
        List<ConfigurationElementDescription> configuration)
    {
        this.configuration = configuration;
    }

    public List<SocketDescription> getInputFlowSockets()
    {
        return inputFlowSockets;
    }

    public void setInputFlowSockets(List<SocketDescription> inputFlowSockets)
    {
        this.inputFlowSockets = inputFlowSockets;
    }

    public List<SocketDescription> getInputValueSockets()
    {
        return inputValueSockets;
    }

    public void setInputValueSockets(List<SocketDescription> inputValueSockets)
    {
        this.inputValueSockets = inputValueSockets;
    }

    public List<SocketDescription> getOutputFlowSockets()
    {
        return outputFlowSockets;
    }

    public void setOutputFlowSockets(List<SocketDescription> outputFlowSockets)
    {
        this.outputFlowSockets = outputFlowSockets;
    }

    public List<SocketDescription> getOutputValueSockets()
    {
        return outputValueSockets;
    }

    public void
        setOutputValueSockets(List<SocketDescription> outputValueSockets)
    {
        this.outputValueSockets = outputValueSockets;
    }

    @Override
    public String toString()
    {
        return "NodeDescription [title=" + title + ", name=" + name
            + ", description=" + description + ", configuration="
            + configuration + ", inputFlowSockets=" + inputFlowSockets
            + ", inputValueSockets=" + inputValueSockets
            + ", outputFlowSockets=" + outputFlowSockets
            + ", outputValueSockets=" + outputValueSockets + "]";
    }

}