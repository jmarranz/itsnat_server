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

import org.itsnat.impl.comp.*;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatCompDOMListenersByDocImpl extends ItsNatCompDOMListenersImpl
{
    public ItsNatCompDOMListenersByDocImpl(ItsNatComponentImpl comp)
    {
        super(comp);
    }

    public void addClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        if (hasEnabledDOMEvents())
        {
            Object[] types = getEnabledDOMEvents().toArray();
            for(int i = 0; i < types.length; i++)
            {
                String type = (String)types[i];
                addInternalEventListener(clientDoc,type);
            }
        }
    }

    public void removeClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        if (hasEnabledDOMEvents())
        {
            Object[] types = getEnabledDOMEvents().toArray();
            for(int i = 0; i < types.length; i++)
            {
                String type = (String)types[i];
                removeInternalEventListener(clientDoc,type,false); // updateClient = false para evitar generación de código inútil
            }
        }
    }

    protected void addInternalEventListener(String type)
    {
        ItsNatDocumentImpl itsNatDoc = getItsNatDocumentImpl();
        ClientDocumentImpl[] clients = itsNatDoc.getAllClientDocumentsCopy();
        for(int i = 0; i < clients.length; i++)
        {
            addInternalEventListener(clients[i],type);
        }
    }

    protected void removeInternalEventListener(String type,boolean updateClient)
    {
        ItsNatDocumentImpl itsNatDoc = getItsNatDocumentImpl();
        ClientDocumentImpl[] clients = itsNatDoc.getAllClientDocumentsCopy();
        for(int i = 0; i < clients.length; i++)
        {
            removeInternalEventListener(clients[i],type,updateClient);
        }
    }
}
