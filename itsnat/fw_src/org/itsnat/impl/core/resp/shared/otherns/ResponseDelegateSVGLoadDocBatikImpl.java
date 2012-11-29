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

package org.itsnat.impl.core.resp.shared.otherns;

import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateSVGLoadDocBatikImpl extends ResponseDelegateSVGLoadDocImpl
{
    /**
     * Creates a new instance of ResponseDelegateSVGLoadDocBatikImpl
     */
    public ResponseDelegateSVGLoadDocBatikImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    protected void rewriteClientUIControlProperties(Element elem,boolean revertJSChanges,StringBuffer code)
    {
        // SVG no tiene controles propios.
        // NO llamamos a rewriteClientHTMLUIControlProperties porque
        // Batik por ahora no hace nada con los elementos XHTML dentro de <foreignObject>
        // http://mail-archives.apache.org/mod_mbox/xmlgraphics-batik-dev/200805.mbox/%3C20080508041459.GB640@arc.mcc.id.au%3E
        // http://mail-archives.apache.org/mod_mbox/xmlgraphics-batik-dev/200805.mbox/%3C20080508041924.GC640@arc.mcc.id.au%3E
    }

    public void dispatchRequestListeners()
    {
        fixApplet();

        super.dispatchRequestListeners();
    }

    protected void fixApplet()
    {
        addFixDOMCodeToSend("Packages.ItsNatBatikAppletScriptUtil.fix(window);\n");
    }

}
