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

package org.itsnat.impl.core.listener.dom.domext;

import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.event.ItsNatTimerHandle;
import org.itsnat.impl.core.doc.ItsNatTimerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.event.client.dom.domext.ClientItsNatTimerEventImpl;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.scriptren.jsren.listener.JSRenderItsNatTimerEventListenerImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.itsnat.impl.core.util.UserDataMonoThreadImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ItsNatTimerEventListenerWrapperImpl extends ItsNatDOMExtEventListenerWrapperImpl implements ItsNatTimerHandle
{
    private static final int SCHEDULED   = 1;
    private static final int EXECUTED    = 2;
    private static final int CANCELLED   = 3;

    protected int commMode;
    protected long time; // incluye el delay de la primera vez (si existe)
    protected long period;
    protected long nextExecutionTime;
    protected boolean fixedRate = false;
    protected int state = SCHEDULED;
    protected ItsNatTimerImpl timer;
    protected UserDataMonoThreadImpl userData;

    /**
     * Creates a new instance of ItsNatTimerEventListenerWrapperImpl
     */
    public ItsNatTimerEventListenerWrapperImpl(EventTarget target,EventListener listener,long time,long period,boolean fixedRate,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc,ItsNatTimerImpl timer)
    {
        super(timer.getItsNatStfulDocument(),timer.getClientDocumentStful(),target,listener,extraParams,preSendCode,eventTimeout,bindToCustomFunc);

        this.commMode = commMode;
        this.time = time;
        this.period = period;
        this.fixedRate = fixedRate;
        this.timer = timer;

        this.nextExecutionTime = time;
    }

    public int getCommModeDeclared()
    {
        return commMode;
    }

    public long getFirstTime()
    {
        return time;
    }

    public long getDelayFirstTime()
    {
        long delay = getFirstTime() - System.currentTimeMillis(); // El tiempo que ha de esperar hasta que le toque ejecutarse por primera vez
        if (delay < 0)
            delay = 0; // YA (ya vamos con retraso, ocurre cuando time es el instante "actual" pero hasta llegar aquí puede haber pasado un milisegundo), si time es una fecha del pasado ejecutamos de todas formas tal y como se documenta java.util.Timer
        return delay;
    }

    public long getPeriod()
    {
        return period;
    }

    public ItsNatTimer getItsNatTimer()
    {
        return timer;
    }

    public ItsNatTimerImpl getItsNatTimerImpl()
    {
        return timer;
    }

    public boolean cancel()
    {
        if (state == CANCELLED) return false; // Ya cancelado

        boolean res = (state == SCHEDULED) || ((state == EXECUTED)&&(period != 0)); // o no se ha ejecutado o se ha ejecutado pero es periódico
        this.state = CANCELLED;
        timer.removeListener(this);  // Si ya fue eliminado no pasa nada
        return res;
    }

    public boolean isCancelled()
    {
        return (state == CANCELLED);
    }

    public long scheduledExecutionTime()
    {
        // Este método si es llamado durante el proceso del evento
        // representa el instante teórico en el que le tocaría ejecutarse (que puede diferir con el momento actual si hay retraso)
        // Si es llamado después del proceso del evento representa la siguiente ejecución prevista.
        return nextExecutionTime;
    }

    @Override
    public void handleEvent(ClientItsNatNormalEventImpl event)
    {
        long computedPeriod = handleTimerEvent((ClientItsNatTimerEventImpl)event);

        if (computedPeriod != -1)  // Sigue ejecutándose (status en EXECUTED y period > 0)
        {
            ClientDocumentStfulImpl clientDoc = event.getClientDocumentStful();
            ClientDocumentStfulDelegateWebImpl clientDocDeleg = (ClientDocumentStfulDelegateWebImpl)clientDoc.getClientDocumentStfulDelegate();
            JSRenderItsNatTimerEventListenerImpl.SINGLETON.updateItsNatTimerEventListenerCode(this,computedPeriod,clientDocDeleg);
        }
    }

    public long handleTimerEvent(ClientItsNatTimerEventImpl event)
    {
        if (state != CANCELLED)  // En algún momento ha sido cancelado pero el evento desde el cliente ha sido enviado (no ha dado tiempo de que llegue la orden al cliente), es posible que esto no ocurra nunca pero por si acaso
        {
            this.state = EXECUTED;

            super.handleEvent(event);

            if (state == CANCELLED)
            {
                // timer periódico cancelado en el handleTimerEvent del usuario, no se ejecutará más
                // Ya está desregistrado (cancel() ya lo hace)
                return -1;
            }
            else if (period == 0)
            {
                // Ya se ha ejecutado y no se ejecutará más, lo eliminamos de la lista
                // state queda en EXECUTED
                timer.removeListener(this);
                return -1;
            }
            else
            {
                // EXECUTED y period > 0
                long computedPeriod;
                if (!fixedRate)
                {
                    this.nextExecutionTime = System.currentTimeMillis() + period;
                    computedPeriod = period;
                }
                else
                {
                    this.nextExecutionTime = nextExecutionTime + period;
                    if (System.currentTimeMillis() >= nextExecutionTime) // Estamos ejecutando un evento cuando el siguiente debería de haberse ya producido, por tanto el siguiente se hará cuanto antes
                        computedPeriod = 0;
                    else
                        computedPeriod = period; // Vamos bien
                }
                return computedPeriod;
            }
        }
        else
        {
            // timer periódico cancelado, no se ejecutará más
            timer.removeListener(this);
            return -1;
        }
    }

    public static String getTypeStatic()
    {
        return "itsnat:timer";
    }

    public String getType()
    {
        return getTypeStatic();
    }

    public UserDataMonoThreadImpl getUserData()
    {
        if (userData == null)
            this.userData = new UserDataMonoThreadImpl(); // Para ahorrar memoria si no se usa. No es necesario sincronizar pues el evento es manejado por un unico hilo
        return userData;
    }

    public boolean containsUserValueName(String name)
    {
        return getUserData().containsName(name);
    }

    public String[] getUserValueNames()
    {
        return getUserData().getUserDataNames();
    }

    public Object getUserValue(String name)
    {
        return getUserData().getUserData(name);
    }

    public Object setUserValue(String name,Object value)
    {
        return getUserData().setUserData(name,value);
    }

    public Object removeUserValue(String name)
    {
        return getUserData().removeUserData(name);
    }

    public ClientItsNatNormalEventImpl createClientItsNatNormalEvent(RequestNormalEventImpl request)
    {
        return new ClientItsNatTimerEventImpl(this,request);
    }
}
