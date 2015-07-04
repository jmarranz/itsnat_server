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
import org.apache.xerces.parsers.SAXParser;
import org.itsnat.impl.core.domimpl.otherns.OtherNSDocumentImpl;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 *
 * @author jmarranz
 */
public class XercesXMLDOMParserWrapperImpl extends XercesDOMParserWrapperImpl
{

    /**
     * Creates a new instance of XercesXMLDOMParserWrapperImpl
     */
    public XercesXMLDOMParserWrapperImpl(boolean namespacesFeature)
    {
        try
        {
            parser.setFeature(NAMESPACES_FEATURE_ID, namespacesFeature);            
            
            parser.setFeature(SCHEMA_VALIDATION_FEATURE_ID, DEFAULT_SCHEMA_VALIDATION);
            parser.setFeature(SCHEMA_FULL_CHECKING_FEATURE_ID, DEFAULT_SCHEMA_FULL_CHECKING);
            parser.setFeature(DYNAMIC_VALIDATION_FEATURE_ID, DEFAULT_DYNAMIC_VALIDATION);

            parser.setFeature(DEFAULT_LOAD_DTD_GRAMMAR_ID, DEFAULT_LOAD_DTD_GRAMMAR);
            parser.setFeature(DEFAULT_LOAD_EXTERNAL_DTD_ID, DEFAULT_LOAD_EXTERNAL_DTD);

          
            
            parser.setProperty("http://apache.org/xml/properties/dom/document-class-name",OtherNSDocumentImpl.class.getName());

            //XMLDocumentFilter[] filters = { new NekoBatikDocumentFilterImpl() };
            //parser.setProperty("http://cyberneko.org/html/properties/filters", filters);
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
        return new XercesXMLDOMParserImpl();
    }
}
