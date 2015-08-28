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
import static org.itsnat.impl.core.browser.web.webkit.BrowserWebKit.GCHROME;
import org.w3c.dom.html.HTMLElement;

/*
 *
 * User agents:

*   Antiguos:

*       - Google Chrome (1.0.154.48)
            Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.19 (KHTML, like Gecko) Chrome/1.0.154.48 Safari/525.19
* 
*   Modernos:
* 
*   - Google Chrome 28.0.1500.72 
*       Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36

 */

public class BrowserWebKitChrome extends BrowserWebKit
{
    protected boolean android;
    
    public BrowserWebKitChrome(String userAgent)
    {
        super(userAgent,GCHROME);
        
        this.android = (userAgent.contains("Android"));
    }

    @Override
    public boolean isMobile()
    {
        if (android) return true;
        return false;
    }

    @Override
    public boolean hasBeforeUnloadSupportHTML()
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
