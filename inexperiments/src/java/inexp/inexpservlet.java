/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inexp;

import inexp.extjsexam.ExtJSExampleLoadApp;
import inexp.hybridcs.HybridCSLoadApp;
import inexp.jooxex.JOOXExampleLoadApp;
import inexp.juel.JUELExampleLoadApp;
import inexp.mathml.MathMLLoadApp;
import inexp.oldwaiaria.OldWAIARIALoadApp;
import inexp.waiaria.WAIARIALoadApp;
import inexp.xpathex.XPathExampleLoadApp;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;

public class inexpservlet extends HttpServletWrapper
{
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();
        ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();

        ItsNatServletContext itsNatCtx = itsNatConfig.getItsNatServletContext();
        itsNatCtx.setMaxOpenDocumentsBySession(5); // To limit the memory of bots identified as legitimate browsers and abusive users

        itsNatServlet.addItsNatServletRequestListener(new GlobalLoadRequestListener());
        itsNatServlet.addEventListener(new GlobalEventListener());

        String pathPrefix = getServletContext().getRealPath("/") + "/WEB-INF/";

        itsNatServlet.registerItsNatDocumentTemplate("main","text/html", pathPrefix + "main/main.html");        
        
        ExtJSExampleLoadApp.init(itsNatServlet, pathPrefix);
        OldWAIARIALoadApp.init(itsNatServlet, pathPrefix);
        WAIARIALoadApp.init(itsNatServlet, pathPrefix);
        MathMLLoadApp.init(itsNatServlet, pathPrefix);
        HybridCSLoadApp.init(itsNatServlet, pathPrefix);
        XPathExampleLoadApp.init(itsNatServlet, pathPrefix);        
        JOOXExampleLoadApp.init(itsNatServlet, pathPrefix);        
        JUELExampleLoadApp.init(itsNatServlet, pathPrefix);        
    }

}
