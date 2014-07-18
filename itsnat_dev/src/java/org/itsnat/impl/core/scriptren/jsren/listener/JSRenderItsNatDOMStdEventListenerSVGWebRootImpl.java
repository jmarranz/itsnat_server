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

package org.itsnat.impl.core.scriptren.jsren.listener;

import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.listener.WaitForEventListenerImpl;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatDOMStdEventListenerSVGWebRootImpl extends JSRenderItsNatDOMStdEventListenerImpl
{
    public static final JSRenderItsNatDOMStdEventListenerSVGWebRootImpl SINGLETON = new JSRenderItsNatDOMStdEventListenerSVGWebRootImpl();

    /** Creates a new instance of JSRenderItsNatDOMStdEventListenerImpl */
    public JSRenderItsNatDOMStdEventListenerSVGWebRootImpl()
    {
    }

    public boolean needsAddListenerReturnElement()
    {
        return false;
    }

    public boolean needsRemoveListenerReturnElement()
    {
        return false;
    }

    @Override    
    protected String addItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        if (itsNatListener.getType().equals("SVGLoad") && 
            !(itsNatListener.getEventListener() instanceof WaitForEventListenerImpl))
        {
            // Es un SVGLoad del usuario vinculado
            EventTarget currTarget = itsNatListener.getCurrentTarget();
            // Eliminamos la posible marca que existiera, recuerda que el equals
            // de WaitForEventListenerImpl es por igualdad de valores.

            WaitForEventListenerImpl mark = new WaitForEventListenerImpl((Element)currTarget,"SVGLoad");
            clientDoc.getClientDocumentStful().getCodeToSendRegistry().removeWaitForEventListener(mark);

            // Si luego el programador desregistra inmediatamente después este listener
            // allá él pero se ha cargado la marca, es un comportamiento demasiado
            // estúpido como para contemplarlo (restaurar la marca eliminada de alguna forma
            // lo cual es complicado).
        }

        return super.addItsNatDOMStdEventListenerCode(itsNatListener, clientDoc);
    }

}
