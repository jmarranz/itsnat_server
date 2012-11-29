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

import org.itsnat.impl.core.domimpl.ElementDocContainer;
import org.w3c.dom.Attr;
import org.w3c.dom.html.HTMLIFrameElement;

/**
 *
 * @author jmarranz
 */
public class HTMLIFrameElementWrapperImpl extends ElementDocContainerWrapperImpl
{
    public HTMLIFrameElementWrapperImpl(HTMLIFrameElement elem)
    {
        super((ElementDocContainer)elem);
    }

    public boolean isJavaApplet()
    {
        return false; // ESTUDIAR si puede contener un applet
    }

    public HTMLIFrameElement getHTMLIFrameElement()
    {
        return (HTMLIFrameElement)elem;
    }

    public String getURL()
    {
        return getHTMLIFrameElement().getSrc();
    }

    public void setURL(String url)
    {
        getHTMLIFrameElement().setSrc(url);
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
