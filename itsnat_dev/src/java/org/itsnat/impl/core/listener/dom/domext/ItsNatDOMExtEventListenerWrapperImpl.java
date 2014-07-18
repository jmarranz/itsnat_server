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

import org.itsnat.impl.core.listener.dom.ItsNatDOMEventListenerWrapperImpl;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.listener.*;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatDOMExtEventListenerWrapperImpl extends ItsNatDOMEventListenerWrapperImpl
{

    /** Creates a new instance of ItsNatDOMExtEventListenerWrapperImpl */
    public ItsNatDOMExtEventListenerWrapperImpl(ItsNatStfulDocumentImpl itsNatDoc,ClientDocumentStfulImpl clientDoc,EventTarget element,EventListener listener,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        super(itsNatDoc,clientDoc,element,listener,extraParams,preSendCode,eventTimeout,bindToCustomFunc);
    }

    public boolean getUseCapture()
    {
        return false;
    }

    public static boolean isExtensionType(String type)
    {
        return type.startsWith("itsnat:");
    }
}
