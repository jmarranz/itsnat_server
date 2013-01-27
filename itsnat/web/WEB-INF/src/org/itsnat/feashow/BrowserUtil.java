/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow;

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
        return (userAgent.indexOf("Opera Mini") != -1) ||
               (userAgent.indexOf("Opera Mobi") != -1) || // Opera Mobile 9.5 beta
               (userAgent.indexOf("SAMSUNG-SGH-i900") != -1) || // Opera Mobile 9.5 (Omnia)
               (userAgent.indexOf("Windows CE") != -1) || // Opera Mobile 9.5 
               (userAgent.indexOf("Symbian") != -1) ||  // Opera Mobile 9.5 & S60WebKit
               (userAgent.indexOf("Nokia6600s") != -1) || // S40WebKit
               (userAgent.indexOf("Android") != -1) ||
               (userAgent.indexOf("Aspen Simulator") != -1) ||
               (userAgent.indexOf("iPod") != -1) ||
               (userAgent.indexOf("iPhone") != -1) ||
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
