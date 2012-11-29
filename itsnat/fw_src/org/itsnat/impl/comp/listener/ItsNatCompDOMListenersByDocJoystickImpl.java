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

import org.itsnat.impl.comp.*;
import java.util.HashMap;
import java.util.Map;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatCompDOMListenersByDocJoystickImpl extends ItsNatCompDOMListenersByDocImpl implements ItsNatCompDOMListenersJoystick
{
    protected Map loadScheduled;
    protected boolean joystickEnabled = false;

    public ItsNatCompDOMListenersByDocJoystickImpl(JoystickModeComponent comp)
    {
        super((ItsNatComponentImpl)comp);

        ItsNatElementComponentImpl elemComp = (ItsNatElementComponentImpl)comp;
        if (elemComp.getBooleanArtifactOrAttribute("joystickMode", elemComp.getItsNatDocumentImpl().isJoystickMode()))
            this.joystickEnabled = true;
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
            comp.disableEventListenersByDoc();
            this.joystickEnabled = value;
            comp.enableEventListenersByDoc(); // De acuerdo al nuevo modo
        }
    }

    public Map getLoadScheduledMap()
    {
        if (loadScheduled == null)
            this.loadScheduled = new HashMap();
        return loadScheduled;
    }

    public boolean mustAddRemove()
    {
        return ItsNatCompDOMListenersJoystickSharedImpl.mustAddRemove(this);
    }

    protected void addInternalEventListener(ClientDocumentImpl clientDoc,String type,boolean useCapture, int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener)
    {
        if (isJoystickEnabled())
            addInternalEventListenerJoystick(clientDoc,type, useCapture, commMode, extraParams, preSendCode, eventTimeout,bindToListener);
        else
            super.addInternalEventListener(clientDoc,type, useCapture, commMode, extraParams, preSendCode, eventTimeout,bindToListener);
    }

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
