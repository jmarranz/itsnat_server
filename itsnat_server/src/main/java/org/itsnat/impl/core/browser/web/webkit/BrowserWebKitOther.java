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

    @Override
    public boolean isMobile()
    {
        return false;
    }

    @Override
    public boolean hasBeforeUnloadSupportHTML()
    {
        switch(browserSubType)
        {
            case SAFARIDESKTOP :
                return true;
        }

        return false;
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
