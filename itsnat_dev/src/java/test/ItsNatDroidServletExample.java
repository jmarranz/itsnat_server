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
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import test.droid.core.TestDroidCoreDocLoadListener;
import test.droid.core.TestDroidGlobalEventListener;
import test.droid.remotectrl.TestDroidRemoteControlListener;
import test.droid.remotectrl.TestDroidRemoteCtrlLauncherDocLoadListener;

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
        //ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();        
        
        //itsNatServlet.addItsNatServletRequestListener(new TestDroidGlobalDocumentLoadListener());
        itsNatServlet.addEventListener(new TestDroidGlobalEventListener(itsNatServlet));
        //itsNatServlet.addItsNatAttachedClientEventListener(new TestDroidGlobalRemoteControlListener());        
        
        String pathPrefix = getServletContext().getRealPath("/") + "/WEB-INF/pages/droid/test/";
        Properties pages = loadProperties(pathPrefix + "pages.properties");        
        
        ItsNatDocumentTemplate docTemplate;        
        
        docTemplate = registerDocument("test_droid_core","android/layout",pathPrefix,pages); 
        docTemplate.addItsNatServletRequestListener(new TestDroidCoreDocLoadListener());
        //docTemplate.addEventListener(new TestCoreStatelessTemplateLevelEventListener(docTemplate));              
        docTemplate.setFastLoadMode(true);    
        docTemplate.setReferrerEnabled(true);
        //docTemplate.setReferrerPushEnabled(true);
        
        docTemplate = registerDocument("test_droid_remote_ctrl","android/layout",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestDroidRemoteCtrlLauncherDocLoadListener());
        docTemplate.addItsNatAttachedClientEventListener(new TestDroidRemoteControlListener(false));        
        
        
        ItsNatDocFragmentTemplate docFragDesc;

        docFragDesc = registerDocFragment("test_droid_core_fragment","android/layout",pathPrefix,pages);        
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
