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
        return (userAgent.contains("Android")) || // Opera Mobile and Opera Mobile Classic and Chrome android
               (userAgent.contains("iPod")) ||
               (userAgent.contains("iPhone")) ||
               (userAgent.contains("iPad")) ||                
               (userAgent.contains("BlackBerry"));
    }        

    public static boolean isOperaMini(ItsNatServletRequest request)
    {
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        return (userAgent.contains("Opera Mini"));
    }    
    
    public static boolean isJoystickModePreferred(ItsNatServletRequest request)
    {
        return isMobileBrowser(request);
    }
    
    public static boolean isMSIEOld(String userAgent)
    {
        if (userAgent.contains("MSIE ") && !userAgent.contains("Opera"))
        {
            int index = userAgent.indexOf("MSIE ");
            int start = index + "MSIE ".length();
            int end = userAgent.indexOf('.',start);
            int version = Integer.parseInt(userAgent.substring(start,end));
            return version < 9;
        }
        return false;
    }        
}
