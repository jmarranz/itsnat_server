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

package org.itsnat.impl.core.comet;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientCometImpl;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class AttachedClientCometNotifierImpl extends CometNotifierImpl
{
    /** Creates a new instance of AttachedClientCometNotifierImpl */
    public AttachedClientCometNotifierImpl(ClientDocumentAttachedClientCometImpl clientDoc)
    {
        super(false,clientDoc); // userDataSync es false porque este notifier es usado internamente y de hecho a día de hoy el ItsNatUserDataImpl base NO se usa

        clientDoc.addAttachedClientCometTask(this);
    }

    public ClientDocumentAttachedClientCometImpl getClientDocumentAttachedClientComet()
    {
        return (ClientDocumentAttachedClientCometImpl)clientDoc;
    }

    public void addCometTask()
    {
        getClientDocumentAttachedClientComet().addAttachedClientCometTask(this);
    }

    public long getEventTimeout()
    {
        return getClientDocumentAttachedClientComet().getEventTimeout();
    }

    public void addEventListener(EventListener listener)
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public void removeEventListener(EventListener listener)
    {
        throw new ItsNatException("INTERNAL ERROR");
    }
}
