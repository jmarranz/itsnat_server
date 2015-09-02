/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee. The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.*;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.feashow.GlobalItsNatServletRequestListener;
import org.itsnat.core.ClientErrorMode;
import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.feashow.FeatureShowcaseDocumentLoadListener;
import org.itsnat.feashow.GlobalEventListener;
import org.itsnat.feashow.IndexDocumentLoadListener;
import org.itsnat.feashow.features.comp.degraded.DisabledEventsAndCompLoadListener;
import org.itsnat.feashow.features.comp.degraded.DisabledScriptAndCompLoadListener;
import org.itsnat.feashow.features.comp.other.custom.LoginCreationItsNatComponentListener;
import org.itsnat.feashow.features.comp.layers.ModalLayerSVGLoadListener;
import org.itsnat.feashow.features.comp.layers.ModalLayerXULLoadListener;
import org.itsnat.feashow.features.comp.lists.FreeListSVGLoadListener;
import org.itsnat.feashow.features.comp.tables.FreeTableSVGLoadListener;
import org.itsnat.feashow.features.comp.xmlcomp.XMLAndCompLoadListener;
import org.itsnat.feashow.features.core.misc.remctrl.RemoteControlSupervision;
import org.itsnat.feashow.features.core.misc.remctrl.RemCtrlDocNotFoundLoadListener;
import org.itsnat.feashow.features.core.misc.remctrl.RemCtrlReqRejectedLoadListener;
import org.itsnat.feashow.features.core.degraded.DisabledEventsLoadListener;
import org.itsnat.feashow.features.core.degraded.DisabledScriptLoadListener;
import org.itsnat.feashow.features.core.ioeaauto.ChildHTMLAutoBindingLoadListener;
import org.itsnat.feashow.features.core.ioeaauto.ChildSVGASVAutoBindingLoadListener;
import org.itsnat.feashow.features.core.ioeaauto.ChildSVGBatikAutoBindingLoadListener;
import org.itsnat.feashow.features.core.ioeaauto.ChildSVGSsrcAutoBindingLoadListener;
import org.itsnat.feashow.features.core.misc.PrettyURLLoadListener;
import org.itsnat.feashow.features.core.misc.remtmpl.GoogleResultTemplateSource;
import org.itsnat.feashow.features.core.misc.remtmpl.RemoteTemplateDocLoadListener;
import org.itsnat.feashow.features.core.misc.remtmpl.RemoteTemplateResultDocLoadListener;
import org.itsnat.feashow.features.core.otherns.SVGInHTMLMimeSVGWebLoadListener;
import org.itsnat.feashow.features.core.referrer.ReferrerPullLoadListener;
import org.itsnat.feashow.features.core.referrer.ReferrerPullResultLoadListener;
import org.itsnat.feashow.features.core.referrer.ReferrerPushLoadListener;
import org.itsnat.feashow.features.core.referrer.ReferrerPushResultLoadListener;
import org.itsnat.feashow.features.core.otherns.SVGInXHTMLMimeLoadListener;
import org.itsnat.feashow.features.core.otherns.XMLExampleLoadListener;
import org.itsnat.feashow.features.core.otherns.SVGPureLoadListener;
import org.itsnat.feashow.features.core.otherns.XULPureLoadListener;
import org.itsnat.feashow.features.stless.comp.StlessFreeListExampleInitialDocLoadListener;
import org.itsnat.manual.core.CoreExampleLoadListener;

public class feashow_servlet extends HttpServletWrapper
{
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();
        ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();

        ItsNatServletContext itsNatCtx = itsNatConfig.getItsNatServletContext();
        itsNatCtx.setMaxOpenDocumentsBySession(6); // To limit the memory of bots identified as legitimate browsers and abusive users
        // http://radomirmladenovic.info/2009/06/15/detecting-code-execution-on-google-app-engine
        boolean gaeEnabled = getServletContext().getServerInfo().startsWith("Google App Engine");

        itsNatCtx.setSessionReplicationCapable(gaeEnabled);
        //itsNatCtx.setSessionSerializeCompressed(false);
        //itsNatCtx.setSessionExplicitSerialize(false);
        
        itsNatConfig.setMaxOpenClientsByDocument(5); // To avoid abusive users in remote/view control
        itsNatConfig.setClientErrorMode(ClientErrorMode.NOT_CATCH_ERRORS); // ClientErrorMode.NOT_CATCH_ERRORS, SHOW_SERVER_AND_CLIENT_ERRORS
        itsNatConfig.setEventTimeout(10*60*1000); // 10 minutes
        itsNatConfig.setDefaultDateFormat(DateFormat.getDateInstance(DateFormat.DEFAULT,Locale.US));
        itsNatConfig.setDefaultNumberFormat(NumberFormat.getInstance(Locale.US));
        itsNatConfig.setEventDispatcherMaxWait(10*60*1000);  // 10 minutes
        
        itsNatServlet.addItsNatServletRequestListener(new GlobalItsNatServletRequestListener());
        itsNatServlet.addEventListener(new GlobalEventListener());
        itsNatServlet.addItsNatAttachedClientEventListener(new RemoteControlSupervision());

        featureShowcase();
        examplesInManual();
    }

    public void featureShowcase()
    {
        String pathPrefix = getServletContext().getRealPath("/");
        pathPrefix += "/WEB-INF/pages/feashow/";

        ItsNatDocumentTemplate docTemplate;

        // Pages

        Properties pages = loadProperties(pathPrefix + "pages.properties");

        docTemplate = registerItsNatDocumentTemplate("feashow.index","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new IndexDocumentLoadListener());
        docTemplate.setScriptingEnabled(false);

        docTemplate = registerItsNatDocumentTemplate("feashow.main","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new FeatureShowcaseDocumentLoadListener());
        docTemplate.setAutoBuildComponents(false);
        docTemplate.addCreateItsNatComponentListener(new LoginCreationItsNatComponentListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.docNotFound","text/html",pathPrefix,pages);
        docTemplate.setScriptingEnabled(false);

        // Core

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.ioeaauto.iframeHTMLAutoBindingExample","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new ChildHTMLAutoBindingLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.ioeaauto.ioeSVGASVAutoBindingExample","image/svg+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new ChildSVGASVAutoBindingLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.ioeaauto.oeSVGSsrcAutoBindingExample","image/svg+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new ChildSVGSsrcAutoBindingLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.ioeaauto.oeaSVGBatikAutoBindingExample","image/svg+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new ChildSVGBatikAutoBindingLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.otherns.svgInXHTMLMimeExample","application/xhtml+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new SVGInXHTMLMimeLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.otherns.svgInHTMLMimeSVGWebExample","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new SVGInHTMLMimeSVGWebLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.otherns.svgPureExample","image/svg+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new SVGPureLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.otherns.xulPureExample","application/vnd.mozilla.xul+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new XULPureLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.otherns.xmlExample","text/xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new XMLExampleLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.referrer.pullExample","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new ReferrerPullLoadListener());
        docTemplate.setReferrerEnabled(true);

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.referrer.pullExampleResult","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new ReferrerPullResultLoadListener());
        docTemplate.setReferrerEnabled(true);

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.referrer.pushExample","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new ReferrerPushLoadListener());
        docTemplate.setReferrerEnabled(true);
        docTemplate.setReferrerPushEnabled(true);

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.referrer.pushExampleResult","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new ReferrerPushResultLoadListener());
        docTemplate.setReferrerEnabled(true);
        docTemplate.setReferrerPushEnabled(true);

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.degraded.disabledEventsExample","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new DisabledEventsLoadListener());
        docTemplate.setEventsEnabled(false);

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.degraded.disabledScriptExample","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new DisabledScriptLoadListener());
        docTemplate.setScriptingEnabled(false);

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.emash.extremeMashupHTMLMimeExample","text/html",pathPrefix,pages);
        docTemplate.setScriptingEnabled(false);

        docTemplate = registerItsNatDocumentTemplateAttachedServer("feashow.ext.core.emash.extremeMashupHTMLMimeExampleProcessor","text/html");
        docTemplate.addItsNatServletRequestListener(new CoreExampleLoadListener());
        docTemplate.setCommMode(CommMode.SCRIPT_HOLD);

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.emash.extremeMashupXHTMLMimeExample","application/xhtml+xml",pathPrefix,pages);
        docTemplate.setScriptingEnabled(false);

        docTemplate = registerItsNatDocumentTemplateAttachedServer("feashow.ext.core.emash.extremeMashupXHTMLMimeExampleProcessor","application/xhtml+xml");
        docTemplate.addItsNatServletRequestListener(new CoreExampleLoadListener());
        docTemplate.setCommMode(CommMode.SCRIPT_HOLD);

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.misc.prettyURLExample","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new PrettyURLLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.misc.remCtrlDocNotFound","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new RemCtrlDocNotFoundLoadListener());
        docTemplate.setScriptingEnabled(false);

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.core.misc.remCtrlReqRejected","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new RemCtrlReqRejectedLoadListener());
        docTemplate.setScriptingEnabled(false);

        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("feashow.ext.core.misc.remoteTemplateExample","text/html","http://www.google.com");
        docTemplate.addItsNatServletRequestListener(new RemoteTemplateDocLoadListener());
        docTemplate.setOnLoadCacheStaticNodes(false);

        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("feashow.ext.core.misc.remoteTemplateExampleResult","text/html",new GoogleResultTemplateSource());
        docTemplate.addItsNatServletRequestListener(new RemoteTemplateResultDocLoadListener());
        docTemplate.setOnLoadCacheStaticNodes(false);
        docTemplate.setEventsEnabled(false);

        // Components

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.comp.lists.freeListSVGEmbExample","application/xhtml+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new FreeListSVGLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.comp.lists.freeListSVGPureExample","image/svg+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new FreeListSVGLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.comp.tables.freeTableSVGEmbExample","application/xhtml+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new FreeTableSVGLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.comp.layers.modalLayerSVGExample","image/svg+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new ModalLayerSVGLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.comp.layers.modalLayerXULExample","application/vnd.mozilla.xul+xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new ModalLayerXULLoadListener());

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.comp.degraded.disabledEventsAndCompExample","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new DisabledEventsAndCompLoadListener());
        docTemplate.setEventsEnabled(false);

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.comp.degraded.disabledScriptAndCompExample","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new DisabledScriptAndCompLoadListener());
        docTemplate.setScriptingEnabled(false);

        docTemplate = registerItsNatDocumentTemplate("feashow.ext.comp.xmlAndCompExample","text/xml",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new XMLAndCompLoadListener());

        // Stateless        
        
        docTemplate = registerItsNatDocumentTemplate("feashow.ext.stless.comp.freeListExample","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new StlessFreeListExampleInitialDocLoadListener());
        docTemplate.setEventsEnabled(false);                 
        
        // HTML fragments

        ItsNatDocFragmentTemplate docFragTemplate;

        Properties htmlFragments = loadProperties(pathPrefix + "html_fragments.properties");
        for(Enumeration fragmentNames = htmlFragments.propertyNames(); fragmentNames.hasMoreElements(); )
        {
            String name = (String)fragmentNames.nextElement();
            docFragTemplate = registerItsNatDocFragmentTemplate(name,"text/html",pathPrefix,htmlFragments);
        }

        // XML fragments

        Properties xmlFragments = loadProperties(pathPrefix + "xml_fragments.properties");
        for(Enumeration fragmentNames = xmlFragments.propertyNames(); fragmentNames.hasMoreElements(); )
        {
            String name = (String)fragmentNames.nextElement();
            docFragTemplate = registerItsNatDocFragmentTemplate(name,"text/xml",pathPrefix,xmlFragments);
        }
    }

    public void examplesInManual()
    {
        // ONLY for documentation purposes (to load files to the SyntaxHighlighter)

        String pathPrefix = getServletContext().getRealPath("/");
        pathPrefix += "/WEB-INF/pages/manual/";

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();

        ItsNatDocumentTemplate docTemplate;
        docTemplate = registerItsNatDocumentTemplate("manual.core.example", "text/html", pathPrefix, "core_example.html");
        docTemplate = registerItsNatDocumentTemplate("manual.core.xmlExample","text/xml",  pathPrefix, "xml_example.xml");
        docTemplate = registerItsNatDocumentTemplate("manual.comp.example",   "text/html", pathPrefix, "comp_example.html");        
        docTemplate = registerItsNatDocumentTemplate("manual.stless.example", "text/xml",  pathPrefix, "stless_example.html");
        docTemplate = registerItsNatDocumentTemplate("manual.stless.example.eventReceiver", "text/xml", pathPrefix, "stless_example_event_receiver.html");        
        
        ItsNatDocFragmentTemplate docFragTemplate;
        docFragTemplate = registerItsNatDocFragmentTemplate("manual.core.xmlFragExample","text/xml",pathPrefix, "xml_fragment_example.xml");
        docFragTemplate = registerItsNatDocFragmentTemplate("manual.stless.example.fragment","text/xml",pathPrefix, "stless_example_fragment.html"); 
    }

    public Properties loadProperties(String path)
    {
        Properties props = new Properties();
        try
        {
            FileInputStream file = new FileInputStream(path);
            props.load(file);
            file.close();
        }
        catch(IOException ex)
        {
            throw new RuntimeException(ex);
        }
        return props;
    }

    public ItsNatDocumentTemplate registerItsNatDocumentTemplate(String name,String mime,String pathPrefix,Properties paths)
    {
        String relPath = paths.getProperty(name);
        if (relPath == null) throw new RuntimeException("Not found in properties file:" + name);
        return registerItsNatDocumentTemplate(name,mime,pathPrefix,relPath);
    }

    public ItsNatDocumentTemplate registerItsNatDocumentTemplate(String name,String mime,String pathPrefix,String relPath)
    {
        String path = pathPrefix + relPath;
        if (!new File(path).exists()) throw new RuntimeException("Not found file:" + path);
        return getItsNatHttpServlet().registerItsNatDocumentTemplate(name,mime, path);
    }    
    
    public ItsNatDocFragmentTemplate registerItsNatDocFragmentTemplate(String name,String mime,String pathPrefix,Properties paths)
    {
        String relPath = paths.getProperty(name);
        if (relPath == null) throw new RuntimeException("Not found in properties file:" + name);
        return registerItsNatDocFragmentTemplate(name,mime,pathPrefix,relPath);
    }

    public ItsNatDocFragmentTemplate registerItsNatDocFragmentTemplate(String name,String mime,String pathPrefix,String relPath)
    {
        String path = pathPrefix + relPath;
        if (!new File(path).exists()) throw new RuntimeException("Not found file:" + path);
        return getItsNatHttpServlet().registerItsNatDocFragmentTemplate(name,mime, path);
    }    
    
    public ItsNatDocumentTemplate registerItsNatDocumentTemplateAttachedServer(String name,String mime)
    {
        return getItsNatHttpServlet().registerItsNatDocumentTemplateAttachedServer(name,mime);
    }
}
