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

package org.itsnat.impl.core.browser.web;

import java.util.Map;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.html.HTMLElement;

/**
  Adobe SVG Viewer Plug-in desde la v3 (ASV3) incluida la v6 beta. Lo llamamos ASV para abreviar

  Adobe SVG Viewer:

  El plug-in SVG de Adobe está muy vinculado con el MSIE_OLD
  sando el mismo user agent y los mismos headers (de hecho comparte los cookies)
  y soporta AJAX a través del MSIE_OLD.
  Sin embargo a pesar de que usa el motor JavaScript del
  MSIE_OLD, el ASV3 es un navegador básicamente W3C.
  No hay posibilidad de detectar un request del ASV por lo que
  consideramos que es un ASV cuando se invoca un documento SVG desde el MSIE_OLD

 *
 * @author jmarranz
 */
public class BrowserAdobeSVG extends BrowserW3C
{
    /**
     * Creates a new instance of BrowserAdobeSVG
     */
    public BrowserAdobeSVG(String userAgent)
    {
        super(userAgent);

        this.browserType = ADOBE_SVG;
    }

    public boolean isMobile()
    {
        return false;
    }

    public boolean isReferrerReferenceStrong()
    {
        // No funciona la navegación desde el SVG por lo que nos
        // da igual los referrers.
        return false;
    }

    public boolean isCachedBackForward()
    {
        return false;
    }

    public boolean isCachedBackForwardExecutedScripts()
    {
        return false;
    }

    public boolean isDOMContentLoadedSupported()
    {
        return false;
    }

    public boolean isBlurBeforeChangeEvent(HTMLElement formElem)
    {
        return false;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        return false;
    }

    public Map<String,String[]> getHTMLFormControlsIgnoreZIndex()
    {
        // No reconoce XHTML embebido.
        return null;
    }

    public boolean hasHTMLCSSOpacity()
    {
        // Sólo SVG
        return false;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return false; // Renderiza SVG pero no el propio XHTML.
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        // En ASV (ambas v3 y v6) no funciona ni insertado antes ni después
        return true;
    }

    public boolean isTextAddedToInsertedSVGScriptNotExecuted()
    {
        return true;
    }

    public boolean isClientWindowEventTarget()
    {
        return false;
    }
}
