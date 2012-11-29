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

package org.itsnat.impl.core.browser.webkit;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLSelectElement;

/*
 * User agents:

    - Motorola Symphony 1.1.0
        Mozilla/5.0 (compatible; OSS/1.0; Chameleon; Linux) Pearl/ MotoWebKit/417.19 (KHTML, like Gecko) BER/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 EGE/9.0

 */

public class BrowserWebKitMoto extends BrowserWebKit
{
    private static final Map tagNamesIgnoreZIndex = new HashMap();
    static
    {
        // El panorama es el siguiente:
        // La opacidad se ignora.
        // Los select,input y textarea ignoran el z-index
        // Los elementos <button> y <a> también ignoran el z-index si
        // el background es transparente (la técnica de imagen transparente no funciona).
        // por lo que conviene definir un color de background, por ejemplo blanco.
        // Esto no impide que los elementos ocultos sean alcanzables y pulsables
        // por los cursores, pero esto no se puede evitar en ningún navegador.

        tagNamesIgnoreZIndex.put("select",null);
        tagNamesIgnoreZIndex.put("input",null);
        tagNamesIgnoreZIndex.put("textarea",null);
    }

    public BrowserWebKitMoto(String userAgent)
    {
        super(userAgent,MOTOWEBKIT);
    }

    public boolean isMobile()
    {
        return true;
    }

    public boolean isFilteredCommentsInMarkup()
    {
        // El WebKit es muy antiguo, anterior al Safari desktop 3.0
        return true;
    }

    public boolean hasBeforeUnloadSupportHTML()
    {
        return false;
    }

    public boolean isXHRSyncSupported()
    {
        // WebKit antiguo
        return false;
    }

    public boolean isXHRPostSupported()
    {
        // MotoWebKit soporta POST sin embargo el WebKit es muy antiguo y el soporte
        // de POST se introdujo con posterioridad. El caso es que tras un cierto número
        // de request se produce un status "undefined" en el XHR, lo peor es que acaba dejando el navegador
        // bloqueado y deja de procesar eventos. Por tanto usamos GET que parece que funciona MUCHO mejor.
        return false;
    }

    public boolean isChangeNotFiredHTMLSelectWithSizeOrMultiple(HTMLSelectElement elem)
    {
        return false;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        return false;
    }

    public boolean isAJAXEmptyResponseFails()
    {
        return (webKitVersion <= 420); // Por si acaso
    }

    public Map getHTMLFormControlsIgnoreZIndex()
    {
        return tagNamesIgnoreZIndex;
    }

    public boolean hasHTMLCSSOpacity()
    {
        // Por lo menos en modo transparente
        return false;
    }

    public boolean isOldEventSystem()
    {
        return true;
    }

    public boolean isSetTimeoutSupported()
    {
        return true;
    }

    public boolean isTextAddedToInsertedHTMLScriptNotExecuted()
    {
        return true;
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        // El SVG funciona penosamente.
        return true; // Por poner algo (idem que isTextAddedToInsertedHTMLScriptNotExecuted)
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return true; // Aunque el SVG funciona penosamente
    }

}
