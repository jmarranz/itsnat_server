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

package org.itsnat.impl.core.event;

import java.util.LinkedList;
import org.itsnat.core.event.ItsNatEventListenerChain;
import org.w3c.dom.events.EventListener;


/**
 *
 * @author jmarranz
 */
public abstract class ItsNatEventListenerChainImpl<T> implements ItsNatEventListenerChain
{
    protected boolean stop = false;
    protected LinkedList<T> listeners;

    public ItsNatEventListenerChainImpl(LinkedList<T> listeners)
    {
        this.listeners = listeners;
    }

    public LinkedList<T> getListeners()
    {
        return listeners;
    }

    public boolean addFirstListenerList(LinkedList<T> listeners)
    {
        // Este método es llamado para añadir sobre la marcha más listeners,
        // es decir, cuando se está despachando ya un listener,
        // estos nuevos listeners están ligados al que se está despachando (que viene a ser un contenedor) por eso
        // los insertamos al principio pues el que se está despachando ya se quitó
        // de la lista pues antes de despachar se elimina.
        // Se conserva el orden propio de los listeners que se añaden
        // Los elementos son o DOM EventListener o ItsNatAttachedClientEventListener
        if (listeners == null) return false;
        return this.listeners.addAll(0,listeners);
    }

    public void continueChain()
    {
        this.stop = false;
    }

    public void stop()
    {
        this.stop = true;
    }

    public boolean isStopped()
    {
        return stop;
    }
}
