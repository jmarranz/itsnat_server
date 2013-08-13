
package inexp.groovyex;

import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.event.ItsNatServletRequestListener;

String pathPrefix = context.getRealPath("/") + "/WEB-INF/groovyex/pages/";

ItsNatDocumentTemplate docTemplate;
docTemplate = itsNatServlet.registerItsNatDocumentTemplate("groovyex","text/html", pathPrefix + "groovyex.html");
ItsNatServletRequestListener listener = new inexp.groovyex.GroovyExampleLoadListener();
docTemplate.addItsNatServletRequestListener(listener);

