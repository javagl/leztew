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
import java.util.logging.Logger;

/**
 * Utility methods related to {@link Category} objects
 */
public class Categories
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(Categories.class.getName());

    public static Category spreadTypes(Category category)
    {
        Category resultCategory = new Category();
        resultCategory.setName(category.getName());

        List<NodeDescription> resultNodeDescriptions =
            new ArrayList<NodeDescription>();
        List<NodeDescription> nodeDescriptions = category.getNodeDescriptions();
        for (NodeDescription nodeDescription : nodeDescriptions)
        {
            List<NodeDescription> instances =
                NodeDescriptions.spreadTypes(nodeDescription);
            if (instances.size() > 1)
            {
                logger.info("Created " + instances.size()
                    + " instances for all types of "
                    + nodeDescription.getName());
            }
            resultNodeDescriptions.addAll(instances);
        }
        resultCategory.setNodeDescriptions(resultNodeDescriptions);

        for (Category child : category.getChildren())
        {
            Category resultChild = spreadTypes(child);
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
