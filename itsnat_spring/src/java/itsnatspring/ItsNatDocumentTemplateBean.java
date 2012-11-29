package itsnatspring;

import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ItsNatDocumentTemplateBean implements InitializingBean,ApplicationContextAware
{
    protected ApplicationContext context;
    protected String name;
    protected String mime;
    protected Object source;
    protected ItsNatServletRequestListener itsNatServletRequestListener;

    public ItsNatServletRequestListener getItsNatServletRequestListener()
    {
        return itsNatServletRequestListener;
    }

    public void setItsNatServletRequestListener(ItsNatServletRequestListener itsNatServletRequestListener)
    {
        this.itsNatServletRequestListener = itsNatServletRequestListener;
    }

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
        ItsNatDocumentTemplate docTemplate =
                itsNatHttpServlet.registerItsNatDocumentTemplate(name,mime,source);
        if (itsNatServletRequestListener != null)
            docTemplate.addItsNatServletRequestListener(itsNatServletRequestListener);
        // More config here...
    }
}
