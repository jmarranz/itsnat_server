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
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class HTMLSelectElementImpl extends HTMLElementImpl implements HTMLSelectElement
{
    protected HTMLCollection options;

    protected HTMLSelectElementImpl()
    {
    }

    public HTMLSelectElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLSelectElementImpl();
    }

    public HTMLOptionElement getOptionElement(int index)
    {
        // Puede haber <optgroup> dentro del select padre
        return (HTMLOptionElement)DOMUtilInternal.getChildElementWithTagNameNS(this,NamespaceUtil.XHTML_NAMESPACE,"option", index);
    }

    public static LinkedList<Node> getOptionsArray(HTMLSelectElement select)
    {
        // Puede haber <optgroup> dentro del select padre, hay que buscar recursivamente
        return DOMUtilInternal.getChildElementListWithTagNameNS(select,NamespaceUtil.XHTML_NAMESPACE,"option",true);
    }

    public LinkedList getOptionsArray()
    {
        return getOptionsArray(this);
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

    public int getSelectedIndex()
    {
        LinkedList options = getOptionsArray();
        if (options != null)
        {
            int i = 0;
            for(Iterator it = options.iterator(); it.hasNext(); i++)
            {
                HTMLOptionElement option = (HTMLOptionElement)it.next();
                if (option.getSelected()) return i;
            }
        }

        return -1;
    }

    public void setSelectedIndex( int selectedIndex )
    {
        HTMLOptionElement optionSelected = null;
        LinkedList options = getOptionsArray();
        if (options != null)
        {
            int i = 0;
            for(Iterator it = options.iterator(); it.hasNext(); i++)
            {
                HTMLOptionElement option = (HTMLOptionElement)it.next();
                if (i == selectedIndex) optionSelected = option;
                else HTMLOptionElementImpl.setSelected(option, false);
            }
        }
        // Hacemos la selección al final, así evitamos que en un momento dado haya dos elementos seleccionados
        if (optionSelected != null)
            HTMLOptionElementImpl.setSelected(optionSelected,true);
    }

    public HTMLCollection getOptions()
    {
        if (options == null )
            options = new HTMLCollectionImpl( this, HTMLCollectionImpl.OPTION );
        return options;
    }

    public int getLength()
    {
        return getOptions().getLength();
    }

    public boolean getDisabled()
    {
        return getAttributeBoolean( "disabled" );
    }

    public void setDisabled( boolean disabled )
    {
        setAttributeBoolean( "disabled", disabled );
    }

    public boolean getMultiple()
    {
        return getAttributeBoolean( "multiple" );
    }

    public void setMultiple( boolean multiple )
    {
        setAttributeBoolean( "multiple", multiple );
    }

    public String getName()
    {
        return getAttribute( "name" );
    }

    public void setName( String name )
    {
        setAttribute( "name", name );
    }

    public int getSize()
    {
        return Integer.parseInt( getAttribute( "size" ) );
    }

    public void setSize( int size )
    {
        setAttribute( "size", String.valueOf( size ) );
    }

    public int getTabIndex()
    {
        return Integer.parseInt( getAttribute( "tabindex" ) );
    }

    public void setTabIndex( int tabIndex )
    {
        setAttribute( "tabindex", String.valueOf( tabIndex ) );
    }

    public void add( HTMLElement element, HTMLElement before )
    {
        insertBefore( element, before );
    }

    public void remove( int index )
    {
        if (index < 0) return;
        HTMLOptionElement option = getOptionElement(index);
        if (option != null)
            option.getParentNode().removeChild(option);
    }

    public void blur()
    {
        methodCallNoParam("blur");
    }

    public void focus()
    {
        methodCallNoParam("focus");
    }

    public HTMLFormElement getForm()
    {
        return getFormBase();
    }

}
