package itsnatspring;

import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class ItsNatBeansRegistryUtil
{
    public final static String itsNatHttpServletBean = "itsNatHttpServlet";
    public final static String itsNatServletConfigBean = "itsNatServletConfig";
    public final static String itsNatServletContextBean = "itsNatServletContext";

    public static ItsNatHttpServlet getItsNatHttpServlet(ApplicationContext context)
    {
        return context.getBean(itsNatHttpServletBean,ItsNatHttpServlet.class);
    }

    public static ItsNatServletConfig getItsNatServletConfig(ApplicationContext context)
    {
        return context.getBean(itsNatServletConfigBean,ItsNatServletConfig.class);
    }

    public static ItsNatServletContext getItsNatServletContext(ApplicationContext context)
    {
        return context.getBean(itsNatServletContextBean,ItsNatServletContext.class);
    }    

    public static void registerSingletons(AbstractApplicationContext context,ItsNatHttpServlet itsNatServlet)
    {
        ItsNatServletConfig itsNatServletCofig = itsNatServlet.getItsNatServletConfig();
        ItsNatServletContext itsNatServletContext = itsNatServletCofig.getItsNatServletContext();
        
        ConfigurableListableBeanFactory beanFact = context.getBeanFactory();
        beanFact.registerSingleton(itsNatHttpServletBean,itsNatServlet);
        beanFact.registerSingleton(itsNatServletConfigBean,itsNatServletCofig);
        beanFact.registerSingleton(itsNatServletContextBean,itsNatServletContext);
    }
}
