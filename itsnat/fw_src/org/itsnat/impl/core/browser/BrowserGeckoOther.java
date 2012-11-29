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

package org.itsnat.impl.core.browser;

import java.util.HashMap;
import java.util.Map;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatSVGDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class BrowserGeckoOther extends BrowserGecko
{
    private static final Map tagNamesIgnoreZIndexSkyFire = new HashMap();
    static
    {
        tagNamesIgnoreZIndexSkyFire.put("select",null);
        tagNamesIgnoreZIndexSkyFire.put("input",new String[]{"text","password","file"}); // Los button,checkbox y radio button no ignoran el z-index
        tagNamesIgnoreZIndexSkyFire.put("textarea",null);
    }

    public BrowserGeckoOther(String userAgent,ItsNatServletRequestImpl itsNatRequest)
    {
        super(userAgent);

        if (userAgent.indexOf("Minimo") != -1)
            this.browserSubType = MINIMO;
        else if (userAgent.indexOf("Fennec") != -1)
            this.browserSubType = FENNEC;
        else if (itsNatRequest.getHeader("X-Skyfire-Version") != null) // http://www.smartphonemag.com/cms/blog/9/skyfire-a-brand-new-and-promising-web-browser-technical-review-comparison
            this.browserSubType = SKYFIRE;  // Ejemplo de resultado: 0.8.0.7081
        else
            this.browserSubType = DESKTOP;

        // Versión de Gecko:
        try
        {
            int start = userAgent.indexOf("rv:");
            start += "rv:".length();
            int end = start + 3; // Para capturar el comienzo "X.X"
            String strVer = userAgent.substring(start,end);
            this.geckoVersion = Float.parseFloat(strVer);
        }
        catch(Exception ex) // Caso de user agent de formato desconocido
        {
            this.geckoVersion = 0;
        }
    }

    public boolean hasBeforeUnloadSupport(ItsNatStfulDocumentImpl itsNatDoc)
    {
        // En XUL está soportado
        return ! (itsNatDoc instanceof ItsNatSVGDocumentImpl);
    }

    public boolean isReferrerReferenceStrong()
    {
        // El nuevo documento siempre se carga antes de que el anterior se destruya
        return false;
    }

    public boolean isCachedBackForward()
    {
        return false;
    }

    public boolean isCachedBackForwardExecutedScripts()
    {
        return false;
    }

    public boolean isDOMContentLoadedSupported()
    {
        return true;
    }

    public boolean isBlurBeforeChangeEvent(HTMLElement formElem)
    {
        // Ojo, en el caso de SkyFire los componentes de texto (input text/password etc y textarea)
        // no lanzan el evento change por lo que nos puede hacernos creer que blur es el último
        if (geckoVersion < (float)1.9) // FireFox 2.0 y anteriores, Minimo 0.2 y SkyFire (0.8 y 0.9 al menos)
            return DOMUtilHTML.isHTMLTextAreaOrInputTextBox(formElem);

        return false; // FireFox 3.0, Fennec
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        if (browserSubType == MINIMO) // El textarea no se puede probar pues "crashes"
            return (formElem instanceof HTMLSelectElement);
        else
            return false;
    }

    public Map getHTMLFormControlsIgnoreZIndex()
    {
        if (isSkyFire())
            return tagNamesIgnoreZIndexSkyFire;
        else
            return null;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return true;
    }

    public boolean isSetTimeoutSupported()
    {
        /* Incluso en SkyFire que es tipo proxy parece que funciona bien por lo menos
         * para valores de varios segundos.
         * Lo mejor de SkyFire es que el cambio que haga el timer se manifiesta
         * automáticamente sin interacción del usuario.
         */
        return true;
    }
}
