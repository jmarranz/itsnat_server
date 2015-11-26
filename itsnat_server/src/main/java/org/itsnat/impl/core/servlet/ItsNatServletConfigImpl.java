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
package org.itsnat.impl.core.servlet;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import org.itsnat.core.ClientErrorMode;
import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.UseGZip;
import org.itsnat.impl.core.*;
import org.itsnat.impl.core.domutil.NamespaceUtil;

/**
 *
 * @author jmarranz
 */
public class ItsNatServletConfigImpl extends ItsNatUserDataImpl implements ItsNatServletConfig
{
    protected ServletConfig servletConfig;
    protected ItsNatServletImpl servlet;
    protected ItsNatServletContextImpl servletContext;
    protected Map<String,Boolean> cacheDOMNodesByMime = new HashMap<String,Boolean>();
    protected Map<String,Object> artifacts;
    protected int commMode = CommMode.XHR_ASYNC_HOLD;
    protected long eventTimeout = -1; // No timeout
    protected String defaultEncoding = "UTF-8"; // "ISO-8859-1" 
    protected boolean debugMode = true;
    protected int clientErrorMode = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS;
    protected boolean loadScriptInline = true;
    protected boolean fastLoadMode = true;
    protected boolean nodeCacheEnabled = true;
    protected boolean autoBuildComponents = false;
    protected int useGZip = UseGZip.SCRIPT; // Sólo el JavaScript
    protected DateFormat dateFormat = DateFormat.getInstance();
    protected NumberFormat numberFormat = NumberFormat.getInstance();
    protected long evtDispMaxWait = 0;
    protected int maxOpenClients = 10;
    protected boolean referrerEnabled = false; // El sistema de referrers requiere que el programador lo necesite por lo que lo normal es que lo active en los templates que lo necesiten
    protected boolean referrerPushEnabled = false; // Idem referrerEnabled
    protected boolean eventsEnabled = true;
    protected boolean scriptEnabled = true;
    protected boolean autoCleanEventListeners = true;
    protected boolean useXHRSyncOnUnloadEvent = true;
    protected boolean usePatternMarkupToRender = false;
    protected boolean selectionOnComponentsUsesKeyboard = true;
    protected boolean joystickMode = false;
    protected boolean markupDrivenComponents = false;
    protected int bitmapDensityReference = 320; // xhdpi
    
    /** Creates a new instance of ItsNatServletConfigImpl */
    public ItsNatServletConfigImpl(ServletConfig servletConfig,ItsNatServletImpl servlet)
    {
        super(true);

        this.servletConfig = servletConfig;
        this.servlet = servlet;

        ItsNatImpl itsNat = servlet.getItsNatImpl();
        this.servletContext = itsNat.getItsNatServletContextImpl(servletConfig.getServletContext());

        cacheDOMNodesByMime.put(NamespaceUtil.MIME_HTML,Boolean.TRUE);
        cacheDOMNodesByMime.put(NamespaceUtil.MIME_XHTML,Boolean.TRUE);
        cacheDOMNodesByMime.put(NamespaceUtil.MIME_SVG,Boolean.TRUE);
        cacheDOMNodesByMime.put(NamespaceUtil.MIME_XUL,Boolean.TRUE);
        cacheDOMNodesByMime.put(NamespaceUtil.MIME_XML,Boolean.FALSE); // No tiene mucho sentido cachear nodos pues si generamos un XML todo seguramente será contenido generado y nada estático
        cacheDOMNodesByMime.put(NamespaceUtil.MIME_ANDROID_LAYOUT, Boolean.FALSE); // No es imaginable rollos de texto etc cacheables      
    }

    @Override
    public ServletConfig getServletConfig()
    {
        return servletConfig;
    }

    @Override
    public ItsNatServletContext getItsNatServletContext()
    {
        return servletContext;
    }

    public ItsNatServletContextImpl getItsNatServletContextImpl()
    {
        return servletContext;
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

    public void checkIsAlreadyUsed()
    {
        servlet.checkIsAlreadyUsed();
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

        this.eventTimeout = timeout; // Un valor 0 o negativo es no usar el timeout
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
    public boolean isOnLoadCacheStaticNodes(String mime)
    {
        Boolean value = cacheDOMNodesByMime.get(mime);
        if (value == null)
            return false;
        return value.booleanValue();
    }

    @Override
    public void setOnLoadCacheStaticNodes(String mime,boolean cache)
    {
        checkIsAlreadyUsed();

        cacheDOMNodesByMime.put(mime,Boolean.valueOf(cache));
    }

    @Override
    public String getDefaultEncoding()
    {
        return defaultEncoding;
    }

    @Override
    public void setDefaultEncoding(String defaultEncoding)
    {
        checkIsAlreadyUsed();

        this.defaultEncoding = defaultEncoding;
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
        checkMaxOpenClientsByDocument(value);
        this.maxOpenClients = value;
    }

    public static void checkMaxOpenClientsByDocument(int value)
    {
        if (value == 0) throw new ItsNatException("Max number of open clients cannot be 0");
    }

    @Override
    public boolean isReferrerEnabled()
    {
        return referrerEnabled;
    }

    @Override
    public void setReferrerEnabled(boolean referrerEnabled)
    {
        checkIsAlreadyUsed();

        this.referrerEnabled = referrerEnabled;
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
    public void setEventsEnabled(boolean enable)
    {
        checkIsAlreadyUsed();

        this.eventsEnabled = enable;
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
    public int getBitmapDensityReference()
    {
        return bitmapDensityReference;
    }

    @Override
    public void setBitmapDensityReference(int density)
    {
        checkIsAlreadyUsed();        
        this.bitmapDensityReference = density;
    }    
}
