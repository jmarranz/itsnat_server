
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.spitut.SPITutGlobalLoadRequestListener;
import org.itsnat.spitut.SPITutMainLoadRequestListener;

public class servlet extends HttpServletWrapper
{
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();

        ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();
        itsNatConfig.setFastLoadMode(true); // Not really needed, is the same as default

        String pathBase = getServletContext().getRealPath("/");
        String pathPages =     pathBase + "/WEB-INF/pages/";
        String pathFragments = pathBase + "/WEB-INF/fragments/";

        itsNatServlet.addItsNatServletRequestListener(new SPITutGlobalLoadRequestListener());

        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("main","text/html", pathPages + "main.html");
        docTemplate.addItsNatServletRequestListener(new SPITutMainLoadRequestListener());
        docTemplate.setEventsEnabled(false);
        
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("google_analytics","text/html", pathPages + "google_analytics.html");
        docTemplate.setScriptingEnabled(false);

        // Fragments
        itsNatServlet.registerItsNatDocFragmentTemplate("not_found","text/html", pathFragments + "not_found.html");
        itsNatServlet.registerItsNatDocFragmentTemplate("overview","text/html",  pathFragments + "overview.html");
        itsNatServlet.registerItsNatDocFragmentTemplate("overview.popup","text/html", pathFragments + "overview_popup.html");
        itsNatServlet.registerItsNatDocFragmentTemplate("detail","text/html", pathFragments + "detail.html");
        itsNatServlet.registerItsNatDocFragmentTemplate("detail.more","text/html", pathFragments + "detail_more.html");
    }
}
