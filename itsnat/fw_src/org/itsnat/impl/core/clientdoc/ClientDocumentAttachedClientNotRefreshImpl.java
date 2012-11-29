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
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.listener.ItsNatAttachedClientNotRefreshEventListenerWrapperImpl;

/**
 *
 * @author jmarranz
 */
public class ClientDocumentAttachedClientNotRefreshImpl extends ClientDocumentAttachedClientImpl
{
    protected ItsNatAttachedClientNotRefreshEventListenerWrapperImpl listener;

    /** Creates a new instance of ClientDocumentAttachedTimerImpl */
    public ClientDocumentAttachedClientNotRefreshImpl(boolean readOnly,int commMode,long eventTimeout,long waitDocTimeout,Browser browser,ItsNatSessionImpl session,ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(readOnly,commMode,eventTimeout,waitDocTimeout,browser,session,itsNatDoc);
    }

    public void startAttachedClient()
    {
        this.listener = new ItsNatAttachedClientNotRefreshEventListenerWrapperImpl(this);
    }

    public ItsNatAttachedClientNotRefreshEventListenerWrapperImpl getItsNatAttachedClientNotRefreshEventListenerWrapper()
    {
        return listener;
    }

    public String getRefreshMethod()
    {
        return "none";
    }
}
