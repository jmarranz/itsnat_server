/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.core.template;

import com.innowhere.relproxy.jproxy.JProxyScriptEngine;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.servlet.ItsNatServletConfigImpl;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.event.ItsNatServletRequestListener;
import java.util.LinkedList;
import java.util.Map;
import org.itsnat.comp.CreateItsNatComponentListener;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatAttachedClientEventListener;
import org.itsnat.impl.core.*;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.req.norm.RequestNormalLoadDocImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatServletResponseImpl;
import org.itsnat.impl.core.template.xml.ItsNatXMLDocumentTemplateImpl;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatDocumentTemplateImpl extends MarkupTemplateImpl implements ItsNatDocumentTemplate
{
    protected LinkedList<ItsNatServletRequestListener> requestListeners;
    protected LinkedList<ItsNatAttachedClientEventListener> attachedEventListeners;
    protected LinkedList<EventListener> domEventListeners;
    protected LinkedList<CreateItsNatComponentListener> createCompListeners;
    protected Map<String,Object> artifacts;
    protected int commMode;
    protected long eventTimeout;
    protected int useGZip;
    protected boolean autoBuildComponents;
    protected boolean fastLoadMode; // No tiene efectos en documentos XML ... para el futuro
    protected boolean loadScriptInline; // No tiene efectos en documentos XML ... para el futuro
    protected boolean nodeCacheEnabled; // No tiene efectos en documentos XML ... para el futuro
    protected DateFormat dateFormat;
    protected NumberFormat numberFormat;
    protected long evtDispMaxWait;
    protected int maxOpenClients;
    protected boolean referrerEnabled;
    protected boolean referrerPushEnabled;
    protected boolean eventsEnabled;
    protected boolean scriptEnabled;
    protected boolean autoCleanEventListeners;
    protected boolean useXHRSyncOnUnloadEvent;
    protected boolean usePatternMarkupToRender;
    protected boolean selectionOnComponentsUsesKeyboard;
    protected boolean joystickMode;
    protected boolean markupDrivenComponents;
    protected boolean debugMode; // En fragmentos por ahora no influye en nada pero en el futuro podría
    protected int clientErrorMode;

    /**
     * Creates a new instance of ItsNatDocumentTemplate
     */
    public ItsNatDocumentTemplateImpl(String name,String mime,MarkupSourceImpl source,ItsNatServletImpl servlet)
    {
        super(name,mime,source,servlet);

        ItsNatServletConfigImpl servletConfig = servlet.getItsNatServletConfigImpl();
        this.commMode = servletConfig.getCommMode();
        this.eventTimeout = servletConfig.getEventTimeout();
        this.useGZip = servletConfig.getUseGZip();
        this.fastLoadMode = servletConfig.isFastLoadMode();
        this.loadScriptInline = servletConfig.isLoadScriptInline();
        this.nodeCacheEnabled = servletConfig.isNodeCacheEnabled();

        this.dateFormat = servletConfig.getDefaultDateFormat();
        this.numberFormat = servletConfig.getDefaultNumberFormat();
        this.evtDispMaxWait = servletConfig.getEventDispatcherMaxWait();
        this.maxOpenClients = servletConfig.getMaxOpenClientsByDocument();
        this.autoBuildComponents = servletConfig.isAutoBuildComponents();
        this.referrerEnabled = servletConfig.isReferrerEnabled();
        this.referrerPushEnabled = servletConfig.isReferrerPushEnabled();
        this.eventsEnabled = servletConfig.isEventsEnabled();
        this.scriptEnabled = servletConfig.isScriptingEnabled();
        this.autoCleanEventListeners = servletConfig.isAutoCleanEventListeners();
        this.useXHRSyncOnUnloadEvent = servletConfig.isUseXHRSyncOnUnloadEvent();
        this.usePatternMarkupToRender = servletConfig.isUsePatternMarkupToRender();
        this.selectionOnComponentsUsesKeyboard = servletConfig.isSelectionOnComponentsUsesKeyboard();
        this.joystickMode = servletConfig.isJoystickMode();
        this.markupDrivenComponents = servletConfig.isMarkupDrivenComponents();
        this.debugMode = servletConfig.isDebugMode();
        this.clientErrorMode = servletConfig.getClientErrorMode();
    }

    public static ItsNatDocumentTemplateImpl createItsNatDocumentTemplate(String name,String mime,Object source,ItsNatServletImpl servlet)
    {
        MarkupSourceImpl markupSource = MarkupSourceImpl.createMarkupSource(source);
        if (NamespaceUtil.isStatefulMime(mime))
            return ItsNatStfulDocumentTemplateNormalImpl.createItsNatStfulDocumentTemplateNormal(name,mime,markupSource,servlet);
        else
            return new ItsNatXMLDocumentTemplateImpl(name,mime,markupSource,servlet);
    }

    public static ItsNatStfulDocumentTemplateAttachedServerImpl createItsNatStfulDocumentTemplateAttachedServer(String name,String mime,ItsNatServletImpl servlet)
    {
        if (NamespaceUtil.isStatefulMime(mime))
            return new ItsNatStfulDocumentTemplateAttachedServerImpl(name,mime,servlet);
        else
            throw new ItsNatException("This MIME " + mime + " is not recognized as stateful and stateless is not valid in attached server mode");
    }

    @Override
    public boolean isDocFragment()
    {
        return false;
    }

    @Override
    public int getCommMode()
    {
        return commMode;
    }

    @Override
    public void setCommMode(int commMode)
    {
        checkIsAlreadyUsed();
        CommModeImpl.checkMode(commMode);
        this.commMode = commMode;
    }

    @Override
    public long getEventTimeout()
    {
        return eventTimeout;
    }

    @Override
    public void setEventTimeout(long timeout)
    {
        checkIsAlreadyUsed();
        this.eventTimeout = timeout;
    }

    @Override
    public int getUseGZip()
    {
        return useGZip;
    }

    @Override
    public void setUseGZip(int value)
    {
        checkIsAlreadyUsed();
        this.useGZip = value;
    }

    @Override
    public boolean isFastLoadMode()
    {
        return fastLoadMode;
    }

    @Override
    public void setFastLoadMode(boolean fastLoadMode)
    {
        checkIsAlreadyUsed();
        this.fastLoadMode = fastLoadMode;
    }

    @Override
    public boolean isLoadScriptInline()
    {
        return loadScriptInline;
    }

    @Override
    public void setLoadScriptInline(boolean value)
    {
        checkIsAlreadyUsed();
        this.loadScriptInline = value;
    }

    @Override
    public boolean isNodeCacheEnabled()
    {
        return nodeCacheEnabled;
    }

    @Override
    public void setNodeCacheEnabled(boolean nodeCacheEnabled)
    {
        checkIsAlreadyUsed();
        this.nodeCacheEnabled = nodeCacheEnabled;
    }

    @Override
    public boolean isAutoBuildComponents()
    {
        return autoBuildComponents;
    }

    @Override
    public void setAutoBuildComponents(boolean autoBuildComponents)
    {
        checkIsAlreadyUsed();
        this.autoBuildComponents = autoBuildComponents;
    }

    @Override
    public DateFormat getDefaultDateFormat()
    {
        return dateFormat;
    }

    @Override
    public void setDefaultDateFormat(DateFormat dateFormat)
    {
        checkIsAlreadyUsed();
        this.dateFormat = dateFormat;
    }

    @Override
    public NumberFormat getDefaultNumberFormat()
    {
        return numberFormat;
    }

    @Override
    public void setDefaultNumberFormat(NumberFormat numberFormat)
    {
        checkIsAlreadyUsed();
        this.numberFormat = numberFormat;
    }

    @Override
    public long getEventDispatcherMaxWait()
    {
        return evtDispMaxWait;
    }

    @Override
    public void setEventDispatcherMaxWait(long wait)
    {
        checkIsAlreadyUsed();
        this.evtDispMaxWait = wait;
    }

    @Override
    public int getMaxOpenClientsByDocument()
    {
        return maxOpenClients;
    }

    @Override
    public void setMaxOpenClientsByDocument(int value)
    {
        checkIsAlreadyUsed();
        ItsNatServletConfigImpl.checkMaxOpenClientsByDocument(value);
        this.maxOpenClients = value;
    }

    @Override
    public boolean isReferrerEnabled()
    {
        return referrerEnabled;
    }

    @Override
    public void setReferrerEnabled(boolean enabled)
    {
        checkIsAlreadyUsed();
        this.referrerEnabled = enabled;
    }


    @Override
    public boolean isReferrerPushEnabled()
    {
        return referrerPushEnabled;
    }

    @Override
    public void setReferrerPushEnabled(boolean referrerPushEnabled)
    {
        checkIsAlreadyUsed();
        this.referrerPushEnabled = referrerPushEnabled;
    }

    @Override
    public boolean isEventsEnabled()
    {
        return eventsEnabled;
    }

    @Override
    public void setEventsEnabled(boolean enabled)
    {
        checkIsAlreadyUsed();
        this.eventsEnabled = enabled;
    }

    @Override
    public boolean isScriptingEnabled()
    {
        return scriptEnabled;
    }

    @Override
    public void setScriptingEnabled(boolean enabled)
    {
        checkIsAlreadyUsed();
        this.scriptEnabled = enabled;

        if (!enabled)
        {
            setEventsEnabled(false);
            setFastLoadMode(true); // Pues el modo "slow" necesariamente necesita enviar JavaScript por los cambios en el DOM en tiempo de carga
        }
    }

    @Override
    public boolean isAutoCleanEventListeners()
    {
        return autoCleanEventListeners;
    }

    @Override
    public void setAutoCleanEventListeners(boolean enable)
    {
        checkIsAlreadyUsed();

        this.autoCleanEventListeners = enable;
    }

    @Override
    public boolean isUseXHRSyncOnUnloadEvent()
    {
        return useXHRSyncOnUnloadEvent;
    }

    @Override
    public void setUseXHRSyncOnUnloadEvent(boolean enable)
    {
        checkIsAlreadyUsed();

        this.useXHRSyncOnUnloadEvent = enable;
    }

    public void setAutoCleanEventListenersTESTING(boolean enable)
    {
        // Este método SOLO ES USADO para testing, no debería
        // ser usado ni por el framework ni por aplicaciones normales

        this.autoCleanEventListeners = enable;
    }

    @Override
    public boolean isUsePatternMarkupToRender()
    {
        return usePatternMarkupToRender;
    }

    @Override
    public void setUsePatternMarkupToRender(boolean usePatternMarkupToRender)
    {
        checkIsAlreadyUsed();
        this.usePatternMarkupToRender = usePatternMarkupToRender;
    }

    @Override
    public boolean isSelectionOnComponentsUsesKeyboard()
    {
        return selectionOnComponentsUsesKeyboard;
    }

    @Override
    public void setSelectionOnComponentsUsesKeyboard(boolean value)
    {
        checkIsAlreadyUsed();
        this.selectionOnComponentsUsesKeyboard = value;
    }

    @Override
    public boolean isJoystickMode()
    {
        return joystickMode;
    }

    @Override
    public void setJoystickMode(boolean value)
    {
        checkIsAlreadyUsed();
        this.joystickMode = value;
    }

    @Override
    public boolean isMarkupDrivenComponents()
    {
        return markupDrivenComponents;
    }

    @Override
    public void setMarkupDrivenComponents(boolean value)
    {
        checkIsAlreadyUsed();
        this.markupDrivenComponents = value;
    }

    @Override
    public boolean isDebugMode()
    {
        return debugMode;
    }

    @Override
    public void setDebugMode(boolean debugMode)
    {
        checkIsAlreadyUsed();
        this.debugMode = debugMode;
    }

    @Override
    public int getClientErrorMode()
    {
        return clientErrorMode;
    }

    @Override
    public void setClientErrorMode(int mode)
    {
        checkIsAlreadyUsed();

        this.clientErrorMode = mode;
    }

    public ItsNatDocumentTemplateVersionImpl getNewestItsNatDocumentTemplateVersion(RequestNormalLoadDocImpl request)
    {
        MarkupSourceImpl source = delegate.getMarkupSource(request);
        ItsNatServletRequestImpl itsNatRequest = request.getItsNatServletRequest();
        ItsNatServletResponseImpl itsNatResponse = itsNatRequest.getItsNatServletResponseImpl();
        return (ItsNatDocumentTemplateVersionImpl)delegate.getNewestMarkupTemplateVersion(source,itsNatRequest,itsNatResponse);
    }

    public boolean hasItsNatServletRequestListeners()
    {
        if (requestListeners == null)
            return false;
        return !requestListeners.isEmpty();
    }

    public LinkedList<ItsNatServletRequestListener> getItsNatServletRequestListenerList()
    {
        if (requestListeners == null)
            this.requestListeners = new LinkedList<ItsNatServletRequestListener>();
        return requestListeners;
    }

    public Iterator<ItsNatServletRequestListener> getItsNatServletRequestListenerIterator()
    {
        // No sincronizamos porque sólo admitimos sólo lectura
        if (requestListeners == null) return null;
        if (requestListeners.isEmpty()) return null;
        return requestListeners.iterator();
    }

    @Override
    public void addItsNatServletRequestListener(ItsNatServletRequestListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        JProxyScriptEngine jProxy = servlet.getItsNatImpl().getJProxyScriptEngineIfConfigured();
        if (jProxy != null)
        {
            listener = jProxy.create(listener,ItsNatServletRequestListener.class);
        }
        
        LinkedList<ItsNatServletRequestListener> requestListeners = getItsNatServletRequestListenerList();
        requestListeners.add(listener);
    }

    @Override
    public void removeItsNatServletRequestListener(ItsNatServletRequestListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        JProxyScriptEngine jProxy = servlet.getItsNatImpl().getJProxyScriptEngineIfConfigured();        
        if (jProxy != null)
        {
            listener = jProxy.create(listener,ItsNatServletRequestListener.class);
        }        
        
        LinkedList<ItsNatServletRequestListener> requestListeners = getItsNatServletRequestListenerList();
        requestListeners.remove(listener); // Ver el manual de RelProxy sobre el uso de equals y proxies
    }

    public LinkedList<ItsNatAttachedClientEventListener> getItsNatAttachedClientEventListenerList()
    {
        if (attachedEventListeners == null)
            this.attachedEventListeners = new LinkedList<ItsNatAttachedClientEventListener>();
        return attachedEventListeners;
    }

    public void getItsNatAttachedClientEventListenerList(LinkedList<ItsNatAttachedClientEventListener> list)
    {
        // No sincronizamos porque sólo admitimos sólo lectura
        if (attachedEventListeners == null)
            return;
        list.addAll(attachedEventListeners);
    }

    @Override
    public void addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        JProxyScriptEngine jProxy = servlet.getItsNatImpl().getJProxyScriptEngineIfConfigured();        
        if (jProxy != null)
        {      
            listener = jProxy.create(listener,ItsNatAttachedClientEventListener.class);
        }
        
        LinkedList<ItsNatAttachedClientEventListener> attachedEventListeners = getItsNatAttachedClientEventListenerList();
        attachedEventListeners.add(listener);
    }

    @Override
    public void removeItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos
       
        JProxyScriptEngine jProxy = servlet.getItsNatImpl().getJProxyScriptEngineIfConfigured();        
        if (jProxy != null)
        {      
            listener = jProxy.create(listener,ItsNatAttachedClientEventListener.class);
        }                
        
        LinkedList<ItsNatAttachedClientEventListener> attachedEventListeners = getItsNatAttachedClientEventListenerList();
        attachedEventListeners.remove(listener);
    }

    public boolean hasGlobalEventListenerListeners()
    {
        if (domEventListeners == null)
            return false;
        return !domEventListeners.isEmpty();
    }    
    
    public LinkedList<EventListener> getGlobalEventListenerList()
    {
        if (domEventListeners == null)
            this.domEventListeners = new LinkedList<EventListener>();
        return domEventListeners;
    }

    public void getGlobalEventListenerList(LinkedList<EventListener> list)
    {
        // No sincronizamos porque sólo admitimos sólo lectura
        if (domEventListeners == null)
            return;
        list.addAll(domEventListeners);
    }

    @Override
    public void addEventListener(EventListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<EventListener> domEventListeners = getGlobalEventListenerList();
        domEventListeners.add(listener);
    }

    @Override
    public void removeEventListener(EventListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<EventListener> domEventListeners = getGlobalEventListenerList();
        domEventListeners.remove(listener);
    }

    public boolean hasCreateItsNatComponentList()
    {
        if (createCompListeners == null) return false;
        return !createCompListeners.isEmpty();
    }

    public LinkedList<CreateItsNatComponentListener> getCreateItsNatComponentList()
    {
        if (createCompListeners == null)
            this.createCompListeners = new LinkedList<CreateItsNatComponentListener>(); // Sólo se crea si se necesita
        return createCompListeners;
    }

    public Iterator<CreateItsNatComponentListener> getCreateItsNatComponentListenerIterator()
    {
        if (!hasCreateItsNatComponentList()) return null;
        return createCompListeners.iterator();
    }

    @Override
    public void addCreateItsNatComponentListener(CreateItsNatComponentListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<CreateItsNatComponentListener> list = getCreateItsNatComponentList();
        list.add(listener);
    }

    @Override
    public void removeCreateItsNatComponentListener(CreateItsNatComponentListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<CreateItsNatComponentListener> list = getCreateItsNatComponentList();
        list.remove(listener);
    }

    public boolean hasArtifacts()
    {
        if (artifacts == null) return false;
        return !artifacts.isEmpty();
    }

    public Map<String,Object> getArtifactMap()
    {
        if (artifacts == null)
            this.artifacts = new HashMap<String,Object>();
        return artifacts;
    }

    @Override
    public Object getArtifact(String name)
    {
        if (!hasArtifacts()) return null;

        Map<String,Object> artifacts = getArtifactMap();
        return artifacts.get(name);
    }

    @Override
    public void registerArtifact(String name,Object value)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar (sólo lectura)

        Map<String,Object> artifacts = getArtifactMap();
        artifacts.put(name,value);
    }

    @Override
    public Object removeArtifact(String name)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar (sólo lectura)

        Map<String,Object> artifacts = getArtifactMap();
        return artifacts.remove(name);
    }

    @Override
    public Object getArtifact(String name,boolean cascade)
    {
        Object artif = getArtifact(name);
        if (cascade && (artif == null))
            artif = getItsNatServletImpl().getItsNatServletConfigImpl().getArtifact(name);
        return artif;
    }

    public ItsNatDocumentImpl loadItsNatDocument(RequestNormalLoadDocImpl request)
    {
        ItsNatDocumentTemplateVersionImpl loader = getNewestItsNatDocumentTemplateVersion(request);
        return loader.loadItsNatDocument(request);
    }

    public boolean canVersionBeSharedBetweenDocs()
    {
        return delegate.canVersionBeSharedBetweenDocs();
    }
}
