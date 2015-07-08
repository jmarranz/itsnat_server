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

package org.itsnat.impl.core.scriptren.jsren.listener.norm.domext;

import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatUserEventListenerWrapperImpl;
import org.itsnat.impl.core.scriptren.shared.listener.JSAndBSRenderItsNatUserEventListenerImpl;
import org.itsnat.impl.core.scriptren.shared.listener.RenderItsNatUserEventListener;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatUserEventListenerImpl extends JSRenderItsNatDOMExtEventListenerImpl implements RenderItsNatUserEventListener
{
    public static final JSRenderItsNatUserEventListenerImpl SINGLETON = new JSRenderItsNatUserEventListenerImpl();

    /** Creates a new instance of JSRenderItsNatUserEventListenerImpl */
    public JSRenderItsNatUserEventListenerImpl()
    {
    }

    private String addItsNatUserEventListenerCode(ItsNatUserEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return JSAndBSRenderItsNatUserEventListenerImpl.addItsNatUserEventListenerCode(itsNatListener,clientDoc,this);
    }

    private String removeItsNatUserEventListenerCode(ItsNatUserEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return JSAndBSRenderItsNatUserEventListenerImpl.removeItsNatUserEventListenerCode(itsNatListener);
    }

    @Override    
    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return addItsNatUserEventListenerCode((ItsNatUserEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    @Override    
    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return removeItsNatUserEventListenerCode((ItsNatUserEventListenerWrapperImpl)itsNatListener,clientDoc);
    }
}
