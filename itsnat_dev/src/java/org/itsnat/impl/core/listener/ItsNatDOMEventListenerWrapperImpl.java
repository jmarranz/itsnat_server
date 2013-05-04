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

package org.itsnat.impl.core.listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.client.ClientItsNatDOMEventImpl;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.listener.trans.ParamTransportUtil;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatDOMEventListenerWrapperImpl extends ItsNatNormalEventListenerWrapperImpl
{
    protected EventListener listener;  // Sujeta el listener, puede ser nulo (caso de async task)
    protected transient WeakReference<EventTarget> currTargetWeakRef;  // EventTarget
    protected ParamTransport[] extraParams;
    protected String preSendCode;
    protected String bindToListener;

    /** Creates a new instance of ItsNatDOMEventListenerWrapperImpl */
    public ItsNatDOMEventListenerWrapperImpl(ItsNatStfulDocumentImpl itsNatDoc,ClientDocumentStfulImpl clientDoc,EventTarget currTarget,EventListener listener,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        super(itsNatDoc,clientDoc,eventTimeout);

        this.currTargetWeakRef = currTarget != null ? new WeakReference<EventTarget>(currTarget) : null; // currTargetWeakRef puede ser null
        this.extraParams = extraParams;
        this.preSendCode = preSendCode;
        this.listener = listener;
        this.bindToListener = bindToListener;
        
        checkClient();        
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        EventTarget currTarget = null;
        if (currTargetWeakRef != null)
            currTarget = currTargetWeakRef.get();

        out.writeObject(currTarget);

        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        EventTarget currTarget = (EventTarget)in.readObject();
        if (currTarget != null)
            this.currTargetWeakRef = new WeakReference<EventTarget>(currTarget);        

        in.defaultReadObject();
    }

    public static boolean canAddItsNatDOMEventListenerWrapper(EventListener listener,ItsNatStfulDocumentImpl itsNatDoc,ClientDocumentStfulImpl clientDoc)
    {
        if (clientDoc != null)
        {
            // Esto nos sirve para asegurarnos de que no es posible
            // registrar un listener especificamente para un cliente control remoto
            // cuando dicho cliente de control remoto NO puede recibir eventos "normales".
            return clientDoc.canReceiveNormalEvents(listener);
        }
        else  // Si clientDoc es null es que es un registro a nivel de documento (umm yo creo que YA nunca clientDoc es null)
            return itsNatDoc.isEventsEnabled();
    }

    public void checkClient()
    {
        // Esto nos sirve para asegurarnos con certeza que no se nos ha pasado nada en seguridad
        // pues no es posible crear y registrar un listener especificamente para un cliente control remoto
        // cuando dicho cliente de control remoto NO puede recibir eventos "normales".
        // Antes de llegar aquí un posible intento es ignorado (no hace nada), pero si llegamos aquí
        // es que algo hemos hecho mal (framework) por lo que provocamos una excepción
        if ((clientDoc != null) && !clientDoc.canReceiveNormalEvents(this))
            throw new ItsNatException("Attempt to register a listener for a remote control client with read only permission");
    }

    public String getPreSendCode()
    {
        return preSendCode;
    }

    public String getBindToListener()
    {
        return bindToListener;
    }

    public boolean isEventTargetExpunged(EventTarget target)
    {
        // Devuelve true si el target ha sido recolectado por el GC
        // Obligamos a pasar el target como parámetro para ayudar a evitar un GC tras la llamada
        return ((target == null) && (getEventTargetWeakRef() != null));
    }

    public WeakReference<EventTarget> getEventTargetWeakRef()
    {
        // Si no es nulo es que guardó un nodo aunque lo hayamos perdido
        return currTargetWeakRef;
    }

    public EventTarget getCurrentTarget()
    {
        if (currTargetWeakRef == null) return null;
        return currTargetWeakRef.get(); // Es null si se ha perdido (GC)
    }

    public String getCodeToSendParamTransports()
    {
        if (extraParams == null)
            return null;

        StringBuilder code = new StringBuilder();
        for(int i = 0; i < extraParams.length; i++)
        {
            ParamTransport param = extraParams[i];
            ParamTransportUtil paramUtil = ParamTransportUtil.getSingleton(param);
            String paramCode = paramUtil.getCodeToSend(param);
            if (paramCode != null)
                code.append( paramCode );
        }
        return code.toString();
    }

    public EventListener getEventListener()
    {
        return listener;
    }

    public void processEvent(ClientItsNatNormalEventImpl event)
    {
        ClientItsNatDOMEventImpl domEvent = (ClientItsNatDOMEventImpl)event;

        beforeAfterHandleEvent(true,domEvent); // Antes de llamar a los listeners del usuario
        handleEvent(domEvent);
        beforeAfterHandleEvent(false,domEvent);
    }

    public void beforeAfterHandleEvent(boolean before,ClientItsNatDOMEventImpl event)
    {
        if (extraParams == null)
            return;

        RequestNormalEventImpl request = event.getRequestNormalEvent();
        ClientDocumentStfulImpl clientDoc = event.getClientDocumentStful();

        boolean wasEnabled = clientDoc.isSendCodeEnabled();
        if (wasEnabled)
            clientDoc.disableSendCode(); // para que no se envíe de nuevo al cliente el valor traído (salvo observers para que vean el cambio del cliente)
        try
        {
            for(int i = 0; i < extraParams.length; i++)
            {
                ParamTransport param = extraParams[i];
                if (param.isSync())
                {
                    ParamTransportUtil paramUtil = ParamTransportUtil.getSingleton(param);
                    if (before)
                        paramUtil.syncServerBeforeDispatch(param,request,event);
                    else
                        paramUtil.syncServerAfterDispatch(param,request,event);
                }
            }
        }
        finally
        {
            if (wasEnabled)
                clientDoc.enableSendCode(); // restauramos
        }
    }

    public void handleEvent(ClientItsNatDOMEventImpl event)
    {
        EventListenerUtil.handleEventIncludingGlobalListeners(listener, event);
    }

    public abstract boolean getUseCapture();
}
