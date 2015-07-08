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

import org.itsnat.core.ItsNatException;
import org.apache.xerces.parsers.DOMParser;

import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.itsnat.impl.core.domimpl.html.HTMLDocumentImpl;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 *
 * @author jmarranz
 */
public class NekoHTMLDOMParserWrapperImpl extends XercesDOMParserWrapperImpl
{

    /** Creates a new instance of NekoHTMLDOMParserWrapperImpl */
    public NekoHTMLDOMParserWrapperImpl(String defaultEncoding)
    {
        try
        {
            parser.setProperty("http://cyberneko.org/html/properties/default-encoding",defaultEncoding); // "ISO-8859-1"
            parser.setProperty("http://cyberneko.org/html/properties/names/elems","match");
            parser.setProperty("http://cyberneko.org/html/properties/names/attrs","no-change");

            parser.setFeature("http://cyberneko.org/html/features/scanner/cdata-sections",true); // Para evitar que se conviertan en comentarios

            // Podríamos quitar los namespaces para usar XHTML porque Xerces no los soporta en HTML
            // segun se dice en:
            // http://nekohtml.sourceforge.net/faq.html#hierarchy
            // "Why do I get a hierarchy request error using DOM?
            // ...The Xerces HTML DOM implementation does not support namespaces and cannot represent XHTML documents with namespace information.
            // Therefore, in order to use the default HTML DOM implementation with NekoHTML's DOMParser to parse XHTML documents, you must turn off namespace processing.
            // parser.setFeature("http://xml.org/sax/features/namespaces", false); "
            // De otra manera en XHTML no crearía HTMLElements para los nodos sino Element XML
            // El problema es que deja de funcionar el namespace "itsnat", por lo que
            // la mejor solución es hacer una clase nueva documento HTML que redefina el método
            // createElementNS

            parser.setProperty("http://apache.org/xml/properties/dom/document-class-name",
                    HTMLDocumentImpl.class.getName());

            // Desactivamos el balanceo de tags, esto hace que el HTMLTagBalancer
            // de NekoHTML no se registre, porque luego *lo reintroducimos*
            // de nuevo (modificandolo) a través del registro de filters, así nos evitamos un fork.
            parser.setFeature("http://cyberneko.org/html/features/balance-tags",false);
            
            XMLDocumentFilter[] filters = { new ItsNatNekoHTMLTagBalancerImpl()  };
            parser.setProperty("http://cyberneko.org/html/properties/filters", filters);
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

    public DOMParser createParser()
    {
        return new NekoHTMLDOMParserImpl();
    }
/*
    public static void main(String[] args) throws Exception
    {
        NekoHTMLDOMParserWrapperImpl parser = new NekoHTMLDOMParserWrapperImpl();
        parser.parser.setProperty("http://apache.org/xml/properties/dom/document-class-name",
                    org.apache.html.dom.HTMLDocumentImpl.class.getName());

        String code = "<html><head></head><body><p>Heello</p></body></html>";
        StringReader reader = new StringReader(code);
        Document doc = parser.parse(new InputSource(reader));
    }
 */
}
