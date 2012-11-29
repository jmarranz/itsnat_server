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
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 *
 * @author jmarranz
 */
public class JSRenderSVGTextSVGWebImpl extends JSRenderOtherNSTextImpl
{
    public static final JSRenderSVGTextSVGWebImpl SINGLETON = new JSRenderSVGTextSVGWebImpl();

    /**
     * Creates a new instance of JSRenderOtherNSTextImpl
     */
    public JSRenderSVGTextSVGWebImpl()
    {
    }

    protected String createNodeCode(Node node,ClientDocumentStfulImpl clientDoc)
    {
        // Redefinimos y usamos el createTextNode especial con segundo parámetro
        // que exige SVGWeb
        Text nodeText = (Text)node;
        return "itsNatDoc.doc.createTextNode(" + dataTextToJS(nodeText,clientDoc) + ",true)";
    }
}
