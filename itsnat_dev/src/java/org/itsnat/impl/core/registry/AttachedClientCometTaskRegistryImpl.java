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

import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.listener.*;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientCometImpl;
import org.itsnat.impl.core.comet.CometNotifierImpl;

/**
 *
 * @author jmarranz
 */
public class AttachedClientCometTaskRegistryImpl extends CometTaskRegistryImpl
{
    /**
     * Creates a new instance of AttachedClientCometTaskRegistryImpl
     */
    public AttachedClientCometTaskRegistryImpl(ClientDocumentAttachedClientCometImpl clientDoc)
    {
        super(clientDoc);
    }

    public boolean canAddItsNatEventListener(CometNotifierImpl notifier)
    {
        return true; // Sí, porque sino no podemos hacer control remoto
    }

    public ClientDocumentAttachedClientCometImpl getClientDocumentAttachedClientComet()
    {
        return (ClientDocumentAttachedClientCometImpl)clientDoc;
    }

    public CometTaskEventListenerWrapper createCometTaskEventListenerWrapper(CometTaskImpl taskContainer,ParamTransport[] extraParams,String preSendCode)
    {
        ClientDocumentAttachedClientCometImpl clientDoc = getClientDocumentAttachedClientComet();
        return new ItsNatAttachedClientCometEventListenerWrapperImpl(taskContainer,clientDoc);
    }
}
