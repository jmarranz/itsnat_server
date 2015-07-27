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

package org.itsnat.manual.core;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.servlet.*;
import org.itsnat.core.ClientErrorMode;
import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.UseGZip;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;

public class servlet extends HttpServletWrapper
{
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();

        ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();

        ItsNatServletContext itsNatCtx = itsNatConfig.getItsNatServletContext();
        itsNatCtx.setMaxOpenDocumentsBySession(6);

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
        itsNatConfig.setDefaultDateFormat(DateFormat.getDateInstance(DateFormat.DEFAULT,Locale.US));
        itsNatConfig.setDefaultNumberFormat(NumberFormat.getInstance(Locale.US));
        itsNatConfig.setEventDispatcherMaxWait(0);
        itsNatConfig.setEventsEnabled(true);
        itsNatConfig.setScriptingEnabled(true);
        itsNatConfig.setUsePatternMarkupToRender(false);
        itsNatConfig.setAutoCleanEventListeners(true);
        itsNatConfig.setUseXHRSyncOnUnloadEvent(true);

        String pathPrefix = getServletContext().getRealPath("/");
        pathPrefix += "/WEB-INF/pages/manual/";

        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("manual.core.example","text/html", pathPrefix + "core_example.html");
        docTemplate.addItsNatServletRequestListener(new CoreExampleLoadListener());

        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("manual.core.xmlExample","text/xml", pathPrefix + "xml_example.xml");
        docTemplate.addItsNatServletRequestListener(new CoreXMLExampleLoadListener());

        ItsNatDocFragmentTemplate docFragTemplate;
        docFragTemplate = itsNatServlet.registerItsNatDocFragmentTemplate("manual.core.xmlFragExample","text/xml",pathPrefix + "xml_fragment_example.xml");
    }

}
