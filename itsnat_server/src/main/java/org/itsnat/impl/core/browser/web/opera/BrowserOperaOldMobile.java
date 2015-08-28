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

package org.itsnat.impl.core.browser.web.opera;

import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.html.HTMLElement;

/**
 * Ahora se llama Opera Mobile Classic pensada para Android 2.3
 * 
 * Soportado partir de la versión 12.10 (indicada en "Version/Num") que es la versión del emulador bajado el 28 de enero de 2012 y según parece 
 * ya está congelada para siempre
 * 
 * User agent: Opera/9.80 (Android 2.3.7; Linux; Opera Tablet/46154) Presto/2.11.355 Version/12.10
 * 
 * Más user-agents: http://www.useragentstring.com/pages/Opera%20Mobile/
 * 
    El Opera Mobile 10 sigue conteniendo la cadena "Opera/9."
    para evitar problemas en webs por cambio de valor a dos dígitos (9=>10)
    concretamente es: Opera/9.80
    Teniendo en cuenta que no existe Opera Mobile 9.8 podemos
    considerar Opera Mobile 10 como la v9.8 (subVersion será 80)  
 * 
 * @author jmarranz
 */
public class BrowserOperaOldMobile extends BrowserOperaOld
{
    /**
     * Creates a new instance of BrowserOperaOldMobile
     */
    public BrowserOperaOldMobile(String userAgent)
    {
        super(userAgent);

        this.browserSubType = OPERA_OLD_MOBILE;
    }

    public static boolean isOperaOldMobile(String userAgent)
    {
        // "Opera Mobi" ya no está siempre, puede ser "Opera Tablet", el sistema operativo es el que decide, Android
        return (userAgent.contains("Android"));
    }

    @Override
    public boolean isMobile()
    {
        return true;
    }

    @Override
    public boolean isCachedBackForwardExecutedScripts()
    {
        return true;
    }

    @Override
    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        // La llamada a focus() es ignorada en los input type=text, suponemos lo mismo en "password" y "file"
        // Si el focus() es ignorado un posterior blur() lo sería
        return DOMUtilHTML.isHTMLInputTextBased(formElem);
    }
}
