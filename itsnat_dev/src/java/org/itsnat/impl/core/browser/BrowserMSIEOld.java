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

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.w3c.dom.html.HTMLElement;

/**
 * User agents:
 *
 * MSIE 6:      Mozilla/4.0 (compatible; MSIE_OLD 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)
 * IE Mobile 6:
 *    Desktop Mode: Mozilla/4.0 (compatible; MSIE_OLD 6.0; Windows NT 5.1)
 *    Mobile Mode:  Mozilla/4.0 (compatible; MSIE_OLD 6.0; Windows CE; IEMobile 8.12; MSIEMobile 6.0)
 *
 * @author jmarranz
 */
public abstract class BrowserMSIEOld extends Browser
{
    public static final int MSIE_DESKTOP = 1;
    public static final int MSIE_MOBILE = 2;

    /** Creates a new instance of BrowserMSIEOld */
    public BrowserMSIEOld(String userAgent)
    {
        super(userAgent);

        this.browserType = MSIE_OLD;
    }

    public static BrowserMSIEOld createBrowserMSIEOld(String userAgent,ItsNatServletRequestImpl itsNatRequest,int version)
    {
        // http://blogs.msdn.com/iemobile/archive/2006/08/03/Detecting_IE_Mobile.aspx
        // Sólo está soportado IE Mobile 6 (Windows Mobile 6 y 6.1)

        if (userAgent.indexOf("MSIEMobile") != -1) // Modo "mobile" de IE Mobile 6. 
            return new BrowserMSIE6(userAgent,version,true);
        else
        {
            // Puede ser la versión mobile en modo "desktop", el User Agent es exacto al de desktop
            String os = itsNatRequest.getHeader("UA-OS");
            boolean mobile = ((os != null) && (os.indexOf("Windows CE") != -1));
            return new BrowserMSIE6(userAgent,version,mobile);
        }
    }

    public boolean isBlurBeforeChangeEvent(HTMLElement formElem)
    {
        // IE Mobile no tiene blur pero cuando lo tenga lo lógico
        // es que se comporte como su hermano mayor.
        return false;
    }

    public boolean isSetTimeoutSupported()
    {
        return true;
    }

    public boolean isTextAddedToInsertedHTMLScriptNotExecuted()
    {
        return false;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return false; // No soporta SVG por ejemplo.
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        return false; // Por poner algo, no soporta SVG
    }    

    public boolean isTextAddedToInsertedSVGScriptNotExecuted()
    {
        return false; // Por poner algo, no soporta SVG
    }
    
    public boolean isClientWindowEventTarget()
    {
        return true;
    }
}
