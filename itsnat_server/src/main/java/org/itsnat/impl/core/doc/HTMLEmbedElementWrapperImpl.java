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

package org.itsnat.impl.core.doc;

import org.itsnat.core.html.ItsNatHTMLEmbedElement;
import org.itsnat.impl.core.domimpl.ElementDocContainer;
import org.w3c.dom.Attr;

/**
 *
 * @author jmarranz
 */
public class HTMLEmbedElementWrapperImpl extends ElementDocContainerWrapperImpl
{
    public HTMLEmbedElementWrapperImpl(ItsNatHTMLEmbedElement elem)
    {
        super((ElementDocContainer)elem);
    }

    public boolean isJavaApplet()
    {
        String type = getElement().getAttribute("type");
        return type.equals("application/x-java-applet");
    }

    public ItsNatHTMLEmbedElement getItsNatHTMLEmbedElement()
    {
        return (ItsNatHTMLEmbedElement)elem;
    }

    public String getURL()
    {
        return getElement().getAttribute( "src" );
    }

    public void setURL(String url)
    {
        getElement().setAttribute( "src", url );
    }

    public String getURLAttrName()
    {
        return "src";
    }

    public boolean isURLAttribute(Attr attr)
    {
        return getURLAttrName().equals(attr.getName());
    }

}
