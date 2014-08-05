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

package org.itsnat.impl.core.scriptren.jsren.node.otherns;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class JSRenderOtherNSAttributeMSIEOldImpl extends JSRenderOtherNSAttributeImpl
{
    public final static JSRenderOtherNSAttributeMSIEOldImpl SINGLETON = new JSRenderOtherNSAttributeMSIEOldImpl();

    /**
     * Creates a new instance of JSRenderOtherNSAttributeW3CImpl
     */
    public JSRenderOtherNSAttributeMSIEOldImpl()
    {
    }

    public boolean isIgnored(Attr attr, Element elem)
    {
        return false;
    }

    public boolean isRenderAttributeAlongsideProperty(String attrName, Element ele)
    {
        // Seguramente no se llama nunca.
        return false;
    }
}
