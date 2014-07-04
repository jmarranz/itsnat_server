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

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.event.client.domext.ClientItsNatUserEventImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ItsNatUserEventListenerWrapperImpl extends ItsNatDOMExtEventListenerWrapperImpl
{
    protected int commMode;
    protected String name;

    /** Creates a new instance of ItsNatUserEventListenerWrapperImpl */
    public ItsNatUserEventListenerWrapperImpl(ItsNatStfulDocumentImpl itsNatDoc,ClientDocumentStfulImpl clientDoc,EventTarget element,String name,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        super(itsNatDoc,clientDoc,element,listener,extraParams,preSendCode,eventTimeout,bindToCustomFunc);

        this.commMode = commMode;
        this.name = name;
    }

    public int getCommModeDeclared()
    {
        return commMode;
    }

    public String getName()
    {
        return name;
    }

    public static String getTypePrefix()
    {
        return "itsnat:user:";
    }

    public static boolean isUserType(String type)
    {
        return type.startsWith(getTypePrefix());
    }

    public static String getType(String name)
    {
        return getTypePrefix() + name;
    }

    public static String getNameFromType(String type)
    {
        return getNameFromType(type,true);
    }

    public static String getNameFromType(String type,boolean check)
    {
        if (check && !isUserType(type)) throw new ItsNatException("Bad type format, must start with: " + getTypePrefix());

        return type.substring(getTypePrefix().length());
    }

    public String getType()
    {
        return getType(getName());
    }

    public ClientItsNatNormalEventImpl createClientItsNatNormalEvent(RequestNormalEventImpl request)
    {
        return new ClientItsNatUserEventImpl(this,request);
    }
}
