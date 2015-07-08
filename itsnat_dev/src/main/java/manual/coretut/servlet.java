/*
 * servlet.java
 *
 * Created on 27 de agosto de 2007, 20:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.coretut;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.itsnat.core.ClientErrorMode;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.CommMode;
import org.itsnat.core.UseGZip;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;

public class servlet extends HttpServletWrapper
{
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();
        ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();

        ItsNatServletContext itsNatCtx = itsNatConfig.getItsNatServletContext();
        itsNatCtx.setMaxOpenDocumentsBySession(-1);

        String serverInfo = getServletContext().getServerInfo();
        boolean gaeEnabled = serverInfo.startsWith("Google App Engine");
        itsNatCtx.setSessionReplicationCapable(gaeEnabled);
        itsNatCtx.setSessionSerializeCompressed(false);
        itsNatCtx.setSessionExplicitSerialize(false);

        itsNatConfig.setDebugMode(true);
        itsNatConfig.setClientErrorMode(ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS);
        itsNatConfig.setLoadScriptInline(true);
        itsNatConfig.setFastLoadMode(true);
        itsNatConfig.setCommMode(CommMode.XHR_ASYNC_HOLD);
        itsNatConfig.setEventTimeout(-1);
        itsNatConfig.setOnLoadCacheStaticNodes("text/html",true);
        itsNatConfig.setOnLoadCacheStaticNodes("text/xml",false);
        itsNatConfig.setNodeCacheEnabled(true);
        itsNatConfig.setDefaultEncoding("UTF-8");
        itsNatConfig.setUseGZip(UseGZip.SCRIPT);
        itsNatConfig.setDefaultDateFormat(
			DateFormat.getDateInstance(DateFormat.DEFAULT,Locale.US));
        itsNatConfig.setDefaultNumberFormat(NumberFormat.getInstance(Locale.US));
        itsNatConfig.setEventDispatcherMaxWait(0);
        itsNatConfig.setEventsEnabled(true);
        itsNatConfig.setScriptingEnabled(true);
        itsNatConfig.setUsePatternMarkupToRender(false);
        itsNatConfig.setAutoCleanEventListeners(true);
        itsNatConfig.setUseXHRSyncOnUnloadEvent(true);
        itsNatConfig.setMaxOpenClientsByDocument(5);

        String pathPrefix = getServletContext().getRealPath("/");
        pathPrefix += "/WEB-INF/pages/manual/";

        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("manual.core.example","text/html", pathPrefix + "core_example.xhtml");
        docTemplate.addItsNatServletRequestListener(new CoreExampleLoadListener());

        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("manual.core.xmlExample","text/xml", pathPrefix + "xml_example.xml");
        docTemplate.addItsNatServletRequestListener(new CoreXMLExampleLoadListener());

        ItsNatDocFragmentTemplate docFragTemplate = itsNatServlet.registerItsNatDocFragmentTemplate("manual.core.xmlFragExample","text/xml",pathPrefix + "xml_fragment_example.xml");
    }

}
