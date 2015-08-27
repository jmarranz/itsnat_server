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

import java.util.ArrayList;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.*;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.BrowserSVGPlugin;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.event.EventListenerInternal;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public class ItsNatCompNormalEventListenersJoystickSharedImpl
{
    public static ArrayList<ItsNatCompNormalEventListenersJoystick> getMustAddRemove(JoystickModeComponent comp)
    {
        ItsNatCompNormalEventListenersAllClientsImpl normalEventListenersByClient = ((ItsNatComponentImpl)comp).getItsNatCompNormalEventListenersAllClients();
        ArrayList<ItsNatCompNormalEventListenersJoystick> res = new ArrayList<ItsNatCompNormalEventListenersJoystick>(1 + normalEventListenersByClient.size()); // El 1 es el registro por documento

        ItsNatCompNormalEventListenersByDocJoystickImpl normalEventListenersByDoc = comp.getItsNatCompNormalEventListenersByDocJoystick();
        if (normalEventListenersByDoc.mustAddRemove())
            res.add(normalEventListenersByDoc);

        ItsNatCompNormalEventListenersByClientImpl[] clients = normalEventListenersByClient.getAllItsNatCompNormalEventListenersByClient();
        for(int i = 0; i < clients.length; i++)
        {
            ItsNatCompNormalEventListenersByClientJoystickImpl client = (ItsNatCompNormalEventListenersByClientJoystickImpl)clients[i];
            if (client.mustAddRemove())
                res.add(client);
        }

        return res;
    }

    public static void addEventListenerJoystick(ArrayList<ItsNatCompNormalEventListenersJoystick> normalEventListeners,Element[] elemList)
    {
        for(int i = 0; i < normalEventListeners.size(); i++)
        {
            ItsNatCompNormalEventListenersJoystick current = normalEventListeners.get(i);
            current.addEventListenerJoystick(elemList);
        }
    }

    public static void removeEventListenerJoystick(ArrayList<ItsNatCompNormalEventListenersJoystick> normalEventListeners,Element[] elemList)
    {
        for(int i = 0; i < normalEventListeners.size(); i++)
        {
            ItsNatCompNormalEventListenersJoystick current = normalEventListeners.get(i);
            current.removeEventListenerJoystick(elemList);
        }
    }

    public static void addEventListenerJoystick(ArrayList<ItsNatCompNormalEventListenersJoystick> normalEventListeners,Element elem)
    {
        for(int i = 0; i < normalEventListeners.size(); i++)
        {
            ItsNatCompNormalEventListenersJoystick current = normalEventListeners.get(i);
            current.addEventListenerJoystick(elem);
        }
    }

    public static void removeEventListenerJoystick(ArrayList<ItsNatCompNormalEventListenersJoystick> normalEventListeners,Element elem)
    {
        for(int i = 0; i < normalEventListeners.size(); i++)
        {
            ItsNatCompNormalEventListenersJoystick current = normalEventListeners.get(i);
            current.removeEventListenerJoystick(elem);
        }
    }

    public static boolean mustAddRemove(ItsNatCompNormalEventListenersJoystick listeners)
    {
        if (!listeners.isJoystickEnabled())
            return false;
        if (!listeners.hasEnabledNormalEvents())
            return false;
        ItsNatDocumentImpl itsNatDoc = listeners.getItsNatDocumentImpl();
        if (itsNatDoc.isLoadingPhaseAndFastLoadMode())
            return false; // Se hace de una vez cuando termina la carga
        return true;
    }

    public static void addInternalEventListenerJoystick(final ItsNatCompNormalEventListenersJoystick listeners,final ClientDocumentImpl clientDoc,final String type,final boolean useCapture,final int commMode,final ParamTransport[] extraParams,final String preSendCode,final long eventTimeout,final String bindToCustomFunc)
    {
        ItsNatDocumentImpl itsNatDoc = listeners.getItsNatDocumentImpl();
        if (itsNatDoc.isLoadingPhaseAndFastLoadMode())
        {
            // En tiempo de carga en modo fast load el acceso a nodos no tolera cambios en el DOM de elementos eliminados/cambiados de posición
            // y eso puede ocurrir mientras se construye la lista
            // Tenemos que delegar el proceso a después de la carga
            EventListener listener = new EventListenerInternal()
            {
                @Override
                public void handleEvent(Event evt)
                {
                    addInternalEventListenerJoystick2(listeners,clientDoc,type, useCapture, commMode, extraParams, preSendCode, eventTimeout,bindToCustomFunc);
                }
            };
            Document doc = itsNatDoc.getDocument();

            Browser browser = clientDoc.getBrowser();
            EventTarget target;
            String eventType;
            if (!(browser instanceof BrowserSVGPlugin))
            {
                target = (EventTarget)((DocumentView)doc).getDefaultView();
                eventType = "load";
            }
            else
            {
                target = (EventTarget)doc.getDocumentElement();
                eventType = "SVGLoad";
            }
            clientDoc.addEventListener(target,eventType,listener,false);
            listeners.getLoadScheduledMap().put(type + "_" + useCapture,listener);
        }
        else
        {
            addInternalEventListenerJoystick2(listeners,clientDoc,type, useCapture, commMode, extraParams, preSendCode, eventTimeout,bindToCustomFunc);
        }
    }

    public static void removeInternalEventListenerJoystick(ItsNatCompNormalEventListenersJoystick listeners,ClientDocumentImpl clientDoc,String type,boolean useCapture,boolean updateClient)
    {
        ItsNatDocumentImpl itsNatDoc = listeners.getItsNatDocumentImpl();
        if (itsNatDoc.isLoadingPhaseAndFastLoadMode())
        {
            EventListener listener = listeners.getLoadScheduledMap().remove(type + "_" + useCapture);
            Document doc = itsNatDoc.getDocument();

            Browser browser = clientDoc.getBrowser();
            EventTarget target;
            String eventType;
            if (!(browser instanceof BrowserSVGPlugin))
            {
                target = (EventTarget)((DocumentView)doc).getDefaultView();
                eventType = "load";
            }
            else
            {
                target = (EventTarget)doc.getDocumentElement();
                eventType = "SVGLoad";
            }

            clientDoc.removeEventListener(target,eventType,listener,false);
        }
        else
        {
            removeInternalEventListenerJoystick2(listeners,clientDoc,type, useCapture,updateClient);
        }
    }

    public static void addInternalEventListenerJoystick2(ItsNatCompNormalEventListenersJoystick listeners,ClientDocumentImpl clientDoc,String type,boolean useCapture, int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        JoystickModeComponent comp = listeners.getJoystickModeComponent();
        Element[] elemList = comp.getContentElementList();

        for(int i = 0; i < elemList.length; i++)
        {
            Element contentElem = elemList[i];
            if (contentElem == null) continue;
            if (DOMUtilInternal.isNodeBoundToDocumentTree(contentElem)) // Esta comprobación es simplemente para que funcione el ejemplo del "Table using Row Span" del Feat. Show. quizás valga para casos especiales del usuario.
                clientDoc.addEventListener((EventTarget)contentElem,type,(ItsNatComponentImpl)comp,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);
        }
    }

    public static void removeInternalEventListenerJoystick2(ItsNatCompNormalEventListenersJoystick listeners,ClientDocumentImpl clientDoc,String type,boolean useCapture,boolean updateClient)
    {
        JoystickModeComponent comp = listeners.getJoystickModeComponent();
        Element[] elemList = comp.getContentElementList();

        for(int i = 0; i < elemList.length; i++)
        {
            Element contentElem = elemList[i];
            if (contentElem == null) continue;
            if (DOMUtilInternal.isNodeBoundToDocumentTree(contentElem)) // Esta comprobación es simplemente para que funcione el ejemplo del "Table using Row Span" del Feat. Show. quizás valga para casos especiales del usuario.
                clientDoc.removeEventListener((EventTarget)contentElem,type,(ItsNatComponentImpl)comp,useCapture);
        }
    }

    public static void addInternalEventListenerJoystick(ItsNatCompNormalEventListenersJoystick listeners,Element contentElem, String type)
    {
        if (contentElem == null) return;

        ItsNatDocumentImpl itsNatDoc = listeners.getItsNatDocumentImpl();
        ClientDocumentImpl[] clients = itsNatDoc.getAllClientDocumentsCopy();
        for(int i = 0; i < clients.length; i++)
        {
            addInternalEventListenerJoystick(listeners,clients[i],contentElem,type);
        }
    }

    public static void removeInternalEventListenerJoystick(ItsNatCompNormalEventListenersJoystick listeners,Element contentElem, String type)
    {
        if (contentElem == null) return;

        ItsNatDocumentImpl itsNatDoc = listeners.getItsNatDocumentImpl();
        ClientDocumentImpl[] clients = itsNatDoc.getAllClientDocumentsCopy();
        for(int i = 0; i < clients.length; i++)
        {
            removeInternalEventListenerJoystick(listeners,clients[i],contentElem,type);
        }
    }

    public static void addInternalEventListenerJoystick(ItsNatCompNormalEventListenersJoystick listeners,ClientDocumentImpl clientDoc,Element contentElem, String type)
    {
        if (contentElem == null) return;

        ItsNatCompNormalEventListenersImpl listenersBase = (ItsNatCompNormalEventListenersImpl)listeners;

        EventListenerParamsImpl params = listenersBase.getEventListenerParams(type);

        boolean useCapture = listenersBase.isUseCapture(params);
        int commMode = listenersBase.getCommModeDeclared(params);
        String preSendCode = listenersBase.getPreSendCode(params);
        long eventTimeout = listenersBase.getEventTimeout(params);
        String bindToCustomFunc = listenersBase.getBindToCustomFunc(params);

        ParamTransport[] extraParams = listenersBase.getParamTransports(type, params,clientDoc);

        clientDoc.addEventListener((EventTarget) contentElem, type,listenersBase.getItsNatComponent(), useCapture, commMode, extraParams, preSendCode, eventTimeout,bindToCustomFunc);
    }

    public static void removeInternalEventListenerJoystick(ItsNatCompNormalEventListenersJoystick listeners,ClientDocumentImpl clientDoc,Element contentElem, String type)
    {
        if (contentElem == null) return;

        ItsNatCompNormalEventListenersImpl listenersBase = (ItsNatCompNormalEventListenersImpl)listeners;

        EventListenerParamsImpl params = listenersBase.getEventListenerParams(type);
        boolean useCapture = listenersBase.isUseCapture(params);

        clientDoc.removeEventListener((EventTarget) contentElem, type,listenersBase.getItsNatComponent(), useCapture);
    }

    public static void addEventListenerJoystick(ItsNatCompNormalEventListenersJoystick listeners,Element[] elemList)
    {
        for(int i = 0; i < elemList.length; i++)
            addEventListenerJoystick(listeners,elemList[i]);
    }

    public static void removeEventListenerJoystick(ItsNatCompNormalEventListenersJoystick listeners,Element[] elemList)
    {
        for(int i = 0; i < elemList.length; i++)
            removeEventListenerJoystick(listeners,elemList[i]);
    }

    public static void addEventListenerJoystick(ItsNatCompNormalEventListenersJoystick listeners,Element contentElem)
    {
        ItsNatCompNormalEventListenersImpl listenersBase = (ItsNatCompNormalEventListenersImpl)listeners;

        if (listenersBase.hasEnabledNormalEvents())
        {
            for (String type : listenersBase.getEnabledNormalEvents())
            {
                addInternalEventListenerJoystick(listeners,contentElem, type);
            }
        }
    }

    public static void removeEventListenerJoystick(ItsNatCompNormalEventListenersJoystick listeners,Element contentElem)
    {
        ItsNatCompNormalEventListenersImpl listenersBase = (ItsNatCompNormalEventListenersImpl)listeners;

        if (listenersBase.hasEnabledNormalEvents())
        {
            for (String type : listenersBase.getEnabledNormalEvents())
            {
                removeInternalEventListenerJoystick(listeners,contentElem, type);
            }
        }
    }
}
