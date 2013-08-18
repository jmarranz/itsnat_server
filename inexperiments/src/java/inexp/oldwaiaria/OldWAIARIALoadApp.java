
package inexp.oldwaiaria;

import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.http.ItsNatHttpServlet;

/**
 *
 * @author jmarranz
 */
public class OldWAIARIALoadApp
{
    public static void init(ItsNatHttpServlet itsNatServlet,String pathPrefix)
    {
        pathPrefix = pathPrefix + "oldwaiaria/pages/";

        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("oldwaiaria_scoreboard","text/html", pathPrefix + "oldwaiaria_scoreboard.xhtml");
        docTemplate.setEventsEnabled(false);
    }
}
