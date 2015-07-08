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
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 *
 * @author jmarranz
 */
public class HTMLTextAreaElementImpl extends HTMLElementImpl implements HTMLTextAreaElement
{
    protected String valueProperty; // Sirve para recordar si explícitamente hemos definido el atributo value

    protected HTMLTextAreaElementImpl()
    {
    }

    public HTMLTextAreaElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLTextAreaElementImpl();
    }

    public String getValueProperty()
    {
        return valueProperty;
    }

    public void setValueProperty(String valueProperty)
    {
        this.valueProperty = valueProperty;
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

    public String getAccessKey()
    {
        return getAttribute( "accesskey" );
    }

    public void setAccessKey( String accessKey )
    {
        setAttribute( "accesskey", accessKey );
    }

    public int getCols()
    {
        return Integer.parseInt( getAttribute( "cols" ) );
    }

    public void setCols( int cols )
    {
        setAttribute( "cols", String.valueOf( cols ) );
    }

    public boolean getDisabled()
    {
        return getAttributeBoolean( "disabled" );
    }

    public void setDisabled( boolean disabled )
    {
        setAttributeBoolean( "disabled", disabled );
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

    public int getRows()
    {
        return Integer.parseInt( getAttribute( "rows" ) );
    }

    public void setRows( int rows )
    {
        setAttribute( "rows", String.valueOf( rows ) );
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
