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
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLLegendElement;

/**
 *
 * @author jmarranz
 */
public class HTMLLegendElementImpl extends HTMLElementImpl implements HTMLLegendElement
{
    protected HTMLLegendElementImpl()
    {
    }

    public HTMLLegendElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLLegendElementImpl();
    }

    public String getAccessKey()
    {
        return getAttribute( "accesskey" );
    }

    public void setAccessKey( String accessKey )
    {
        setAttribute( "accesskey", accessKey );
    }

    public String getAlign()
    {
        return getAttribute( "align" );
    }

    public void setAlign( String align )
    {
        setAttribute( "align", align );
    }

    public HTMLFormElement getForm()
    {
        return getFormBase();
    }
}
