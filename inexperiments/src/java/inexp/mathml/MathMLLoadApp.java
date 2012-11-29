/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.mathml;

import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;

/**
 *
 * @author jmarranz
 */
public class MathMLLoadApp
{
    public static void init(ItsNatHttpServlet itsNatServlet,String pathPrefix)
    {
        pathPrefix = pathPrefix + "mathml/pages/";

        // http://webkit.org/demos/mathml/MathMLDemo.xhtml
        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("mathml","text/html", pathPrefix + "mathml.xhtml");
        docTemplate.setEventsEnabled(false);
    }
}
