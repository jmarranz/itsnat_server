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

import java.util.Map;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLSelectElement;

/*
 *  Safari y BlackBerry y desconocidos
 *
 *
 * User agents:

*   Antiguos:
        - Safari 3.0.4 Mac (AppleWebKit/523.15.1) Safari 2.x no está soportado.
            Mozilla/5.0 (Macintosh; U; Intel Mac OS X;en-us) AppleWebKit/523.15.1 (KTHTML, like Gecko) Version/3.0.4 Safari/523.15

        - Safari 3.1 Windows (AppleWebKit/525.13)
            Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES) AppleWebKit/525.13 (KHTML, like Gecko) Version/3.1 Safari/525.13

        - Safari 4 beta Windows (AppleWebKit/528.16)
            Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES) AppleWebKit/528.16 (KHTML, like Gecko) Version/4.0 Safari/528.16


*   Modernos:
* 
*   - Safari 5.1.7  
*       Windows: Mozilla/5.0 (Windows NT 5.1) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2
*       Mac: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2 
* 
    - BlackBerry JDE 6.0, desde esta versión está basado en WebKit
        Mozilla/5.0 (BlackBerry; U; BlackBerry 9800; en-GB) AppleWebKit/534.1+ (KHTML, like Gecko) Version/6.0.0.141 Mobile Safari/534.1+
 */

public class BrowserWebKitOther extends BrowserWebKit
{
    public BrowserWebKitOther(String userAgent,int browserSubType)
    {
        super(userAgent,browserSubType);
    }

    public boolean isMobile()
    {
        if (browserSubType == BLACKBERRY) return true;
        return false;
    }

    public boolean hasBeforeUnloadSupportHTML()
    {
        switch(browserSubType)
        {
            case SAFARIDESKTOP :
                return true;
            case BLACKBERRY:
                return false;
        }

        return false;
    }

    public boolean isXHRSyncSupported()
    {
        /* Si soporta request síncronos el XMLHttpRequest
         * No está soportado normalmente en WebKits antiguos y algunas versiones móviles.
         */
        return true;
    }


    public boolean isXHRPostSupported()
    {
        return true;
    }

    public boolean isChangeNotFiredHTMLSelectWithSizeOrMultiple(HTMLSelectElement elem)
    {
        return false;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        return false;
    }

    public Map<String,String[]> getHTMLFormControlsIgnoreZIndex()
    {
        return null;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return true;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return true;
    }
    
    public boolean isInsertedSVGScriptNotExecuted()
    {
        switch(browserSubType)
        {
            case SAFARIDESKTOP:
            case BLACKBERRY:
                // El simulador del JDE 5.0 9800 renderiza SVG pero NO se ejecutan los <script>
                // imagino que cuando se active JavaScript funcionará correctamente pues la versión
                // primera del WebKit no es nada antigua (534.1+)
                return false;
        }

        return false; // Por si se me pasa algún navegador
    }

    @Override
    public boolean isReferrerReferenceStrong()
    {
        if (browserSubType == BLACKBERRY) return true; // Idem que las BlackBerryOld 
        else return super.isReferrerReferenceStrong();
    }
    
    @Override
    public boolean isCachedBackForward()
    {
        if (browserSubType == BLACKBERRY) return true; // Idem que las BlackBerryOld 
        else return super.isCachedBackForward();
    }

    @Override
    public boolean isCachedBackForwardExecutedScripts()
    {
        if (browserSubType == BLACKBERRY) return true; // Idem que las BlackBerryOld 
        else return super.isCachedBackForwardExecutedScripts();
    }
    
    public boolean isChangeEventNotFiredUseBlur(HTMLElement formElem)
    {
        return false; // Cosa del Chrome
    }          
}
