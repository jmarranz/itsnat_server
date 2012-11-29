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
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLTableCellElement;

/**
 *
 * @author jmarranz
 */
public class HTMLTableCellElementImpl extends HTMLElementImpl implements HTMLTableCellElement
{
    protected HTMLTableCellElementImpl()
    {
    }

    public HTMLTableCellElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLTableCellElementImpl();
    }

    public int getCellIndex()
    {
        Node parent = getParentNode(); // No consideramos el caso de que el padre NO sea un HTMLTableRowElement
        int index = 0;
        Node child = parent.getFirstChild();
        while (child != null)
        {
            if (child instanceof HTMLTableCellElement) // Puede haber comentarios, espacios en blanco etc
            {
                if (child == this)
                    return index;
                index++;
            }
            child = child.getNextSibling();
        }
        return -1;
    }

    // Incluido recientemente en Xerces, no lo soportamos
    // http://xerces.apache.org/xerces2-j/javadocs/api/org/w3c/dom/html/HTMLTableCellElement.html
    public void setCellIndex(int cellIndex)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getAbbr()
    {
        return getAttribute( "abbr" );
    }

    public void setAbbr( String abbr )
    {
        setAttribute( "abbr", abbr );
    }

    public String getAlign()
    {
        return getAttribute( "align" );
    }

    public void setAlign( String align )
    {
        setAttribute( "align", align );
    }

    public String getAxis()
    {
        return getAttribute( "axis" );
    }

    public void setAxis( String axis )
    {
        setAttribute( "axis", axis );
    }

    public String getBgColor()
    {
        return getAttribute( "bgcolor" );
    }

    public void setBgColor( String bgColor )
    {
        setAttribute( "bgcolor", bgColor );
    }

    public String getCh()
    {
        return getAttribute( "char" );
    }

    public void setCh( String ch )
    {
        setAttribute( "char", ch );
    }

    public String getChOff()
    {
        return getAttribute( "charoff" );
    }

    public void setChOff( String chOff )
    {
        setAttribute( "charoff", chOff );
    }

    public int getColSpan()
    {
        return Integer.parseInt( getAttribute( "colspan" ) );
    }

    public void setColSpan( int colspan )
    {
        setAttribute( "colspan", String.valueOf( colspan ) );
    }

    public String getHeaders()
    {
        return getAttribute( "headers" );
    }

    public void setHeaders( String headers )
    {
        setAttribute( "headers", headers );
    }

    public String getHeight()
    {
        return getAttribute( "height" );
    }

    public void setHeight( String height )
    {
        setAttribute( "height", height );
    }

    public boolean getNoWrap()
    {
        return getAttributeBoolean( "nowrap" );
    }

    public void setNoWrap( boolean noWrap )
    {
        setAttributeBoolean( "nowrap", noWrap );
    }

    public int getRowSpan()
    {
        return Integer.parseInt( getAttribute( "rowspan" ) );
    }

    public void setRowSpan( int rowspan )
    {
        setAttribute( "rowspan", String.valueOf( rowspan ) );
    }

    public String getScope()
    {
        return getAttribute( "scope" );
    }

    public void setScope( String scope )
    {
        setAttribute( "scope", scope );
    }

    public String getVAlign()
    {
        return getAttribute( "valign" );
    }

    public void setVAlign( String vAlign )
    {
        setAttribute( "valign", vAlign );
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
