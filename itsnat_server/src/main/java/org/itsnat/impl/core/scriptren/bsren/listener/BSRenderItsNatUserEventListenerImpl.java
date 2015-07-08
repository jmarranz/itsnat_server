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

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatUserEventListenerWrapperImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.scriptren.shared.listener.JSAndBSRenderItsNatUserEventListenerImpl;
import org.itsnat.impl.core.scriptren.shared.listener.RenderItsNatUserEventListener;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class BSRenderItsNatUserEventListenerImpl extends BSRenderItsNatDOMExtEventListenerImpl implements RenderItsNatUserEventListener
{
    public static final BSRenderItsNatUserEventListenerImpl SINGLETON = new BSRenderItsNatUserEventListenerImpl();

    /** Creates a new instance of BSRenderItsNatUserEventListenerImpl */
    public BSRenderItsNatUserEventListenerImpl()
    {
    }

    private String addItsNatUserEventListenerCode(ItsNatUserEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return JSAndBSRenderItsNatUserEventListenerImpl.addItsNatUserEventListenerCode(itsNatListener,clientDoc,this);
    }

    private String removeItsNatUserEventListenerCode(ItsNatUserEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return JSAndBSRenderItsNatUserEventListenerImpl.removeItsNatUserEventListenerCode(itsNatListener);
    }

    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return addItsNatUserEventListenerCode((ItsNatUserEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return removeItsNatUserEventListenerCode((ItsNatUserEventListenerWrapperImpl)itsNatListener,clientDoc);
    }
}
