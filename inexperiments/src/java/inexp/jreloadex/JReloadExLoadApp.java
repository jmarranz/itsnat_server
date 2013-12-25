package inexp.jreloadex;


import com.innowhere.relproxy.RelProxyOnReloadListener;
import com.innowhere.relproxy.jproxy.JProxy;
import com.innowhere.relproxy.jproxy.JProxyConfig;
import com.innowhere.relproxy.jproxy.JProxyDiagnosticsListener;
import java.lang.reflect.Method;
import java.util.Arrays;
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
        String inputPath = context.getRealPath("/") + "/WEB-INF/jreloadex/code/";           
        String classFolder = null; // context.getRealPath("/") + "/WEB-INF/classes";
        Iterable<String> compilationOptions = Arrays.asList(new String[]{"-source","1.6","-target","1.6"});
        long scanPeriod = 300;        
        JProxyDiagnosticsListener diagnosticsListener = null;
        
        RelProxyOnReloadListener proxyListener = new RelProxyOnReloadListener() {
            public void onReload(Object objOld, Object objNew, Object proxy, Method method, Object[] args) {
                System.out.println("Reloaded " + objNew + " Calling method: " + method);
            }        
        };            
                    
        JProxyConfig jpConfig = JProxy.createJProxyConfig();
        jpConfig.setEnabled(true)
                .setRelProxyOnReloadListener(proxyListener)
                .setInputPath(inputPath)
                .setScanPeriod(scanPeriod)
                .setClassFolder(classFolder)
                .setCompilationOptions(compilationOptions)
                .setJProxyDiagnosticsListener(diagnosticsListener);        
        
        JProxy.init(jpConfig);
        
        
        
        FalseDB db = new FalseDB();

        String pathPrefix = context.getRealPath("/") + "/WEB-INF/jreloadex/pages/";

        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("jreloadex","text/html", pathPrefix + "jreloadex.html");

        ItsNatServletRequestListener listener = JProxy.create(new inexp.jreloadex.JReloadExampleLoadListener(db), ItsNatServletRequestListener.class);
        docTemplate.addItsNatServletRequestListener(listener);
    } 
}




