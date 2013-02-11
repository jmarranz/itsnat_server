package test;
/*
 * ItsNatServletExample.java
 *
 * Created on 5 de octubre de 2006, 9:56
 */

import test.shared.TestRemoteControlListener;
import test.shared.TestGlobalRemoteControlListener;
import test.shared.TestGlobalDocumentLoadListener;
import test.shared.TestGlobalEventListener;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.CommMode;
import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.*;
import org.itsnat.core.ClientErrorMode;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.UseGZip;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import test.asyncforms.TestAutoSyncFormsDocLoadListener;
import test.cache.TestCacheLoadDocListener;
import test.clientmut.TestClientMutationDocLoadListener;
import test.comp.html.TestComplexHTMLTableStructure;
import test.comp.TestComponentsDocLoadListener;
import test.comp.TestCreateItsNatComponentListener;
import test.core.TestCoreAttachServerLauncherDocLoadListener;
import test.core.TestCoreDocLoadListener;
import test.coreiframe.TestCoreIFrameDocLoadListener;
import test.noajax.TestNoAJAXDocLoadListener;
import test.noscript.TestNoScriptDocLoadListener;
import test.iframehtml.TestIFrameHTMLDocLoadListener;
import test.ioeasvg.TestSVGBoundDocLoadListener;
import test.ioeasvg.TestIFrameObjEmbAppletSVGParentDocLoadListener;
import test.ioeasvg.TestSVGBoundSavareseDocLoadListener;
import test.ioeasvg.TestSVGSavareseParentDocLoadListener;
import test.mobile.TestMobileDocLoadListener;
import test.prettyurl.TestPrettyURLDocLoadListener;
import test.referrerpull.TestReferrerPullDocLoadListener;
import test.referrerpull.TestReferrerPullNextDocLoadListener;
import test.referrerpush.TestReferrerPushDocLoadListener;
import test.referrerpush.TestReferrerPushNextDocLoadListener;
import test.remotectrl.TestRemoteCtrlLauncherDocLoadListener;
import test.remtmpl.GoogleSearchResultSource;
import test.remtmpl.TestRemoteTemplateDocLoadListener;
import test.remtmpl.TestRemoteTemplateResultDocLoadListener;
import test.shared.TestSerialization;
import test.svg.TestSVGAttachServerLauncherDocLoadListener;
import test.svg.TestSVGDocLoadListener;
import test.svgweb.TestSVGWebDocLoadListener;
import test.svgxhtml.TestSVGInXHTMLAdobeSVGDocLoadListener;
import test.svgxhtml.TestSVGInXHTMLDocLoadListener;
import test.xml.TestXMLDocLoadListener;
import test.xml_comp.TestXMLComponentsDocLoadListener;
import test.xul.TestXULAttachServerLauncherDocLoadListener;
import test.xul.TestXULDocLoadListener;


/**
 *
 * @author jmarranz
 * @version
 */
public class ItsNatServletExample extends HttpServletWrapper
{
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        boolean debugMode = true;
        boolean loadScriptInline = true;
        boolean fastLoadMode = true;
        int commMode = CommMode.XHR_ASYNC_HOLD; // XHR_SYNC  XHR_ASYNC XHR_ASYNC_HOLD SCRIPT SCRIPT_HOLD  (el modo SCRIPT es MUY asíncrono de verdad)
        boolean onLoadCachingNodesHTML = true;
        boolean onLoadCachingNodesSVG = true;
        boolean onLoadCachingNodesXUL = true;
        boolean onLoadCachingNodesXML = true;
        boolean nodeCache = true;
        int useGZip = UseGZip.SCRIPT; // NONE SCRIPT
        int clientErrorMode = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS; // SHOW_SERVER_AND_CLIENT_ERRORS , NOT_CATCH_ERRORS , SHOW_SERVER_ERRORS
        boolean autoCleanEventListeners = true;
        boolean useXHRSyncOnUnloadEvent = true;
        int maxOpenClientsByDocument = 4; // incluye el owner
        boolean joystickMode = false;

        TestSerialization.enable = false;
        
        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();

        SharedInitContextConf.init(getServletContext(), itsNatServlet);
        
        ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();
        
        itsNatConfig.setMaxOpenClientsByDocument(maxOpenClientsByDocument);
        itsNatConfig.setDebugMode(debugMode);
        itsNatConfig.setLoadScriptInline(loadScriptInline);
        itsNatConfig.setFastLoadMode(fastLoadMode);
        itsNatConfig.setCommMode(commMode);
        itsNatConfig.setEventTimeout(-1);
        itsNatConfig.setOnLoadCacheStaticNodes("text/html",onLoadCachingNodesHTML);
        itsNatConfig.setOnLoadCacheStaticNodes("application/xhtml+xml",onLoadCachingNodesHTML);
        itsNatConfig.setOnLoadCacheStaticNodes("image/svg+xml",onLoadCachingNodesSVG);
        itsNatConfig.setOnLoadCacheStaticNodes("application/vnd.mozilla.xul+xml",onLoadCachingNodesXUL);
        itsNatConfig.setOnLoadCacheStaticNodes("text/xml",onLoadCachingNodesXML);
        itsNatConfig.setNodeCacheEnabled(nodeCache);
        itsNatConfig.setUseGZip(useGZip);
        itsNatConfig.setClientErrorMode(clientErrorMode);
        itsNatConfig.setUsePatternMarkupToRender(false);
        itsNatConfig.setAutoCleanEventListeners(autoCleanEventListeners);
        itsNatConfig.setUseXHRSyncOnUnloadEvent(useXHRSyncOnUnloadEvent);

        itsNatConfig.setDefaultDateFormat(DateFormat.getTimeInstance(DateFormat.LONG,Locale.US));
        itsNatConfig.setDefaultNumberFormat(NumberFormat.getNumberInstance(Locale.US));

        
        itsNatServlet.addItsNatServletRequestListener(new TestGlobalDocumentLoadListener());
        itsNatServlet.addEventListener(new TestGlobalEventListener(itsNatServlet));
        itsNatServlet.addItsNatAttachedClientEventListener(new TestGlobalRemoteControlListener());

        String pathPrefix = getServletContext().getRealPath("/") + "/WEB-INF/pages/test/";
        Properties pages = loadProperties(pathPrefix + "pages.properties");

        ItsNatDocumentTemplate docTemplate;


        docTemplate = registerDocument("test_core","text/html",pathPrefix,pages); // "application/xhtml+xml"  "text/html"
        docTemplate.addItsNatServletRequestListener(new TestCoreDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));
        docTemplate.addEventListener(new TestGlobalEventListener(docTemplate));
        //docTemplate.setReferrerEnabled(true);

        docTemplate = registerDocument("test_core_attached_server_launcher","text/html",pathPrefix,pages);  // "application/xhtml+xml"  "text/html"
        // El motivo de este template es para poder generar una página con un template complejo
        // evitando hacer una copia como HTML estático, pero el template tiene <comment> e <include>
        // que no se resuelven en modo "attached server" porque el template "es la página cliente"
        // y en general renderización inicial que no será posible al hacer el attachment.
        docTemplate.addItsNatServletRequestListener(new TestCoreAttachServerLauncherDocLoadListener());
        docTemplate.setScriptingEnabled(false);
        docTemplate.setFastLoadMode(true); // FUNDAMENTAL para añadir los <script> de attachment a la página inicial

        docTemplate = itsNatServlet.registerItsNatDocumentTemplateAttachedServer("test_core_attached_server","text/html");  // "application/xhtml+xml"  "text/html"
        docTemplate.addItsNatServletRequestListener(new TestCoreDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));
        docTemplate.addEventListener(new TestGlobalEventListener(docTemplate));
        docTemplate.setCommMode(CommMode.SCRIPT_HOLD);  // SCRIPT_HOLD será lo normal en attached server

        docTemplate = registerDocument("test_core_iframe","text/html",pathPrefix,pages); // "application/xhtml+xml"  "text/html"
        docTemplate.addItsNatServletRequestListener(new TestCoreIFrameDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));
        //docTemplate.setReferrerEnabled(true);

        docTemplate = registerDocument("test_components","text/html",pathPrefix,pages); // "application/xhtml+xml"  "text/html"
        docTemplate.addItsNatServletRequestListener(new TestComponentsDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));
        docTemplate.addCreateItsNatComponentListener(new TestCreateItsNatComponentListener());
        // docTemplate.setAutoBuildComponents(true);
        docTemplate.registerArtifact("tableComplexStructure",new TestComplexHTMLTableStructure());
        docTemplate.setJoystickMode(joystickMode);
        //docTemplate.setScriptingEnabled(false);
        //docTemplate.setEventsEnabled(false);

        docTemplate = registerDocument("test_client_mutation","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestClientMutationDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_client_mutation_svg","image/svg+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestClientMutationDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_remote_ctrl","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestRemoteCtrlLauncherDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_cache","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestCacheLoadDocListener());

        docTemplate = registerDocument("test_autosync_forms","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestAutoSyncFormsDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_xml","text/xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestXMLDocLoadListener());
        docTemplate.setOnLoadCacheStaticNodes(false);

        docTemplate = registerDocument("test_xml_comp","text/xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestXMLComponentsDocLoadListener());
        docTemplate.setOnLoadCacheStaticNodes(false);
        docTemplate.setAutoBuildComponents(true);


        docTemplate = registerDocument("test_svg","image/svg+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestSVGDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));
        docTemplate.setReferrerEnabled(true);

        docTemplate = registerDocument("test_svg_attached_server_launcher","image/svg+xml",pathPrefix,pages);
        // Ver notas en test_core_attached_server_launcher
        docTemplate.addItsNatServletRequestListener(new TestSVGAttachServerLauncherDocLoadListener());
        docTemplate.setScriptingEnabled(false);
        docTemplate.setFastLoadMode(true);

        docTemplate = itsNatServlet.registerItsNatDocumentTemplateAttachedServer("test_svg_attached_server","image/svg+xml");
        docTemplate.addItsNatServletRequestListener(new TestSVGDocLoadListener());
        docTemplate.setCommMode(CommMode.SCRIPT_HOLD);  // SCRIPT_HOLD será lo normal en attached server


        docTemplate = registerDocument("test_svg_batik","text/html",pathPrefix,pages);
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));
        //docTemplate.setScriptingEnabled(false);

        docTemplate = registerDocument("test_svg_in_xhtml","application/xhtml+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestSVGInXHTMLDocLoadListener());
        docTemplate.setAutoBuildComponents(true); // No sirve para nada, simplemente para que se pase por los elementos svg y detectar que no de error por no ser HTML
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_svg_in_html","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestSVGInXHTMLDocLoadListener());
        docTemplate.setAutoBuildComponents(true); // No sirve para nada, simplemente para que se pase por los elementos svg y detectar que no de error por no ser HTML
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_svg_in_html_asv","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestSVGInXHTMLAdobeSVGDocLoadListener());
        docTemplate.setAutoBuildComponents(true); // No sirve para nada, simplemente para que se pase por los elementos svg y detectar que no de error por no ser HTML
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_svgweb","text/html",pathPrefix,pages); // "text/html"  "application/xhtml+xml"
        docTemplate.addItsNatServletRequestListener(new TestSVGWebDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));


        docTemplate = registerDocument("test_svg_asv_iframe_parent","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestIFrameObjEmbAppletSVGParentDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_svg_asv_object_parent","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestIFrameObjEmbAppletSVGParentDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_svg_asv_embed_parent","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestIFrameObjEmbAppletSVGParentDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_svg_ssrc_object_parent","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestSVGSavareseParentDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_svg_ssrc_embed_parent","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestSVGSavareseParentDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_svg_batik_applet_autob_parent","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestIFrameObjEmbAppletSVGParentDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_svg_batik_object_autob_parent","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestIFrameObjEmbAppletSVGParentDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_svg_batik_embed_autob_parent","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestIFrameObjEmbAppletSVGParentDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));


        docTemplate = registerDocument("test_no_ajax","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestNoAJAXDocLoadListener());
        docTemplate.setEventsEnabled(false);

        docTemplate = registerDocument("test_no_script","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestNoScriptDocLoadListener());
        docTemplate.setScriptingEnabled(false);

        docTemplate = registerDocument("test_referrer_pull","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestReferrerPullDocLoadListener());
        docTemplate.setReferrerEnabled(true);

        docTemplate = registerDocument("test_referrer_pull_next","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestReferrerPullNextDocLoadListener());
        docTemplate.setReferrerEnabled(true);

        docTemplate = registerDocument("test_referrer_push","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestReferrerPushDocLoadListener());
        docTemplate.setReferrerEnabled(true);
        docTemplate.setReferrerPushEnabled(true);

        docTemplate = registerDocument("test_referrer_push_next","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestReferrerPushNextDocLoadListener());
        docTemplate.setReferrerEnabled(true);
        docTemplate.setReferrerPushEnabled(true);

        docTemplate = registerDocument("test_pretty_url","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestPrettyURLDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));
        //docTemplate.setFrameworkScriptFilesBasePath("/itsnat_dev/js");

        docTemplate = registerDocument("test_mobile","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestMobileDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));
        //docTemplate.setReferrerEnabled(true);

        // docTemplate = registerDocument("test_anything","text/html",pathPrefix,pages);
        // docTemplate.addItsNatServletRequestListener(new TestAnythingDocLoadListener());

        docTemplate = registerDocument("test_xul","application/vnd.mozilla.xul+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestXULDocLoadListener());
        docTemplate.setReferrerEnabled(true);
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_xul_attached_server_launcher","application/vnd.mozilla.xul+xml",pathPrefix,pages);
        // Ver notas en test_core_attached_server_launcher
        docTemplate.addItsNatServletRequestListener(new TestXULAttachServerLauncherDocLoadListener());
        docTemplate.setScriptingEnabled(false);
        docTemplate.setFastLoadMode(true);

        docTemplate = itsNatServlet.registerItsNatDocumentTemplateAttachedServer("test_xul_attached_server","application/vnd.mozilla.xul+xml");
        docTemplate.addItsNatServletRequestListener(new TestXULDocLoadListener());
        docTemplate.setCommMode(CommMode.SCRIPT_HOLD);  // SCRIPT_HOLD será lo normal en attached server
        

        docTemplate = registerDocument("test_iframe","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestIFrameHTMLDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = registerDocument("test_svg_bound","image/svg+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestSVGBoundDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));
//docTemplate.setScriptingEnabled(false);

        docTemplate = registerDocument("test_svg_bound_savarese","image/svg+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestSVGBoundSavareseDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));

        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("test_remote_url","text/html","http://www.google.com");
        docTemplate.addItsNatServletRequestListener(new TestRemoteTemplateDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));
        docTemplate.setOnLoadCacheStaticNodes(false);

        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("test_remote_url_result","text/html",new GoogleSearchResultSource());
        docTemplate.addItsNatServletRequestListener(new TestRemoteTemplateResultDocLoadListener());
        //docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));
        docTemplate.setOnLoadCacheStaticNodes(false);
        docTemplate.setEventsEnabled(false); // No tiene estado esta página

        ItsNatDocFragmentTemplate docFragDesc;

        docFragDesc = registerDocFragment("test_html_fragment","text/html",pathPrefix,pages);

        docFragDesc = registerDocFragment("test_html_fragment_fragment","text/html",pathPrefix,pages);

        docFragDesc = registerDocFragment("test_html_with_svg_fragment","text/html",pathPrefix,pages);

        docFragDesc = registerDocFragment("test_xml_fragment","text/xml",pathPrefix,pages);

        docFragDesc = registerDocFragment("test_svg_fragment","image/svg+xml",pathPrefix,pages);

        docFragDesc = registerDocFragment("test_svg_fragment_2","image/svg+xml",pathPrefix,pages);

        docFragDesc = registerDocFragment("test_xul_fragment","application/vnd.mozilla.xul+xml",pathPrefix,pages);

        docFragDesc = registerDocFragment("test_xul_fragment_2","application/vnd.mozilla.xul+xml",pathPrefix,pages);



        //ExtJSExampleLoadApp.init(itsNatServlet, pathPrefix + "/../");
    }

    public ItsNatDocumentTemplate registerDocument(String name,String mime,String pathPrefix, Properties pages)
    {
        return getItsNatHttpServlet().registerItsNatDocumentTemplate(name,mime, pathPrefix + pages.getProperty(name));
    }

    public ItsNatDocFragmentTemplate registerDocFragment(String name,String mime,String pathPrefix, Properties pages)
    {
        return getItsNatHttpServlet().registerItsNatDocFragmentTemplate(name,mime, pathPrefix + pages.getProperty(name));
    }

    public Properties loadProperties(String path)
    {
        Properties pages = new Properties();
        try
        {
            FileInputStream filePages = new FileInputStream(path);
            pages.load(filePages);
            filePages.close();
        }
        catch(IOException ex)
        {
            throw new RuntimeException(ex);
        }
        return pages;
    }

}
