/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.core.markup.parse;

import java.io.IOException;
import org.apache.xerces.parsers.DOMParser;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.xml.sax.*;
import org.w3c.dom.*;

public abstract class XercesDOMParserWrapperImpl implements ErrorHandler
{
    /** Namespaces feature id (http://xml.org/sax/features/namespaces). */
    protected static final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";

    /** Validation feature id (http://xml.org/sax/features/validation). */
    protected static final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";

    /** Schema validation feature id (http://apache.org/xml/features/validation/schema). */
    protected static final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";

    /** Schema full checking feature id (http://apache.org/xml/features/validation/schema-full-checking). */
    protected static final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";

    /** Dynamic validation feature id (http://apache.org/xml/features/validation/dynamic). */
    protected static final String DYNAMIC_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/dynamic";

    /** Default parser name (dom.wrappers.Xerces). */
    protected static final String DEFAULT_PARSER_NAME = "dom.wrappers.Xerces";

    protected static final String DEFAULT_LOAD_DTD_GRAMMAR_ID = "http://apache.org/xml/features/nonvalidating/load-dtd-grammar";

    protected static final String DEFAULT_LOAD_EXTERNAL_DTD_ID = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    /** Default repetition (1). */
    protected static final int DEFAULT_REPETITION = 1;

    /** Default namespaces support (true). */
    protected static final boolean DEFAULT_NAMESPACES = true;

    /** Default validation support (false). */
    protected static final boolean DEFAULT_VALIDATION = false;

    /** Default Schema validation support (false). */
    protected static final boolean DEFAULT_SCHEMA_VALIDATION = false;

    /** Default Schema full checking support (false). */
    protected static final boolean DEFAULT_SCHEMA_FULL_CHECKING = false;

    /** Default dynamic validation support (false). */
    protected static final boolean DEFAULT_DYNAMIC_VALIDATION = false;

    protected static final boolean DEFAULT_LOAD_DTD_GRAMMAR = false; // Esta la he añadido yo, por defecto es true, pero quizás ralentiza si se conecta al DTD (COMPROBAR)

    protected static final boolean DEFAULT_LOAD_EXTERNAL_DTD = false; // Idem

    protected DOMParser parser;

    /**
     * Creates a new instance of XercesDOMParserWrapperImpl
     */
    public XercesDOMParserWrapperImpl()
    {
        this.parser = createParser();

        parser.setErrorHandler(this);

        try
        {
            parser.setFeature(VALIDATION_FEATURE_ID, DEFAULT_VALIDATION);
            // Para favorecer que los template sean multithread en lectura
            // http://xerces.apache.org/xerces-j/features.html#defer-node-expansion
            // parser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion",false);
        }
        catch(SAXNotRecognizedException ex)
        {
            throw new ItsNatException(ex);
        }
        catch(SAXNotSupportedException ex)
        {
            throw new ItsNatException(ex);
        }
    }

    public static XercesDOMParserWrapperImpl createXercesDOMParserWrapper(int namespaceOfMIME,String defaultEncoding)
    {
        if (NamespaceUtil.isMIME_HTML_or_XHTML(namespaceOfMIME))
            return new NekoHTMLDOMParserWrapperImpl(defaultEncoding);
        else if (NamespaceUtil.isMIME_ANDROID(namespaceOfMIME))
            return new XercesXMLDOMParserWrapperImpl(true); // Antes se pasaba false pero a causa de una interpretación errónea, cuando esté claro y consolidado se puede quitar esta condición 
        else
            return new XercesXMLDOMParserWrapperImpl(true);
    }

    public abstract DOMParser createParser();

    public Document parse(InputSource input)
    {
        try
        {
            parser.parse(input);
        }
        catch(SAXException ex)
        {
            throw new ItsNatException(ex);
        }
        catch (IOException ex)
        {
            throw new ItsNatException(ex);
        }

        return parser.getDocument();
    }

    @Override
    public void error(org.xml.sax.SAXParseException ex) throws org.xml.sax.SAXException
    {
        printError("Error", ex);
    }

    @Override
    public void fatalError(org.xml.sax.SAXParseException ex) throws org.xml.sax.SAXException
    {
        printError("Fatal Error", ex);
        throw ex;
    }

    @Override
    public void warning(org.xml.sax.SAXParseException ex) throws org.xml.sax.SAXException
    {
        printError("Warning", ex);
    }

    protected void printError(String type, SAXParseException ex) throws org.xml.sax.SAXException
    {
        //String errMsg =
        System.err.print("[");
        System.err.print(type);
        System.err.print("] ");
        String systemId = ex.getSystemId();
        if (systemId != null) {
            int index = systemId.lastIndexOf('/');
            if (index != -1)
                systemId = systemId.substring(index + 1);
            System.err.print(systemId);
        }
        System.err.print(':');
        System.err.print(ex.getLineNumber());
        System.err.print(':');
        System.err.print(ex.getColumnNumber());
        System.err.print(": ");
        System.err.print(ex.getMessage());
        System.err.println();
        System.err.flush();

    } // printError(String,SAXParseException)
}
