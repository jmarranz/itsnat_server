
package inexp.groovyexrel;

import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.event.ItsNatServletRequestListener;

String pathPrefix = context.getRealPath("/") + "/WEB-INF/groovyexrel/pages/";

ItsNatDocumentTemplate docTemplate;
docTemplate = itsNatServlet.registerItsNatDocumentTemplate("groovyexrel","text/html", pathPrefix + "groovyexrel.html");

ItsNatServletRequestListener listener = inexp.groovyexrel.GProxy.create(new inexp.groovyexrel.GroovyExReloadedLoadListener(), ItsNatServletRequestListener.class);
docTemplate.addItsNatServletRequestListener(listener);

