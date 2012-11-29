package itsnatspring;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 *
 * @author jmarranz
 */
public class itsnatservlet extends HttpServletWrapper
{
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();

        GenericApplicationContext rootContext = new GenericApplicationContext();
        ItsNatBeansRegistryUtil.registerSingletons(rootContext, itsNatServlet);
        rootContext.refresh();

        String springXMLPath = config.getInitParameter("spring_config");
        if (springXMLPath == null)
            throw new RuntimeException("spring_config initialization parameter is not specified in web.xml");

        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {springXMLPath},rootContext);
    }

}
