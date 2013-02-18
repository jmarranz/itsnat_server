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

import java.util.HashMap;
import java.util.Map;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.*;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class ItsNatCompDOMListenersByClientJoystickImpl extends ItsNatCompDOMListenersByClientImpl implements ItsNatCompDOMListenersJoystick
{
    protected Map<String,EventListener> loadScheduled = new HashMap<String,EventListener>();
    protected boolean joystickEnabled = false;

    public ItsNatCompDOMListenersByClientJoystickImpl(JoystickModeComponent comp,ClientDocumentImpl clientDoc)
    {
        super((ItsNatComponentImpl)comp,clientDoc);
    }

    public JoystickModeComponent getJoystickModeComponent()
    {
        return (JoystickModeComponent)comp;
    }

    public boolean isJoystickEnabled()
    {
        return joystickEnabled;
    }

    public void setJoystickEnabled(boolean value)
    {
        if (joystickEnabled != value)
        {
            comp.disableEventListenersByClient(this);
            this.joystickEnabled = value;
            comp.enableEventListenersByClient(this); // De acuerdo al nuevo modo
        }
    }

    public Map<String,EventListener> getLoadScheduledMap()
    {
        if (loadScheduled == null)
            this.loadScheduled = new HashMap<String,EventListener>();
        return loadScheduled;
    }

    public boolean mustAddRemove()
    {
        return ItsNatCompDOMListenersJoystickSharedImpl.mustAddRemove(this);
    }

    @Override
    protected void addInternalEventListener(ClientDocumentImpl clientDoc,String type,boolean useCapture, int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        if (isJoystickEnabled())
            addInternalEventListenerJoystick(clientDoc,type, useCapture, commMode, extraParams, preSendCode, eventTimeout,bindToListener);
        else
            super.addInternalEventListener(clientDoc,type, useCapture, commMode, extraParams, preSendCode, eventTimeout,bindToListener);
    }

    @Override
    protected void removeInternalEventListener(ClientDocumentImpl clientDoc,String type,boolean useCapture,boolean updateClient)
    {
        if (isJoystickEnabled())
            removeInternalEventListenerJoystick(clientDoc,type, useCapture, updateClient);
        else
            super.removeInternalEventListener(clientDoc,type, useCapture, updateClient);
    }

    private void addInternalEventListenerJoystick(ClientDocumentImpl clientDoc,String type,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        ItsNatCompDOMListenersJoystickSharedImpl.addInternalEventListenerJoystick(this,clientDoc,type,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToListener);
    }

    private void removeInternalEventListenerJoystick(ClientDocumentImpl clientDoc,String type,boolean useCapture,boolean updateClient)
    {
        ItsNatCompDOMListenersJoystickSharedImpl.removeInternalEventListenerJoystick(this,clientDoc,type,useCapture,updateClient);
    }

    public void addEventListenerJoystick(Element[] elemList)
    {
        ItsNatCompDOMListenersJoystickSharedImpl.addEventListenerJoystick(this, elemList);
    }

    public void removeEventListenerJoystick(Element[] elemList)
    {
        ItsNatCompDOMListenersJoystickSharedImpl.removeEventListenerJoystick(this, elemList);
    }

    public void addEventListenerJoystick(Element contentElem)
    {
        ItsNatCompDOMListenersJoystickSharedImpl.addEventListenerJoystick(this, contentElem);
    }

    public void removeEventListenerJoystick(Element contentElem)
    {
        ItsNatCompDOMListenersJoystickSharedImpl.removeEventListenerJoystick(this, contentElem);
    }
}
