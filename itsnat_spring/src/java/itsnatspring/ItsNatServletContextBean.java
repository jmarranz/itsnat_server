package itsnatspring;

import org.itsnat.core.ItsNatServletContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ItsNatServletContextBean implements InitializingBean,ApplicationContextAware
{
    protected ApplicationContext context;
    protected Integer maxOpenDocumentsBySession;
    protected Boolean sessionReplicationCapable;
    protected Boolean sessionSerializeCompressed;
    protected Boolean sessionExplicitSerialize;

    public ItsNatServletContextBean()
    {
    }

    public int getMaxOpenDocumentsBySession()
    {
        return maxOpenDocumentsBySession;
    }

    public void setMaxOpenDocumentsBySession(int maxOpenDocumentsBySession)
    {
        this.maxOpenDocumentsBySession = maxOpenDocumentsBySession;
    }

    public boolean getSessionReplicationCapable()
    {
        return sessionReplicationCapable;
    }

    public void setSessionReplicationCapable(boolean sessionReplicationCapable)
    {
        this.sessionReplicationCapable = sessionReplicationCapable;
    }

    public boolean getSessionSerializeCompressed()
    {
        return sessionSerializeCompressed;
    }

    public void setSessionSerializeCompressed(boolean sessionSerializeCompressed)
    {
        this.sessionSerializeCompressed = sessionSerializeCompressed;
    }

    public boolean getSessionExplicitSerialize()
    {
        return sessionExplicitSerialize;
    }

    public void setSessionExplicitSerialize(boolean sessionExplicitSerialize)
    {
        this.sessionExplicitSerialize = sessionExplicitSerialize;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.context = context;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        ItsNatServletContext itsNatServletContext = ItsNatBeansRegistryUtil.getItsNatServletContext(context);
        if (maxOpenDocumentsBySession != null)
            itsNatServletContext.setMaxOpenDocumentsBySession(maxOpenDocumentsBySession);        
        if (sessionReplicationCapable != null)
            itsNatServletContext.setSessionReplicationCapable(sessionReplicationCapable);
        if (sessionSerializeCompressed != null)
            itsNatServletContext.setSessionSerializeCompressed(sessionSerializeCompressed);
        if (sessionExplicitSerialize != null)
            itsNatServletContext.setSessionExplicitSerialize(sessionExplicitSerialize);
    }
}
