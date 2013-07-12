/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inexp;

import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.http.ItsNatHttpSession;


/**
 *
 * @author jmarranz
 */
public class BrowserUtil
{
    public static boolean isMobileBrowser(ItsNatServletRequest request)
    {
        if (isIEMobile6on6(request))
            return true;
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        return (userAgent.indexOf("Android") != -1) ||  // Opera Mobile
               (userAgent.indexOf("S60") != -1) || // Opera Mobile
               (userAgent.indexOf("Symbian") != -1) ||  // Opera Mobile 9.5 & S60WebKit
               (userAgent.indexOf("Nokia6600s") != -1) || // S40WebKit
               (userAgent.indexOf("iPod") != -1) ||
               (userAgent.indexOf("iPhone") != -1) ||
               (userAgent.indexOf("iPad") != -1) ||                
               (userAgent.indexOf("BlackBerry") != -1);
    }

    public static boolean isJoystickModePreferred(ItsNatServletRequest request)
    {
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        if (userAgent.indexOf("Android") != -1)
            return true;
        return false;
    }

    public static boolean isIEMobile6on6(ItsNatServletRequest request)
    {
        // IE Mobile (WM 6.1.4) both modes, desktop and mobile
        HttpServletRequest servRequest = (HttpServletRequest)request.getServletRequest();
        String os = servRequest.getHeader("UA-OS");
        return ((os != null) && (os.indexOf("Windows CE") != -1));
    }
}
