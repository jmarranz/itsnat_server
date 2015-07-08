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
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLFrameElement;

/**
 *
 * @author jmarranz
 */
public class HTMLFrameElementImpl extends HTMLElementImpl implements HTMLFrameElement
{
    protected HTMLFrameElementImpl()
    {
    }

    public HTMLFrameElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLFrameElementImpl();
    }

    public String getFrameBorder()
    {
        return getAttribute( "frameborder" );
    }

    public void setFrameBorder( String frameBorder )
    {
        setAttribute( "frameborder", frameBorder );
    }

    public String getLongDesc()
    {
        return getAttribute( "longdesc" );
    }

    public void setLongDesc( String longDesc )
    {
        setAttribute( "longdesc", longDesc );
    }

    public String getMarginHeight()
    {
        return getAttribute( "marginheight" );
    }

    public void setMarginHeight( String marginHeight )
    {
        setAttribute( "marginheight", marginHeight );
    }

    public String getMarginWidth()
    {
        return getAttribute( "marginwidth" );
    }

    public void setMarginWidth( String marginWidth )
    {
        setAttribute( "marginwidth", marginWidth );
    }

    public String getName()
    {
        return getAttribute( "name" );
    }

    public void setName( String name )
    {
        setAttribute( "name", name );
    }

    public boolean getNoResize()
    {
        return getAttributeBoolean( "noresize" );
    }

    public void setNoResize( boolean noResize )
    {
        setAttributeBoolean( "noresize", noResize );
    }

    public String getScrolling()
    {
        return getAttribute( "scrolling" );
    }

    public void setScrolling( String scrolling )
    {
        setAttribute( "scrolling", scrolling );
    }

    public String getSrc()
    {
        return getAttribute( "src" );
    }

    public void setSrc( String src )
    {
        setAttribute( "src", src );
    }

    public Document getContentDocument()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
