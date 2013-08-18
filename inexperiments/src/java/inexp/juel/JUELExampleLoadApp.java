package inexp.juel;

import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;

public class JUELExampleLoadApp
{
    public static void init(ItsNatHttpServlet itsNatServlet,String pathPrefix)
    {
        pathPrefix = pathPrefix + "juel/pages/";
        
        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("juel","text/html", pathPrefix + "juel.html");
        docTemplate.addItsNatServletRequestListener(new JUELExampleLoadListener());
    }
}
