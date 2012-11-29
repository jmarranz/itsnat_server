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
 * Los demás navegadores menos Android, iPhone, MotoWebKit, Bolt y navegadores Symbian nativos,
 * es decir: Safari, Chrome, Iris, QtWebKit, Adobe Air, Palm Pre
 *
 *
 * User agents:

    - Safari 3.0.4 Mac (AppleWebKit/523.15.1) Safari 2.x no está soportado.
        Mozilla/5.0 (Macintosh; U; Intel Mac OS X;en-us) AppleWebKit/523.15.1 (KTHTML, like Gecko) Version/3.0.4 Safari/523.15

    - Safari 3.1 Windows (AppleWebKit/525.13)
        Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES) AppleWebKit/525.13 (KHTML, like Gecko) Version/3.1 Safari/525.13

    - Safari 4 beta Windows (AppleWebKit/528.16)
        Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES) AppleWebKit/528.16 (KHTML, like Gecko) Version/4.0 Safari/528.16

    - Iris Browser 1.0.8 (Windows Mobile)
        v1.0.8: Mozilla/5.0 (Windows NT; U; Mobile; en) AppleWebKit/420+ (KHTML, like Gecko) WM5 Iris/1.0.8 Safari/419.3
        v1.1.3: Mozilla/5.0 (Windows NT; U; en) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Iris/1.1.3 Safari/525.20
        v1.1.7: Mozilla/5.0 (Windows NT; U; en) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Iris/1.1.7 Safari/525.20
        v1.1.9: Mozilla/5.0 (Windows NT; U; en) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Iris/1.1.9 Safari/525.20
 * 
    - QtWebKit (Windows salvo que se diga lo contrario)
        Desktop v4.4: Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES) AppleWebKit/523.15 (KHTML, like Gecko, Safari/419.3) demobrowser/0.1
        QtJambi v4.4: Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en) AppleWebKit/523.15 (KHTML, like Gecko) Safari/419.3 Qt
        Embedded Linux (v4.4.1): Mozilla/5.0 (QtEmbedded; N; Linux; en-US) AppleWebKit/523.15 (KHTML, like Gecko, Safari/419.3) demobrowser/0.1
        Embedded WinCE (no oficial todavía): Mozilla/5.0 (Windows; N; Unknown; C -) AppleWebKit/527+ (KHTML, like Gecko, Safari/419.3) QtLauncher/0.1
        Arora 0.5:   Mozilla/5.0 (Windows; U; Windows NT 5.1; C -) AppleWebKit/527+ (KHTML, like Gecko, Safari/419.3) Arora/0.5 (Change: )
        Desktop v4.5.3: Mozilla/5.0 (Windows; N; Windows NT 5.1; es-ES) AppleWebKit/527+ (KHTML, like Gecko, Safari/419.3) demobrowser/0.1

    - Google Chrome (1.0.154.48)
        Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.19 (KHTML, like Gecko) Chrome/1.0.154.48 Safari/525.19

    - Adobe Air HTML Component (Adobe Air v1.5.1)
        Mozilla/5.0 (Windows; U; es-ES) AppleWebKit/526.9+ (KHTML, like Gecko) AdobeAIR/1.5.1

    - SWTWebKitBrowser 0.5 http://www.genuitec.com/about/labs.html
        Por ahora no podemos identificarlo.
        Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/530.0 (KHTML, like Gecko) Version/3.2.1 Safari/530.0

    - Palm Pre (WebOS) Mojo SDK 0.3.3
        Mozilla/5.0 (webOS/1.0; U; en-US) AppleWebKit/525.27.1 (KHTML, like Gecko) Version/1.0 Safari/525.27.1 Pre/1.0

    - BlackBerry JDE 6.0, desde esta versión está basado en WebKit
        Mozilla/5.0 (BlackBerry; U; BlackBerry 9800; en-GB) AppleWebKit/534.1+ (KHTML, like Gecko) Version/6.0.0.141 Mobile Safari/534.1+

    - Vienna RSS reader user agent (NO SOPORTADO)
        Vienna/2.1.0.2102 (Mac OS X; http://vienna-rss.sourceforge.net)


  * Algunos análisis:

    - Iris Browser (AppleWebKit/420+) => misma versión de webkit que el iPhone real pero diferente plataforma

        DOMContentLoaded : NO
        beforeunload : en teoría admite beforeunload pero como no admite eventos síncronos el caso es que destruye el request al dejar la página NO llegando al servidor.
        Unload AJAX: NO, por la misma razón que el beforeunload, no llega al servidor
        Unload Guarantied : NO (ni funciona el unload)
        Cached Back/Forward : NO
        Eventos síncronos: NO
        AJAX POST form encoded: estudiar (a lo mejor es el error status = 0)
        SVG: en teoría Iris soporta SVG pero en Windows Mobile no lo soporta
 */

public class BrowserWebKitOther extends BrowserWebKit
{
    public BrowserWebKitOther(String userAgent,int browserSubType)
    {
        super(userAgent,browserSubType);
    }

    public boolean isMobile()
    {
        switch(browserSubType)
        {
            case IRIS:
            case WEBOS:
            case BLACKBERRY:
                    return true;
        }
        // HACER: debería determinarse el caso de QTWEBKIT que es "mobile"
        return false;
    }

    public boolean isFilteredCommentsInMarkup()
    {
        return false;
    }

    public boolean hasBeforeUnloadSupportHTML()
    {
        // Iris admite beforeunload pero no llega al servidor por lo que consideramos que no se soporta
        switch(browserSubType)
        {
            case SAFARIDESKTOP :
            case QTWEBKIT:
            case GCHROME:
            case WEBOS: 
            //case SWTWEBKITBROWSER:
                return true;
        }

        // Curiosamente ADOBEAIR no soporta beforeunload en HTML (menos aun SVG que no lo soporta)
        // tampoco iPhone.
        return false;
    }

    public boolean isXHRSyncSupported()
    {
        /* Si soporta request síncronos el XMLHttpRequest
         * No está soportado normalmente en WebKits antiguos y algunas versiones móviles.
         */
        switch(browserSubType)
        {
            case IRIS: // Yo creo que la versión más nueva sí lo soporta
            case WEBOS: // Existe el soporte pero está desactivado: https://prerelease.palm.com/clearspace_community/message/1214#1214
                    return false;
        }
        // El QtWebKit el único evento que no soporta síncrono es el "unload"
        return true;
    }

    public boolean canSendXHRSyncUnload()
    {
        /* El QtWebKit se cuelga al enviar el unload por XHR síncrono
           pues por ejemplo también se cuelga con un simple alert ejecutado en el unload (sin AJAX).
           Ocurre también en QtWebKit Embedded Linux 4.4.1 y en Win CE (no oficial) usando un WebKit más moderno (527+).
           En definitiva evitar cualquier proceso largo síncrono al procesar el evento unload
         */
        if (browserSubType == QTWEBKIT)
            return false;  // En modo asíncrono se puede enviar pero total no llega y QTWEBKIT admite AJAX síncrono

        return super.canSendXHRSyncUnload();
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
        // El caso de Iris 1.0.8 (AppleWebKit/420+) es más raro porque un simple
        // espacio NO vale son necesarios al menos 10, por lo menos en el caso del "Event Monitor" del Feature Showcase,
        // (fuera de este ejemplo no he podido reproducirlo), de otra manera se cuelga el motor AJAX.
        // No se han detectado más casos, sin embargo el iPhone "real" con firmware antiguo (420+)
        // no ha sido testeado así que por si acaso retornamos espacios en ese caso
        // y  nos "curamos en salud"
        // Nota: estas pruebas se han hecho en modo compresión con Gzip
        switch(browserSubType)
        {
            case IRIS:
                    return true;
        }
        if (webKitVersion <= 420) return true; // Por si acaso

        return false;
    }

    public Map getHTMLFormControlsIgnoreZIndex()
    {
        return null;
    }

    public boolean hasHTMLCSSOpacity()
    {
        switch(browserSubType)
        {
            case IRIS: // La versión 1.1.3 parece que sí pero no lo hace del todo bien
            case WEBOS:
                   return false;
        }

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
        switch(browserSubType)
        {
            case IRIS:
                // La primera versión no soporta SVG, no se cuando se soportó SVG
                // pero en la 1.1.7 SVG funciona
                // En este caso consideramos sólo las últimas versiones (el markup SVG fallará obviamente en una versión antigua)
            case SAFARIDESKTOP:
            case GCHROME:
            case QTWEBKIT: // En la versión alpha Win Mobile que tengo no funciona el SVG, no consideramos dicho caso
            case BLACKBERRY:
                return true;
            case WEBOS: // No soporta SVG.
            case ADOBEAIR: // No soporta SVG.
                return false; 
        }

        return true; // Por si se me pasa algún navegador
    }

    public boolean isTextAddedToInsertedHTMLScriptNotExecuted()
    {
        return false;
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        switch(browserSubType)
        {
            case IRIS:
                // La primera versión no soporta SVG, no se cuando se soportó SVG
                // pero en la 1.1.7 SVG funciona y no hay problema con la inserción
                // de scripts siendo el WebKit un 525
                return false;
            case SAFARIDESKTOP:
            case GCHROME:
                // Ni la versión 3 (3.1 WebKit 525.13) al menos de Safari desktop ni el Chrome 1.0 (WebKit 525.19)
                // ejecutan el texto dentro de <script> SVG, ni dentro del <script>
                // antes de insertar, ni añadido después.
                // Sin embargo en Chrome 2.0 (WebKit 530) y Safari 4 (531.9) funciona bien en ambos casos,
                // luego devolvemos false (no hacer nada).
                return (webKitVersion <= 525);
            case QTWEBKIT:
                // En Qt 4.4 (WebKit 523.15) no es ejecutado
                // En Arora 0.5 y Qt 4.5.3 (WebKit 527+) SI es ejecutado
                // En la versión alpha Win Mobile que tengo no funciona el SVG.
                // Desconocemos el WebKit concreto de cambio por lo que nos basamos
                // en la casuística de Safari y Chrome en donde el WebKit 525 no ejecuta
                return (webKitVersion <= 525);
            case WEBOS:
                return false; // No soporta SVG, por poner algo.
            case ADOBEAIR:
                return false; // No soporta SVG, por poner algo.
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
