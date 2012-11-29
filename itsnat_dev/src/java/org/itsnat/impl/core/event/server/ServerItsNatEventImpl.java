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

package org.itsnat.impl.core.event.server;

import org.itsnat.core.CommMode;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.event.ItsNatEventImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ServerItsNatEventImpl extends ItsNatEventImpl
{
    /** Creates a new instance of ServerItsNatEventImpl */
    public ServerItsNatEventImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return (ItsNatStfulDocumentImpl)getSource();
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return (ClientDocumentStfulImpl)getClientDocumentImpl();
    }

    public int getCommMode()
    {
        return CommMode.XHR_SYNC;
    }

    public ItsNatServletRequestImpl getItsNatServletRequestImpl()
    {
        // Por devolver algo, es raro que sea null pues lo normal es que
        // todo lo que se hace en una app. web esté dentro de una request del client al servidor
        return getItsNatStfulDocument().getCurrentItsNatServletRequest();
    }

}
