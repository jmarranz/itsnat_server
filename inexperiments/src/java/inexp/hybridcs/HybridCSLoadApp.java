
package inexp.hybridcs;

import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;

public class HybridCSLoadApp
{
    public static void init(ItsNatHttpServlet itsNatServlet,String pathPrefix)
    {
        pathPrefix = pathPrefix + "hybridcs/pages/";

        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("hybridcs","text/html", pathPrefix + "hybridcs.xhtml");
        docTemplate.addItsNatServletRequestListener(new HybridCSLoadListener());
    }
}
