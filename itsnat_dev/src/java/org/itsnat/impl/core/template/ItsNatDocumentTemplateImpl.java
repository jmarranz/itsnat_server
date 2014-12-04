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

    public boolean isDocFragment()
    {
        return false;
    }

    public int getCommMode()
    {
        return commMode;
    }

    public void setCommMode(int commMode)
    {
        checkIsAlreadyUsed();
        CommModeImpl.checkMode(commMode);
        this.commMode = commMode;
    }

    public long getEventTimeout()
    {
        return eventTimeout;
    }

    public void setEventTimeout(long timeout)
    {
        checkIsAlreadyUsed();
        this.eventTimeout = timeout;
    }

    public int getUseGZip()
    {
        return useGZip;
    }

    public void setUseGZip(int value)
    {
        checkIsAlreadyUsed();
        this.useGZip = value;
    }

    public boolean isFastLoadMode()
    {
        return fastLoadMode;
    }

    public void setFastLoadMode(boolean fastLoadMode)
    {
        checkIsAlreadyUsed();
        this.fastLoadMode = fastLoadMode;
    }

    public boolean isLoadScriptInline()
    {
        return loadScriptInline;
    }

    public void setLoadScriptInline(boolean value)
    {
        checkIsAlreadyUsed();
        this.loadScriptInline = value;
    }

    public boolean isNodeCacheEnabled()
    {
        return nodeCacheEnabled;
    }

    public void setNodeCacheEnabled(boolean nodeCacheEnabled)
    {
        checkIsAlreadyUsed();
        this.nodeCacheEnabled = nodeCacheEnabled;
    }

    public boolean isAutoBuildComponents()
    {
        return autoBuildComponents;
    }

    public void setAutoBuildComponents(boolean autoBuildComponents)
    {
        checkIsAlreadyUsed();
        this.autoBuildComponents = autoBuildComponents;
    }

    public DateFormat getDefaultDateFormat()
    {
        return dateFormat;
    }

    public void setDefaultDateFormat(DateFormat dateFormat)
    {
        checkIsAlreadyUsed();
        this.dateFormat = dateFormat;
    }

    public NumberFormat getDefaultNumberFormat()
    {
        return numberFormat;
    }

    public void setDefaultNumberFormat(NumberFormat numberFormat)
    {
        checkIsAlreadyUsed();
        this.numberFormat = numberFormat;
    }

    public long getEventDispatcherMaxWait()
    {
        return evtDispMaxWait;
    }

    public void setEventDispatcherMaxWait(long wait)
    {
        checkIsAlreadyUsed();
        this.evtDispMaxWait = wait;
    }

    public int getMaxOpenClientsByDocument()
    {
        return maxOpenClients;
    }

    public void setMaxOpenClientsByDocument(int value)
    {
        checkIsAlreadyUsed();
        ItsNatServletConfigImpl.checkMaxOpenClientsByDocument(value);
        this.maxOpenClients = value;
    }

    public boolean isReferrerEnabled()
    {
        return referrerEnabled;
    }

    public void setReferrerEnabled(boolean enabled)
    {
        checkIsAlreadyUsed();
        this.referrerEnabled = enabled;
    }


    public boolean isReferrerPushEnabled()
    {
        return referrerPushEnabled;
    }

    public void setReferrerPushEnabled(boolean referrerPushEnabled)
    {
        checkIsAlreadyUsed();
        this.referrerPushEnabled = referrerPushEnabled;
    }

    public boolean isEventsEnabled()
    {
        return eventsEnabled;
    }

    public void setEventsEnabled(boolean enabled)
    {
        checkIsAlreadyUsed();
        this.eventsEnabled = enabled;
    }

    public boolean isScriptingEnabled()
    {
        return scriptEnabled;
    }

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

    public boolean isAutoCleanEventListeners()
    {
        return autoCleanEventListeners;
    }

    public void setAutoCleanEventListeners(boolean enable)
    {
        checkIsAlreadyUsed();

        this.autoCleanEventListeners = enable;
    }

    public boolean isUseXHRSyncOnUnloadEvent()
    {
        return useXHRSyncOnUnloadEvent;
    }

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

    public boolean isUsePatternMarkupToRender()
    {
        return usePatternMarkupToRender;
    }

    public void setUsePatternMarkupToRender(boolean usePatternMarkupToRender)
    {
        checkIsAlreadyUsed();
        this.usePatternMarkupToRender = usePatternMarkupToRender;
    }

    public boolean isSelectionOnComponentsUsesKeyboard()
    {
        return selectionOnComponentsUsesKeyboard;
    }

    public void setSelectionOnComponentsUsesKeyboard(boolean value)
    {
        checkIsAlreadyUsed();
        this.selectionOnComponentsUsesKeyboard = value;
    }

    public boolean isJoystickMode()
    {
        return joystickMode;
    }

    public void setJoystickMode(boolean value)
    {
        checkIsAlreadyUsed();
        this.joystickMode = value;
    }

    public boolean isMarkupDrivenComponents()
    {
        return markupDrivenComponents;
    }

    public void setMarkupDrivenComponents(boolean value)
    {
        checkIsAlreadyUsed();
        this.markupDrivenComponents = value;
    }

    public boolean isDebugMode()
    {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode)
    {
        checkIsAlreadyUsed();
        this.debugMode = debugMode;
    }

    public int getClientErrorMode()
    {
        return clientErrorMode;
    }

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


    public void addItsNatServletRequestListener(ItsNatServletRequestListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<ItsNatServletRequestListener> requestListeners = getItsNatServletRequestListenerList();
        requestListeners.add(listener);
    }

    public void removeItsNatServletRequestListener(ItsNatServletRequestListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<ItsNatServletRequestListener> requestListeners = getItsNatServletRequestListenerList();
        requestListeners.remove(listener);
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

    public void addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<ItsNatAttachedClientEventListener> attachedEventListeners = getItsNatAttachedClientEventListenerList();
        attachedEventListeners.add(listener);
    }

    public void removeItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

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

    public void addEventListener(EventListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<EventListener> domEventListeners = getGlobalEventListenerList();
        domEventListeners.add(listener);
    }

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

    public void addCreateItsNatComponentListener(CreateItsNatComponentListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<CreateItsNatComponentListener> list = getCreateItsNatComponentList();
        list.add(listener);
    }

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

    public Object getArtifact(String name)
    {
        if (!hasArtifacts()) return null;

        Map<String,Object> artifacts = getArtifactMap();
        return artifacts.get(name);
    }

    public void registerArtifact(String name,Object value)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar (sólo lectura)

        Map<String,Object> artifacts = getArtifactMap();
        artifacts.put(name,value);
    }

    public Object removeArtifact(String name)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar (sólo lectura)

        Map<String,Object> artifacts = getArtifactMap();
        return artifacts.remove(name);
    }

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
