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

package org.itsnat.impl.core.listener.droid;

import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.event.client.droid.ClientItsNatDroidKeyEventImpl;
import org.itsnat.impl.core.event.client.droid.ClientItsNatDroidMotionEventImpl;
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ItsNatDroidEventListenerWrapperImpl extends ItsNatNormalEventListenerWrapperImpl
{
    protected int commMode;
    protected String type;
    protected boolean useCapture;    
    
    /** Creates a new instance of ItsNatDroidEventListenerWrapperImpl */
    public ItsNatDroidEventListenerWrapperImpl(ItsNatStfulDocumentImpl itsNatDoc,ClientDocumentStfulImpl clientDoc,EventTarget currTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        super(itsNatDoc,clientDoc,currTarget,listener,extraParams,preSendCode,eventTimeout,bindToCustomFunc);    
        
        this.commMode = commMode;
        this.type = type;
        this.useCapture = useCapture;        
    }
    
    public boolean getUseCapture()
    {
        return useCapture;
    }

    @Override
    public String getType()
    {
        return type;
    }

    @Override
    public ClientItsNatNormalEventImpl createClientItsNatNormalEvent(RequestNormalEventImpl request)
    {
        if ("click".equals(type) ||
            "touchstart".equals(type) || 
            "touchend".equals(type) ||
            "touchmove".equals(type) ||
            "touchcancel".equals(type))
            return new ClientItsNatDroidMotionEventImpl(this,request);
        else if ("keydown".equals(type) ||
                 "keyup".equals(type))
            return new ClientItsNatDroidKeyEventImpl(this,request);
        
        return null;
    }

    @Override
    public int getCommModeDeclared()
    {
        return commMode;
    }
}
