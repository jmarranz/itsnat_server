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

import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.attachcli.ItsNatAttachedClientCometEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.attachcli.ItsNatAttachedClientEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.attachcli.ItsNatAttachedClientTimerEventListenerWrapperImpl;
import org.itsnat.impl.core.scriptren.bsren.listener.BSRenderItsNatEventListenerImpl;

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
    public String addItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return addItsNatEventListenerCodeInherit(itsNatListener,clientDoc);
    }

    @Override    
    public String removeItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return removeItsNatEventListenerCodeInherit(itsNatListener,clientDoc);
    }
    
    public static String addAttachUnloadListenerCode(ClientDocumentAttachedClientImpl clientDoc)
    {        
        int commMode = clientDoc.getCommModeDeclared();

        return "itsNatDoc.addAttachUnloadListener(" + commMode + ");\n";    
    }
    
    protected abstract String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc);    
    protected abstract String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc);    
}
