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
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.scriptren.shared.listener.JSAndBSRenderItsNatDOMStdEventListenerImpl;
import org.itsnat.impl.core.scriptren.shared.listener.RenderItsNatDOMStdEventListener;

/**
 *
 * @author jmarranz
 */
public abstract class BSRenderItsNatDOMStdEventListenerImpl extends BSRenderItsNatDOMEventListenerImpl implements RenderItsNatDOMStdEventListener
{
    /** Creates a new instance of BSRenderItsNatDOMStdEventListenerImpl */
    public BSRenderItsNatDOMStdEventListenerImpl()
    {
    }

    public static BSRenderItsNatDOMStdEventListenerImpl getBSRenderItsNatDOMStdEventListener(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return BSRenderItsNatDOMStdEventListenerDefaultImpl.SINGLETON;
    }

    protected String addItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return JSAndBSRenderItsNatDOMStdEventListenerImpl.addItsNatDOMStdEventListenerCode(itsNatListener,clientDoc,this);
    }

    protected String removeItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return JSAndBSRenderItsNatDOMStdEventListenerImpl.removeItsNatDOMStdEventListenerCode(itsNatListener,clientDoc,this);   
    }

    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return addItsNatDOMStdEventListenerCode((ItsNatDOMStdEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return removeItsNatDOMStdEventListenerCode((ItsNatDOMStdEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

}
