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

package org.itsnat.impl.comp.listener;

import java.io.Serializable;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.CommModeImpl;


public class EventListenerParamsImpl implements Serializable
{
    protected boolean useCapture;
    protected int commMode;
    protected ParamTransport[] extraParams;
    protected String preSendCode;
    protected long eventTimeout;
    protected String bindToListener;

    /** Creates a new instance of EventListenerParamsImpl */
    public EventListenerParamsImpl(boolean useCapture,int commMode,ParamTransport[] extraParams,
            String preSendCode,long eventTimeout,String bindToListener)
    {
        this.useCapture = useCapture;
        CommModeImpl.checkMode(commMode);
        this.commMode = commMode;
        this.extraParams = extraParams;
        this.preSendCode = preSendCode;
        this.eventTimeout = eventTimeout;
        this.bindToListener = bindToListener;
    }

    public boolean isUseCapture()
    {
        return useCapture;
    }

    public int getCommModeDeclared()
    {
        return commMode;
    }

    public ParamTransport[] getExtraParams()
    {
        return extraParams;
    }

    public String getPreSendCode()
    {
        return preSendCode;
    }

    public long getEventTimeout()
    {
        return eventTimeout;
    }

    public String getBindToListener()
    {
        return bindToListener;
    }
}
