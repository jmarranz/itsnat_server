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

package org.itsnat.feashow.features.core.misc.remtmpl;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.tmpl.TemplateSource;

/**
 *
 * @author jmarranz
 */
public class GoogleResultTemplateSource implements TemplateSource
{
    public boolean isMustReload(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        return true;
    }

    public InputStream getInputStream(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        String query = request.getServletRequest().getParameter("q");

        try
        {
            query = java.net.URLEncoder.encode(query,"UTF-8");
            URL url = new URL("http://www.google.com/search?q=" + query);
            URLConnection conn = url.openConnection();

            HttpServletRequest httpRequest = (HttpServletRequest)request.getServletRequest();
            String userAgent = httpRequest.getHeader("User-Agent");
            conn.setRequestProperty( "User-Agent", userAgent);
             
            //conn.setRequestProperty( "User-Agent", "Mozilla/5.0 (compatible; MSIE 6.0; Windows NT 5.1)");

            return conn.getInputStream();
        }
        catch(Exception ex) { throw new RuntimeException(ex); }
    }
}
