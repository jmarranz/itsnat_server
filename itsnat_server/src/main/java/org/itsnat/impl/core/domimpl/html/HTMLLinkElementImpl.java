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
import org.w3c.dom.html.HTMLLinkElement;

/**
 *
 * @author jmarranz
 */
public class HTMLLinkElementImpl extends HTMLElementImpl implements HTMLLinkElement
{
    protected HTMLLinkElementImpl()
    {
    }

    public HTMLLinkElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLLinkElementImpl();
    }

    public boolean getDisabled()
    {
        return getAttributeBoolean( "disabled" );
    }

    public void setDisabled( boolean disabled )
    {
        setAttributeBoolean( "disabled", disabled );
    }

    public String getCharset()
    {
        return getAttribute( "charset" );
    }

    public void setCharset( String charset )
    {
        setAttribute( "charset", charset );
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

    public String getMedia()
    {
        return getAttribute( "media" );
    }

    public void setMedia( String media )
    {
        setAttribute( "media", media );
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
