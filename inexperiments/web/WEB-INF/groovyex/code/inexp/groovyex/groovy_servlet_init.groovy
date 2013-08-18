
package inexp.groovyex;

import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.event.ItsNatServletRequestListener;
import java.lang.reflect.Method;
import inexp.groovyex.GProxy;
import inexp.groovyex.GProxyListener;


GProxy.init(servlet.getGroovyScriptEngine(),true,
    { 
        Object objOld,Object objNew,Object proxy, Method method, Object[] args -> 
        println("Reloaded " + objNew + " Calling method: " + method)
    } as GProxyListener);


FalseDB db = new FalseDB();

String pathPrefix = context.getRealPath("/") + "/WEB-INF/groovyex/pages/";

ItsNatDocumentTemplate docTemplate;
docTemplate = itsNatServlet.registerItsNatDocumentTemplate("groovyex","text/html", pathPrefix + "groovyex.html");

ItsNatServletRequestListener listener = inexp.groovyex.GProxy.create(new inexp.groovyex.GroovyExampleLoadListener(db), ItsNatServletRequestListener.class);
docTemplate.addItsNatServletRequestListener(listener);

