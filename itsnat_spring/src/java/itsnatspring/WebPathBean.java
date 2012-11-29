package itsnatspring;

import javax.servlet.ServletContext;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author jmarranz
 */
public class WebPathBean implements InitializingBean,ApplicationContextAware
{
    protected ApplicationContext context;
    protected String relativePath;
    protected String absolutePath;

    public String getRelativePath()
    {
        return relativePath;
    }

    public void setRelativePath(String relativePath)
    {
        this.relativePath = relativePath;
    }

    public String getAbsolutePath()
    {
        return absolutePath;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.context = context;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        ItsNatHttpServlet itsNatHttpServlet = ItsNatBeansRegistryUtil.getItsNatHttpServlet(context);
        ServletContext context = itsNatHttpServlet.getHttpServlet().getServletContext();
        this.absolutePath = context.getRealPath("/") + relativePath;
    }
}
