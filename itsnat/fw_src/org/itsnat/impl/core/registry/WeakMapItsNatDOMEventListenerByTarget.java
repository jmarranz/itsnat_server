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
import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.impl.core.listener.ItsNatDOMEventListenerWrapperImpl;
import org.itsnat.impl.core.registry.ItsNatDOMEventListenerListSameTarget.Pair;
import org.itsnat.impl.core.util.WeakMapExpungeableImpl;
import org.itsnat.impl.core.util.WeakMapExpungeableImpl.ExpungeListener;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class WeakMapItsNatDOMEventListenerByTarget implements ExpungeListener,Serializable
{
    protected ItsNatDOMEventListenerRegistryImpl parentRegistry;
    protected WeakMapExpungeableImpl eventListenersByTarget = new WeakMapExpungeableImpl(this);

    // Evitamos "sujetar" los nodos DOM porque un listener sólo tiene
    // sentido (por lo menos en ItsNat) si el nodo está vinculado al Document
    // por lo que si está en el Document ya está sujeto, si no está en el Document
    // (y nadie más lo sujeta) evitamos así memory leaks en programadores descuidados
    // que no liberan listeners por ejemplo antes nodos que ser pierden
    // al eliminarse su fragmento. Esto funciona incluso con el AutoCleanEventListeners desactivado.
    // Esto no es suficiente con componentes que son listeners sí mismos en donde olvidemos hacer dispose(),
    // estos necesitan el AutoCleanEventListeners también activado para que pueda actuar el Garbage Collector,
    // porque el registro sujeta a los componentes con referencias normales por ejemplo en remoteListenersById
    // y el propio componente tiene una referencia normal al nodo, la simple
    // eliminación del nodo del documento no libera al nodo para el GC pues hay más referencias.

    public WeakMapItsNatDOMEventListenerByTarget(ItsNatDOMEventListenerRegistryImpl parentRegistry)
    {
        this.parentRegistry = parentRegistry;
    }

    public boolean containsItsNatDOMEventListener(EventTarget target,String type,EventListener listener,boolean useCapture)
    {
        ItsNatDOMEventListenerListSameTarget targetList = (ItsNatDOMEventListenerListSameTarget)eventListenersByTarget.get(target);
        if (targetList == null)
            return false;
        return targetList.containsItsNatDOMEventListener(type,useCapture,listener);
    }

    public ItsNatDOMEventListenerListSameTarget getItsNatDOMEventListenersByTarget(EventTarget target)
    {
        if (!parentRegistry.isValidEventTarget(target,false)) return null; // Nos ahorramos la búsqueda

        return (ItsNatDOMEventListenerListSameTarget)eventListenersByTarget.get(target);
    }

    public EventListener[] getEventListenersArrayCopy(EventTarget target,String type,boolean useCapture)
    {
        if (!parentRegistry.isValidEventTarget(target,false)) return null; // Nos ahorramos la búsqueda

        ItsNatDOMEventListenerListSameTarget targetList = (ItsNatDOMEventListenerListSameTarget)eventListenersByTarget.get(target);
        if (targetList == null)
            return null;
        return targetList.getEventListenersArrayCopy(type,useCapture);
    }

    public ItsNatDOMEventListenerWrapperImpl removeItsNatDOMEventListener(EventTarget target,String type,EventListener listener,boolean useCapture)
    {
        ItsNatDOMEventListenerListSameTarget targetList = (ItsNatDOMEventListenerListSameTarget)eventListenersByTarget.get(target);
        if (targetList == null)
            return null;

        ItsNatDOMEventListenerWrapperImpl listenerWrapper = targetList.removeItsNatDOMEventListener(type,useCapture,listener);

        if (targetList.isEmpty())
            eventListenersByTarget.remove(target);

        return listenerWrapper;
    }

    public void removeItsNatDOMEventListener(ItsNatDOMEventListenerWrapperImpl listenerWrapper)
    {
        EventTarget target = listenerWrapper.getCurrentTarget();
        ItsNatDOMEventListenerListSameTarget targetList = (ItsNatDOMEventListenerListSameTarget)eventListenersByTarget.get(target);
        if (targetList == null)
            return;

        targetList.removeItsNatDOMEventListener(listenerWrapper);

        if (targetList.isEmpty())
            eventListenersByTarget.remove(target);
    }

    public int removeAllItsNatDOMEventListeners(EventTarget target,boolean updateClient)
    {
        if (!parentRegistry.isValidEventTarget(target,false)) return 0; // No pudo registrarse, nos ahorramos una búsqueda inútil

        ItsNatDOMEventListenerListSameTarget targetList = (ItsNatDOMEventListenerListSameTarget)eventListenersByTarget.get(target);
        if (targetList == null)
            return 0;

        LinkedList<Pair> listeners = targetList.getAllItsNatDOMEventListenersCopy();
        if (listeners == null)
            return 0;
        // "listeners" es una copia por lo que es desechable e inmutable
        for(Pair pair : listeners)
        {
            ItsNatDOMEventListenerWrapperImpl listener = pair.getListenerWrapper();
            parentRegistry.removeItsNatDOMEventListener(listener,updateClient,false);
        }

        targetList.removeAllItsNatDOMEventListeners(); // No hace falta pero por si acaso y para que quede claro

        eventListenersByTarget.remove(target);

        return listeners.size(); // El número de listeners removidos
    }

    protected void addItsNatDOMEventListener(ItsNatDOMEventListenerWrapperImpl listenerWrapper)
    {
        EventTarget target = listenerWrapper.getCurrentTarget();
        ItsNatDOMEventListenerListSameTarget targetList = (ItsNatDOMEventListenerListSameTarget)eventListenersByTarget.get(target);
        if (targetList == null)
        {
            targetList = new ItsNatDOMEventListenerListSameTarget();
            eventListenersByTarget.put(target,targetList);
        }
        targetList.addItsNatDOMEventListener(listenerWrapper.getType(),listenerWrapper.getUseCapture(),listenerWrapper.getEventListener(),listenerWrapper);
    }

    public void processExpunged(Object value)
    {
        ItsNatDOMEventListenerListSameTarget targetList = (ItsNatDOMEventListenerListSameTarget)value;

        LinkedList<Pair> listeners = targetList.getAllItsNatDOMEventListenersCopy();
        if (listeners == null)
            return;
        // "listeners" es una copia por lo que es desechable e inmutable
        for(Pair pair : listeners)
        {
            ItsNatDOMEventListenerWrapperImpl listener = pair.getListenerWrapper();
            parentRegistry.removeItsNatDOMEventListener(listener,false,true);

            // Pasamos updateClient = false para evitar llamar getJSRender().removeEventListenerCode(listenerWrapper);
            // precisamente porque este método se llama cuando nadie sujeta el nodo
            // y por tanto no pertenece al Document.
            // Al menos evitamos que haya memory leaks en el servidor
            // aunque podrá haber en el cliente, para evitarlo han de desregistrarse
            // explícitamente los listeners cuando no se necesitan o el nodo DOM se va a perder
        }
    }
}
