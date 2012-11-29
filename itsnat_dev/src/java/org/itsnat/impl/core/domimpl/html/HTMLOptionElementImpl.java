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

import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class HTMLOptionElementImpl extends HTMLElementImpl implements HTMLOptionElement
{
    protected HTMLOptionElementImpl()
    {
    }

    public HTMLOptionElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLOptionElementImpl();
    }

    public boolean getDefaultSelected()
    {
        // Por poner algo pues no es un verdadero atributo
        // debería gestionarse como una propiedad
        return getAttributeBoolean( "defaultSelected" );
    }

    public void setDefaultSelected( boolean defaultSelected )
    {
        // Por poner algo pues no es un verdadero atributo
        // debería gestionarse como una propiedad
        setAttributeBoolean( "defaultSelected", defaultSelected );
    }

    public String getText()
    {
        return getTextContent();
    }

    public int getIndex()
    {
        // El padre puede ser un <optgroup>
        Node select = getParentNode();
        while ( (select != null) && ! ( select instanceof HTMLSelectElement ) )
            select = select.getParentNode();

        LinkedList options = HTMLSelectElementImpl.getOptionsArray((HTMLSelectElement)select);
        // options NO puede ser nulo pues está dentro este <option>
        int i = 0;
        for(Iterator it = options.iterator(); it.hasNext(); i++)
            if (it.next() == this) return i;

        return -1;
    }

    // Incluido recientemente en Xerces, no lo soportamos
    // http://xerces.apache.org/xerces2-j/javadocs/api/org/w3c/dom/html/HTMLOptionElement.html
    public void setIndex(int index)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean getDisabled()
    {
        return getAttributeBoolean( "disabled" );
    }

    public void setDisabled( boolean disabled )
    {
        setAttributeBoolean( "disabled", disabled );
    }

    public String getLabel()
    {
        return getAttribute( "label" );
    }

    public void setLabel( String label )
    {
        setAttribute( "label", label );
    }

    public boolean getSelected()
    {
        return getAttributeBoolean( "selected" );
    }

    public void setSelected( boolean selected )
    {
        setAttributeBoolean( "selected", selected );
    }

    public static void setSelected(HTMLOptionElement option, boolean selected )
    {
        // Esto es porque una antigua versión del W3C DOM no incluía
        // el HTMLOptionElement.setSelected y si el servidor de aplicaciones incluye esa
        // versión el setSelected NO es visible. Ocurre al menos con el Tomcat 5.5
        // ejecutado con JDK 1.4
        setAttributeBoolean(option, "selected", selected );
    }

    public String getValue()
    {
        return getAttribute( "value" );
    }

    public void setValue( String value )
    {
        setAttribute( "value", value );
    }

    public HTMLFormElement getForm()
    {
        return getFormBase();
    }

}
