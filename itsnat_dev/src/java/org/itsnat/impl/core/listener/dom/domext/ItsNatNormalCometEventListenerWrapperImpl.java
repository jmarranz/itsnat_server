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

package org.itsnat.impl.core.listener.dom.domext;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.CommModeImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.comet.CometNotifierImpl;
import org.itsnat.impl.core.listener.*;
import org.itsnat.impl.core.comet.NormalCometNotifierImpl;
import org.itsnat.impl.core.event.client.dom.domext.ClientItsNatNormalCometEventImpl;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class ItsNatNormalCometEventListenerWrapperImpl extends ItsNatGenericTaskEventListenerWrapperImpl implements CometTaskEventListenerWrapper
{
    /**
     * Creates a new instance of ItsNatNormalCometEventListenerWrapperImpl
     */
    public ItsNatNormalCometEventListenerWrapperImpl(ClientDocumentStfulImpl clientDoc,CometTaskImpl task,ParamTransport[] extraParams,String preSendCode)
    {
        super(clientDoc,task,null,getEventListener(task),extraParams,preSendCode,task.getEventTimeout(),null);
    }

    public int getCommModeDeclared()
    {
        NormalCometNotifierImpl notifier = (NormalCometNotifierImpl)getCometTask().getCometNotifier();
        return notifier.getCommMode();
    }

    public static EventListener getEventListener(CometNotifierImpl notifier)
    {
        return ((NormalCometNotifierImpl)notifier).getEventListenerDispatcher();
    }
    
    public static EventListener getEventListener(CometTaskImpl task)
    {
        return getEventListener(task.getCometNotifier());
    }

    public CometTaskImpl getCometTask()
    {
        return (CometTaskImpl)task;
    }

    public static String getTypeStatic()
    {
        return "itsnat:comet";
    }

    public String getType()
    {
        return getTypeStatic();
    }

    public ClientItsNatNormalEventImpl createClientItsNatNormalEvent(RequestNormalEventImpl request)
    {
        return new ClientItsNatNormalCometEventImpl(this,request);
    }

}
