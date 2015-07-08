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
import org.w3c.dom.html.HTMLOListElement;

/**
 *
 * @author jmarranz
 */
public class HTMLOListElementImpl extends HTMLElementImpl implements HTMLOListElement
{
    protected HTMLOListElementImpl()
    {
    }

    public HTMLOListElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLOListElementImpl();
    }

    public boolean getCompact()
    {
        return getAttributeBoolean( "compact" );
    }

    public void setCompact( boolean compact )
    {
        setAttributeBoolean( "compact", compact );
    }

    public int getStart()
    {
        return Integer.parseInt( getAttribute( "start" ) );
    }

    public void setStart( int start )
    {
        setAttribute( "start", String.valueOf( start ) );
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
