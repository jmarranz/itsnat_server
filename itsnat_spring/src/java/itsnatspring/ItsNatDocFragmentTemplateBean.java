package itsnatspring;

import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author jmarranz
 */
public class ItsNatDocFragmentTemplateBean implements InitializingBean,ApplicationContextAware
{
    protected ApplicationContext context;
    protected String name;
    protected String mime;
    protected Object source;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMime()
    {
        return mime;
    }

    public void setMime(String mime)
    {
        this.mime = mime;
    }

    public Object getSource()
    {
        return source;
    }

    public void setSource(Object source)
    {
        this.source = source;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.context = context;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        // name, mime and source are mandatory
        ItsNatHttpServlet itsNatHttpServlet = ItsNatBeansRegistryUtil.getItsNatHttpServlet(context);
        ItsNatDocFragmentTemplate fragTemplate;
        fragTemplate = itsNatHttpServlet.registerItsNatDocFragmentTemplate(name,mime,source);
        // More config here...
    }

}
