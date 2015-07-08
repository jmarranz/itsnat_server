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
import org.w3c.dom.html.HTMLBodyElement;

/**
 *
 * @author jmarranz
 */
public class HTMLBodyElementImpl extends HTMLElementImpl implements HTMLBodyElement
{
    protected HTMLBodyElementImpl()
    {
    }

    public HTMLBodyElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLBodyElementImpl();
    }

    public String getALink()
    {
        return getAttribute( "alink" );
    }

    public void setALink(String aLink)
    {
        setAttribute( "alink", aLink );
    }

    public String getBackground()
    {
        return getAttribute( "background" );
    }

    public void setBackground( String background )
    {
        setAttribute( "background", background );
    }

    public String getBgColor()
    {
        return getAttribute( "bgcolor" );
    }

    public void setBgColor(String bgColor)
    {
        setAttribute( "bgcolor", bgColor );
    }

    public String getLink()
    {
        return getAttribute( "link" );
    }

    public void setLink(String link)
    {
        setAttribute( "link", link );
    }

    public String getText()
    {
        return getAttribute( "text" );
    }

    public void setText(String text)
    {
        setAttribute( "text", text );
    }

    public String getVLink()
    {
        return getAttribute( "vlink" );
    }

    public void setVLink(String vLink)
    {
        setAttribute( "vlink", vLink );
    }
}
