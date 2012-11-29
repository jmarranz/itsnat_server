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

package org.itsnat.impl.core.browser.opera;

import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author jmarranz
 */
public abstract class BrowserOpera9 extends BrowserOpera
{
    public BrowserOpera9(String userAgent)
    {
        super(userAgent);
    }

    public boolean isDOMContentLoadedSupported()
    {
        return true;
    }

    public boolean isBlurBeforeChangeEvent(HTMLElement formElem)
    {
        // Opera Mini no lanza el evento blur, pero si lo hiciera
        // lo haría como sus "hermanos".
        return false;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return true; // Soporta SVG al menos.
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        return false;
    }

    public boolean isTextAddedToInsertedSVGScriptNotExecuted()
    {
        // En Opera 9 desktop el texto del script se ejecuta si
        // se inserta junto al elemento <script>, no funciona si es después
        // En Opera 10 funciona bien en ambos casos, la solución al problema
        // es compatible con que funcione bien pues en la reinserción no se ejecuta de nuevo.
        // En Opera Mobile 9.5 y 9.7 Win Mobile funcionan bien con esta solución.
        // En Opera Mobile 9.5 UIQ el navegador es un visor de SVG sin scripts o funciona mal por lo menos.
        // En Opera Mini 4 no hay problema pues el render siempre es lo último de Opera.
        return true;
    }
}
