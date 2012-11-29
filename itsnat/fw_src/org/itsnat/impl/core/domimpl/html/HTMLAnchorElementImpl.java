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
import org.w3c.dom.html.HTMLAnchorElement;

/**
 *
 * @author jmarranz
 */
public class HTMLAnchorElementImpl extends HTMLElementImpl implements HTMLAnchorElement
{
    protected HTMLAnchorElementImpl()
    {
    }

    public HTMLAnchorElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLAnchorElementImpl();
    }

    public void blur()
    {
        methodCallNoParam("blur");
    }

    public void focus()
    {
        methodCallNoParam("focus");
    }

    public String getAccessKey()
    {
        return getAttribute( "accesskey" );
    }

    public void setAccessKey( String accessKey )
    {
        setAttribute( "accesskey", accessKey );
    }

    public String getCharset()
    {
        return getAttribute( "charset" );
    }

    public void setCharset( String charset )
    {
        setAttribute( "charset", charset );
    }

    public String getCoords()
    {
        return getAttribute( "coords" );
    }

    public void setCoords( String coords )
    {
        setAttribute( "coords", coords );
    }

    public String getHref()
    {
        return getAttribute( "href" );
    }

    public void setHref( String href )
    {
        setAttribute( "href", href );
    }

    public String getHreflang()
    {
        return getAttribute( "hreflang" );
    }

    public void setHreflang( String hreflang )
    {
        setAttribute( "hreflang", hreflang );
    }

    public String getName()
    {
        return getAttribute( "name" );
    }

    public void setName( String name )
    {
        setAttribute( "name", name );
    }

    public String getRel()
    {
        return getAttribute( "rel" );
    }

    public void setRel( String rel )
    {
        setAttribute( "rel", rel );
    }

    public String getRev()
    {
        return getAttribute( "rev" );
    }

    public void setRev( String rev )
    {
        setAttribute( "rev", rev );
    }

    public String getShape()
    {
        return getAttribute( "shape" );
    }

    public void setShape( String shape )
    {
        setAttribute( "shape", shape );
    }

    public int getTabIndex()
    {
        return Integer.parseInt( getAttribute( "tabindex" ) );
    }

    public void setTabIndex( int tabIndex )
    {
        setAttribute( "tabindex", String.valueOf( tabIndex ) );
    }

    public String getTarget()
    {
        return getAttribute( "target" );
    }

    public void setTarget( String target )
    {
        setAttribute( "target", target );
    }

    public String getType()
    {
        return getAttribute( "type" );
    }

    public void setType( String type )
    {
        setAttribute( "type", type );
    }

}
