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

package org.itsnat.impl.core.listener.domext;

import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.event.client.domext.ClientItsNatContinueEventImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ItsNatContinueEventListenerWrapperImpl extends ItsNatDOMExtEventListenerWrapperImpl
{
    protected int commMode;

    /** Creates a new instance of ItsNatContinueEventListenerWrapperImpl */
    public ItsNatContinueEventListenerWrapperImpl(ItsNatStfulDocumentImpl itsNatDoc,ClientDocumentStfulImpl clientDoc,EventTarget element,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        super(itsNatDoc,clientDoc,element,listener,extraParams,preSendCode,eventTimeout,bindToListener);

        this.commMode = commMode;
    }

    public int getCommModeDeclared()
    {
        return commMode;
    }
    
    public static String getTypeStatic()
    {
        return "itsnat:continue";
    }

    public static boolean isContinueType(String type)
    {
        return type.startsWith(getTypeStatic());
    }

    public String getType()
    {
        return getTypeStatic();
    }

    public ClientItsNatNormalEventImpl createClientItsNatNormalEvent(RequestNormalEventImpl request)
    {
        return new ClientItsNatContinueEventImpl(this,request);
    }
}
