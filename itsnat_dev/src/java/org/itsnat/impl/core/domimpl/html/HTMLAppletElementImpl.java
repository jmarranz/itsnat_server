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

package org.itsnat.impl.core.domimpl.html;

import org.itsnat.impl.core.domimpl.ElementDocContainer;
import org.itsnat.core.html.ItsNatHTMLAppletElement;
import org.itsnat.impl.core.doc.ElementDocContainerWrapperImpl;
import org.itsnat.impl.core.doc.HTMLAppletElementWrapperImpl;
import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLAppletElement;

/**
 *
 * @author jmarranz
 */
public class HTMLAppletElementImpl extends HTMLElementImpl implements HTMLAppletElement,ElementDocContainer,ItsNatHTMLAppletElement
{
    protected HTMLAppletElementWrapperImpl docContWrap = new HTMLAppletElementWrapperImpl(this);

    protected HTMLAppletElementImpl()
    {
    }

    public HTMLAppletElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLAppletElementImpl();
    }

    public ElementDocContainerWrapperImpl getElementDocContainerWrapper()
    {
        return docContWrap;
    }

    public Document getContentDocument()
    {
        return docContWrap.getContentDocument();
    }

    public String getAlign()
    {
        return getAttribute( "align" );
    }

    public void setAlign( String align )
    {
        setAttribute( "align", align );
    }

    public String getAlt()
    {
        return getAttribute( "alt" );
    }

    public void setAlt( String alt )
    {
        setAttribute( "alt", alt );
    }

    public String getArchive()
    {
        return getAttribute( "archive" );
    }

    public void setArchive( String archive )
    {
        setAttribute( "archive", archive );
    }

    public String getCode()
    {
        return getAttribute( "code" );
    }

    public void setCode( String code )
    {
        setAttribute( "code", code );
    }

    public String getCodeBase()
    {
        return getAttribute( "codebase" );
    }

    public void setCodeBase( String codeBase )
    {
        setAttribute( "codebase", codeBase );
    }

    public String getHeight()
    {
        return getAttribute( "height" );
    }

    public void setHeight( String height )
    {
        setAttribute( "height", height );
    }

    public String getHspace()
    {
        return getAttribute( "height" );
    }

    public void setHspace( String height )
    {
        setAttribute( "height", height );
    }

    public String getName()
    {
        return getAttribute( "name" );
    }

    public void setName( String name )
    {
        setAttribute( "name", name );
    }

    public String getObject()
    {
        return getAttribute( "object" );
    }

    public void setObject( String object )
    {
        setAttribute( "object", object );
    }

    public String getVspace()
    {
        return getAttribute( "vspace" );
    }

    public void setVspace( String vspace )
    {
        setAttribute( "vspace", vspace );
    }

    public String getWidth()
    {
        return getAttribute( "width" );
    }

    public void setWidth( String width )
    {
        setAttribute( "width", width );
    }

}
