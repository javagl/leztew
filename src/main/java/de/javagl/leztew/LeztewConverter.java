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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asciidoctor.ast.Cell;
import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.Row;
import org.asciidoctor.ast.Section;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.ast.Table;
import org.asciidoctor.converter.AbstractConverter;

/**
 * Implementation of an Asciidoc Converter that receives the main content nodes,
 * and generates an instance of itself, containing the {@link NodeDescription}
 * objects that have been extracted from the content.
 */
public class LeztewConverter extends AbstractConverter<Object>
{
    /**
     * The logger used in this class
     */
    private static final Logger logger =
        Logger.getLogger(LeztewConverter.class.getName());

    /**
     * The (0-based) section number of the "Functional Specification"
     */
    private static final int FUNCTIONAL_SPECIFICATION_SECTION_NUMBER = 3;

    /**
     * The (0-based) section number of the "Nodes" section within the
     * "Functional Specification" section.
     */
    private static final int NODES_SECTION_NUMBER = 0;

    /**
     * The default log level
     */
    private final Level level = Level.FINE;

    /**
     * The mapping from section names to the mappings of subsection names to the
     * lists of {@link NodeDescription} objects that have been found in the
     * respective section.
     */
    private final Category nodeDescriptions;

    /**
     * Default constructor
     * 
     * @param backend The backend
     * @param opts The options
     */
    public LeztewConverter(String backend, Map<String, Object> opts)
    {
        super(backend, opts);
        this.nodeDescriptions = new Category("Nodes");
    }

    /**
     * Returns a reference to the root {@link Category} of nodes
     * 
     * @return The {@link Category} object
     */
    public Category getNodeDescriptions()
    {
        return nodeDescriptions;
    }

    @Override
    public LeztewConverter convert(ContentNode node, String transform,
        Map<Object, Object> opts)
    {
        logger.log(level, "Convert " + node);
        if (node instanceof Document)
        {
            Document document = (Document) node;
            List<Section> sections = findSections(document);
            Section functionalSpecificationSection =
                sections.get(FUNCTIONAL_SPECIFICATION_SECTION_NUMBER);
            processFunctionalSpecificationSection(
                functionalSpecificationSection);
        }
        return this;
    }

    /**
     * Process "Section 4, Functional Specification"
     * 
     * @param functionalSpecificationSection The section
     */
    private void processFunctionalSpecificationSection(
        Section functionalSpecificationSection)
    {
        List<Section> sections = findSections(functionalSpecificationSection);
        Section nodesSection = sections.get(NODES_SECTION_NUMBER);
        processNodesSection(nodesSection);
    }

    /**
     * Process "Section 4.1, Nodes"
     * 
     * @param nodesSection The section
     */
    private void processNodesSection(Section nodesSection)
    {
        List<Section> sections = findSections(nodesSection);
        for (Section section : sections)
        {
            processNodesSubSection(section);
        }
    }

    /**
     * Process a subsection of "Section 4.1, Nodes", like "Section 4.1.1. Math
     * nodes".
     * 
     * @param nodesSubSection The section
     */
    private void processNodesSubSection(Section nodesSubSection)
    {
        logger.log(level,
            "Processing nodes sub-section " + nodesSubSection.getTitle()
                + " at level " + nodesSubSection.getLevel());

        String title = nodesSubSection.getTitle();

        Category childCategory = new Category(title);
        nodeDescriptions.addChild(childCategory);

        List<Section> sections = findSections(nodesSubSection);
        for (Section section : sections)
        {
            processNodesGroupSection(section, childCategory);
        }
    }

    /**
     * Process a subsection of a nodes subsection, like "4.1.1.2. Arithmetic
     * Nodes"
     * 
     * @param nodesGroupSection The section
     * @param category This will store the {@link NodeDescription} objects that
     *        have been created
     */
    private void processNodesGroupSection(Section nodesGroupSection,
        Category category)
    {
        logger.log(level,
            "Processing nodes group section " + nodesGroupSection.getTitle()
                + " at level " + nodesGroupSection.getLevel());

        String title = nodesGroupSection.getTitle();
        Category childCategory = new Category(title);
        category.addChild(childCategory);

        List<Section> sections = findSections(nodesGroupSection);
        for (Section section : sections)
        {
            processNodesDefinitionsSection(section, childCategory);
        }
    }

    /**
     * Process a section that defines a single node, like "4.1.1.2.10.
     * Subtraction".
     * 
     * This is expected to contain tables that can be processed with
     * {@link #processOperationTable(Table, String)}.
     * 
     * @param nodesDefinitionsSection The section
     * @param category This will store the {@link NodeDescription} objects that
     *        have been created
     */
    private void processNodesDefinitionsSection(Section nodesDefinitionsSection,
        Category category)
    {
        logger.log(Level.INFO,
            "Processing nodes definitions section "
                + nodesDefinitionsSection.getTitle() + " at level "
                + nodesDefinitionsSection.getLevel());

        String title = nodesDefinitionsSection.getTitle();
        List<StructuralNode> blocks = nodesDefinitionsSection.getBlocks();
        List<Table> tables = findAll(blocks, Table.class);
        for (Table table : tables)
        {
            NodeDescription nodeDescription =
                processOperationTable(table, title);
            if (nodeDescription != null)
            {
                category.addNodeDescription(nodeDescription);
            }
        }
    }


    /**
     * Process a single table that was found for a node definition, like that in
     * "4.1.1.2.10. Subtraction".
     * 
     * @param table The table
     * @param title The title for the node description
     * @return The {@link NodeDescription}, or <code>null</code> if the table
     *         could not be parsed.
     */
    private NodeDescription processOperationTable(Table table, String title)
    {
        logger.log(level, "Table " + table);
        List<Row> body = table.getBody();

        if (!body.get(0).getCells().get(0).getSource().equals("Operation"))
        {
            logger.severe("Expected an 'Operation' table. Ignoring.");
            return null;
        }

        NodeDescription nodeDescription = new NodeDescription();
        int i = 0;
        while (i < body.size())
        {
            Row row = body.get(i);
            List<Cell> cells = row.getCells();

            // The first row should always have "Operation", "name",
            // "description".
            //
            // Otherwise, when a row starts with "Configuration",
            // "Input flow sockets", "Input value sockets",
            // "Output flow sockets", or "Output value sockets", then it may
            // be followed by several other rows that all define them,
            // depending on the row span of the first column
            if (cells.get(0).getSource().equals("Operation"))
            {
                String name = stripBackticks(cells.get(1).getSource());
                nodeDescription.setName(name);
                nodeDescription.setDescription(cells.get(2).getSource());
                i++;
            }
            else if (cells.get(0).getSource().equals("Configuration"))
            {
                int n = Math.max(cells.get(0).getRowspan(), 1);
                processConfigurationDescriptions(body, i,
                    nodeDescription.getConfiguration());
                i += n;
            }
            else if (cells.get(0).getSource().equals("Input flow sockets"))
            {
                int n = Math.max(cells.get(0).getRowspan(), 1);
                processSocketDescriptions(body, i,
                    nodeDescription.getInputFlowSockets());
                i += n;
            }
            else if (cells.get(0).getSource().equals("Input value sockets"))
            {
                int n = Math.max(cells.get(0).getRowspan(), 1);
                processSocketDescriptions(body, i,
                    nodeDescription.getInputValueSockets());
                i += n;
            }
            else if (cells.get(0).getSource().equals("Output flow sockets"))
            {
                int n = Math.max(cells.get(0).getRowspan(), 1);
                processSocketDescriptions(body, i,
                    nodeDescription.getOutputFlowSockets());
                i += n;
            }
            else if (cells.get(0).getSource().equals("Output value sockets"))
            {
                int n = Math.max(cells.get(0).getRowspan(), 1);
                processSocketDescriptions(body, i,
                    nodeDescription.getOutputValueSockets());
                i += n;
            }
            else
            {
                logger.warning("Unexpected row in table: "
                    + cells.get(0).getSource() + " - ignoring");
                i++;
            }

        }

        logger.log(level, "Final nodeDescription " + nodeDescription);
        nodeDescription.setTitle(title);
        return nodeDescription;

    }

    /**
     * Process the configuration descriptions from the given table body
     * 
     * @param body The table body
     * @param startRow The row where to start reading the configuration
     *        descriptions
     * @param configuration The list that will store the resulting configuration
     *        descriptions.
     */
    private void processConfigurationDescriptions(List<Row> body, int startRow,
        List<ConfigurationElementDescription> configuration)
    {
        int r = startRow;
        Row row = body.get(r);
        List<Cell> cells = row.getCells();
        int n = Math.max(cells.get(0).getRowspan(), 1);
        for (int j = 0; j < n; j++)
        {
            int i0 = 0;
            int i1 = 1;
            // In the first row, the name/description cells are indented
            // by one, because the first column contains "Configuration"
            if (j == 0)
            {
                i0++;
                i1++;
            }
            ConfigurationElementDescription s =
                new ConfigurationElementDescription();
            String source = cells.get(i0).getSource();
            String declaration = stripBackticks(source);
            String type = parseType(declaration);
            String name = parseName(declaration);
            if (type != null && name != null)
            {
                s.setType(type);
                s.setName(name);
            }
            else
            {
                logger.warning("Could not extract type and name from \""
                    + declaration + "\"");
            }
            s.setDescription(cells.get(i1).getSource());
            configuration.add(s);
            if (j < n - 1)
            {
                r++;
                row = body.get(r);
                cells = row.getCells();
            }
        }
    }

    /**
     * Process the socket descriptions from the given table body
     * 
     * @param body The table body
     * @param startRow The row where to start reading the socket descriptions
     * @param socketDescriptions The list that will store the resulting socket
     *        descriptions.
     */
    private void processSocketDescriptions(List<Row> body, int startRow,
        List<SocketDescription> socketDescriptions)
    {
        int r = startRow;
        Row row = body.get(r);
        List<Cell> cells = row.getCells();
        int n = Math.max(cells.get(0).getRowspan(), 1);
        for (int j = 0; j < n; j++)
        {
            int i0 = 0;
            int i1 = 1;
            // In the first row, the name/description cells are indented
            // by one, because the first column contains the category
            // name (like "Input value sockets")
            if (j == 0)
            {
                i0++;
                i1++;
            }
            SocketDescription s = new SocketDescription();
            String source = cells.get(i0).getSource();
            String declaration = stripBackticks(source);
            String type = parseType(declaration);
            String name = parseName(declaration);
            if (type != null && name != null)
            {
                s.setType(type);
                s.setName(name);
            }
            else
            {
                // Flow sockets don't have a type
                s.setName(declaration);
            }
            s.setDescription(cells.get(i1).getSource());
            socketDescriptions.add(s);
            if (j < n - 1)
            {
                r++;
                row = body.get(r);
                cells = row.getCells();
            }
        }
    }

    /**
     * Traverse the given node, recursively, printing unspecified debug
     * information
     * 
     * @param node The node
     */
    void traverse(StructuralNode node)
    {
        logger.log(level, "Traverse " + node);
        if (node instanceof Table)
        {
            Table table = (Table) node;
            logger.log(level, "Table " + table);
        }
        List<StructuralNode> blocks = node.getBlocks();
        for (StructuralNode block : blocks)
        {
            traverse(block);
        }
    }

    @Override
    public void write(Object output, OutputStream out) throws IOException
    {
        // Nothing to do here
    }

    /**
     * Obtain all blocks from the given structural node, and return all
     * <code>Section</code> objects that it contains.
     * 
     * @param node The node
     * @return The sections
     */
    private static List<Section> findSections(StructuralNode node)
    {
        List<StructuralNode> blocks = node.getBlocks();
        List<Section> sections = findAll(blocks, Section.class);
        return sections;
    }

    /**
     * Return a list of all objects in the given iterable that have the given
     * type.
     * 
     * @param elements The elements
     * @param type The type
     * @param <T> The type
     * @return The objects
     */
    private static <T> List<T> findAll(Iterable<?> elements, Class<T> type)
    {
        List<T> list = new ArrayList<T>();
        for (Object element : elements)
        {
            if (type.isInstance(element))
            {
                @SuppressWarnings("unchecked")
                T value = (T) element;
                list.add(value);
            }
        }
        return list;
    }

    /**
     * Try to parse what would likely be a type declaration in the given string:
     * If it consists of two whitespace-separated tokens, then the first token
     * is returned. Otherwise, <code>null</code> is returned.
     * 
     * @param s The string
     * @return The type
     */
    private static String parseType(String s)
    {
        StringTokenizer st = new StringTokenizer(s);
        if (st.countTokens() == 2)
        {
            return st.nextToken();
        }
        return null;
    }

    /**
     * Try to parse what would likely be a name in the given string: If it
     * consists of two whitespace-separated tokens, then the first token is
     * returned. Otherwise, <code>null</code> is returned.
     * 
     * @param s The string
     * @return The name
     */
    private static String parseName(String s)
    {
        StringTokenizer st = new StringTokenizer(s);
        if (st.countTokens() == 2)
        {
            st.nextToken();
            return st.nextToken();
        }
        return null;
    }

    /**
     * If the given string starts and ends with backticks (omitting leading and
     * trailing whitespace), then the part in the backticks will be returned
     * (omitting leading and trailing whitespace)
     * 
     * @param input The input string
     * @return The result
     */
    private static String stripBackticks(String input)
    {
        String s = input.trim();
        if (s.startsWith("`") && s.endsWith("`"))
        {
            s = s.substring(1, s.length() - 1).trim();
        }
        return s;
    }

}