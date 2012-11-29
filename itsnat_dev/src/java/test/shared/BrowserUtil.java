
package test.shared;

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
        if (isSkyFire(request))
            return true;
        if (isIEMobile6on6(request))
            return true;
        if (isUCWEB(request))
            return true;
        if (isBolt(request))
            return true;
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        return (userAgent.indexOf("Minimo") != -1) ||
               (userAgent.indexOf("Fennec") != -1) ||
               (userAgent.indexOf("Opera Mini") != -1) ||
               (userAgent.indexOf("Opera Mobi") != -1) || // Opera Mobile 9.5 beta
               (userAgent.indexOf("SAMSUNG-SGH-i900") != -1) || // Opera Mobile 9.5 (Omnia)
               ( ((userAgent.indexOf("MOT-") != -1) || (userAgent.indexOf("Motorola") != -1)) && (userAgent.indexOf("Opera") != -1)) || // Opera Mobile 8.x on some Motorola devices
               (userAgent.indexOf("NetFront") != -1) ||
               (userAgent.indexOf("Windows CE") != -1) || // Opera Mobile 8/9.5 & Pocket IE
               (userAgent.indexOf("Symbian") != -1) ||  // Opera Mobile 8/9.5 & S60WebKit
               (userAgent.indexOf("Nokia6600s") != -1) || // S40WebKit
               (userAgent.indexOf("Android") != -1) ||
               (userAgent.indexOf("Iris") != -1) ||
               (userAgent.indexOf("Aspen Simulator") != -1) ||
               (userAgent.indexOf("iPod") != -1) ||
               (userAgent.indexOf("iPhone") != -1) ||
               (userAgent.indexOf("BlackBerry") != -1) ||
               (userAgent.indexOf("MotoWebKit") != -1) ||
               (userAgent.indexOf("webOS") != -1);
    }

    public static boolean isJoystickModePreferred(ItsNatServletRequest request)
    {
        if (isUCWEB(request))
            return true;
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

    public static boolean isSkyFire(ItsNatServletRequest request)
    {
        HttpServletRequest servRequest = (HttpServletRequest)request.getServletRequest();
        return (servRequest.getHeader("X-Skyfire-Version") != null);
    }

    public static boolean isUCWEB(ItsNatServletRequest request)
    {
        HttpServletRequest servRequest = (HttpServletRequest)request.getServletRequest();
        String accept = servRequest.getHeader("accept");
        return ((accept != null) && (accept.indexOf("dn/") != -1));
    }

    public static boolean isUCWEBWinMobile(ItsNatServletRequest request)
    {
        if (!BrowserUtil.isUCWEB(request))
            return false;
        // UCWEB Win Mobile user agent simulates an old Pocket IE
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        return (userAgent.indexOf("Windows CE") != -1);
    }

    public static boolean isBolt(ItsNatServletRequest request)
    {
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        return (userAgent.indexOf("BOLT") != -1);
    }

    public static boolean isPocketIE(ItsNatServletRequest request)
    {
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        // Space in " IEMobile" is necessary to distinguish between Pocket IE and IE Mobile (WM 6.1.4)
        return (userAgent.indexOf(" IEMobile") != -1);
    }

    public static boolean isMotoWebKit(ItsNatServletRequest request)
    {
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        return (userAgent.indexOf("MotoWebKit") != -1);
    }

/*
    public static boolean isOpera(ItsNatServletRequest request)
    {
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        return (userAgent.indexOf("Opera") != -1) &&
                !isUCWEB(request);
    }

    public static boolean isMSIE(ItsNatServletRequest request)
    {
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        return (userAgent.indexOf("MSIE") != -1) &&
                !isOpera(request) &&
                !isUCWEB(request);
    }
 *
 */
}
