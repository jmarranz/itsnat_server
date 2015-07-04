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
package org.itsnat.impl.core.clientdoc.web;

import java.io.Serializable;
import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class SVGWebInfoImpl implements Serializable
{
    protected ClientDocumentStfulDelegateWebImpl clientDoc;
    protected boolean forceFlash;
    protected int metaForceFlashPos;

    public SVGWebInfoImpl(ClientDocumentStfulDelegateWebImpl clientDoc,boolean forceFlash,int metaForceFlashPos)
    {
        this.clientDoc = clientDoc;
        this.forceFlash = forceFlash;
        this.metaForceFlashPos = metaForceFlashPos;
    }

    public boolean isForceFlash()
    {
        return forceFlash;
    }

    public boolean hasMetaForceFlash()
    {
        return metaForceFlashPos >= 0;
    }

    public int getMetaForceFlashPos()
    {
        return metaForceFlashPos;
    }

    public boolean isUsingSVGWebFlash()
    {
        if (isForceFlash()) return true;

        BrowserWeb browser = clientDoc.getBrowserWeb();
        if (browser instanceof BrowserW3C)
        {
            // En navegadores con SVG nativo (los soportados por SVGWeb menos MSIE)
            // y forceFlash desactivado al final el SVG es procesado nativamente
            // por lo que el envolver con <script type="image/svg+xml"> no sirve
            // para nada pues el SVGWeb "desenvolverá" de nuevo el SVG contenido
            // quitando el <script>. Por tanto evitamos que el SVGWeb actúe
            // en este caso pues no aporta nada.
            return false;
        }
        else
        {
            // En MSIE el SVG es siempre procesado por SVGWeb con Flash, da igual forceFlash (que es más bien
            // una opción para los navegadores con SVG nativo)
            return true;
        }
    }

    public boolean isSVGRootElementProcessedBySVGWebFlash(Element elem)
    {
        if (!isUsingSVGWebFlash())
            return false; // Aunque esté declarado como procesable por SVGWeb si el flash no es forzado y no se necesita (navegadores W3C) entonces no es procesado por el Flash de SVGWeb

        // Si es el nodo raíz de un fragmento de SVG en línea dentro del X/HTML
        if (!NamespaceUtil.isSVGRootElement(elem))
            return false; // No es SVG o no es raíz, hay otros SVG por encima

        String svgEngine = elem.getAttributeNS(NamespaceUtil.ITSNAT_NAMESPACE,"svgengine");
        if (svgEngine.equals("svgweb")) return true;

        svgEngine = elem.getAttribute("svgengine");
        if (svgEngine.equals("svgweb")) return true;

        return false; // Sólo se procesa si se ha declarado
    }

    public boolean isSVGNodeProcessedBySVGWebFlash(Node node)
    {
        if (!isUsingSVGWebFlash())
            return false; // Aunque esté declarado como procesable por SVGWeb si el flash no es forzado y no se necesita (navegadores W3C) entonces no es procesado por el Flash de SVGWeb

        Element rootElem = NamespaceUtil.getSVGRootElement(node);
        if (rootElem == null) return false; // No es SVG el nodo
        return isSVGRootElementProcessedBySVGWebFlash(rootElem);
    }

    public static boolean isSVGRootElementProcessedBySVGWebFlash(Element elem,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        SVGWebInfoImpl svgWebInfo = clientDoc.getSVGWebInfo();
        if (svgWebInfo == null) return false;
        return svgWebInfo.isSVGRootElementProcessedBySVGWebFlash(elem);
    }

    public static boolean isSVGNodeProcessedBySVGWebFlash(Node node,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        SVGWebInfoImpl svgWebInfo = clientDoc.getSVGWebInfo();
        if (svgWebInfo == null) return false;
        return svgWebInfo.isSVGNodeProcessedBySVGWebFlash(node);
    }

    public static boolean isSVGWebEnabled(ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        SVGWebInfoImpl svgWebInfo = clientDoc.getSVGWebInfo();
        return (svgWebInfo != null);
    }
}
