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
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;
import org.itsnat.impl.core.registry.ItsNatNormalEventListenerListSameTarget.Pair;
import org.itsnat.impl.core.util.WeakMapExpungeableImpl;
import org.itsnat.impl.core.util.WeakMapExpungeableImpl.ExpungeListener;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class WeakMapItsNatNormalEventListenerByTarget implements ExpungeListener,Serializable
{
    protected ItsNatNormalEventListenerRegistryImpl parentRegistry;
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

    public WeakMapItsNatNormalEventListenerByTarget(ItsNatNormalEventListenerRegistryImpl parentRegistry)
    {
        this.parentRegistry = parentRegistry;
    }

    public boolean containsItsNatNormalEventListener(EventTarget target,String type,EventListener listener,boolean useCapture)
    {
        ItsNatNormalEventListenerListSameTarget targetList = (ItsNatNormalEventListenerListSameTarget)eventListenersByTarget.get(target);
        if (targetList == null)
            return false;
        return targetList.containsItsNatNormalEventListener(type,useCapture,listener);
    }

    public ItsNatNormalEventListenerListSameTarget getItsNatNormalEventListenersByTarget(EventTarget target)
    {
        return (ItsNatNormalEventListenerListSameTarget)eventListenersByTarget.get(target);
    }

    public EventListener[] getEventListenersArrayCopy(EventTarget target,String type,boolean useCapture)
    {
        if (!parentRegistry.isValidEventTarget(target,type,false)) return null; // Nos ahorramos la búsqueda

        ItsNatNormalEventListenerListSameTarget targetList = (ItsNatNormalEventListenerListSameTarget)eventListenersByTarget.get(target);
        if (targetList == null)
            return null;
        return targetList.getEventListenersArrayCopy(type,useCapture);
    }

    public ItsNatNormalEventListenerWrapperImpl removeItsNatNormalEventListener(EventTarget target,String type,EventListener listener,boolean useCapture)
    {
        ItsNatNormalEventListenerListSameTarget targetList = (ItsNatNormalEventListenerListSameTarget)eventListenersByTarget.get(target);
        if (targetList == null)
            return null;

        ItsNatNormalEventListenerWrapperImpl listenerWrapper = targetList.removeItsNatNormalEventListener(type,useCapture,listener);

        if (targetList.isEmpty())
            eventListenersByTarget.remove(target);

        return listenerWrapper;
    }

    public void removeItsNatNormalEventListener(ItsNatNormalEventListenerWrapperImpl listenerWrapper)
    {
        EventTarget target = listenerWrapper.getCurrentTarget();
        ItsNatNormalEventListenerListSameTarget targetList = (ItsNatNormalEventListenerListSameTarget)eventListenersByTarget.get(target);
        if (targetList == null)
            return;

        targetList.removeItsNatNormalEventListener(listenerWrapper);

        if (targetList.isEmpty())
            eventListenersByTarget.remove(target);
    }

    public int removeAllItsNatNormalEventListeners(EventTarget target,boolean updateClient)
    {
        ItsNatNormalEventListenerListSameTarget targetList = (ItsNatNormalEventListenerListSameTarget)eventListenersByTarget.get(target);
        if (targetList == null)
            return 0;

        LinkedList<Pair> listeners = targetList.getAllItsNatNormalEventListenersCopy();
        if (listeners == null)
            return 0;
        // "listeners" es una copia por lo que es desechable e inmutable
        for(Pair pair : listeners)
        {
            ItsNatNormalEventListenerWrapperImpl listener = pair.getListenerWrapper();
            parentRegistry.removeItsNatNormalEventListener(listener,updateClient,false);
        }

        targetList.removeAllItsNatNormalEventListeners(); // No hace falta pero por si acaso y para que quede claro

        eventListenersByTarget.remove(target);

        return listeners.size(); // El número de listeners removidos
    }

    public void addItsNatNormalEventListener(ItsNatNormalEventListenerWrapperImpl listenerWrapper)
    {
        EventTarget target = listenerWrapper.getCurrentTarget();
        ItsNatNormalEventListenerListSameTarget targetList = (ItsNatNormalEventListenerListSameTarget)eventListenersByTarget.get(target);
        if (targetList == null)
        {
            targetList = new ItsNatNormalEventListenerListSameTarget();
            eventListenersByTarget.put(target,targetList);
        }
        targetList.addItsNatNormalEventListener(listenerWrapper.getType(),listenerWrapper.getUseCapture(),listenerWrapper.getEventListener(),listenerWrapper);
    }

    @Override
    public void processExpunged(Object value)
    {
        ItsNatNormalEventListenerListSameTarget targetList = (ItsNatNormalEventListenerListSameTarget)value;

        LinkedList<Pair> listeners = targetList.getAllItsNatNormalEventListenersCopy();
        if (listeners == null)
            return;
        // "listeners" es una copia por lo que es desechable e inmutable
        for(Pair pair : listeners)
        {
            ItsNatNormalEventListenerWrapperImpl listener = pair.getListenerWrapper();
            parentRegistry.removeItsNatNormalEventListener(listener,false,true);

            // Pasamos updateClient = false para evitar llamar getJSRender().removeEventListenerCode(listenerWrapper);
            // precisamente porque este método se llama cuando nadie sujeta el nodo
            // y por tanto no pertenece al Document.
            // Al menos evitamos que haya memory leaks en el servidor
            // aunque podrá haber en el cliente, para evitarlo han de desregistrarse
            // explícitamente los listeners cuando no se necesitan o el nodo DOM se va a perder
        }
    }
}
