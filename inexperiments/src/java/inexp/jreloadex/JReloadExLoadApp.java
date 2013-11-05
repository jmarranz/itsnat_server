package inexp.jreloadex;

import inexp.jreloadex.jproxy.JProxyListener;
import inexp.jreloadex.jproxy.JProxy;
import java.lang.reflect.Method;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.event.ItsNatServletRequestListener;

/**
 *
 * @author jmarranz
 */
public class JReloadExLoadApp
{
    public static void init(ItsNatHttpServlet itsNatServlet,ServletConfig config)
    {    
        ServletContext context = itsNatServlet.getItsNatServletContext().getServletContext();
        String pathInput = context.getRealPath("/") + "/WEB-INF/jreloadex/code/";           
        String classFolder = null; // context.getRealPath("/") + "/WEB-INF/classes";
        
        JProxy.init(true, pathInput,classFolder, 200, new JProxyListener() {
            public void onReload(Object objOld, Object objNew, Object proxy, Method method, Object[] args) {
                System.out.println("Reloaded " + objNew + " Calling method: " + method);
            }
        });
        
        FalseDB db = new FalseDB();

        String pathPrefix = context.getRealPath("/") + "/WEB-INF/jreloadex/pages/";

        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("jreloadex","text/html", pathPrefix + "jreloadex.html");

        ItsNatServletRequestListener listener = JProxy.create(new inexp.jreloadex.JReloadExampleLoadListener(db), ItsNatServletRequestListener.class);
        docTemplate.addItsNatServletRequestListener(listener);
    } 
}




