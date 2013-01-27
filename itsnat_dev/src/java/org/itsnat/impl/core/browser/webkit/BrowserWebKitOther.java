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

import java.util.Map;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLSelectElement;

/*
 * Los demás navegadores menos Android, iPhone y navegadores Symbian nativos,
 * es decir: Safari, Chrome
 *
 *
 * User agents:

    - Safari 3.0.4 Mac (AppleWebKit/523.15.1) Safari 2.x no está soportado.
        Mozilla/5.0 (Macintosh; U; Intel Mac OS X;en-us) AppleWebKit/523.15.1 (KTHTML, like Gecko) Version/3.0.4 Safari/523.15

    - Safari 3.1 Windows (AppleWebKit/525.13)
        Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES) AppleWebKit/525.13 (KHTML, like Gecko) Version/3.1 Safari/525.13

    - Safari 4 beta Windows (AppleWebKit/528.16)
        Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES) AppleWebKit/528.16 (KHTML, like Gecko) Version/4.0 Safari/528.16

    - Google Chrome (1.0.154.48)
        Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.19 (KHTML, like Gecko) Chrome/1.0.154.48 Safari/525.19

    - BlackBerry JDE 6.0, desde esta versión está basado en WebKit
        Mozilla/5.0 (BlackBerry; U; BlackBerry 9800; en-GB) AppleWebKit/534.1+ (KHTML, like Gecko) Version/6.0.0.141 Mobile Safari/534.1+


 */

public class BrowserWebKitOther extends BrowserWebKit
{
    public BrowserWebKitOther(String userAgent,int browserSubType)
    {
        super(userAgent,browserSubType);
    }

    public boolean isMobile()
    {
        if (browserSubType == BLACKBERRY) return true;
        return false;
    }

    public boolean isFilteredCommentsInMarkup()
    {
        return false;
    }

    public boolean hasBeforeUnloadSupportHTML()
    {
        switch(browserSubType)
        {
            case SAFARIDESKTOP :
            case GCHROME:
                return true;
        }

        // Curiosamente iPhone no soporta beforeunload en HTML (menos aun SVG que no lo soporta)
        return false;
    }

    public boolean isXHRSyncSupported()
    {
        /* Si soporta request síncronos el XMLHttpRequest
         * No está soportado normalmente en WebKits antiguos y algunas versiones móviles.
         */
        return true;
    }


    public boolean isXHRPostSupported()
    {
        return true;
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
        // El retorno vacío puede dejar
        // el motor AJAX en un estado erróneo más allá del request (hay que recargar la página)
        // Esto ha sido detectado en el ejemplo "Event Monitor" del Feature Showcase
        // No se han detectado más casos, sin embargo el iPhone "real" con firmware antiguo (420+)
        // no ha sido testeado así que por si acaso retornamos espacios en ese caso
        // y  nos "curamos en salud"
        // Nota: estas pruebas se han hecho en modo compresión con Gzip

        if (webKitVersion <= 420) return true; // Por si acaso

        return false;
    }

    public Map getHTMLFormControlsIgnoreZIndex()
    {
        return null;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return true;
    }

    public boolean isOldEventSystem()
    {
        return false;
    }

    public boolean isSetTimeoutSupported()
    {
        return true;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return true;
    }

    public boolean isTextAddedToInsertedHTMLScriptNotExecuted()
    {
        return false;
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        switch(browserSubType)
        {
            case SAFARIDESKTOP:
            case GCHROME:
                // Ni la versión 3 (3.1 WebKit 525.13) al menos de Safari desktop ni el Chrome 1.0 (WebKit 525.19)
                // ejecutan el texto dentro de <script> SVG, ni dentro del <script>
                // antes de insertar, ni añadido después.
                // Sin embargo en Chrome 2.0 (WebKit 530) y Safari 4 (531.9) funciona bien en ambos casos,
                // luego devolvemos false (no hacer nada).
                return (webKitVersion <= 525);
            case BLACKBERRY:
                // El simulador del JDE 5.0 9800 renderiza SVG pero NO se ejecutan los <script>
                // imagino que cuando se active JavaScript funcionará correctamente pues la versión
                // primera del WebKit no es nada antigua (534.1+)
                return false;
        }

        return false; // Por si se me pasa algún navegador
    }

    public boolean isReferrerReferenceStrong()
    {
        if (browserSubType == BLACKBERRY) return true; // Idem que las BlackBerryOld 
        else return super.isReferrerReferenceStrong();
    }
    
    public boolean isCachedBackForward()
    {
        if (browserSubType == BLACKBERRY) return true; // Idem que las BlackBerryOld 
        else return super.isCachedBackForward();
    }

    public boolean isCachedBackForwardExecutedScripts()
    {
        if (browserSubType == BLACKBERRY) return true; // Idem que las BlackBerryOld 
        else return super.isCachedBackForwardExecutedScripts();
    }
}
