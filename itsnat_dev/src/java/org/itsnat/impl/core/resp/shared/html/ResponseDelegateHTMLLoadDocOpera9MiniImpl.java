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

package org.itsnat.impl.core.resp.shared.html;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.event.EventListenerInternal;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateHTMLLoadDocOpera9MiniImpl extends ResponseDelegateHTMLLoadDocOpera9Impl
{
    public ResponseDelegateHTMLLoadDocOpera9MiniImpl(ResponseLoadStfulDocumentValid responseParent)
    {
        super(responseParent);
    }

    public void dispatchRequestListeners()
    {
        super.dispatchRequestListeners();

        fixBackButton(); // Debe añadirse lo más último posible pues registra un listener "unload" que debe ejecutarse el último
    }

    public void fixBackButton()
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        if (!clientDoc.isScriptingEnabled())
            return;

        // Opera Mini no recibe clicks en BODY, ni en window ni en document etc
        // Si se vuelve via back o similares es posible que la página a la que se vuelve
        // haya ya ejecutado el evento unload, en ese caso lo que hay que hacer es recargar la página
        // en cuanto el usuario toque algo.

        Document doc = clientDoc.getItsNatStfulDocument().getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();
        EventListener listener = new EventListenerInternal()
        {
            public void handleEvent(Event evt) {}
        };
        StringBuffer preSendCode = new StringBuffer();
        preSendCode.append("event.setMustBeSent(false);"); // Evita enviar el evento unload
        preSendCode.append("var func = function()");
        preSendCode.append("  { itsNatDoc.disabledEvents = true; window.location.reload(true); return false; };"); // El return false evita que se envíe el evento y el itsNatDoc.disabledEvents = true evita otros eventos
        preSendCode.append("itsNatDoc.addGlobalEventListener(func);");
        clientDoc.addEventListener((EventTarget)view, "unload", listener, false,preSendCode.toString());
    }
}

