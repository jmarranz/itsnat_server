package itsnatspring;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Properties;
import org.itsnat.core.ItsNatServletConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author jmarranz
 */
public class ItsNatServletConfigBean implements InitializingBean,ApplicationContextAware
{
    protected ApplicationContext context;
    protected Boolean debugMode;
    protected Integer clientErrorMode;
    protected String defaultEncoding;    
    protected Properties onLoadCacheStaticNodes = new Properties();
    protected DateFormat defaultDateFormat;
    protected NumberFormat defaultNumberFormat;

    public ItsNatServletConfigBean()
    {
    }

    public boolean getDebugMode()
    {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode)
    {
        this.debugMode = debugMode;
    }

    public int getClientErrorMode()
    {
        return clientErrorMode;
    }

    public void setClientErrorMode(int clientErrorMode)
    {
        this.clientErrorMode = clientErrorMode;
    }

    public String getDefaultEncoding()
    {
        return defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding)
    {
        this.defaultEncoding = defaultEncoding;
    }

    public Properties getOnLoadCacheStaticNodes()
    {
        return onLoadCacheStaticNodes;
    }

    public void setOnLoadCacheStaticNodes(Properties onLoadCacheStaticNodes)
    {
        this.onLoadCacheStaticNodes = onLoadCacheStaticNodes;
    }

    public DateFormat getDefaultDateFormat()
    {
        return defaultDateFormat;
    }

    public void setDefaultDateFormat(DateFormat defaultDateFormat)
    {
        this.defaultDateFormat = defaultDateFormat;
    }

    public NumberFormat getDefaultNumberFormat()
    {
        return defaultNumberFormat;
    }

    public void setDefaultNumberFormat(NumberFormat defaultNumberFormat)
    {
        this.defaultNumberFormat = defaultNumberFormat;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.context = context;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        ItsNatServletConfig itsNatServletConfig = ItsNatBeansRegistryUtil.getItsNatServletConfig(context);
        if (debugMode != null)
            itsNatServletConfig.setDebugMode(debugMode);
        if (clientErrorMode != null)
            itsNatServletConfig.setClientErrorMode(clientErrorMode);     
        if (defaultEncoding != null)
            itsNatServletConfig.setDefaultEncoding(defaultEncoding);        
        if (onLoadCacheStaticNodes != null)
        {
            for(Enumeration props = onLoadCacheStaticNodes.propertyNames(); props.hasMoreElements(); )
            {
                String mime = (String)props.nextElement();
                Boolean value = Boolean.valueOf(onLoadCacheStaticNodes.getProperty(mime));
                itsNatServletConfig.setOnLoadCacheStaticNodes(mime,value.booleanValue());
            }
        }
        if (defaultDateFormat != null)
            itsNatServletConfig.setDefaultDateFormat(defaultDateFormat);
        if (defaultNumberFormat != null)
            itsNatServletConfig.setDefaultNumberFormat(defaultNumberFormat);
    }

}
