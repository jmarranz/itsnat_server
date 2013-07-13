/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.jooxex;

import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;

public class JOOXExampleLoadApp
{
    public static void init(ItsNatHttpServlet itsNatServlet,String pathPrefix)
    {
        pathPrefix = pathPrefix + "jooxex/pages/";

        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("jooxex","text/html", pathPrefix + "jooxex.html");
        docTemplate.addItsNatServletRequestListener(new JOOXExampleLoadListener());
    }
}
