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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * The main class of leztew
 */
public class LeztewMain
{

    /**
     * Entry point of the application
     * 
     * @param args Not used
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        LoggerUtil.initLogging();

        File inputFile = new File("./data/Specification.adoc");
        File outputFile = new File("./data/nodes.json");

        Category nodes = read(inputFile);
        write(nodes, outputFile);
        
        File outputFileSpread = new File("./data/nodes-spread.json");
        Category nodesSpread = Categories.spreadTypes(nodes);
        write(nodesSpread, outputFileSpread);
        
    }

    /**
     * Read the {@link Node} objects from the given file
     * 
     * @param file The file
     * @return The {@link Node} objects
     * @throws IOException If an IO error occurs
     */
    private static Category read(File file) throws IOException
    {
        String content = new String(Files.readAllBytes(file.toPath()));

        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.javaConverterRegistry().register(LeztewConverter.class);
        Options options = Options.builder().backend("ast-json").build();
        LeztewConverter leztewConverter =
            asciidoctor.convert(content, options, LeztewConverter.class);
        Category nodes = leztewConverter.getNodes();
        return nodes;
    }

    /**
     * Write the given {@link Node} objects to the given file
     * 
     * @param nodes The {@link Node} objects
     * @param file The file
     * @throws IOException If an IO error occurs
     */
    private static void write(Category nodes, File file)
        throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(Include.NON_EMPTY);
        objectMapper.writeValue(file, nodes);
    }
}
