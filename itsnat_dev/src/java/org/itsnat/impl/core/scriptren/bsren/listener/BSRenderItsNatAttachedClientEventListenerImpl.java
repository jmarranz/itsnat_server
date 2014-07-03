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

package org.itsnat.impl.core.scriptren.bsren.listener;

import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.scriptren.jsren.listener.*;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.ItsNatAttachedClientCometEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.ItsNatAttachedClientEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.ItsNatAttachedClientTimerEventListenerWrapperImpl;

/**
 *
 * @author jmarranz
 */
public abstract class BSRenderItsNatAttachedClientEventListenerImpl extends BSRenderItsNatEventListenerImpl
{

    /** Creates a new instance of BSRenderItsNatAttachedClientEventListenerImpl */
    public BSRenderItsNatAttachedClientEventListenerImpl()
    {
    }

    public static BSRenderItsNatAttachedClientEventListenerImpl getBSRenderItsNatAttachedClientEventListener(ItsNatAttachedClientEventListenerWrapperImpl itsNatListener)
    {
        if (itsNatListener instanceof ItsNatAttachedClientTimerEventListenerWrapperImpl)
            return BSRenderItsNatAttachedClientTimerEventListenerImpl.SINGLETON;
        else if (itsNatListener instanceof ItsNatAttachedClientCometEventListenerWrapperImpl)
            return BSRenderItsNatAttachedClientCometEventListenerImpl.SINGLETON;
        return null;
    }

    @Override
    public void addItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        String code = addItsNatEventListenerCodeInherit(itsNatListener,clientDoc);
        clientDoc.addCodeToSend(code);
    }

    @Override    
    public void removeItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        String code = removeItsNatEventListenerCodeInherit(itsNatListener,clientDoc);
        clientDoc.addCodeToSend(code);
    }
}
