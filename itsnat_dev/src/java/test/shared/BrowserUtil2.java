/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.shared;

import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatServletRequest;

/**
 * Ponemos aquí cosas que necesitamos pero que no se necesitan en el Feature Showcase
 *
 * @author jmarranz
 */
public class BrowserUtil2
{

    public static boolean isS60WebKit(ItsNatServletRequest request)
    {
        HttpServletRequest httpReq = (HttpServletRequest) request.getServletRequest();
        String userAgent = httpReq.getHeader("User-Agent");
        return (userAgent.indexOf("AppleWebKit") != -1) && (userAgent.indexOf("Symbian") != -1);
    }


    public static boolean isOpera(ItsNatServletRequest request)
    {
        HttpServletRequest httpReq = (HttpServletRequest) request.getServletRequest();
        String userAgent = httpReq.getHeader("User-Agent");
        return (userAgent.indexOf("Opera") != -1) &&
                !BrowserUtil.isUCWEB(request);
    }

    public static boolean isMSIE(ItsNatServletRequest request)
    {
        HttpServletRequest httpReq = (HttpServletRequest) request.getServletRequest();
        String userAgent = httpReq.getHeader("User-Agent");
        return (userAgent.indexOf("MSIE") != -1) &&
                !isOpera(request) &&
                !BrowserUtil.isUCWEB(request);
    }

    public static boolean isMSIE6(ItsNatServletRequest request)
    {
        if (isMSIE(request))
        {
            HttpServletRequest httpReq = (HttpServletRequest) request.getServletRequest();
            String userAgent = httpReq.getHeader("User-Agent");
            return (userAgent.indexOf("MSIE 6.0;") != -1);
        }
        return false;
    }

    public static boolean isBatik(ItsNatServletRequest request)
    {
        HttpServletRequest httpReq = (HttpServletRequest) request.getServletRequest();
        String userAgent = httpReq.getHeader("User-Agent");
        return (userAgent.indexOf("Batik") != -1);
    }
}
