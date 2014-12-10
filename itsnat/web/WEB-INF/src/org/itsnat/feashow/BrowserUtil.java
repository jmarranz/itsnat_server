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
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        return (userAgent.indexOf("Android") != -1) || // Opera Mobile and Opera Mobile Classic and Chrome android
               (userAgent.indexOf("iPod") != -1) ||
               (userAgent.indexOf("iPhone") != -1) ||
               (userAgent.indexOf("iPad") != -1) ||                
               (userAgent.indexOf("BlackBerry") != -1);
    }        

    public static boolean isOperaMini(ItsNatServletRequest request)
    {
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        return (userAgent.indexOf("Opera Mini") != -1);
    }    
    
    public static boolean isJoystickModePreferred(ItsNatServletRequest request)
    {
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        if (userAgent.indexOf("Android") != -1)
            return true;
        return false;
    }

}
