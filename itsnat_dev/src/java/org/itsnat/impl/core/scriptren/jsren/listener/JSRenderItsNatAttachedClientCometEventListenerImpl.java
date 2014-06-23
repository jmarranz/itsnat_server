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
import org.itsnat.impl.core.listener.ItsNatAttachedClientCometEventListenerWrapperImpl;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatAttachedClientCometEventListenerImpl extends JSRenderItsNatAttachedClientEventListenerImpl
{
    public static final JSRenderItsNatAttachedClientCometEventListenerImpl SINGLETON = new JSRenderItsNatAttachedClientCometEventListenerImpl();

    /**
     * Creates a new instance of JSRenderItsNatAttachedClientCometEventListenerImpl
     */
    private JSRenderItsNatAttachedClientCometEventListenerImpl()
    {
    }

    private String addItsNatAttachedClientCometEventListenerCode(ItsNatAttachedClientCometEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        String listenerId = itsNatListener.getId();
        int sync = itsNatListener.getCommModeDeclared();
        long eventTimeout = getEventTimeout(itsNatListener,clientDoc);

        return "itsNatDoc.sendAttachCometTaskRefresh(\"" + listenerId + "\"," + sync + "," + eventTimeout + ");\n";
    }

    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return addItsNatAttachedClientCometEventListenerCode((ItsNatAttachedClientCometEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return null;
    }
}
