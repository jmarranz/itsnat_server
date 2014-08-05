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

package org.itsnat.impl.core.scriptren.jsren.dom.node.otherns;

import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.web.SVGWebInfoImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.JSRenderAttributeImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderOtherNSAttributeImpl extends JSRenderAttributeImpl
{

    /**
     * Creates a new instance of JSRenderOtherNSAttributeImpl
     */
    public JSRenderOtherNSAttributeImpl()
    {
    }

    public static JSRenderOtherNSAttributeImpl getJSRenderOtherNSAttribute(Element elem,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        if (SVGWebInfoImpl.isSVGNodeProcessedBySVGWebFlash(elem,clientDoc))
            return JSRenderSVGAttributeSVGWebImpl.SINGLETON;
        else
        {
            // MSIE no reconoce elementos con namespaces ni siquiera en XHTML, pero aún asi lo renderizamos, podría ser un atributo especial ItsNat por ejemplo
            BrowserWeb browser = clientDoc.getBrowserWeb();
            if (browser instanceof BrowserMSIEOld)
                return JSRenderOtherNSAttributeMSIEOldImpl.SINGLETON;
            else
                return JSRenderOtherNSAttributeW3CImpl.SINGLETON;
        }
    }
}
