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

package org.itsnat.impl.core.jsren.dom.node.otherns;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class JSRenderOtherNSElementMSIEPocketImpl extends JSRenderOtherNSElementMSIEOldImpl
{
    public static final JSRenderOtherNSElementMSIEPocketImpl SINGLETON = new JSRenderOtherNSElementMSIEPocketImpl();

    /**
     * Creates a new instance of JSRenderOtherNSElementW3CImpl
     */
    public JSRenderOtherNSElementMSIEPocketImpl()
    {
    }

    protected String createElement(Element nodeElem,String tagName,ClientDocumentStfulImpl clientDoc)
    {
        // Pocket IE no tolera crear elementos con tagName tipo "prefijo:localName"
        // createElement devuelve null. No hay soporte de namespaces en Pocket IE
        // Por tanto si quitamos el prefijo podemos "soportar algo" elementos
        // no XHTML en documentos X/HTML. Al menos que no de error.

        String localName = nodeElem.getLocalName();
        return super.createElement(nodeElem, localName,clientDoc);
    }
}
