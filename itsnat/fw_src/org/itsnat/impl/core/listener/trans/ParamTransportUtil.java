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

import org.itsnat.core.event.NodeAllAttribTransport;
import org.itsnat.core.event.SingleParamTransport;
import org.itsnat.core.event.NodeCompleteTransport;
import org.itsnat.core.event.NodeMutationTransport;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ParamTransportUtil
{

    /**
     * Creates a new instance of ParamTransportUtil
     */
    public ParamTransportUtil()
    {
    }

    public static ParamTransportUtil getSingleton(ParamTransport param)
    {
        if (param instanceof SingleParamTransport)
            return SingleParamTransportUtil.getSingleParamTransportUtilSingleton((SingleParamTransport)param);
        else if (param instanceof NodeAllAttribTransport)
            return NodeAllAttribTransportUtil.SINGLETON;
        else if (param instanceof NodeCompleteTransport)
            return NodeCompleteTransportUtil.SINGLETON;
        else if (param instanceof NodeMutationTransport)
            return NodeMutationTransportUtil.SINGLETON;

        return null;
    }

    public abstract String getCodeToSend(ParamTransport param);

    public abstract void syncServerBeforeDispatch(ParamTransport param,RequestNormalEventImpl request,ClientItsNatNormalEventImpl event);
    public abstract void syncServerAfterDispatch(ParamTransport param, RequestNormalEventImpl request,ClientItsNatNormalEventImpl event);

}
