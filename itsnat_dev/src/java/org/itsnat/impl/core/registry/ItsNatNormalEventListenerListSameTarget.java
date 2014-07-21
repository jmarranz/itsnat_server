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

package org.itsnat.impl.core.registry;

import java.io.Serializable;
import java.util.LinkedList;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.event.ItsNatEventListenerChainImpl;
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;
import org.itsnat.impl.core.util.MapListImpl;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class ItsNatNormalEventListenerListSameTarget implements Serializable
{
    protected MapListImpl<String,Pair> listeners = new MapListImpl<String,Pair>();

    /**
     * Creates a new instance of ItsNatNormalEventListenerListSameTarget
     */
    public ItsNatNormalEventListenerListSameTarget()
    {
    }

    public static String getKey(String type,boolean useCapture)
    {
        return useCapture + "_" + type;
    }

    public boolean isEmpty()
    {
        return listeners.isEmpty();
    }

    public boolean containsItsNatNormalEventListener(String type,boolean useCapture,EventListener listener)
    {
        // El id puede ser null, hay ámbitos en los que no se utiliza
        String key = getKey(type,useCapture);
        Pair value = new Pair(listener,null);
        return listeners.contains(key,value);
    }

    public void addItsNatNormalEventListener(String type,boolean useCapture,EventListener listener)
    {
        addItsNatNormalEventListener(type,useCapture,listener,null);
    }

    public void addItsNatNormalEventListener(String type,boolean useCapture,EventListener listener,ItsNatNormalEventListenerWrapperImpl listenerWrapper)
    {
        // El id puede ser null, hay ámbitos en los que no se utiliza
        String key = getKey(type,useCapture);
        Pair value = new Pair(listener,listenerWrapper);
        listeners.add(key,value);
    }

    public ItsNatNormalEventListenerWrapperImpl removeItsNatNormalEventListener(String type,boolean useCapture,EventListener listener)
    {
        String key = getKey(type,useCapture);
        Pair removed = listeners.remove(key,new Pair(listener,null));
        if (removed == null)
            return null;
        return removed.getListenerWrapper();
    }

    public boolean removeItsNatNormalEventListener(ItsNatNormalEventListenerWrapperImpl listenerWrapper)
    {
        ItsNatNormalEventListenerWrapperImpl listenerRes = removeItsNatNormalEventListener(listenerWrapper.getType(),listenerWrapper.getUseCapture(),listenerWrapper.getEventListener());
        if (listenerRes == null) return false; // Ya se eliminó probablemente
        if (listenerWrapper != listenerRes)
            throw new ItsNatException("INTERNAL ERROR");
        return true;
    }

    public void removeAllItsNatNormalEventListeners()
    {
        listeners.clear();
    }

    public LinkedList<Pair> getAllItsNatNormalEventListenersCopy()
    {
        return listeners.getAllValuesCopy();
    }

    public boolean hasItsNatNormalEventListeners(String type,boolean useCapture)
    {
        LinkedList<Pair> listeners = getItsNatNormalEventListeners(type,useCapture);
        return (listeners != null) && !listeners.isEmpty();
    }

    public LinkedList<Pair> getItsNatNormalEventListeners(String type,boolean useCapture)
    {
        String key = getKey(type,useCapture);
        return listeners.get(key);
    }

    public boolean getItsNatNormalEventListenerList(String type,boolean useCapture,ItsNatEventListenerChainImpl<EventListener> chain)
    {
        // Ver notas en getEventListenersArrayCopy

        LinkedList<Pair> list = getItsNatNormalEventListeners(type,useCapture);
        if (list == null)
            return false; // no se ha añadido ninguno

        LinkedList<EventListener> evtListeners = new LinkedList<EventListener>();
        for(Pair currPair : list)
        {
            evtListeners.add(currPair.getListener());
        }
        
        chain.addFirstListenerList(evtListeners);

        return true;
    }

    public EventListener[] getEventListenersArrayCopy(String type,boolean useCapture)
    {
        // De esta manera permitimos que al procesar un evento
        // se pueda añadir o quitar a su vez un listener (el Iterator no lo permite), lo cual
        // es conveniente en los componentes (en el blur de un componente editor reutilizado)

        LinkedList<Pair> list = getItsNatNormalEventListeners(type,useCapture);
        if (list == null)
            return null;

        EventListener[] listeners = new EventListener[list.size()];
        int i = 0;
        for(Pair currPair : list)
        {
            listeners[i] = currPair.getListener();
            i++;
        }
        return listeners;
    }

    public static class Pair implements Serializable
    {
        private EventListener listener;
        private ItsNatNormalEventListenerWrapperImpl listenerWrapper; // puede ser null en el caso de búsqueda

        public Pair(EventListener listener,ItsNatNormalEventListenerWrapperImpl listenerWrapper)
        {
            this.listener = listener;
            this.listenerWrapper = listenerWrapper; // puede ser null en el caso de que no se conozca
        }

        public EventListener getListener()
        {
            return listener;
        }

        public ItsNatNormalEventListenerWrapperImpl getListenerWrapper()
        {
            return listenerWrapper;
        }

        @Override
        public boolean equals(Object other)
        {
            boolean res = super.equals(other);
            if (res) return true;
            if (other == null) return false;
            // El listenerWrapper no cuenta pues puede ser null
            return listener == ((Pair)other).listener;
        }

        @Override
        public int hashCode()
        {
            return listener.hashCode();
        }
    }

}
