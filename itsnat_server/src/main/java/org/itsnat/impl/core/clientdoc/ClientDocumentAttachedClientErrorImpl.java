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

import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;

/**
 *
 * @author jmarranz
 */
public class ClientDocumentAttachedClientErrorImpl extends ClientDocumentWithoutDocumentImpl
{
    protected int phase;

    /** Creates a new instance of ClientDocumentAttachedClientErrorImpl */
    public ClientDocumentAttachedClientErrorImpl(ItsNatSessionImpl itsNatSession)
    {
        super(itsNatSession);
    }

    public void registerInSession()
    {
        // No se guarda
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return null;
    }

    public int getPhase()
    {
        return phase;
    }

    public void setPhase(int phase)
    {
        this.phase = phase;
    }

    public int getCommModeDeclared()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public long getEventTimeout()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public long getWaitDocTimeout()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public boolean isAccepted()
    {
        return false;
    }

    public void setAccepted(boolean accepted)
    {
        if (accepted) throw new ItsNatException("Not supported in this context");
    }

    public boolean isReadOnly()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void setReadOnly(boolean readOnly)
    {
        throw new ItsNatException("Not supported in this context");
    }

  
}
