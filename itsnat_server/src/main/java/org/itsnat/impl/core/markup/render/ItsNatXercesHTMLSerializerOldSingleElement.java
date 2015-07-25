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
package org.itsnat.impl.core.markup.render;

import java.io.IOException;
import java.io.Writer;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author jmarranz
 */
public class ItsNatXercesHTMLSerializerOldSingleElement extends ItsNatXercesHTMLSerializerOld
{
    protected boolean doingRootElement;
        
    public ItsNatXercesHTMLSerializerOldSingleElement(/*boolean xhtml,*/ Writer writer, OutputFormat format)
    {
        super(writer,format); 
        this.doingRootElement = true;        
    }      

    @Override
    protected boolean isDocumentState()
    {
        // Xerces tiende a generar el DOCTYPE cuando no se especifica, en nuestro caso respetamos el deseo del diseñador del template, además así evitamos la generación de DOCTYPE en la serialización de Element autónomos
        //if (doingElement)
        //    return false;
        return super.isDocumentState();
    }

    @Override
    public void serialize(Element elmnt) throws IOException {
        super.serialize(elmnt);
    }    
    
    @Override
    public void startElement( String namespaceURI, String localName, String rawName, Attributes attrs )
        throws SAXException
    {
        super.startElement(namespaceURI, localName, rawName, attrs);
    }
    
    @Override
    public void endElement( String namespaceURI, String localName, String rawName )
        throws SAXException
    {
        super.endElement(namespaceURI, localName, rawName);
    }    
    
    @Override
    protected void serializeElement( Element elem )
        throws IOException
    {
        if (this.doingRootElement) 
        {
            this._started = true;
            this.doingRootElement = false;
        }
        super.serializeElement(elem);
    }    
}
