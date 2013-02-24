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

package org.itsnat.impl.core.clientdoc;

import java.util.LinkedList;
import org.itsnat.core.CometNotifier;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.event.CodeToSendListener;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 * Por ahora este objeto no sirve gran cosa, es para que haya constancia de que hay un cliente
 * Todos las clases derivadas son clientes temporales (de error o similares)
 * que no forman parte del ciclo de vida normal de un cliente.
 *
 * @author jmarranz
 */
public abstract class ClientDocumentNoServerDocImpl extends ClientDocumentImpl
{
    protected CodeToSendRegistryImpl codeToSend = new CodeToSendRegistryImpl(this);

    public ClientDocumentNoServerDocImpl(ItsNatSessionImpl session)
    {
        super(session.getBrowser(),session);
    }

    public ItsNatDocumentImpl getItsNatDocumentImpl()
    {
        return null;
    }

    public boolean isScriptingEnabled()
    {
        return true;
    }

    public CodeToSendRegistryImpl getCodeToSendRegistry()
    {
        return codeToSend;
    }

    public String getCodeToSendAndReset()
    {
        return getCodeToSendRegistry().getCodeToSendAndReset();
    }

    public void addCodeToSend(Object code)
    {
        getCodeToSendRegistry().addCodeToSend(code);
    }

/*
    public void addCodeToSend(int index,Object code)
    {
        getCodeToSendRegistry().addCodeToSend(index,code);
    }
*/

    public boolean isSendCodeEnabled()
    {
        return getCodeToSendRegistry().isSendCodeEnabled();
    }

    public void disableSendCode()
    {
        getCodeToSendRegistry().disableSendCode();
    }

    public void enableSendCode()
    {
        getCodeToSendRegistry().enableSendCode();
    }

    public void addCodeToSendListener(CodeToSendListener listener)
    {
        getCodeToSendRegistry().addCodeToSendListener(listener);
    }

    public void removeCodeToSendListener(CodeToSendListener listener)
    {
        getCodeToSendRegistry().removeCodeToSendListener(listener);
    }

    public CometNotifier createCometNotifier()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public CometNotifier createCometNotifier(long eventTimeout)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public CometNotifier createCometNotifier(int commMode,long eventTimeout)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void startEventDispatcherThread(Runnable task)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public boolean dispatchEvent(EventTarget target, Event evt) throws EventException
    {
        throw new ItsNatException("Not supported in this context");
    }

    public boolean dispatchEvent(EventTarget target, Event evt, int commMode, long eventTimeout) throws EventException
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addContinueEventListener(EventTarget target, EventListener listener)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addContinueEventListener(EventTarget target, EventListener listener, int commMode, ParamTransport[] extraParams, String preSendCode, long eventTimeout)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public ItsNatTimer createItsNatTimer()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addAsynchronousTask(Runnable task, boolean lockDoc, int maxWait, EventTarget target, EventListener listener, int commMode, ParamTransport[] extraParams, String preSendCode, long eventTimeout)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addAsynchronousTask(Runnable task, EventListener listener)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, int commMode)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, ParamTransport extraParam)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, ParamTransport[] extraParams)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, String preSendCode)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, int commMode, ParamTransport[] extraParams, String preSendCode, long eventTimeout)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, int commMode, ParamTransport[] extraParams, String preSendCode, long eventTimeout,String bindToListener)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void removeEventListener(EventTarget target, String type, EventListener listener, boolean useCapture)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void removeEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addMutationEventListener(EventTarget target, EventListener listener, boolean useCapture)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addMutationEventListener(EventTarget target,EventListener listener,boolean useCapture,int commMode,String preSendCode,long eventTimeout)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addMutationEventListener(EventTarget target, EventListener listener, boolean useCapture, int commMode, String preSendCode, long eventTimeout,String bindToListener)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void removeMutationEventListener(EventTarget target, EventListener listener, boolean useCapture)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addUserEventListener(EventTarget target, String name, EventListener listener, int commMode, ParamTransport[] extraParams, String preSendCode, long eventTimeout,String bindToListener)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addUserEventListener(EventTarget target, String name, EventListener listener, int commMode, ParamTransport[] extraParams, String preSendCode, long eventTimeout)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addUserEventListener(EventTarget target, String name, EventListener listener)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void removeUserEventListener(EventTarget target, String name, EventListener listener)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void getGlobalEventListenerList(LinkedList<EventListener> list)
    {
        // Este es un cliente temporal por lo que no tiene sentido los global event listeners
        // este método es llamado para hacer acopio de listeners globales,
        // no lanzamos una excepción porque es posible que algún tipo de evento
        // que da lugar a un cliente especial de este tipo sea procesado
        // por los listeners globales a nivel de documento, servlet etc
        // aunque en este tipo de cliente no tengan sentido (porque en su ciclo
        // de vida o no hay eventos caso de los attached server o no duran más de un evento
        // caso de los de error).
    }

    public void addEventListener(EventListener listener)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void addEventListener(int index,EventListener listener)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void removeEventListener(EventListener listener)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public ScriptUtil getScriptUtil()
    {
        throw new ItsNatException("Not supported in this context");
    }
}
