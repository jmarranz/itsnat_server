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

package org.itsnat.impl.core.resp.attachcli;

import java.util.LinkedList;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.event.client.ItsNatAttachedClientEventErrorImpl;
import org.itsnat.impl.core.listener.ItsNatAttachedClientEventListenerUtil;
import org.itsnat.impl.core.req.RequestImpl;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClient;

/**
 *
 * @author jmarranz
 */
public class ResponseAttachedClientErrorSharedImpl
{

    public static boolean processGlobalListeners(ResponseAttachedClient response)
    {
        RequestAttachedClient request = response.getRequestAttachedClient();

        ItsNatServletImpl itsNatServlet = ((RequestImpl)request).getItsNatServletRequest().getItsNatServletImpl();

        if (!itsNatServlet.hasItsNatAttachedClientEventListeners())
            return false;

        LinkedList listeners = new LinkedList();
        itsNatServlet.getItsNatAttachedClientEventListenerList(listeners);

        ItsNatAttachedClientEventErrorImpl event = new ItsNatAttachedClientEventErrorImpl(request);
        ItsNatAttachedClientEventListenerUtil.handleEventListeners(event, listeners);

        return true;
    }

}
