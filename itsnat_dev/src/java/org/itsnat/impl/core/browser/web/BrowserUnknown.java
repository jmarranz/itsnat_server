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
import org.w3c.dom.html.HTMLElement;

/**
 * Normalmente será el caso de un robot, los robots no suelen interpretar
 * JavaScript y por tanto no tienen eventos.
 * No se guardará el documento en el servidor (como si fuera AJAX disabled).
 *
 * Suponemos que es W3C respecto a todo lo demás
 *
 * @author jmarranz
 */
public class BrowserUnknown extends BrowserW3C
{

    /** Creates a new instance of BrowserUnknown */
    public BrowserUnknown(String userAgent)
    {
        super(userAgent);

        this.browserType = UNKNOWN;
    }

    public boolean isMobile()
    {
        return false;
    }

    public boolean isReferrerReferenceStrong()
    {
        // Si es un robot evitamos guardar referencias fuertes.
        return false;
    }

    public boolean isCachedBackForward()
    {
        // Es un robot, nos da igual.
        return false;
    }

    public boolean isCachedBackForwardExecutedScripts()
    {
        // Si es un robot no tendrá mucho sentido hablar de back/forward
        // pero devolviendo false evitamos la generación de código de recarga etc
        return false;
    }

    public boolean isDOMContentLoadedSupported()
    {
        // Nos da igual pues no hay eventos.
        return false;
    }

    public boolean isBlurBeforeChangeEvent(HTMLElement formElem)
    {
        return false; // Es el comportamiento más normal (aunque realmente nos da igual)
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        return false;
    }

    public Map<String,String[]> getHTMLFormControlsIgnoreZIndex()
    {
        return null;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return true;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return true; // Por poner algo.
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        return false; // Por poner algo
    }

    public boolean isTextAddedToInsertedSVGScriptNotExecuted()
    {
        return false; // Por poner algo
    }

    public boolean isClientWindowEventTarget()
    {
        return true; // Por poner algo
    }
}
