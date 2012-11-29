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

package org.itsnat.impl.core.browser;

import java.util.Map;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.html.HTMLElement;

/**
 * En un applet Batik el user agent es configurable pero el que propone
 * por defecto tiene el formato "Batik/..."
 * Por ejemplo el applet envía por defecto (se puede cambiar):
 *  "Batik/development.build"
 *
 * @author jmarranz
 */
public class BrowserBatik extends BrowserW3C
{

    /** Creates a new instance of BrowserUnknown */
    public BrowserBatik(String userAgent)
    {
        super(userAgent);

        this.browserType = BATIK;
    }

    public static boolean isBatik(String userAgent)
    {
        return (userAgent.indexOf("Batik") != -1);
    }

    public boolean isMobile()
    {
        return false;
    }

    public boolean hasBeforeUnloadSupport(ItsNatStfulDocumentImpl itsNatDoc)
    {
        return false;
    }

    public boolean isReferrerReferenceStrong()
    {
        return false;
    }

    public boolean isCachedBackForward()
    {
        // No hay back/forward
        return false;
    }

    public boolean isCachedBackForwardExecutedScripts()
    {
        // No hay back/forward
        return false;
    }

    public boolean isDOMContentLoadedSupported()
    {
        // window ni siquiera es EventTarget en Batik applet
        return false;
    }

    public boolean isBlurBeforeChangeEvent(HTMLElement formElem)
    {
        return false; // Por poner algo, Batik no renderiza HTML embebido en SVG
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        return false; // Por poner algo, Batik no renderiza HTML embebido en SVG
    }

    public Map getHTMLFormControlsIgnoreZIndex()
    {
        return null;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return false; // Por poner algo, Batik no renderiza HTML embebido en SVG
    }

    public boolean isSetTimeoutSupported()
    {
        return true;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return false; // Por poner algo, Batik no renderiza documentos HTML
    }

    public boolean isTextAddedToInsertedHTMLScriptNotExecuted()
    {
        return false; // Por poner algo.
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        // No se ejecuta de ninguna forma
        return true;
    }

    public boolean isTextAddedToInsertedSVGScriptNotExecuted()
    {
        // No se ejecuta de ninguna forma
        return true;
    }

    public boolean isClientWindowEventTarget()
    {
        return false; // No en Batik applet.
    }

    public boolean isNeededAbsoluteURL()
    {
        // El XMLHttpRequest lo necesita
        return true;
    }
}
