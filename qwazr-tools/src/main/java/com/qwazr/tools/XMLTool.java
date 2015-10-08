/**
 * Copyright 2014-2015 Emmanuel Keller / QWAZR
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package com.qwazr.tools;

import com.jamesmurty.utils.XMLBuilder2;
import com.qwazr.utils.IOUtils;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import java.io.*;

public class XMLTool extends AbstractTool {

    private SAXParserFactory saxParserFactory;
    private DocumentBuilderFactory documentBuilderFactory;

    @Override
    public void load(File parentDir) {
	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	saxParserFactory.setNamespaceAware(true);
	documentBuilderFactory = DocumentBuilderFactory.newInstance();
	documentBuilderFactory.setNamespaceAware(true);
    }

    @Override
    public void unload() {
    }

    /**
     * @param root
     *            the name of the root element
     * @return an new XML builder instance
     *         <p>
     *         {@link XMLBuilder2}
     */
    public XMLBuilder2 create(String root) {
	return XMLBuilder2.create(root);
    }

    /**
     * Save the XML to the file described by the given path
     *
     * @param builder
     * @param path
     * @throws IOException
     */
    public void saveTo(XMLBuilder2 builder, String path) throws IOException {
	FileWriter writer = new FileWriter(path);
	try {
	    builder.toWriter(true, writer, null);
	} finally {
	    writer.close();
	}
    }

    /**
     * Parse and XML stream
     *
     * @param jsObject
     * @param is
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void parseStream(ScriptObjectMirror jsObject, InputStream is) throws ParserConfigurationException,
	    SAXException, IOException {
	DefaultHandler defaultHandler = (DefaultHandler) ScriptUtils.convert(jsObject, DefaultHandler.class);
	SAXParser saxParser = saxParserFactory.newSAXParser();
	saxParser.parse(is, defaultHandler);
    }

    /**
     * Parse an XML file
     *
     * @param jsObject
     * @param path
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void parseFile(ScriptObjectMirror jsObject, String path) throws IOException, SAXException,
	    ParserConfigurationException {
	InputStream in = new BufferedInputStream(new FileInputStream(path));
	try {
	    parseStream(jsObject, in);
	} finally {
	    IOUtils.closeQuietly(in);
	}
    }

    /**
     * Parse an XML string and build a DOM object
     * 
     * @param xmlString
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public Document domParseString(String xmlString) throws IOException, SAXException, ParserConfigurationException {
	DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
	InputSource input = new InputSource();
	input.setCharacterStream(new StringReader(xmlString));
	return docBuilder.parse(input);
    }

    /**
     * Parse an XML file and build a DOM object
     * 
     * @param file
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public Document domParseFile(String file) throws ParserConfigurationException, IOException, SAXException {
	DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
	return docBuilder.parse(file);
    }

}