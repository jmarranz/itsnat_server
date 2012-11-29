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

import org.itsnat.impl.core.doc.ElementDocContainerWrapperImpl;
import org.itsnat.impl.core.domimpl.ElementDocContainer;
import org.itsnat.impl.core.doc.HTMLObjectElementWrapperImpl;
import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLObjectElement;

/**
 *
 * @author jmarranz
 */
public class HTMLObjectElementImpl extends HTMLElementImpl implements HTMLObjectElement,ElementDocContainer
{
    protected HTMLObjectElementWrapperImpl docContWrap = new HTMLObjectElementWrapperImpl(this);

    protected HTMLObjectElementImpl()
    {
    }

    public HTMLObjectElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLObjectElementImpl();
    }

    public ElementDocContainerWrapperImpl getElementDocContainerWrapper()
    {
        return docContWrap;
    }

    public String getCode()
    {
        return getAttribute( "code" );
    }

    public void setCode( String code )
    {
        setAttribute( "code", code );
    }

    public String getAlign()
    {
        return getAttribute( "align" );
    }

    public void setAlign( String align )
    {
        setAttribute( "align", align );
    }

    public String getArchive()
    {
        return getAttribute( "archive" );
    }

    public void setArchive( String archive )
    {
        setAttribute( "archive", archive );
    }

    public String getBorder()
    {
        return getAttribute( "border" );
    }

    public void setBorder( String border )
    {
        setAttribute( "border", border );
    }

    public String getCodeBase()
    {
        return getAttribute( "codebase" );
    }

    public void setCodeBase( String codeBase )
    {
        setAttribute( "codebase", codeBase );
    }

    public String getCodeType()
    {
        return getAttribute( "codetype" );
    }

    public void setCodeType( String codeType )
    {
        setAttribute( "codetype", codeType );
    }

    public String getData()
    {
        return getAttribute( "data" );
    }

    public void setData( String data )
    {
        setAttribute( "data", data );
    }

    public boolean getDeclare()
    {
        return getAttributeBoolean( "declare" );
    }

    public void setDeclare( boolean declare )
    {
        setAttributeBoolean( "declare", declare );
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
        return getAttribute( "hspace" );
    }

    public void setHspace( String hspace )
    {
        setAttribute( "hspace", hspace );
    }

    public String getName()
    {
        return getAttribute( "name" );
    }

    public void setName( String name )
    {
        setAttribute( "name", name );
    }

    public String getStandby()
    {
        return getAttribute( "standby" );
    }

    public void setStandby( String standby )
    {
        setAttribute( "standby", standby );
    }

    public int getTabIndex()
    {
        return Integer.parseInt( getAttribute( "tabindex" ) );
    }

    public void setTabIndex( int tabIndex )
    {
        setAttribute( "tabindex", String.valueOf( tabIndex ) );
    }

    public String getType()
    {
        return getAttribute( "type" );
    }

    public void setType( String type )
    {
        setAttribute( "type", type );
    }

    public String getUseMap()
    {
        return getAttribute( "usemap" );
    }

    public void setUseMap( String useMap )
    {
        setAttribute( "usemap", useMap );
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

    public Document getContentDocument()
    {
        return docContWrap.getContentDocument();
    }

    public HTMLFormElement getForm()
    {
        return getFormBase();
    }

}
