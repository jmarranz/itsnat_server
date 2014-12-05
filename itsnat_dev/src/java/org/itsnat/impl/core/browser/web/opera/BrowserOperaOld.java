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

import org.itsnat.impl.core.browser.web.BrowserW3C;
import java.util.Map;
import org.itsnat.impl.core.browser.*;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.w3c.dom.html.HTMLElement;

/**
  En Opera el unload no se dispara siempre y onbeforeunload no está definido
  http://www.quirksmode.org/bugreports/archives/2004/11/load_and_unload.html

 * @author jmarranz
 */
public abstract class BrowserOperaOld extends BrowserW3C
{
    public static final int OPERA_OLD_DESKTOP = 1;
    public static final int OPERA_OLD_MINI = 2;
    public static final int OPERA_OLD_MOBILE = 3;

    /** Creates a new instance of BrowserOpera */
    public BrowserOperaOld(String userAgent)
    {
        super(userAgent);

        this.browserType = OPERA_OLD;
    }

    public static BrowserOperaOld createBrowserOperaOld(String userAgent)
    {
        if (BrowserOperaOldMini.isOperaMini(userAgent))
            return new BrowserOperaOldMini(userAgent);
        else if (BrowserOperaOldMobile.isOperaMobile9(userAgent))
            return new BrowserOperaOldMobile(userAgent);
        else
            return new BrowserOperaOldDesktop(userAgent);
    }

    public static boolean isOpera(String userAgent,ItsNatServletRequestImpl itsNatRequest)
    {
        return (userAgent.indexOf("Opera") != -1);
    }

    @Override
    public boolean hasBeforeUnloadSupport(ItsNatStfulDocumentImpl itsNatDoc)
    {
        return false; // Tampoco en HTML
    }

    public boolean isReferrerReferenceStrong()
    {
        // El back/forward está cacheado en el cliente.
        return true;
    }

    public boolean isCachedBackForward()
    {
        // Opera Mini: sólo tiene Back
        return true;
    }

    public boolean isClientWindowEventTarget()
    {
        return true;
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
    
    public boolean hasHTMLCSSOpacity()
    {
        return true;
    }    
    
    public Map<String,String[]> getHTMLFormControlsIgnoreZIndex()
    {
        // En teoría todos los elementos no ignoran el z-index, sin embargo
        // aunque Opera Mobile 9.x está diseñado para pantallas táctiles,
        // hasta cierto punto soporta navegación por cursores,
        // y he descubierto que a través de los cursores podemos llegar y "pulsar"
        // todos los elementos con listeners de layers por debajo, tanto en Win CE como en Symbian.

        // El problema es que cualquier elemento no form con
        // un listener "click" es susceptible también de llegarse a él usando
        // los cursores, desafortunadamente "el cuadro de foco" se muestra
        // al pasar por el mismo.

        // No consideramos ese caso pues su pulsación es premeditada o accidental
        // y en ese caso lo que hacemos es lanzar una excepción.

        return null;
    }    
}
