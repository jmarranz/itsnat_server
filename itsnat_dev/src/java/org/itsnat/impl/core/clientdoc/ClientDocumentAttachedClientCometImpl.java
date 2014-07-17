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

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.CommModeImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.comet.AttachedClientCometNotifierImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.listener.CometTaskEventListenerWrapper;
import org.itsnat.impl.core.registry.CometTaskRegistryImpl;
import org.itsnat.impl.core.registry.AttachedClientCometTaskRegistryImpl;

/**
 *
 * @author jmarranz
 */
public class ClientDocumentAttachedClientCometImpl extends ClientDocumentAttachedClientImpl
{
    protected AttachedClientCometNotifierImpl attachClientCometNotifier;
    protected AttachedClientCometTaskRegistryImpl attachClientCometTaskRegistry;

    /** Creates a new instance of ClientDocumentAttachedClientCometImpl */
    public ClientDocumentAttachedClientCometImpl(boolean readOnly,int commMode,long eventTimeout,long waitDocTimeout,Browser browser,ItsNatSessionImpl session,ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(readOnly,commMode,eventTimeout,waitDocTimeout,browser,session,itsNatDoc);

        if (!CommModeImpl.isPureAsyncMode(commMode))
            throw new ItsNatException("Communication mode must be pure synchronous in Comet");
    }

    public void startAttachedClient()
    {
        this.attachClientCometNotifier = new AttachedClientCometNotifierImpl(this);
    }

    public CometTaskRegistryImpl getAttachedClientCometTaskRegistry()
    {
        if (attachClientCometTaskRegistry == null)
            this.attachClientCometTaskRegistry = new AttachedClientCometTaskRegistryImpl(this); // para ahorrar memoria si no se usa
        return attachClientCometTaskRegistry;
    }

    public void addAttachedClientCometTask(AttachedClientCometNotifierImpl notifier)
    {
        getAttachedClientCometTaskRegistry().addCometTask(notifier);
    }

    public CometTaskEventListenerWrapper removeAttachedClientCometTask(String id)
    {
        return getAttachedClientCometTaskRegistry().removeCometTask(id);
    }

    @Override
    protected void setInvalidInternal()
    {
        super.setInvalidInternal();

        if (attachClientCometNotifier != null) // Por si se llamara este método antes de startAttachedClient()
            attachClientCometNotifier.stop();
    }

    @Override
    public void normalEventReceivedInDocument()
    {
        super.normalEventReceivedInDocument();

        if (attachClientCometNotifier != null) // Por si se llamara este método antes de startAttachedClient()
            attachClientCometNotifier.notifyClient();
    }

    public String getRefreshMethod()
    {
        return "comet";
    }
}
