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
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.comet.CometNotifierImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.scriptren.jsren.listener.JSRenderItsNatEventListenerImpl;
import org.itsnat.impl.core.listener.*;
import org.itsnat.impl.core.scriptren.bsren.listener.BSRenderItsNatEventListenerImpl;
import org.itsnat.impl.core.util.MapUniqueId;

/**
 *
 * @author jmarranz
 */
public abstract class CometTaskRegistryImpl extends EventListenerRegistryImpl implements Serializable
{
    protected MapUniqueId<ItsNatEventListenerWrapperImpl> tasks;
    protected ClientDocumentStfulImpl clientDoc;

    /**
     * Creates a new instance of CometTaskRegistryImpl
     */
    public CometTaskRegistryImpl(ClientDocumentStfulImpl clientDoc)
    {
        this.clientDoc = clientDoc;

        this.tasks = new MapUniqueId<ItsNatEventListenerWrapperImpl>(clientDoc.getUniqueIdGenerator());
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return clientDoc.getItsNatStfulDocument();
    }

    public abstract CometTaskEventListenerWrapper createCometTaskEventListenerWrapper(CometTaskImpl taskContainer);

    public abstract boolean canAddItsNatEventListener(CometNotifierImpl notifier);

    public void addCometTask(CometNotifierImpl notifier)
    {
        // Se supone que ItsNatDocument está sincronizado (bloqueado por este hilo)
        if (!canAddItsNatEventListener(notifier))
            return;

        CometTaskImpl taskContainer = new CometTaskImpl(notifier);
        ItsNatEventListenerWrapperImpl listener = (ItsNatEventListenerWrapperImpl)createCometTaskEventListenerWrapper(taskContainer);

        tasks.put(listener);

        
        ClientDocumentStfulDelegateImpl clientDocDeleg = clientDoc.getClientDocumentStfulDelegate();        
        addItsNatEventListenerCode(listener,clientDocDeleg);       
    }

    public CometTaskEventListenerWrapper removeCometTask(String id)
    {
        // Método no público, es llamado por el framework
        // En este contexto hay que recordar que el ItsNatDocument está bloqueado
        // por el hilo actual.

        CometTaskEventListenerWrapper listener = (CometTaskEventListenerWrapper)tasks.removeById(id);
        if (listener == null)
            throw new ItsNatException("Comet Task with id " + id + " does not exist");

        // Ver comentarios en AsyncTaskRegistry.removeAsynchronousTask

        return listener;
    }
}
