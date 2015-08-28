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

package org.itsnat.impl.core.browser.web;

import java.util.Map;
import org.itsnat.impl.core.browser.web.webkit.BrowserWebKit;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.w3c.dom.html.HTMLElement;

/**
 * Vale para FireFox 
 *
 * Versiones de Gecko: http://developer.mozilla.org/En/Gecko
 *
 * Ejemplos de user agent:
 * FireFox  2.0:   Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.16) Gecko/20080702 Firefox/2.0.0.16
 * FireFox  3.0:   Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.1) Gecko/2008070208 Firefox/3.0.1
 *          3.5.2: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2
 *
 *
 * @author jmarranz
 */
public class BrowserGecko extends BrowserW3C
{
    protected float geckoVersion;

    
    /** Creates a new instance of BrowserGecko */
    public BrowserGecko(String userAgent,ItsNatServletRequestImpl itsNatRequest)
    {
        super(userAgent);

        this.browserType = GECKO;

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

    public static BrowserGecko createBrowserGecko(String userAgent,ItsNatServletRequestImpl itsNatRequest)
    {
        return new BrowserGecko(userAgent,itsNatRequest);
    }

    public static boolean isGecko(String userAgent,ItsNatServletRequestImpl itsNatRequest)
    {
        // Los navegadores WebKit incluyen en userAgent la palabra Gecko.
        // No usamos la palabra "FireFox" para soportar Mozilla y Camino también
        return (userAgent.contains("Gecko")) &&
                !BrowserWebKit.isWebKit(userAgent);
    }

    @Override
    public boolean isMobile()
    {
        return false;
    }

    @Override
    public boolean isInsertedSVGScriptNotExecuted()
    {
        return false; 
    }

    @Override
    public boolean isTextAddedToInsertedSVGScriptNotExecuted()
    {
        return false;
    }

    @Override
    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return true; // SVG y MathML al menos
    }
    
    @Override
    public boolean isReferrerReferenceStrong()
    {
        // El nuevo documento siempre se carga antes de que el anterior se destruya
        return false;
    }

    @Override
    public boolean isCachedBackForward()
    {
        return false;
    }

    @Override
    public boolean isCachedBackForwardExecutedScripts()
    {
        return false;
    }

    @Override
    public boolean isDOMContentLoadedSupported()
    {
        return true;
    }

    @Override
    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        return false;
    }

    @Override
    public Map<String,String[]> getHTMLFormControlsIgnoreZIndex()
    {
        return null;
    }


}
