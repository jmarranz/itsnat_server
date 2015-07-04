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
import org.itsnat.impl.core.listener.dom.domext.ItsNatTimerEventListenerWrapperImpl;
import org.itsnat.impl.core.scriptren.shared.listener.JSAndBSRenderItsNatTimerEventListenerImpl;
import org.itsnat.impl.core.scriptren.shared.listener.RenderItsNatTimerEventListener;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatTimerEventListenerImpl extends JSRenderItsNatDOMExtEventListenerImpl implements RenderItsNatTimerEventListener
{
    private static final JSRenderItsNatTimerEventListenerImpl SINGLETON = new JSRenderItsNatTimerEventListenerImpl();

    /** Creates a new instance of JSRenderItsNatTimerEventListenerImpl */
    public JSRenderItsNatTimerEventListenerImpl()
    {
    }

    public static JSRenderItsNatTimerEventListenerImpl getJSRenderItsNatTimerEventListener()
    {
        return SINGLETON;
    }
    
    private String addItsNatTimerEventListenerCode(ItsNatTimerEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return JSAndBSRenderItsNatTimerEventListenerImpl.addItsNatTimerEventListenerCode(itsNatListener, clientDoc, this);
    }

    private String removeItsNatTimerEventListenerCode(ItsNatTimerEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return JSAndBSRenderItsNatTimerEventListenerImpl.removeItsNatTimerEventListenerCode(itsNatListener);
    }

    public String updateItsNatTimerEventListenerCode(ItsNatTimerEventListenerWrapperImpl itsNatListener,long computedPeriod,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return JSAndBSRenderItsNatTimerEventListenerImpl.updateItsNatTimerEventListenerCode(itsNatListener, computedPeriod);
    }

    @Override    
    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return addItsNatTimerEventListenerCode((ItsNatTimerEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    @Override    
    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return removeItsNatTimerEventListenerCode((ItsNatTimerEventListenerWrapperImpl)itsNatListener,clientDoc);
    }
}
