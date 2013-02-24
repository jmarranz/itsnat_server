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

package org.itsnat.impl.comp.listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.*;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.event.ItsNatEventImpl;
import org.itsnat.impl.core.event.ItsNatEventListenerChainImpl;
import org.itsnat.impl.core.listener.EventListenerUtil;
import org.itsnat.impl.core.registry.ItsNatDOMEventListenerListSameTarget;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatCompDOMListenersImpl implements Serializable
{
    protected ItsNatComponentImpl comp;
    protected ItsNatDOMEventListenerListSameTarget userDOMListenersBefore;
    protected ItsNatDOMEventListenerListSameTarget userDOMListenersAfter;
    protected Set<String> enabledDOMEvents;
    protected Map<String,EventListenerParamsImpl> evtListParams;

    public ItsNatCompDOMListenersImpl(ItsNatComponentImpl comp)
    {
        this.comp = comp;
    }

    public ItsNatComponentImpl getItsNatComponent()
    {
        return comp;
    }

    public ItsNatDocumentImpl getItsNatDocumentImpl()
    {
        return comp.getItsNatDocumentImpl();
    }

    public boolean hasEnabledDOMEvents()
    {
        if (enabledDOMEvents == null) return false;
        return !enabledDOMEvents.isEmpty();
    }

    public Set<String> getEnabledDOMEvents()
    {
        if (enabledDOMEvents == null)
            this.enabledDOMEvents = new HashSet<String>();
        return enabledDOMEvents;
    }

    public boolean hasUserDOMListeners(boolean before)
    {
        if (before)
            return (userDOMListenersBefore != null) && !userDOMListenersBefore.isEmpty();
        else
            return (userDOMListenersAfter != null) && !userDOMListenersAfter.isEmpty();
    }

    public boolean hasUserDOMListeners(String type,boolean before)
    {
        if (!hasUserDOMListeners(before)) return false;
        return getUserDOMListeners(before).hasItsNatDOMEventListeners(type, false);
    }

    public ItsNatDOMEventListenerListSameTarget getUserDOMListeners(boolean before)
    {
        if (before)
        {
            if (userDOMListenersBefore == null) // Para ahorrar memoria si no se usa (ej. Labels)
                this.userDOMListenersBefore = new ItsNatDOMEventListenerListSameTarget();
            return userDOMListenersBefore;
        }
        else
        {
            if (userDOMListenersAfter == null) // Para ahorrar memoria si no se usa (ej. Labels)
                this.userDOMListenersAfter = new ItsNatDOMEventListenerListSameTarget();
            return userDOMListenersAfter;
        }
    }

    public void processDOMEventUserListeners(Event evt,boolean before)
    {
        // Derivar para hacer lo específico antes de delegar en los listeners del usuario
        if (hasUserDOMListeners(before))
        {
            // No se ejecutarán los global listeners de nuevo pues ya se ejecutaron antes
            // de llegar aquí y es el mismo evento que viene del cliente.

            @SuppressWarnings("unchecked")
            ItsNatEventListenerChainImpl<EventListener> chain = ((ItsNatEventImpl)evt).getItsNatEventListenerChainImpl();
            if (getUserDOMListeners(before).getItsNatDOMEventListenerList(evt.getType(),false,chain)) // Se ha añadido alguno
                EventListenerUtil.handleEventListeners(evt,chain);
        }
    }

    public void addUserEventListener(String type,EventListener listener,boolean before)
    {
        enableEventListener(type); // primero activamos porque sino no llegan
        getUserDOMListeners(before).addItsNatDOMEventListener(type,false,listener);
    }

    public void removeUserEventListener(String type,EventListener listener,boolean before)
    {
        getUserDOMListeners(before).removeItsNatDOMEventListener(type,false,listener);
        // No hacemos disableEventListener porque son cosas diferentes, normalmente hay comportamientos por defecto en el componente
    }

    public void disableEventListeners(boolean updateClient)
    {
        if (hasEnabledDOMEvents())
        {
            Object[] types = getEnabledDOMEvents().toArray(); // Llamamos al toArray() porque se van eliminando al iterar
            for(int i = 0; i < types.length; i++)
            {
                String type = (String)types[i];
                disableEventListener(type,updateClient);
            }
        }
    }

    public void enableEventListener(String type)
    {
        Set<String> enabledDOMEvents = getEnabledDOMEvents();
        if (enabledDOMEvents.contains(type))
            return; // ya fue activado
        addInternalEventListener(type);
        enabledDOMEvents.add(type);
    }

    public void disableEventListener(String type)
    {
        disableEventListener(type,true);
    }

    public void disableEventListener(String type,boolean updateClient)
    {
        Set<String> enabledDOMEvents = getEnabledDOMEvents();
        if (!enabledDOMEvents.contains(type))
            return;  // No fue activado
        removeInternalEventListener(type,updateClient);
        enabledDOMEvents.remove(type);
    }

    public Map<String,EventListenerParamsImpl> getEventListenerParamMap()
    {
        if (evtListParams == null)
            this.evtListParams = new HashMap<String,EventListenerParamsImpl>(); // lazy load
        return evtListParams;
    }

    protected abstract void addInternalEventListener(String type);
    protected abstract void removeInternalEventListener(String type,boolean updateClient);

    public void addInternalEventListener(ClientDocumentImpl clientDoc,String type)
    {
        EventListenerParamsImpl params = getEventListenerParams(type);
        ParamTransport[] extraParams = getParamTransports(type,params,clientDoc);

        boolean useCapture = isUseCapture(params);
        int commMode = getCommModeDeclared(params);
        String preSendCode = getPreSendCode(params);
        long eventTimeout = getEventTimeout(params);
        String bindToListener = getBindToListener(params);

        addInternalEventListener(clientDoc,type,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToListener);
    }

    protected void removeInternalEventListener(ClientDocumentImpl clientDoc,String type,boolean updateClient)
    {
        boolean useCapture;
        EventListenerParamsImpl params = getEventListenerParams(type);
        if (params != null)
            useCapture = params.isUseCapture();
        else
            useCapture = false;

        removeInternalEventListener(clientDoc,type,useCapture,updateClient);
    }

    protected void addInternalEventListener(ClientDocumentImpl clientDoc,String type,boolean useCapture, int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        clientDoc.addEventListener((EventTarget)comp.getNode(),type,comp,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToListener);
    }

    protected void removeInternalEventListener(ClientDocumentImpl clientDoc,String type,boolean useCapture,boolean updateClient)
    {
        clientDoc.removeEventListener((EventTarget)comp.getNode(),type,comp,useCapture,updateClient);
    }

    public EventListenerParamsImpl getEventListenerParams(String type)
    {
        if (evtListParams == null)
            return null;
        return evtListParams.get(type); // puede ser null
    }

    public boolean isUseCapture(EventListenerParamsImpl params)
    {
        if (params == null)
            return false;
        return params.isUseCapture();
    }

    public int getCommModeDeclared(EventListenerParamsImpl params)
    {
        if (params == null)
            return getItsNatDocumentImpl().getCommMode();
        return params.getCommModeDeclared();
    }

    public String getPreSendCode(EventListenerParamsImpl params)
    {
        if (params == null)
            return null;
        return params.getPreSendCode();
    }

    public long getEventTimeout(EventListenerParamsImpl params)
    {
        if (params == null)
            return getItsNatDocumentImpl().getEventTimeout();
        return params.getEventTimeout();
    }

    public String getBindToListener(EventListenerParamsImpl params)
    {
        if (params == null)
            return getItsNatDocumentImpl().getBindToListener();
        return params.getBindToListener();
    }

    public ParamTransport[] getParamTransports(String type,EventListenerParamsImpl params,ClientDocumentImpl clientDoc)
    {
        ParamTransport[] extraParamsUser = null;
        if (params != null) extraParamsUser = params.getExtraParams();

        return getParamTransports(type,extraParamsUser,clientDoc);
    }

    private ParamTransport[] getParamTransports(String type,ParamTransport[] extraParamsUser,ClientDocumentImpl clientDoc)
    {
        ParamTransport[] extraParamsInt = comp.getInternalParamTransports(type,clientDoc);

        ParamTransport[] extraParamsFinal = null;
        if ((extraParamsUser != null) || (extraParamsInt != null))
        {
            if (extraParamsUser == null)
                extraParamsFinal = extraParamsInt; // Si es null pues vale también
            else if (extraParamsInt == null)
                extraParamsFinal = extraParamsUser; // "
            else
            {
                // Los dos arrays existen
                ArrayList<ParamTransport> auxArray = new ArrayList<ParamTransport>();                
                auxArray.addAll(Arrays.asList(extraParamsUser));
                auxArray.addAll(Arrays.asList(extraParamsInt));

                extraParamsFinal = auxArray.toArray(new ParamTransport[auxArray.size()]);
            }
        }

        return extraParamsFinal;
    }


    public void setEventListenerParams(String type,boolean useCapture,int commMode,
            ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        // Se quita y se carga de nuevo el listener de este tipo (si hubiera)
        disableEventListener(type);

        EventListenerParamsImpl params = new EventListenerParamsImpl(useCapture,commMode,extraParams,preSendCode,eventTimeout,null);
        Map<String,EventListenerParamsImpl> evtListParams = getEventListenerParamMap();
        evtListParams.put(type,params); // Substituye el que ya hubiera (si existiera)

        enableEventListener(type);
    }
}
