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
import org.w3c.dom.html.HTMLTableColElement;

/**
 *
 * @author jmarranz
 */
public class HTMLTableColElementImpl extends HTMLElementImpl implements HTMLTableColElement
{
    protected HTMLTableColElementImpl()
    {
    }

    public HTMLTableColElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLTableColElementImpl();
    }

    public String getAlign()
    {
        return getAttribute( "align" );
    }

    public void setAlign( String align )
    {
        setAttribute( "align", align );
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

    public int getSpan()
    {
        return Integer.parseInt( getAttribute( "span" ) );
    }

    public void setSpan( int span )
    {
        setAttribute( "span", String.valueOf( span ) );
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
