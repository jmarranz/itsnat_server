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

import java.io.Serializable;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.core.mut.doc.BeforeAfterMutationRenderListener;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public class AutoBuildCompBeforeAfterMutationRenderListener implements BeforeAfterMutationRenderListener,Serializable
{
    protected ItsNatDocComponentManagerImpl compMgr;

    /** Creates a new instance of AutoBuildCompBeforeAfterMutationRenderListener */
    public AutoBuildCompBeforeAfterMutationRenderListener(ItsNatDocComponentManagerImpl compMgr)
    {
        this.compMgr = compMgr;
    }

    public ItsNatDocComponentManagerImpl getItsNatComponentManager()
    {
        return compMgr;
    }

    public void beforeRender(Node node,MutationEvent mutEvent)
    {
        String type = mutEvent.getType();
        if (type.equals("DOMNodeRemoved"))
        {
            // node no está eliminado todavía del árbol DOM servidor
            ItsNatDocComponentManagerImpl compMgr = getItsNatComponentManager();
            compMgr.removeItsNatComponent(node,true);
        }
    }

    public void afterRender(Node node,MutationEvent mutEvent)
    {
        String type = mutEvent.getType();
        if (type.equals("DOMNodeInserted"))
        {
            // Hay que tener en cuenta que el nodo ya está insertado en el DOM servidor
            ItsNatDocComponentManagerImpl compMgr = getItsNatComponentManager();
            compMgr.addItsNatComponent(node,null,null,true); // Si no puede ser un componente no hace nada
        }
    }
}
