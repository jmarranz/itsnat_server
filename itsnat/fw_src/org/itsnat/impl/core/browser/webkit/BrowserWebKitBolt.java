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
   Bolt es tipo proxy por lo que hay "dos versiones", la del servidor y la del
 * cliente aunque supongo que la gente de BitStream tenderá a soportar
 * las versiones del pasado aunque posiblemente con cambio de comportamiento
 * como ocurrió entre la 1.0 y la 1.5

 * User agent

        Fecha?: Mozilla/5.0 (X11; 78; CentOS; US-en) AppleWebKit/527+ (KHTML, like Gecko) Bolt/0.862 Version/3.0 Safari/523.15 (Nota: el 523.15 yo creo que es un despiste, el AppleWebKit parece que es el 527+ pues tiene DOMContentLoaded)
        9-10-2009 (released Bolt 1.5): Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; BOLT/1.500) AppleWebKit/530+ (KHTML, like Gecko) Version/4.0 Safari/530.17
*/

public class BrowserWebKitBolt extends BrowserWebKit
{
    public BrowserWebKitBolt(String userAgent)
    {
        super(userAgent,BOLT);
    }

    public boolean isMobile()
    {
        return true;
    }

    public boolean isFilteredCommentsInMarkup()
    {
        return false;
    }

    public boolean hasBeforeUnloadSupportHTML()
    {
        return true;
    }

    public boolean isXHRSyncSupported()
    {
        return true;
    }

    public boolean isXHRPostSupported()
    {
        return true;
    }

    public boolean isChangeNotFiredHTMLSelectWithSizeOrMultiple(HTMLSelectElement elem)
    {
        // El problema es que por ahora (incluso 1.5) los select multiple son combo box
        // por lo que esta pregunta no tiene mucho sentido en Bolt.
        return false;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        return false;
    }

    public boolean isAJAXEmptyResponseFails()
    {
        // El servidor siempre ha sido un WebKit moderno desde el primer día (5xx)
        return false;
    }

    public Map getHTMLFormControlsIgnoreZIndex()
    {
        return null;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return false;
    }

    public boolean isOldEventSystem()
    {
        return false;
    }

    public boolean isSetTimeoutSupported()
    {
        /* En las primeras versiones del servidor el window.setTimeout se ejecutaba inmediatamente después
         * de que el script que lo ejecutaba terminara lo cual no era aceptable
           pero desde la v1.5 del servidor el setTimeout funciona bien, al menos
           lo he probado para 1 minuto (60000). Obviamente no hay actualización
           automática del cliente pero cuando hay comunicación con el servidor
          el cliente se actualiza con las acciones propias del listener del setTimeout.
         */
        return true;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return true; // Soporta SVG por ejemplo.
    }

    public boolean isTextAddedToInsertedHTMLScriptNotExecuted()
    {
        return false;
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        return false; // Como es basado en servidor el WebKit es bastante moderno (por ejemplo ya usa WebKit 530.17)
    }
}
