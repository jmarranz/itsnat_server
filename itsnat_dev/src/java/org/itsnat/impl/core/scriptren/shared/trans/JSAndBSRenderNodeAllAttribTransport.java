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

package org.itsnat.impl.core.scriptren.shared.trans;

import java.util.HashMap;
import java.util.Map;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.opera.BrowserOpera;
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
public class JSAndBSRenderNodeAllAttribTransport extends JSAndBSRenderParamTransport
{
    public static final JSAndBSRenderNodeAllAttribTransport SINGLETON = new JSAndBSRenderNodeAllAttribTransport();

    /**
     * Creates a new instance of ParamTransportUtil
     */
    public JSAndBSRenderNodeAllAttribTransport()
    {
    }

    public String getCodeToSend(ParamTransport param)
    {
        return "  event.getUtil().transpAllAttrs(event);\n";
    }

}
