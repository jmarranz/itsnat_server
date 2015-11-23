/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.tmpl.ItsNatDroidDocumentTemplate;
import test.droid.comp.TestDroidComponentsDocLoadListener;
import test.droid.comp.TestDroidCreateItsNatComponentListener;
import test.droid.core.TestDroidCoreDocLoadListener;
import test.droid.drawable.TestDroidDrawableLoadListener;
import test.droid.shared.TestDroidGlobalEventListener;
import test.droid.remotectrl.TestDroidRemoteControlListener;
import test.droid.remotectrl.TestDroidRemoteCtrlLauncherDocLoadListener;
import test.droid.remres.TestDroidRemoteResourcesDocLoadListener;
import test.droid.shared.TestDroidGlobalDocumentLoadListener;
import test.droid.shared.TestDroidGlobalRemoteControlListener;
import test.droid.stateless.core.TestDroidStatelessCoreEventDocLoadListener;
import test.droid.stateless.core.TestDroidStatelessCoreInitialDocLoadListener;
import test.droid.stateless.core.TestDroidStatelessCoreTemplateLevelEventListener;

/**
 *
 * @author jmarranz
 */
public class ItsNatDroidServletExample extends HttpServletWrapper
{
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();
        ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();        
        
        itsNatConfig.setBitmapDensityReference(320);
        
        itsNatServlet.addItsNatServletRequestListener(new TestDroidGlobalDocumentLoadListener());
        itsNatServlet.addEventListener(new TestDroidGlobalEventListener(itsNatServlet));
        itsNatServlet.addItsNatAttachedClientEventListener(new TestDroidGlobalRemoteControlListener());        
      
        
        String pathPrefix = getServletContext().getRealPath("/") + "/WEB-INF/pages/droid/test/";
        Properties pages = loadProperties(pathPrefix + "pages.properties");        
        
        ItsNatDocumentTemplate docTemplate;        
        
        docTemplate = registerDocument("test_droid_core","android/layout",pathPrefix,pages); 
        docTemplate.addItsNatServletRequestListener(new TestDroidCoreDocLoadListener());
        //docTemplate.addEventListener(new TestCoreStatelessTemplateLevelEventListener(docTemplate));              
        docTemplate.addItsNatAttachedClientEventListener(new TestDroidRemoteControlListener(false));        
        docTemplate.setFastLoadMode(true);    
        docTemplate.setReferrerEnabled(true);
        //docTemplate.setReferrerPushEnabled(true);
        //docTemplate.setLoadScriptInline(false);
        //docTemplate.setEventsEnabled(false);
        //docTemplate.setScriptingEnabled(false);        
        //docTemplate.setCommMode(CommMode.XHR_SYNC);
        
        docTemplate = registerDocument("test_droid_remote_resources","android/layout",pathPrefix,pages);        
        docTemplate.addItsNatServletRequestListener(new TestDroidRemoteResourcesDocLoadListener());        
        ((ItsNatDroidDocumentTemplate)docTemplate).setBitmapDensityReference(320);
        
        docTemplate = registerDocument("test_droid_remote_drawable","text/xml",pathPrefix,pages);        
        docTemplate.addItsNatServletRequestListener(new TestDroidDrawableLoadListener());              
                
        
        docTemplate = registerDocument("test_droid_remote_ctrl","android/layout",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestDroidRemoteCtrlLauncherDocLoadListener());
        ((ItsNatDroidDocumentTemplate)docTemplate).setBitmapDensityReference(320);        
        
        // Stateless main
        
        docTemplate = registerDocument("test_droid_stateless_core_initial","android/layout",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestDroidStatelessCoreInitialDocLoadListener());       
        docTemplate.setEventsEnabled(false);
        ((ItsNatDroidDocumentTemplate)docTemplate).setBitmapDensityReference(320);
        
        // Stateless to load fragment       
        docTemplate = registerDocument("test_droid_stateless_core_event","android/layout",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestDroidStatelessCoreEventDocLoadListener());
        docTemplate.addEventListener(new TestDroidStatelessCoreTemplateLevelEventListener(docTemplate));        
        docTemplate.setEventsEnabled(false);
        ((ItsNatDroidDocumentTemplate)docTemplate).setBitmapDensityReference(320);        
        
        // Components
        docTemplate = registerDocument("test_droid_components","android/layout",pathPrefix,pages); 
        docTemplate.addItsNatServletRequestListener(new TestDroidComponentsDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestDroidRemoteControlListener(false));
        docTemplate.addCreateItsNatComponentListener(new TestDroidCreateItsNatComponentListener());
        // docTemplate.setAutoBuildComponents(true);
        //docTemplate.registerArtifact("tableComplexStructure",new TestComplexHTMLTableStructure());
        //docTemplate.setJoystickMode(joystickMode);
        //docTemplate.setScriptingEnabled(false);
        //docTemplate.setEventsEnabled(false);        
        ((ItsNatDroidDocumentTemplate)docTemplate).setBitmapDensityReference(320);        
        
    
        
        
        // Attached server
        
        /*
        docTemplate = registerDocument("test_droid_core_attached_server_launcher","android/layout",pathPrefix,pages);  // El motivo de este template es para poder generar una página con un template complejo evitando hacer una copia como estático
        docTemplate.addItsNatServletRequestListener(new TestDroidCoreAttachServerLauncherDocLoadListener());
        docTemplate.setScriptingEnabled(false);
        docTemplate.setFastLoadMode(true); // REVISAR ?????? FUNDAMENTAL para añadir los <script> de attachment a la página inicial        
        
        docTemplate = itsNatServlet.registerItsNatDocumentTemplateAttachedServer("test_droid_core_attached_server","android/layout");  
        docTemplate.addItsNatServletRequestListener(new TestCoreDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestRemoteControlListener(false));
        docTemplate.addEventListener(new TestGlobalEventListener(docTemplate));
        //docTemplate.setCommMode(CommMode.SCRIPT_HOLD);        
        */
        
        // Fragments 
        
        ItsNatDocFragmentTemplate docFragDesc;

        docFragDesc = registerDocFragment("test_droid_core_fragment","android/layout",pathPrefix,pages);        
        docFragDesc = registerDocFragment("test_droid_remote_resources_fragment","android/layout",pathPrefix,pages);         
    }
  
    public ItsNatDocumentTemplate registerDocument(String name,String mime,String pathPrefix, Properties pages)
    {
        String fileName = pages.getProperty(name);
        if (fileName == null) throw new RuntimeException("Template with name " + name + " not found");
        return getItsNatHttpServlet().registerItsNatDocumentTemplate(name,mime, pathPrefix + fileName);
    }

    public ItsNatDocFragmentTemplate registerDocFragment(String name,String mime,String pathPrefix, Properties pages)
    {
        String fileName = pages.getProperty(name);
        if (fileName == null) throw new RuntimeException("Template with name " + name + " not found");        
        return getItsNatHttpServlet().registerItsNatDocFragmentTemplate(name,mime, pathPrefix + fileName);
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
