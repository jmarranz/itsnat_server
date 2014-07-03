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

package org.itsnat.impl.core.scriptren.jsren.listener;

import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.ItsNatAttachedClientEventListenerWrapperImpl;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderItsNatEventListenerImpl
{

    /** Creates a new instance of JSRenderItsNatEventListenerImpl */
    public JSRenderItsNatEventListenerImpl()
    {
    }

    public static JSRenderItsNatEventListenerImpl getJSRenderItsNatEventListener(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        if (itsNatListener instanceof ItsNatNormalEventListenerWrapperImpl)
            return JSRenderItsNatNormalEventListenerImpl.getJSRenderItsNatNormalEventListener((ItsNatNormalEventListenerWrapperImpl)itsNatListener,clientDoc);
        else if (itsNatListener instanceof ItsNatAttachedClientEventListenerWrapperImpl)
            return JSRenderItsNatAttachedClientEventListenerImpl.getJSRenderItsNatAttachedClientEventListener((ItsNatAttachedClientEventListenerWrapperImpl)itsNatListener);
        return null;
    }


    // clientDoc NO puede ser nulo
    public abstract String addItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc);

    // clientDoc NO puede ser nulo
    public abstract String removeItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc);

    protected abstract String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc);

    protected abstract String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc);

}
