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

package org.itsnat.impl.core.scriptren.bsren.listener.attachcli;

import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.attachcli.ItsNatAttachedClientTimerEventListenerWrapperImpl;
import org.itsnat.impl.core.scriptren.jsren.listener.attachcli.JSRenderItsNatAttachedClientTimerEventListenerImpl;
import org.itsnat.impl.core.scriptren.shared.listener.attachcli.JSAndBSRenderItsNatAttachedClientTimerEventListenerImpl;

/**
 *
 * @author jmarranz
 */
public class BSRenderItsNatAttachedClientTimerEventListenerImpl extends BSRenderItsNatAttachedClientEventListenerImpl
{
    public static final BSRenderItsNatAttachedClientTimerEventListenerImpl SINGLETON = new BSRenderItsNatAttachedClientTimerEventListenerImpl();

    /**
     * Creates a new instance of BSRenderItsNatAttachedClientTimerEventListenerImpl
     */
    public BSRenderItsNatAttachedClientTimerEventListenerImpl()
    {
    }

    public static BSRenderItsNatAttachedClientTimerEventListenerImpl getBSRenderItsNatAttachedClientTimerEventListener()
    {
        return SINGLETON;
    }    
    
    private static String addItsNatAttachedClientTimerEventListenerCode(ItsNatAttachedClientTimerEventListenerWrapperImpl listener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return JSAndBSRenderItsNatAttachedClientTimerEventListenerImpl.addItsNatAttachedClientTimerEventListenerCode(listener);
    }

    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return addItsNatAttachedClientTimerEventListenerCode((ItsNatAttachedClientTimerEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return null; // Nada que hacer
    }
}
