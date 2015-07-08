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

import java.lang.reflect.Method;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.SingleParamTransport;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.conv.StringToObjectConverter;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class NodePropertyTransportUtil extends SingleParamTransportUtil
{
    public static final NodePropertyTransportUtil SINGLETON = new NodePropertyTransportUtil();

    /**
     * Creates a new instance of NodePropertyTransportUtil
     */
    public NodePropertyTransportUtil()
    {
    }

    public void syncServerBeforeDispatch(ParamTransport param,RequestNormalEventImpl request,ClientItsNatNormalEventImpl event)
    {
        Element elem = (Element)event.getCurrentTarget();

        String name = ((SingleParamTransport)param).getName();
        String value = request.getAttrOrParam(name);

        NodePropertyTransport prop = (NodePropertyTransport)param;
        String attrName = prop.getAttrName();
        if (attrName != null)
        {
            // Sincronizamos la propiedad como un atributo del elemento en el servidor
            if (value != null)
                DOMUtilInternal.setAttribute(elem,attrName,value);
            else
                elem.removeAttribute(attrName);  // Si es null lo indicamos eliminando el atributo
        }
        else
        {
            // Ej. si name es "data" => "setData"
            Class<?> type = prop.getType();
            String methodName = prop.getJavaSetMethodName();
            Class<?> nodeClass = elem.getClass();
            try
            {
                Method method = nodeClass.getMethod(methodName,new Class<?>[]{type});
                Object valueArg = StringToObjectConverter.convert(value,type);
                method.invoke(elem,new Object[]{valueArg});
            }
            catch (Exception ex)
            {
                throw new ItsNatException(ex,event);
            }
        }
    }

    public void syncServerAfterDispatch(ParamTransport param, RequestNormalEventImpl request,ClientItsNatNormalEventImpl event)
    {
    }
}
