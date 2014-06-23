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

import java.util.HashMap;
import java.util.Map;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLSelectElement;

/*
 * Soportado desde 2.1.  NO SE CONSIDERA el Chrome para Android
 * 
 * User agents:

    - Android desde Beta r1 (0.6) hasta v1.1 r1
        Beta r1 (0.6): Mozilla/5.0 (Linux; U; Android 0.6; en-us; generic) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2
        V1.0 r2:       Mozilla/5.0 (Linux; U; Android 1.0; en-us; generic) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2
        V1.1 r1:       Mozilla/5.0 (Linux; U; Android 1.0; en-us; generic) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2
        V1.5 r1:       Mozilla/5.0 (Linux; U; Android 1.5; en-us; sdk Build/CUPCAKE) AppleWebKit/528.5+ (KHTML, like Gecko) Version/3.1.2 Mobile Safari/525.20.1
        v1.6:
        v2.0           Mozilla/5.0 (Linux; U; Android 2.0; en-us; sdk Build/ECLAIR) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17

        No hay versiones intermedias entre 1.1 y 1.5
        El navegador de la 1.1 parece idéntico al de la 1.0 y no hay forma de distinguirlo.
 */

public class BrowserWebKitAndroid extends BrowserWebKit
{
    protected int mainVersion;
    //protected int subVersion;

    private static final Map<String,String[]> tagNamesIgnoreZIndex = new HashMap<String,String[]>();
    static
    {
        // Verificado hasta la v2 incluida:
        tagNamesIgnoreZIndex.put("select",null);
        tagNamesIgnoreZIndex.put("input",new String[]{"text","password","file","checkbox","radio"});
        // El caso de INPUT checkbox y radio es especial, no ignoran el z-index pero podemos llegar a ellos usando el cursor
        // y aunque ignoran los eventos queda feo. Con "button" no ocurre.
        tagNamesIgnoreZIndex.put("textarea",null);
    }

    public BrowserWebKitAndroid(String userAgent)
    {
        super(userAgent,ANDROID);

        // Versión del Android: "Android M.s;"
        try
        {
            int start = userAgent.indexOf("Android ");
            start += "Android ".length();
            int end = start;
            int dot = -1;
            while(true)
            {
                char c = userAgent.charAt(end);
                if (c == '.') dot = end;
                else if (c == ';') break;  // Hay algún caso en donde no va seguido el ; del número
                end++;
            }
            this.mainVersion = Integer.parseInt(userAgent.substring(start,dot));
            //this.subVersion =  Integer.parseInt(userAgent.substring(dot + 1,end));
        }
        catch(Exception ex) // Caso de user agent de formato desconocido
        {
            this.mainVersion = 2;
            //this.subVersion =  0;
        }
    }

    public boolean isMobile()
    {
        return true;
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
        return false;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        return false;
    }

    public Map<String,String[]> getHTMLFormControlsIgnoreZIndex()
    {
        return tagNamesIgnoreZIndex;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return true;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        // http://caniuse.com/svg
        return mainVersion >= 3; // SVG soportado.
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        // http://caniuse.com/svg        
        return mainVersion < 3; // No soporta SVG por debajo del 3
    }
    
    public boolean isChangeEventNotFiredUseBlur(HTMLElement formElem)
    {
        return false; // Cosa del Chrome
    }        
}
