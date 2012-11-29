
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.spitut.SPITutGlobalEventListener;
import org.itsnat.spitut.SPITutGlobalLoadRequestListener;
import org.itsnat.spitut.SPITutMainLoadRequestListener;

public class servlet extends HttpServletWrapper
{
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();

        ItsNatServletContext itsNatCtx = itsNatServlet.getItsNatServletContext();
        itsNatCtx.setMaxOpenDocumentsBySession(4); // To avoid abusive users

        ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();
        itsNatConfig.setFastLoadMode(true); // Not really needed, is the same as default

        String pathBase = getServletContext().getRealPath("/");
        String pathPages =     pathBase + "/WEB-INF/pages/";
        String pathFragments = pathBase + "/WEB-INF/fragments/";

        itsNatServlet.addEventListener(new SPITutGlobalEventListener());
        itsNatServlet.addItsNatServletRequestListener(new SPITutGlobalLoadRequestListener());

        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("main","text/html",
                    pathPages + "main.xhtml");
        docTemplate.addItsNatServletRequestListener(new SPITutMainLoadRequestListener());

        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("google_analytics","text/html",
                    pathPages + "google_analytics.xhtml");
        docTemplate.setScriptingEnabled(false);

        // Fragments
        itsNatServlet.registerItsNatDocFragmentTemplate("not_found","text/html",
                    pathFragments + "not_found.xhtml");
        itsNatServlet.registerItsNatDocFragmentTemplate("overview","text/html",
                    pathFragments + "overview.xhtml");
        itsNatServlet.registerItsNatDocFragmentTemplate("overview.popup","text/html",
                    pathFragments + "overview_popup.xhtml");
        itsNatServlet.registerItsNatDocFragmentTemplate("detail","text/html",
                    pathFragments + "detail.xhtml");
        itsNatServlet.registerItsNatDocFragmentTemplate("detail.more","text/html",
                    pathFragments + "detail_more.xhtml");
    }
}
