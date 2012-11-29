
package itsnatspring;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

public class ItsNatServletRequestListenerBean 
        implements ItsNatServletRequestListener,ApplicationContextAware,InitializingBean
{
    protected AbstractApplicationContext context;
    protected String documentBeanName;

    public ItsNatServletRequestListenerBean() { }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocumentInitialize docWrapper =
                context.getBean(documentBeanName,ItsNatDocumentInitialize.class);
        docWrapper.load(request, response);
    }

    public String getDocumentBeanName()
    {
        return documentBeanName;
    }

    public void setDocumentBeanName(String documentBeanName)
    {
        this.documentBeanName = documentBeanName;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.context = (AbstractApplicationContext)context;
    }

    @Override
    public void afterPropertiesSet() throws Exception  
    {
        // Checking correctly defined:
        if (documentBeanName == null) throw new RuntimeException("docBeanName property is mandatory");
        if (!context.getBeanFactory().isPrototype(documentBeanName)) // Si no estuviera definido da error
             throw new RuntimeException("Bean " + documentBeanName + " must be \"prototype\" scoped");
    }
}
