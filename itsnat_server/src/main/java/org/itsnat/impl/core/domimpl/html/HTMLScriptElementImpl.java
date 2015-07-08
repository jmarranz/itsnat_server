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
import org.w3c.dom.html.HTMLScriptElement;

/**
 *
 * @author jmarranz
 */
public class HTMLScriptElementImpl extends HTMLElementImpl implements HTMLScriptElement
{
    protected HTMLScriptElementImpl()
    {
    }

    public HTMLScriptElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLScriptElementImpl();
    }

    public String getText()
    {
        return getTextContent();
    }

    public void setText( String text )
    {
        setTextContent(text);
    }

    public String getHtmlFor()
    {
        return getAttribute( "for" );
    }


    public void setHtmlFor( String htmlFor )
    {
        setAttribute( "for", htmlFor );
    }


       public String getEvent()
    {
        return getAttribute( "event" );
    }


    public void setEvent( String event )
    {
        setAttribute( "event", event );
    }

    public String getCharset()
    {
        return getAttribute( "charset" );
    }

    public void setCharset( String charset )
    {
        setAttribute( "charset", charset );
    }

    public boolean getDefer()
    {
        return getAttributeBoolean( "defer" );
    }

    public void setDefer( boolean defer )
    {
        setAttributeBoolean( "defer", defer );
    }

    public String getSrc()
    {
        return getAttribute( "src" );
    }

    public void setSrc( String src )
    {
        setAttribute( "src", src );
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
