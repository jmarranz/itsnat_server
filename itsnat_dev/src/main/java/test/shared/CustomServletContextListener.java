package test.shared;

import com.innowhere.relproxy.RelProxyOnReloadListener;
import com.innowhere.relproxy.jproxy.JProxy;
import com.innowhere.relproxy.jproxy.JProxyCompilerListener;
import com.innowhere.relproxy.jproxy.JProxyConfig;
import com.innowhere.relproxy.jproxy.JProxyDiagnosticsListener;
import com.innowhere.relproxy.jproxy.JProxyInputSourceFileExcludedListener;
import com.innowhere.relproxy.jproxy.JProxyScriptEngine;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import org.itsnat.core.ItsNat;
import org.itsnat.core.ItsNatBoot;
import org.itsnat.core.ItsNatServletContext;
import test.AnyThingServlet;
import test.ItsNatDroidServletExample;
import test.ItsNatDroidServletNoItsNat;
import test.ItsNatServletExample;

/**
 *
 * @author jmarranz
 */
public class CustomServletContextListener implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        System.out.println("CustomServletContextListener contextInitialized");

        ItsNat itsNat = ItsNatBoot.get();
        ServletContext context = sce.getServletContext();

        SharedInitContextConf.init(context,itsNat);

        // Use example of RelProxy in development time:
        String realPath = context.getRealPath("/"); // NetBeans Maven: /target/itsnat-dev-1.0-SNAPSHOT/ dir
        String inputPath = realPath + "../../src/main/java/";

        boolean gaeEnabled = context.getServerInfo().startsWith("Google App Engine");
        if (gaeEnabled)
        {
            // In GAE we can't call to new File(inputPath).exists() if inputPath is a parent directory of the web app
            System.out.println("WARNING: MOST OF EXAMPLES ARE NOT GOING TO WORK IN GOOGLE APP ENGINE IF SESSION SERIALIZATION IS ENABLED...");
            System.out.println("WARNING: ...BECAUSE STACK SIZE IS TOO SMALL AND SERIALIZING A SINGLE NODE (LIKE IN NodeCacheRegistryImpl)...");
            System.out.println("WARNING: ...USUALLY SERIALIZES A LOT OF RELATED NODES CONSUMING A LOT OF STACK MEMORY (EXPECTED StackOverflowException)...");
            System.out.println("WARNING: ...OTHER PROBLEMS ARE RESTRICTED CLASSES (FOR INSTANCE INTERNAL SWING CLASSES)");
            
            System.out.println("RelProxy disabled, production mode (Google App Engine) detected");            
            return;
        }

        ItsNatServletContext itsNatCtx = itsNat.getItsNatServletContext(context);        
        if (itsNatCtx.isSessionReplicationCapable())
        {
            System.out.println("RelProxy disabled, serialization is enabled (not compatible with RelProxy)");            
            return;
        }
        
        if (new File(inputPath).exists())
        {
            System.out.println("RelProxy to be enabled, development mode detected");
        }
        else
        {
            System.out.println("RelProxy disabled, production mode detected");
            return;
        }


        JProxyInputSourceFileExcludedListener excludedListener = new JProxyInputSourceFileExcludedListener()
        {
            @Override
            public boolean isExcluded(File file, File rootFolderOfSources)
            {
                String absPath = file.getAbsolutePath();

                if (file.isDirectory())
                {
                    return absPath.endsWith(File.separatorChar + "manual") ||
                           absPath.endsWith(File.separatorChar + "test" + File.separatorChar + "shared");
                }
                else
                {
                    return absPath.endsWith(File.separatorChar + AnyThingServlet.class.getSimpleName() + ".java") ||
                           absPath.endsWith(File.separatorChar + ItsNatDroidServletExample.class.getSimpleName() + ".java") ||
                           absPath.endsWith(File.separatorChar + ItsNatDroidServletNoItsNat.class.getSimpleName() + ".java") ||
                           absPath.endsWith(File.separatorChar + ItsNatServletExample.class.getSimpleName() + ".java");
                }
            }
        };


        String classFolder = null; // Optional: context.getRealPath("/") + "/WEB-INF/classes";
        Iterable<String> compilationOptions = Arrays.asList(new String[]{"-source","1.6","-target","1.6"});
        long scanPeriod = 1000;

        RelProxyOnReloadListener proxyListener = new RelProxyOnReloadListener() {
            @Override
            public void onReload(Object objOld, Object objNew, Object proxy, Method method, Object[] args) {
                System.out.println("Reloaded " + objNew + " Calling method: " + method);
            }
        };

        JProxyCompilerListener compilerListener = new JProxyCompilerListener(){
            @Override
            public void beforeCompile(File file)
            {
                System.out.println("Before compile: " + file);
            }

            @Override
            public void afterCompile(File file)
            {
                System.out.println("After compile: " + file);
            }
        };

        JProxyDiagnosticsListener diagnosticsListener = new JProxyDiagnosticsListener()
        {
            @Override
            public void onDiagnostics(DiagnosticCollector<JavaFileObject> diagnostics)
            {
                List<Diagnostic<? extends JavaFileObject>> diagList = diagnostics.getDiagnostics();
                int i = 1;
                for (Diagnostic diagnostic : diagList)
                {
                   System.err.println("Diagnostic " + i);
                   System.err.println("  code: " + diagnostic.getCode());
                   System.err.println("  kind: " + diagnostic.getKind());
                   System.err.println("  line number: " + diagnostic.getLineNumber());
                   System.err.println("  column number: " + diagnostic.getColumnNumber());
                   System.err.println("  start position: " + diagnostic.getStartPosition());
                   System.err.println("  position: " + diagnostic.getPosition());
                   System.err.println("  end position: " + diagnostic.getEndPosition());
                   System.err.println("  source: " + diagnostic.getSource());
                   System.err.println("  message: " + diagnostic.getMessage(null));
                   i++;
                }
            }
        };

        JProxyScriptEngine engine = ItsNatBoot.get().getJProxyScriptEngine();

        JProxyConfig jpConfig = JProxy.createJProxyConfig();
        jpConfig.setEnabled(true)
                .setRelProxyOnReloadListener(proxyListener)
                .setInputPath(inputPath)
                .setJProxyInputSourceFileExcludedListener(excludedListener)
                .setScanPeriod(scanPeriod)
                .setClassFolder(classFolder)
                .setCompilationOptions(compilationOptions)
                .setJProxyCompilerListener(compilerListener)
                .setJProxyDiagnosticsListener(diagnosticsListener);

        engine.init(jpConfig);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        System.out.println("CustomServletContextListener contextDestroyed");
        JProxy.stop();
    }

}
