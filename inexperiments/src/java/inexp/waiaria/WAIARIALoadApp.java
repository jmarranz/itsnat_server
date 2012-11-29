/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.waiaria;


import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;

/**
 *
 * @author jmarranz
 */
public class WAIARIALoadApp
{
    public static void init(ItsNatHttpServlet itsNatServlet,String pathPrefix)
    {
        pathPrefix = pathPrefix + "waiaria/pages/";

        // http://www.w3.org/WAI/PF/aria-practices/#TreeView
        // http://codetalks.org/source/widgets/tree/tree.html
        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("waiaria_tree","text/html", pathPrefix + "waiaria_tree.html");
        docTemplate.addItsNatServletRequestListener(new WAIARIALoadListener());
    }
}
