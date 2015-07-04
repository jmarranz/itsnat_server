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

package org.itsnat.impl.core.listener.trans;

import org.itsnat.core.event.NodeAttributeTransport;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.opera.BrowserOperaOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class NodeAttributeTransportUtil extends SingleParamTransportUtil
{
    public static final NodeAttributeTransportUtil SINGLETON = new NodeAttributeTransportUtil();

    /**
     * Creates a new instance of NodeAttributeTransportUtil
     */
    public NodeAttributeTransportUtil()
    {
    }

    public void syncServerBeforeDispatch(ParamTransport param,RequestNormalEventImpl request,ClientItsNatNormalEventImpl event)
    {
        Element elem = (Element)event.getCurrentTarget();

        String name = ((NodeAttributeTransport)param).getName();
        String value = request.getAttrOrParam(name);
        if (value != null)
            DOMUtilInternal.setAttribute(elem,name,value);
        else
        {
            // Ver notas NodeAllAttribTransportUtil
            ClientDocumentStfulImpl clientDoc = event.getClientDocumentStful();
            Browser browser = clientDoc.getBrowser();
            boolean toLowerCase = (browser instanceof BrowserOperaOld) && request.getItsNatDocument().isMIME_HTML();
            if (toLowerCase) name = name.toLowerCase();
            elem.removeAttribute(name);  // Si es null es que ha sido borrado en el cliente
        }
    }

    public void syncServerAfterDispatch(ParamTransport param, RequestNormalEventImpl request,ClientItsNatNormalEventImpl event)
    {
    }
}
