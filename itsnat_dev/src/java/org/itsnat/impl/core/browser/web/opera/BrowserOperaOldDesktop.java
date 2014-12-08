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

import org.w3c.dom.html.HTMLElement;

/**
 * Soportado desde la versión 12.17 que es la última pues parece que ya no se actualiza más
 *
 * Ejemplos user agent: (parece que "Presto" se introdujo en 9.6 según listado de botsvsbrowsers.com)
 *
 *   Opera/9.80 (Windows NT 5.1) Presto/2.12.388 Version/12.17
     Opera/9.80 (Windows NT 5.1) Presto/2.12.388 Version/12.12
      
    Antiguas:
 *  "Opera/9.26 (Windows NT 5.1; U; en)"
 *  "Opera/9.63 (Windows NT 5.1; U; en) Presto/2.1.1"
 *  Opera 10: "Opera/9.80 (Windows NT 5.1; U; en) Presto/2.2.15 Version/10.00"
 *            Notar que no aparece el "10" esto es para evitar confundir a algunos "browser sniffers" que lo detectarían como una versión antigua (la v1 seguramente)
 *
 * @author jmarranz
 */
public class BrowserOperaOldDesktop extends BrowserOperaOld
{
    /**
     * Creates a new instance of BrowserOperaDesktop
     */
    public BrowserOperaOldDesktop(String userAgent)
    {
        super(userAgent);

        this.browserSubType = OPERA_OLD_DESKTOP;
    }

    public boolean isMobile()
    {
        return false;
    }

    public boolean isCachedBackForwardExecutedScripts()
    {
        // Hagas lo que hagas Opera 9 no recarga la página ante un back/forward
        // (ignora headers etc), por ahora parece innegociable:
        // http://my.opera.com/yngve/blog/2007/02/27/introducing-cache-contexts-or-why-the
        // Al menos he conseguido que el evento onload se emita enviando:
        // http://www.experts-exchange.com/Programming/Languages/Scripting/JavaScript/Q_21907326.html

        return true;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        return false;
    }

}
