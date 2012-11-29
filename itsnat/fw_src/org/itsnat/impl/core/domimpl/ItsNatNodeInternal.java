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

import org.itsnat.impl.core.domimpl.deleg.DelegateNodeImpl;
import org.itsnat.core.ItsNatNode;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public interface ItsNatNodeInternal extends Node,EventTarget,ItsNatNode
{
    /* Este método creará el DelegateNodeImpl si no está creado, incluso en multihilo (clonación del template),
     * Para evitar problemas de multihilo se llamará a getDelegateNode() en los constructores.
     * No creamos el DelegateNodeImpl al definir el atributo porque las clases base de algunos
     * tipos de nodos lanzan eventos DOM mutation tal que los métodos fireXXX necesitan llamar
     * a  getDelegateNode() por lo que este método puede llamarse antes que los constructores,
     * por eso en getDelegateNode() hacemos la construcción aunque intentemos crear los DelegateNodeImpl
     * explícitamente en los constructores.
     */
    public DelegateNodeImpl getDelegateNode();

    public void addEventListenerInternal(String type, EventListener listener, boolean useCapture);
    public void removeEventListenerInternal(String type, EventListener listener, boolean useCapture);

    // Métodos en los que se crean/despachan mutation events.
    public void fireDOMNodeInsertedIntoDocumentEvent();
    public void fireDOMNodeRemovedFromDocumentEvent();
    public void fireDOMCharacterDataModifiedEvent(String oldv,String newv);
}
