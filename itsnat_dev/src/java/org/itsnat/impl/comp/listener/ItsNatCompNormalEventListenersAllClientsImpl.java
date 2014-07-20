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

package org.itsnat.impl.comp.listener;

import java.io.Serializable;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.comp.ItsNatComponentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentMapImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatCompNormalEventListenersAllClientsImpl implements Serializable
{
    protected ItsNatComponentImpl comp;
    protected ClientDocumentMapImpl normalEventListenersByClient;

    public ItsNatCompNormalEventListenersAllClientsImpl(ItsNatComponentImpl comp)
    {
        this.comp = comp;
        ItsNatDocumentImpl itsNatDoc = comp.getItsNatDocumentImpl();
        this.normalEventListenersByClient = ClientDocumentMapImpl.createClientDocumentMap(itsNatDoc);
        ClientDocumentImpl[] clients = itsNatDoc.getAllClientDocumentsCopy();
        for(int i = 0; i < clients.length; i++)
        {
            ClientDocumentImpl clientDoc = clients[i];
            addItsNatCompNormalEventListenersByClient(clientDoc);
        }
    }

    public int size()
    {
        return normalEventListenersByClient.size();
    }

    public ItsNatCompNormalEventListenersByClientImpl getItsNatCompNormalEventListenersByClient(ClientDocumentImpl clientDoc)
    {
        return (ItsNatCompNormalEventListenersByClientImpl)normalEventListenersByClient.get(clientDoc);
    }

    public ItsNatCompNormalEventListenersByClientImpl[] getAllItsNatCompNormalEventListenersByClient()
    {
        ItsNatCompNormalEventListenersByClientImpl[] clients = new ItsNatCompNormalEventListenersByClientImpl[normalEventListenersByClient.size()];
        normalEventListenersByClient.fillAllValues(clients);
        return clients;
    }

    public ItsNatCompNormalEventListenersByClientImpl addItsNatCompNormalEventListenersByClient(ClientDocumentImpl clientDoc)
    {
        ItsNatCompNormalEventListenersByClientImpl domListeners = comp.createItsNatCompNormalEventListenersByClient(clientDoc);
        if (normalEventListenersByClient.put(clientDoc, domListeners) != null)
            throw new ItsNatException("INTERNAL ERROR");
        return domListeners;
    }

    public ItsNatCompNormalEventListenersByClientImpl removeItsNatCompNormalEventListenersByClient(ClientDocumentImpl clientDoc)
    {
        return (ItsNatCompNormalEventListenersByClientImpl)normalEventListenersByClient.remove(clientDoc);
    }
}
