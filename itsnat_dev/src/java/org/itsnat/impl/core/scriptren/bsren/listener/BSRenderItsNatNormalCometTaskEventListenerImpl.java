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
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatNormalCometEventListenerWrapperImpl;
import org.itsnat.impl.core.scriptren.shared.listener.JSAndBSRenderItsNatNormalCometTaskEventListenerImpl;
import org.itsnat.impl.core.scriptren.shared.listener.RenderItsNatNormalCometTaskEventListener;

/**
 *
 * @author jmarranz
 */
public class BSRenderItsNatNormalCometTaskEventListenerImpl extends BSRenderItsNatGenericTaskEventListenerImpl implements RenderItsNatNormalCometTaskEventListener
{
    public static final BSRenderItsNatNormalCometTaskEventListenerImpl SINGLETON = new BSRenderItsNatNormalCometTaskEventListenerImpl();

    /**
     * Creates a new instance of BSRenderItsNatNormalCometTaskEventListenerImpl
     */
    private BSRenderItsNatNormalCometTaskEventListenerImpl()
    {
    }

    private String addNormalCometTaskEventListenerCode(ItsNatNormalCometEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return JSAndBSRenderItsNatNormalCometTaskEventListenerImpl.addNormalCometTaskEventListenerCode(itsNatListener, clientDoc,this);
    }

    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return addNormalCometTaskEventListenerCode((ItsNatNormalCometEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return null; // Nada que hacer
    }
}
