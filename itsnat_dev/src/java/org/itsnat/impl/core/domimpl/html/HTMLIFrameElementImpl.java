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
import org.itsnat.impl.core.doc.HTMLIFrameElementWrapperImpl;
import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLIFrameElement;

/**
 *
 * @author jmarranz
 */
public class HTMLIFrameElementImpl extends HTMLElementImpl implements HTMLIFrameElement,ElementDocContainer
{
    protected HTMLIFrameElementWrapperImpl docContWrap = new HTMLIFrameElementWrapperImpl(this);

    protected HTMLIFrameElementImpl()
    {
    }

    public HTMLIFrameElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLIFrameElementImpl();
    }

    public ElementDocContainerWrapperImpl getElementDocContainerWrapper()
    {
        return docContWrap;
    }

    public String getAlign()
    {
        return getAttribute( "align" );
    }

    public void setAlign( String align )
    {
        setAttribute( "align", align );
    }

    public String getFrameBorder()
    {
        return getAttribute( "frameborder" );
    }

    public void setFrameBorder( String frameBorder )
    {
        setAttribute( "frameborder", frameBorder );
    }

    public String getHeight()
    {
        return getAttribute( "height" );
    }

    public void setHeight( String height )
    {
        setAttribute( "height", height );
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

}
