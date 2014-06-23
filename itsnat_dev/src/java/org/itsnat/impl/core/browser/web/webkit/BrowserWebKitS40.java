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

import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLSelectElement;

/*
 *   S40WebKit desde S40 6th Edition
 * 
     User agent (simulador S40 6th Edition SDK v0.9, la primera versión)
        Mozilla/5.0 ( Nokia6600s/2.0 (04.08) Profile/MIDP-2.1 Configuration/CLDC-1.1) AppleWebKit/420+ (KHTML, like Gecko) Safari/420+

        Nota: la "s" es de "slide" por el modelo Nokia 6600 slide: http://europe.nokia.com/A41005068
        Más info: http://mobiledeviceinfo.com/device/details/nokia_6600s_ver1
 */

public class BrowserWebKitS40 extends BrowserWebKitSymbian
{
    public BrowserWebKitS40(String userAgent)
    {
        super(userAgent,S40WEBKIT);
    }

    public boolean isXHRSyncSupported()
    {
        return false;
    }

    public boolean isXHRPostSupported()
    {
        // Por lo menos en el emulador
        return false;
    }

    public boolean isChangeNotFiredHTMLSelectWithSizeOrMultiple(HTMLSelectElement elem)
    {
        // Nota: elem puede ser null.

        // El S40WebKit lanza el change cuando se ejecuta el blur (se pincha fuera del componente)
        // el problema es que NO siempre se lanza.

        if (elem == null) return true;

        return elem.hasAttribute("multiple") ||
               elem.hasAttribute("size");
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
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
    
    public boolean isChangeEventNotFiredUseBlur(HTMLElement formElem)
    {
        return false; // Cosa del Chrome
    }          
}
