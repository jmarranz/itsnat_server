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

import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class HTMLInputElementImpl extends HTMLElementImpl implements HTMLInputElement
{
    protected HTMLInputElementImpl()
    {
    }

    public HTMLInputElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLInputElementImpl();
    }

    public String getDefaultValue()
    {
        // Por poner algo pues no es un verdadero atributo
        // debería procesarse como una propiedad
        return getAttribute( "defaultValue" );
    }

    public void setDefaultValue( String defaultValue )
    {
        setAttribute( "defaultValue", defaultValue );
    }

    public boolean getDefaultChecked()
    {
        // Por poner algo pues no es un verdadero atributo
        // debería procesarse como una propiedad
        return getAttributeBoolean( "defaultChecked" );
    }

    public void setDefaultChecked( boolean defaultChecked )
    {
        setAttributeBoolean( "defaultChecked", defaultChecked );
    }

    public String getAccept()
    {
        return getAttribute( "accept" );
    }

    public void setAccept( String accept )
    {
        setAttribute( "accept", accept );
    }

    public String getAccessKey()
    {
        return getAttribute( "accesskey" );
    }

    public void setAccessKey( String accessKey )
    {
        setAttribute( "accesskey", accessKey );
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

    public boolean getChecked()
    {
        return getAttributeBoolean( "checked" );
    }

    public void setChecked( boolean checked )
    {
        setAttributeBoolean( "checked", checked );
    }

    public boolean getDisabled()
    {
        return getAttributeBoolean( "disabled" );
    }

    public void setDisabled( boolean disabled )
    {
        setAttributeBoolean( "disabled", disabled );
    }

    public int getMaxLength()
    {
        return Integer.parseInt( getAttribute( "maxlength" ) );
    }

    public void setMaxLength( int maxLength )
    {
        setAttribute( "maxlength", String.valueOf( maxLength ) );
    }

    public String getName()
    {
        return getAttribute( "name" );
    }

    public void setName( String name )
    {
        setAttribute( "name", name );
    }

    public boolean getReadOnly()
    {
        return getAttributeBoolean( "readonly" );
    }

    public void setReadOnly( boolean readOnly )
    {
        setAttributeBoolean( "readonly", readOnly );
    }

    public String getSize()
    {
        return getAttribute( "size" );
    }

    public void setSize( String size )
    {
        setAttribute( "size", size );
    }

    public String getSrc()
    {
        return getAttribute( "src" );
    }

    public void setSrc( String src )
    {
        setAttribute( "src", src );
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

    public String getUseMap()
    {
        return getAttribute( "usemap" );
    }

    public void setUseMap( String useMap )
    {
        setAttribute( "usemap", useMap );
    }

    public String getValue()
    {
        return getAttribute( "value" );
    }

    public void setValue( String value )
    {
        setAttribute( "value", value );
    }

    public void blur()
    {
        methodCallNoParam("blur");
    }

    public void click()
    {
        methodCallNoParam("click");
    }

    public void focus()
    {
        methodCallNoParam("focus");
    }

    public void select()
    {
        methodCallNoParam("select");
    }

    public HTMLFormElement getForm()
    {
        return getFormBase();
    }

}
