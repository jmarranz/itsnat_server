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

package org.itsnat.impl.core.jsren.listener;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatDOMStdEventListenerBatikImpl extends JSRenderItsNatDOMStdEventListenerImpl
{
    public static final JSRenderItsNatDOMStdEventListenerBatikImpl SINGLETON = new JSRenderItsNatDOMStdEventListenerBatikImpl();

    /** Creates a new instance of JSRenderItsNatDOMStdEventListenerImpl */
    public JSRenderItsNatDOMStdEventListenerBatikImpl()
    {
    }

    protected boolean needsAddListenerReturnElement()
    {
        return false;
    }

    protected boolean needsRemoveListenerReturnElement()
    {
        return false;
    }

    protected String addItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        EventTarget nodeTarget = itsNatListener.getCurrentTarget();
        if (!isEventTargetValid(nodeTarget))
            return "";
        else
            return super.addItsNatDOMStdEventListenerCode(itsNatListener, clientDoc);
    }

    protected String removeItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        EventTarget nodeTarget = itsNatListener.getCurrentTarget();
        if (!isEventTargetValid(nodeTarget))
            return "";
        else
            return super.removeItsNatDOMStdEventListenerCode(itsNatListener, clientDoc);
    }

    protected boolean isEventTargetValid(EventTarget nodeTarget)
    {
        // Estudiamos si el target tiene métodos add/removeEventListener
        // pues de otra manera da error
        if (nodeTarget instanceof AbstractView)
        {
            // Batik no soporta add/removeEventListener en window que es un objeto WindowWrapper en el applet
            // Tampoco en el objeto AbstractView del applet
            return false;
        }
        return true;
    }
}
