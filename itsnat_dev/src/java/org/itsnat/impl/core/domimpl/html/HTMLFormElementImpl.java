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
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLFormElement;

/**
 *
 * @author jmarranz
 */
public class HTMLFormElementImpl extends HTMLElementImpl implements HTMLFormElement
{
    protected HTMLCollection elements;

    protected HTMLFormElementImpl()
    {
    }

    public HTMLFormElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLFormElementImpl();
    }

    public String getName()
    {
        return getAttribute( "name" );
    }

    public void setName( String name )
    {
        setAttribute( "name", name );
    }

    public String getAcceptCharset()
    {
        return getAttribute( "accept-charset" );
    }

    public void setAcceptCharset( String acceptCharset )
    {
        setAttribute( "accept-charset", acceptCharset );
    }

    public String getAction()
    {
        return getAttribute( "action" );
    }

    public void setAction( String action )
    {
        setAttribute( "action", action );
    }

    public String getEnctype()
    {
        return getAttribute( "enctype" );
    }

    public void setEnctype( String enctype )
    {
        setAttribute( "enctype", enctype );
    }

    public String getMethod()
    {
        return getAttribute( "method" );
    }

    public void setMethod( String method )
    {
        setAttribute( "method", method );
    }

    public String getTarget()
    {
        return getAttribute( "target" );
    }

    public void setTarget( String target )
    {
        setAttribute( "target", target );
    }

    public void reset()
    {
        methodCallNoParam("reset");
    }

    public void submit()
    {
        methodCallNoParam("submit");
    }

    public int getLength()
    {
        return getElements().getLength();
    }

    public HTMLCollection getElements()
    {
        if (elements == null)
            this.elements = new HTMLCollectionImpl(this,HTMLCollectionImpl.FORM_ELEMENT);
        return elements;
    }

}
