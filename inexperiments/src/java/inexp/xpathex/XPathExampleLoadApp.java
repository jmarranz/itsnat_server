
package inexp.xpathex;

import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;

public class XPathExampleLoadApp
{
    public static void init(ItsNatHttpServlet itsNatServlet,String pathPrefix)
    {
        pathPrefix = pathPrefix + "xpathex/pages/";

        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("xpathex","text/html", pathPrefix + "xpathex.html");
        docTemplate.addItsNatServletRequestListener(new XPathExampleLoadListener());
    }
}
