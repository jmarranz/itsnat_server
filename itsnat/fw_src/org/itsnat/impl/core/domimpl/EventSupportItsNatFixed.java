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

package org.itsnat.impl.core.domimpl;

import java.util.HashSet;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.events.AbstractEvent;
import org.apache.batik.dom.events.EventListenerList;
import org.apache.batik.dom.events.EventSupport;
import org.apache.batik.dom.events.NodeEventTarget;

/**
 *
 * @author jmarranz
 */
public class EventSupportItsNatFixed extends EventSupport
{
    public EventSupportItsNatFixed(AbstractNode n)
    {
        super(n);
    }

    @Override
    protected void fireEventListeners(NodeEventTarget node,
                                      AbstractEvent e,
                                      EventListenerList.Entry[] listeners,
                                      HashSet stoppedGroups,
                                      HashSet toBeStoppedGroups)
    {
        // DOM Level 2 no especifica cual es el orden de "dispatching"
        // de los event listeners, sin embargo DOM Level 3 determina
        // que el orden ha de ser el mismo que el de inserción
        // que es el orden que usa Xerces y los navegadores W3C (FireFox, Safari etc)
        // Sin embargo Batik despacha primero el último, de ahí que
        // cambiemos nosotros el orden (gracias a lo bien hecho que está Batik)
        // El orden de los listener es FUNDAMENTAL para ItsNat pues ItsNat
        // usa mutation event listeners, en donde el primero que se ha de ejecutar
        // es el mutation event listener renderizador, pero en ciertos casos es interesante
        // que el usuario añada sus mutation listeners 
        // Hay que tener en cuenta que el array NO es una copia del que almacena
        // Batik en el registro, por lo que DEBEMOS crear una copia con el orden cambiado.

        EventListenerList.Entry[] listCopy = null;
        if ((listeners != null)&&(listeners.length > 1)) // Yo creo que nunca es nulo, pero por si acaso
        {
            int len = listeners.length;
            listCopy = new EventListenerList.Entry[len];
            for(int i = 0; i < len; i++)
            {
                listCopy[len - i - 1] = listeners[i];
            }
        }
        else listCopy = listeners; // No merece la pena crear un objeto copia

        super.fireEventListeners(node,e,listCopy,stoppedGroups,toBeStoppedGroups);
    }
}
