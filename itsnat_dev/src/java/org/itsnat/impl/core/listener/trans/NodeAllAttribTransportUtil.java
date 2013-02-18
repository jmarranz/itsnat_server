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

import java.util.HashMap;
import java.util.Map;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.opera.BrowserOpera;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author jmarranz
 */
public class NodeAllAttribTransportUtil extends ParamTransportUtil
{
    public static final NodeAllAttribTransportUtil SINGLETON = new NodeAllAttribTransportUtil();

    /**
     * Creates a new instance of ParamTransportUtil
     */
    public NodeAllAttribTransportUtil()
    {
    }

    public String getCodeToSend(ParamTransport param)
    {
        return "  event.getUtil().transpAllAttrs(event);\n";
    }

    public void syncServerBeforeDispatch(ParamTransport param,RequestNormalEventImpl request,ClientItsNatNormalEventImpl event)
    {
        syncWithServer(request,event);
    }

    public void syncServerAfterDispatch(ParamTransport param, RequestNormalEventImpl request,ClientItsNatNormalEventImpl event)
    {
    }

    public static void syncWithServer(RequestNormalEventImpl request,ClientItsNatNormalEventImpl event)
    {
        Element elem = (Element)event.getCurrentTarget();
        
        int attrNum = Integer.parseInt(request.getAttrOrParamExist("itsnat_attr_num"));
        if (attrNum > 0)
        {
            // En HTML da igual mayúsculas y minúsculas en los nodos y en los atributos
            // MSIE, FireFox y Safari devuelven en minúsculas los atributos (Node.attributes)
            // pero Opera en mayúsculas.            
            ClientDocumentImpl clientDoc = request.getClientDocument();
            Browser browser = clientDoc.getBrowser();
            boolean toLowerCase = (browser instanceof BrowserOpera) && request.getItsNatDocument().isMIME_HTML();
            Map<String,String> remoteAttribs = new HashMap<String,String>();
            for(int i = 0; i < attrNum; i++)
            {
                String name = request.getAttrOrParamExist("itsnat_attr_" + i);
                String value = request.getAttrOrParamExist(name);
                DOMUtilInternal.setAttribute(elem,name,value);

                if (toLowerCase) name = name.toLowerCase();
                remoteAttribs.put(name,value);
            }

            // Ahora eliminamos aquellos que ya no están en el cliente
            if (elem.hasAttributes())
            {
                NamedNodeMap attribs = elem.getAttributes();
                for(int i = 0; i < attribs.getLength(); i++)
                {
                    Attr attr = (Attr)attribs.item(i);
                    String name = attr.getName();
                    if (toLowerCase) name = name.toLowerCase();
                    if (!remoteAttribs.containsKey(name))
                        attribs.removeNamedItem(name);
                }
            }
        }
    }
}
