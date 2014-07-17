/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.web.remtmpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.tmpl.TemplateSource;

/**
 *
 * @author jmarranz
 */
public class GoogleSearchResultSource implements TemplateSource
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
            conn.setRequestProperty("User-Agent", userAgent);

            // Notas sobre cómo especificar un proxy, usuario, password etc
            // http://www.javaworld.com/javaworld/javatips/jw-javatip42.html
            // http://www.javaworld.com/javaworld/javatips/jw-javatip47.html

            return conn.getInputStream();
        }
        catch(Exception ex) { throw new RuntimeException(ex); }
    }
}
