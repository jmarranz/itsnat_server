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

import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLSelectElement;

/*
   Sólo soportados DESDE S60 5th v1.0
  
    - User agents (varios simuladores probados, primera versión: S60 3rd FP 1)
        S60 3rd Feature Pack 1: Mozilla/5.0 (SymbianOS/9.2; U; [en]; Series60/3.1 Nokia3250/1.00 ) Profile/MIDP-2.0 Configuration/CLDC-1.1; AppleWebKit/413 (KHTML, like Gecko) Safari/413
        S60 3rd Feature Pack 2: Mozilla/5.0 (SymbianOS/9.2; U; [en]; Series60/3.2 Nokia3250/1.00; Profile/MIDP-2.1 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413
        S60 5th v0.9:           Mozilla/5.0 (SymbianOS/9.2; U; Series60/5.0 Nokia3250/1.00; Profile/MIDP-2.1 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413
        S60 5th v1.0:           Mozilla/5.0 (SymbianOS/9.2; U; Series60/5.0 Nokia3250/1.00; Profile/MIDP-2.1 Configuration/CLDC-1.1 ) AppleWebKit/525 (KHTML, like Gecko) Version/3.0 Safari/525

      He encontrado en www.botsvsbrowsers.com user-agents de una versión "previa" a las anteriores, pero no hay ningún emulador con ella:
        Mozilla/5.0 (SymbianOS/9.1; U; [en]; NokiaN73-1/3.0704.1.0.1 Series60/3.0) AppleWebKit/413 (KHTML, like Gecko) Safari/413
      Lo confirma: http://wiki.forum.nokia.com/index.php/User-Agent_headers_for_Nokia_devices#OSS_Browser_.28Web_browser.29_-_HTML.2C_XHTML-MP

    Como se puede ver el WebKit es en teoría el mismo (413) en las primeras versiones sin embargo hay algunas diferencias en comportamiento
    por ejemplo el filtrado de comentarios. Por lo que parece Nokia ha cogido el WebKit 413 y lo evoluciona
    a su manera.

    Por tanto la forma de detectar las primeras versiones es a través de "Series60/x.x"

    Sobre versiones de S60: http://en.wikipedia.org/wiki/S60_platform

 */

public class BrowserWebKitS60 extends BrowserWebKitSymbian
{
    protected float s60Version;

    public BrowserWebKitS60(String userAgent)
    {
        super(userAgent,S60WEBKIT);

        try
        {
            int start = userAgent.indexOf("Series60/");
            start += "Series60/".length();
            int end = start;
            while(true)
            {
                char c = userAgent.charAt(end);
                if (c == ' ')
                    break;
                end++;
            }
            String strVer = userAgent.substring(start,end);
            this.s60Version = Float.parseFloat(strVer);
        }
        catch(Exception ex) // Caso de user agent de formato desconocido
        {
            this.s60Version = 5.0f; // La mínima soportada
        }
    }

    public boolean isXHRSyncSupported()
    {
        return true;
    }

    public boolean isXHRPostSupported()
    {
        return true;
    }

    public boolean isDOMContentLoadedSupported()
    {
        // Redefinimos el método por defecto, en el S60WebKit 5th v1.0 con WebKit 525
        // TAMPOCO NO FUNCIONA el DOMContentLoaded, lo que rompe la regla del WebKit 525
        return false;
    }

    public boolean isFilteredCommentsInMarkup()
    {
        return false; 
    }

    public boolean isTextAddedToInsertedHTMLScriptNotExecuted()
    {
        return false; 
    }

    public boolean isOldEventSystem()
    {
        return false;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        // En principio no hay problema, el focus() es procesado, el problema es que
        // en cuanto el cursor deja el área el evento blur se lanza.
        // El problema es que en edición inplace puede haber una recolocación de
        // los elementos que hace al salir el control
        // de edición no salga debajo del cursor por lo que el blur se lanza.
        // Lo mejor por tanto es no ejecutar el focus().
        // El S60 5th v0.9 no tiene este problema pero por si acaso hacemos lo mismo, así evitamos
        // que salga automáticamente el editor de texto.
        return true;
    }

    public boolean isChangeNotFiredHTMLSelectWithSizeOrMultiple(HTMLSelectElement elem)
    {
        return false;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return false; // No soporta SVG por ejemplo.
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        return false; // Por poner algo pues SVG no está soportado
    }
}
