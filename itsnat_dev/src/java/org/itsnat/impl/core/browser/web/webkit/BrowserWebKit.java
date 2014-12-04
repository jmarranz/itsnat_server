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

package org.itsnat.impl.core.browser.web.webkit;

import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.doc.web.ItsNatHTMLDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.html.HTMLElement;

/**
   * De acuerdo con el sistema de versiones de WebKit
    (ejemplo: https://bugs.webkit.org/show_bug.cgi?id=17754, ver combo "Version")
    las últimas versiones importantes de WebKit son:

    413  usada por S60WebKit
    420+ parece ser la versión usada por el primer firmware iPhone
    523.x Safari 3.0
    525.x Safari 3.1
    528.x Safari 4 beta
    531.9 Safari 4.0.3
 
   Una tabla similar se encuentra en (no estoy de acuerdo en algunos casos):
   http://en.wikipedia.org/wiki/WebKit
 
 * @author jmarranz
 */
public abstract class BrowserWebKit extends BrowserW3C
{
    // Los números se alternan porque se han eliminado varios subtipos
    protected static final int SAFARIDESKTOP = 1;
    protected static final int IPHONE = 2;
    protected static final int ANDROID = 3;
    protected static final int GCHROME = 7;
    protected static final int BLACKBERRY = 12;

    protected int webKitVersion;

    /** Creates a new instance of BrowserWebKit */
    public BrowserWebKit(String userAgent,int browserSubType)
    {
        super(userAgent);

        this.browserType = WEBKIT;
        this.browserSubType = browserSubType;

        // Versión del WebKit:
        try
        {
            int start = userAgent.indexOf("WebKit/");
            start += "WebKit/".length();
            int end = start;
            while(true)
            {
                char c = userAgent.charAt(end);
                if ((c == '.') || (c == '+') || (c == ' '))
                    break;
                end++;
            }
            String strVer = userAgent.substring(start,end);
            this.webKitVersion = Integer.parseInt(strVer);
        }
        catch(Exception ex) // Caso de user agent de formato desconocido
        {
            this.webKitVersion = 0;
        }
    }

    public static BrowserWebKit createBrowserWebKit(String userAgent)
    {
        if (userAgent.indexOf("Chrome") != -1) // Debe chequearse antes que "Android" porque incluimos el Chrome de Android
            return new BrowserWebKitChrome(userAgent);                  
        else if (userAgent.indexOf("Android") != -1)
            return new BrowserWebKitAndroid(userAgent);
        else if ((userAgent.indexOf("iPod") != -1) ||
                 (userAgent.indexOf("iPhone") != -1) ||
                 (userAgent.indexOf("iPad") != -1))              
            return new BrowserWebKitIOS(userAgent);
        else
        {
            int browserSubType;
            if (userAgent.indexOf("BlackBerry") != -1) 
                browserSubType = BLACKBERRY;
            else
                browserSubType = SAFARIDESKTOP; // Safari Destkop o WebKit desconocido (suponemos Safari desktop)

            return new BrowserWebKitOther(userAgent,browserSubType);
        }
    }

    public static boolean isWebKit(String userAgent)
    {
        // Podría usarse "Safari" pero algún navegador antiguo no la tenía
        return (userAgent.indexOf("WebKit") != -1);
    }

    /* Si soporta request síncronos el XMLHttpRequest
     * No está soportado normalmente en WebKits antiguos, pues tardo tiempo en añadirse
     * y quizas no hay un código compartido para todas las plataformas:
        http://owb.sand-labs.org/pipermail/owb-dev/2007-November/000102.html
        https://bugs.webkit.org/show_bug.cgi?id=17754
        http://trac.webkit.org/projects/webkit/changeset/26875 (se ve el cambio de no implementado, notImplemented(), a una implementación, fecha: 2007-10-22)

       No hay otra forma de insertar un script externo (el añadir un <script src=...> dinámicamente carga el archivo asíncronamente)
     */
    public abstract boolean isXHRSyncSupported();

    public boolean canSendXHRSyncUnload()
    {
        // Se redefine en algún caso
        return isXHRSyncSupported();
    }

    /* POST es siempre preferible por ejemplo
     * para evitar posibles cacheos de la URL en el cliente.
     * WebKits antiguos pueden no soportarlo
     */
    public abstract boolean isXHRPostSupported();


    public boolean isReferrerReferenceStrong()
    {
        // El nuevo documento siempre se carga antes de que el anterior se destruya
        // salvo en BlackBerry (se redefine)
        return false;
    }

    public boolean isCachedBackForward()
    {
        // Pero en BlackBerry se redefine
        return false;
    }

    public boolean isCachedBackForwardExecutedScripts()
    {
        // Pero en BlackBerry se redefine
        return false;
    }

    @Override
    public boolean hasBeforeUnloadSupport(ItsNatStfulDocumentImpl itsNatDoc)
    {
        // El evento beforeunload fue introducido por MSIE, no es W3C, por tanto en SVG (cuando es soportado) es ignorado
        if (itsNatDoc instanceof ItsNatHTMLDocumentImpl)
            return hasBeforeUnloadSupportHTML();
        else
            return false; // Caso de SVG
    }
    
    public abstract boolean hasBeforeUnloadSupportHTML();


    public boolean isDOMContentLoadedSupported()
    {
        // Se propuso para Mac desde 420+ : https://bugs.webkit.org/show_bug.cgi?id=5122
        // pero al parecer fue introducido en Safari 3.1 Webkit 525 (Safari 3.0 por ejemplo no lo soporta)
        // http://dev.mootools.net/ticket/732
        // http://developer.yahoo.com/yui/docs/Env.js.html  Webkit nightly 1/2008:525+  Supports DOMContentLoaded event.
        // No he encontrado ningún navegador que contradiga esta regla del 525
        // salvo el S60WebKit 5th v1.0, se redefine en ese caso

        // Navegadores que han tenido versiones < 525:
        //    Safari,iPhone
        // Navegadores que NO han llegado a 525 : S40WebKit ya veremos si se cumple esta regla del 525.
        // Los demás navegadores (Android, Chrome, SWTWebKit) parten de WebKit superior a 525.
        return webKitVersion >= 525;
    }

    public boolean isBlurBeforeChangeEvent(HTMLElement formElem)
    {
        // Caso S60WEBKIT antiguos con WebKit 413 (en WebKit 525 funciona ya bien),
        // quizás se deba a que el WebKit es muy antiguo, 413 y 417 respectivamente y se decidió cambiarlo después.
        // Además S60WEBKIT lanza 2 blurs cuando debe ser uno sólo
        // El S40WebKit no tiene este problema pues empieza en 420
        // Consideramos el 420 como el primer WebKit sin este "fallo" pues
        // todos los demás navegadores soportados funcionan bien y tienen el 420 o mayor

        return (webKitVersion < 420) && DOMUtilHTML.isHTMLTextAreaOrInputTextBox(formElem);
    }

   
    public boolean isTextAddedToInsertedSVGScriptNotExecuted()
    {
        // Cuando la inserción del script funciona funciona bien
        // en los dos casos (añadiendo antes el código o después de insertado el elemento <script>).
        return isInsertedSVGScriptNotExecuted();
    }

    public boolean isClientWindowEventTarget()
    {
        return true;
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
        
    public abstract boolean isChangeEventNotFiredUseBlur(HTMLElement formElem);
}
