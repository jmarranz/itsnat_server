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
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.util.HasUniqueId;
import org.itsnat.impl.core.util.UniqueId;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatEventListenerWrapperImpl implements HasUniqueId,Serializable
{
    protected UniqueId idObj;
    protected ItsNatStfulDocumentImpl itsNatDoc; // Nunca es null

    /** Creates a new instance of ItsNatEventListenerWrapperImpl */
    public ItsNatEventListenerWrapperImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        this.idObj = itsNatDoc.getUniqueIdGenerator().generateUniqueId("el"); // el = event listener
    }

    public abstract int getCommModeDeclared();

    public abstract long getEventTimeout();

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return itsNatDoc;
    }

    public String getId()
    {
        return idObj.getId();
    }

    public UniqueId getUniqueId()
    {
        return idObj;
    }
}
