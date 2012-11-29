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

package org.itsnat.impl.core.clientdoc;

import java.io.Serializable;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatXMLDocumentImpl;


public abstract class ClientDocumentMapImpl implements Serializable
{
    protected ItsNatDocumentImpl itsNatDoc;
    protected Object ownerValue;

    public ClientDocumentMapImpl(ItsNatDocumentImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public static ClientDocumentMapImpl createClientDocumentMap(ItsNatDocumentImpl itsNatDoc)
    {
        if (itsNatDoc instanceof ItsNatStfulDocumentImpl)
            return new ClientDocumentStfulMapImpl((ItsNatStfulDocumentImpl)itsNatDoc);
        else if (itsNatDoc instanceof ItsNatXMLDocumentImpl)
            return new ClientDocumentMapXMLImpl((ItsNatXMLDocumentImpl)itsNatDoc);
        return null;
    }

    public ClientDocumentImpl getClientDocumentOwner()
    {
        return itsNatDoc.getClientDocumentOwnerImpl();
    }

    public Object get(ClientDocumentImpl clientDoc)
    {
        if (clientDoc == getClientDocumentOwner())
            return ownerValue;
        else
            return null;
    }

    public Object put(ClientDocumentImpl clientDoc,Object value)
    {
        Object old = null;
        if (clientDoc == getClientDocumentOwner())
        {
            old = this.ownerValue;
            this.ownerValue = value;
        }
        return old;
    }

    public abstract Object remove(ClientDocumentImpl clientDoc);

    public int size()
    {
        return 1;
    }

    public void fillAllValues(Object[] values)
    {
        values[0] = this.ownerValue;
    }

    public Object getOwnerValue()
    {
        return ownerValue;
    }

    public void execAction(ClientDocumentMapAction action)
    {
        action.exec(getClientDocumentOwner(), ownerValue);
    }

}
