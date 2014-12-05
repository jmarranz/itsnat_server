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

import java.util.Map;
import org.w3c.dom.html.HTMLElement;

/**
 * Soportado al menos la versión 4
 *
 * Derivamos de BrowserOpera porque el motor en el servidor es a partir de Opera 9
 *
 * User agent: Opera/9.60 (J2ME/MIDP; Opera Mini/4.0.10406/538; U; en) Presto/2.2.0
 *
 * Limitaciones:
 *  http://dev.opera.com/articles/view/javascript-support-in-opera-mini-4/
 *  http://dev.opera.com/articles/view/evolving-the-internet-on-your-phone-des/
 *
 * Limitaciones detectadas por mi:
 * - La propiedad "data" de los nodos Text es sólo lectura aunque no da error al intentar cambiarla
 *
 * Depurar JavaScript con Opera Mini

   En Opera Mini el JavaScript se ejecuta en un servidor de Opera.
   Los alert() funcionan pero sólo puede ejecutarse uno, pues los scripts si tardan mucho se paran,
   cuando hay un alert el servidor de Opera da por acabado el script.
   Por ello es mejor depurar sacando visualmente información a la pantalla, por ejemplo:

    document.body.appendChild(document.createTextNode(TEXTO + " "));

 * @author jmarranz
 */
public class BrowserOperaOldMini extends BrowserOperaOld
{

    /** Creates a new instance of BrowserOperaMini */
    public BrowserOperaOldMini(String userAgent)
    {
        super(userAgent);

        this.browserSubType = OPERA_OLD_MINI;
    }

    public static boolean isOperaMini(String userAgent)
    {
        return (userAgent.indexOf("Opera Mini") != -1);
    }

    public boolean isMobile()
    {
        return true;
    }

    public boolean isCachedBackForwardExecutedScripts()
    {
        // Cuando se hace un Back ni siquiera se ejecuta el load
        return false;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        // El focus() se procesa (se genera evento) el problema es que el blur creo que no se lanza
        // aunque en edición inplace se hacen trucos específicos para Opera Mini
        return false;
    }
}
