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
import test.droid.TestDroidDocLoadListener;
import test.droid.TestDroidGlobalEventListener;

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
        
        String pathPrefix = getServletContext().getRealPath("/") + "/WEB-INF/pages/test/";
        Properties pages = loadProperties(pathPrefix + "pages.properties");        
        
        ItsNatDocumentTemplate docTemplate;        
        
        docTemplate = registerDocument("test_droid","android/layout",pathPrefix,pages); 
        docTemplate.addItsNatServletRequestListener(new TestDroidDocLoadListener());
        //docTemplate.addEventListener(new TestCoreStatelessTemplateLevelEventListener(docTemplate));        
        // docTemplate.setEventsEnabled(false);        
        docTemplate.setFastLoadMode(false);        
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
