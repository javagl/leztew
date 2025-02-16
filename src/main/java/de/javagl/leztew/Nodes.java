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
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Utility methods related to {@link Node} objects
 */
class Nodes
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(Nodes.class.getName());

    /**
     * Spread out all type instantiations of the given node.
     * 
     * If any input- or output value socket of the given node contains a type
     * like <code>floatN</code> or <code>float{2|3}</code>, then the respective
     * instantiations of nodes will be returned.
     * 
     * Otherwise, a list containing only the given node is returned.
     * 
     * @param node The node
     * @return The instantiations
     */
    static List<Node> spreadTypes(Node node)
    {
        List<Node> result = new ArrayList<Node>();

        // When there are no templated types, just return the
        // given node
        Set<String> allTemplateValues = collectTypeTemplateValues(node);
        if (allTemplateValues.isEmpty())
        {
            result.add(node);
            return result;
        }

        // Otherwise, return one node for each template
        // value, with the socket type that contained a template
        // being replaced by the respective value
        for (String templateValue : allTemplateValues)
        {
            Node instance = new Node(node);
            List<Socket> inputValues = instance.getInputValueSockets();
            for (Socket s : inputValues)
            {
                String type = s.getType();
                List<String> templateValues = getTypeTemplateValues(type);
                if (templateValues != null)
                {
                    s.setType(templateValue);
                }
            }
            List<Socket> outputValues = instance.getOutputValueSockets();
            for (Socket s : outputValues)
            {
                String type = s.getType();
                List<String> templateValues = getTypeTemplateValues(type);
                if (templateValues != null)
                {
                    s.setType(templateValue);
                }
            }
            result.add(instance);
        }
        return result;
    }

    /**
     * Collect all types that are described by the "templates" in the given
     * node.
     * 
     * If any input- or output value socket of the given contains a type like
     * <code>floatN</code> or <code>float{2|3}</code>, then the respective
     * instantiation of these types will be returned.
     * 
     * Otherwise, an empty set is returned.
     * 
     * @param node The node
     * @return The type template values
     */
    private static Set<String> collectTypeTemplateValues(Node node)
    {
        Set<String> allTemplateValues = new LinkedHashSet<String>();

        List<Socket> inputValues = node.getInputValueSockets();
        for (Socket s : inputValues)
        {
            String type = s.getType();
            List<String> templateValues = getTypeTemplateValues(type);
            if (templateValues != null)
            {
                if (allTemplateValues.isEmpty())
                {
                    allTemplateValues.addAll(templateValues);
                }
                else
                {
                    Set<String> newTemplateValues =
                        new LinkedHashSet<String>(templateValues);
                    if (!allTemplateValues.equals(newTemplateValues))
                    {
                        logger.warning("Inconsistent templating: Found "
                            + allTemplateValues + " and " + newTemplateValues);
                    }
                }
            }
        }
        List<Socket> outputValues = node.getOutputValueSockets();
        for (Socket s : outputValues)
        {
            String type = s.getType();
            List<String> templateValues = getTypeTemplateValues(type);
            if (templateValues != null)
            {
                if (allTemplateValues.isEmpty())
                {
                    allTemplateValues.addAll(templateValues);
                }
                else
                {
                    Set<String> newTemplateValues =
                        new LinkedHashSet<String>(templateValues);
                    if (!allTemplateValues.equals(newTemplateValues))
                    {
                        logger.warning("Inconsistent templating: Found "
                            + allTemplateValues + " and " + newTemplateValues);
                    }
                }
            }
        }
        return allTemplateValues;
    }

    /**
     * Returns the template values for the given type.
     * 
     * If the type is of the form <code>typeN</code>, then this will return
     * <code>type, type2, type3, type3, type2x2, type3x3, type4x4</code>.
     * 
     * Otherwise, if the type is of the form <code>type{X|Y...}</code>, then
     * <code>typeX, typeY ... </code> will be returned.
     * 
     * Otherwise, <code>null</code> is returned.
     * 
     * @param type The type
     * @return The template values
     */
    private static List<String> getTypeTemplateValues(String type)
    {
        if (type == null)
        {
            return null;
        }
        if (type.endsWith("N"))
        {
            String base = type.substring(0, type.length() - 1);
            return Arrays.asList(base, base + "2", base + "3", base + "4",
                base + "2x2", base + "3x3", base + "4x4");
        }
        int i0 = type.lastIndexOf("{");
        int i1 = type.lastIndexOf("}");
        if (i0 == -1 | i1 == -1)
        {
            return null;
        }
        String base = type.substring(0, i0);
        String values = type.substring(i0 + 1, i1);
        String[] tokens = values.split("\\|");
        List<String> result = new ArrayList<String>();
        for (String token : tokens)
        {
            result.add(base + token);
        }
        return result;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Nodes()
    {
        // Private constructor to prevent instantiation
    }
}
