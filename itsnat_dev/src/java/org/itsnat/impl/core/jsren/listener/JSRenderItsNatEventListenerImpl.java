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

package org.itsnat.impl.core.jsren.listener;

import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
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

    public static JSRenderItsNatEventListenerImpl getJSRenderItsNatEventListener(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        if (itsNatListener instanceof ItsNatNormalEventListenerWrapperImpl)
            return JSRenderItsNatNormalEventListenerImpl.getJSRenderItsNatNormalEventListener((ItsNatNormalEventListenerWrapperImpl)itsNatListener,clientDoc);
        else if (itsNatListener instanceof ItsNatAttachedClientEventListenerWrapperImpl)
            return JSRenderItsNatAttachedClientEventListenerImpl.getJSRenderItsNatAttachedClientEventListener((ItsNatAttachedClientEventListenerWrapperImpl)itsNatListener);
        return null;
    }

    public static void addItsNatEventListenerCode(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        if (clientDoc != null)
        {
            // Aunque el listener sea de todos los clientes sólo deseamos renderizar para un cliente concreto
            JSRenderItsNatEventListenerImpl render = JSRenderItsNatEventListenerImpl.getJSRenderItsNatEventListener(itsNatListener, clientDoc);
            render.addItsNatEventListenerCodeClient(itsNatListener,clientDoc);
        }
        else
        {
            // Para todos los clientes
            ItsNatStfulDocumentImpl itsNatDoc = itsNatListener.getItsNatStfulDocument();
            if (!itsNatDoc.hasClientDocumentAttachedClient())
            {
                clientDoc = itsNatDoc.getClientDocumentStfulOwner();

                JSRenderItsNatEventListenerImpl render = JSRenderItsNatEventListenerImpl.getJSRenderItsNatEventListener(itsNatListener, clientDoc);
                render.addItsNatEventListenerCodeClient(itsNatListener,clientDoc);
            }
            else
            {
                ClientDocumentStfulImpl[] clients = itsNatDoc.getAllClientDocumentStfulsCopy();
                for(int i = 0; i < clients.length; i++)
                {
                    clientDoc = clients[i];

                    JSRenderItsNatEventListenerImpl render = JSRenderItsNatEventListenerImpl.getJSRenderItsNatEventListener(itsNatListener, clientDoc);
                    render.addItsNatEventListenerCodeClient(itsNatListener,clientDoc);
                }
            }
        }
    }

    public static void removeItsNatEventListenerCode(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        if (clientDoc != null)
        {
            // Aunque el listener sea de todos los clientes sólo deseamos renderizar para un cliente concreto
            JSRenderItsNatEventListenerImpl render = JSRenderItsNatEventListenerImpl.getJSRenderItsNatEventListener(itsNatListener, clientDoc);
            render.removeItsNatEventListenerCodeClient(itsNatListener,clientDoc);
        }
        else
        {
            // Para todos
            ItsNatStfulDocumentImpl itsNatDoc = itsNatListener.getItsNatStfulDocument();
            if (!itsNatDoc.hasClientDocumentAttachedClient())
            {
                clientDoc = itsNatDoc.getClientDocumentStfulOwner();

                JSRenderItsNatEventListenerImpl render = JSRenderItsNatEventListenerImpl.getJSRenderItsNatEventListener(itsNatListener, clientDoc);
                render.removeItsNatEventListenerCodeClient(itsNatListener,clientDoc);
            }
            else
            {
                ClientDocumentStfulImpl[] clients = itsNatDoc.getAllClientDocumentStfulsCopy();
                for(int i = 0; i < clients.length; i++)
                {
                    clientDoc = clients[i];

                    JSRenderItsNatEventListenerImpl render = JSRenderItsNatEventListenerImpl.getJSRenderItsNatEventListener(itsNatListener, clientDoc);
                    render.removeItsNatEventListenerCodeClient(itsNatListener,clientDoc);
                }
            }
        }
    }

    public long getEventTimeout(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        return itsNatListener.getEventTimeout();
    }

    // clientDoc NO puede ser nulo
    public abstract void addItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc);

    // clientDoc NO puede ser nulo
    public abstract void removeItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc);

    protected abstract String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc);

    protected abstract String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc);

}
