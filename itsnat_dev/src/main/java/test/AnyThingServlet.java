/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import test.shared.SharedInitContextConf;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import test.web.anything.TestAnythingDocLoadListener;

/**
 *
 * @author jmarranz
 */
public class AnyThingServlet extends HttpServletWrapper
{
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        int maxOpenClientsByDocument = 4; // incluye el owner
        
        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();

        ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();
        itsNatConfig.setMaxOpenClientsByDocument(maxOpenClientsByDocument);


        String pathPrefix = getServletContext().getRealPath("/") + "/WEB-INF/pages/web/test/";
        Properties pages = loadProperties(pathPrefix + "pages.properties");

        ItsNatDocumentTemplate docTemplate;
        docTemplate = registerDocument("test_anything","text/html",pathPrefix,pages);
        docTemplate.addItsNatServletRequestListener(new TestAnythingDocLoadListener());
        docTemplate.setFastLoadMode(false);
    }

    public ItsNatDocumentTemplate registerDocument(String name,String mime,String pathPrefix, Properties pages)
    {
        return getItsNatHttpServlet().registerItsNatDocumentTemplate(name,mime, pathPrefix + pages.getProperty(name));
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
