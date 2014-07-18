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

package org.itsnat.impl.core.event.client.dom;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.event.EventInternal;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.listener.dom.ItsNatDOMEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ClientItsNatDOMEventImpl extends ClientItsNatNormalEventImpl implements EventInternal
{
    protected boolean stopPropagation = false;
    protected boolean preventDefault = false;

    /**
     * Creates a new instance of ClientItsNatDOMEventImpl
     */
    public ClientItsNatDOMEventImpl(ItsNatDOMEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public void checkInitializedEvent()
    {
        // Está bien formado porque viene del cliente.
    }

    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg)
    {
        throw new ItsNatException("Not implemented",this);
    }

    public void setTarget(EventTarget target)
    {
        if (getTarget() != target) throw new ItsNatException("Event target cannot be changed");
    }

    public ItsNatDOMEventListenerWrapperImpl getDOMEventListenerWrapper()
    {
        return (ItsNatDOMEventListenerWrapperImpl)listenerWrapper;
    }



    public void stopPropagation()
    {
        this.stopPropagation = true;
    }

    public boolean getStopPropagation()
    {
        return stopPropagation;
    }

    public void preventDefault()
    {
        this.preventDefault = true;
    }

    public boolean getPreventDefault()
    {
        return preventDefault;
    }

    public EventTarget getCurrentTarget()
    {
        return getDOMEventListenerWrapper().getCurrentTarget();
    }

    public long getTimeStamp()
    {
        // El parámetro timeStamp lo enviamos a medida en el cliente, por una parte porque el Event de MSIE
        // no lo soporta, por otra parte porque en Chrome Event.timeStamp es erróneo pues no es un entero
        // es una fecha como cadena, finalmente porque hay eventos propios (extensiones) de ItsNat que
        // implementan Event y que si queremos simular este atributo es mejor generar el valor
        // en el cliente y enviarlo al servidor y no generarlo con un System.currentTimeMillis()
        // pues los eventos pueden encolarse en el cliente y estar un tiempo significativo "atrapados"
        // si se genera en el servidor el timeStamp quedará falseado
        return getParameterLong("timeStamp");
    }
}
