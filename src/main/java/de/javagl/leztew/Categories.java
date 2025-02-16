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
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

/**
 * Utility methods related to {@link Category} objects
 */
class Categories
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(Categories.class.getName());

    /**
     * Creates a new {@link Category} that is a copy of the given one, with
     * instantiations for different node types.
     * 
     * Each node that defines a "template type" will be replaced by a list of
     * node instances, one for each type.
     * 
     * @param category The input category
     * @param createSubCategories Whether the instances should be put into an
     *        own category
     * @return The output category
     */
    public static Category spreadTypes(Category category,
        boolean createSubCategories)
    {
        Category resultCategory = new Category();
        resultCategory.setName(category.getName());

        List<Node> resultNodes = new ArrayList<Node>();
        List<Node> nodes = category.getNodes();
        for (Node node : nodes)
        {
            Map<String, Node> instances = Nodes.spreadTypes(node);
            if (instances.size() == 1)
            {
                resultNodes.add(node);
            }
            else
            {

                logger.info("Created " + instances.size()
                    + " instances for all types of " + node.getName());

                if (createSubCategories)
                {
                    Category subCategory = new Category(node.getName());
                    category.addChild(subCategory);
                    for (Entry<String, Node> entry : instances.entrySet())
                    {
                        String templateName = entry.getKey();
                        Node instance = entry.getValue();
                        Node newInstance = new Node(instance);
                        newInstance.setTitle(
                            node.getTitle() + " (" + templateName + ")");
                        subCategory.addNode(newInstance);
                    }
                }
                else
                {
                    resultNodes.addAll(instances.values());
                }
            }
        }
        resultCategory.setNodes(resultNodes);

        for (Category child : category.getChildren())
        {
            Category resultChild = spreadTypes(child, createSubCategories);
            resultCategory.addChild(resultChild);
        }
        return resultCategory;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Categories()
    {
        // Private constructor to prevent instantiation
    }

}
