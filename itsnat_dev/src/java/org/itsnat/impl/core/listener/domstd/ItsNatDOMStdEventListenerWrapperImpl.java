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

package org.itsnat.impl.core.listener.domstd;

import org.itsnat.core.CommMode;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatServletResponseImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.event.DOMStdEventTypeInfo;
import org.itsnat.impl.core.event.client.ClientItsNatDOMEventImpl;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.event.client.domstd.ClientItsNatDOMStdEventFactory;
import org.itsnat.impl.core.event.client.domstd.ClientItsNatDOMStdEventImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.event.domstd.JSRenderItsNatDOMStdEventImpl;
import org.itsnat.impl.core.listener.ItsNatDOMEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ItsNatDOMStdEventListenerWrapperImpl extends ItsNatDOMEventListenerWrapperImpl
{
    protected int commMode;
    protected String type;
    protected boolean useCapture;
    protected int typeCode;

    /**
     * Creates a new instance of ItsNatDOMStdEventListenerWrapperImpl
     */
    public ItsNatDOMStdEventListenerWrapperImpl(ItsNatStfulDocumentImpl itsNatDoc,ClientDocumentStfulImpl clientDoc,EventTarget elem,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        super(itsNatDoc,clientDoc,elem,listener,extraParams,preSendCode,eventTimeout,bindToCustomFunc);

        this.commMode = commMode;
        this.type = type;
        this.useCapture = useCapture;

        this.typeCode = DOMStdEventTypeInfo.getEventTypeCode(type);  // type no cambia por lo que no hay problema de "sincronización". Obtenerlo de una vez es mejor pues hay que buscar en una colección
    }

    public int getCommModeDeclared()
    {
        return commMode;
    }

    public String getType()
    {
        return type;
    }

    public int getTypeCode()
    {
        return typeCode;
    }

    public boolean getUseCapture()
    {
        return useCapture;
    }

    @Override
    public void handleEvent(ClientItsNatDOMEventImpl event)
    {
        super.handleEvent(event);

        boolean sync = (event.getCommMode() == CommMode.XHR_SYNC); // Sólo XHR síncrono es completamente síncrono

        StringBuilder retEvent = new StringBuilder();
        if (sync)
        {
            if (event.getPreventDefault())
            {
                ClientDocumentStfulDelegateWebImpl clientDoc = (ClientDocumentStfulDelegateWebImpl)event.getClientDocumentStful().getClientDocumentStfulDelegate();
                BrowserWeb browser = clientDoc.getBrowserWeb();
                JSRenderItsNatDOMStdEventImpl render = JSRenderItsNatDOMStdEventImpl.getJSItsNatDOMStdEventRender((ClientItsNatDOMStdEventImpl)event,browser);
                retEvent.append( render.getPreventDefault("event.getNativeEvent()",clientDoc) );
            }

            if (event.getStopPropagation())
            {
                ClientDocumentStfulDelegateWebImpl clientDoc = (ClientDocumentStfulDelegateWebImpl)event.getClientDocumentStful().getClientDocumentStfulDelegate();
                BrowserWeb browser = clientDoc.getBrowserWeb();
                JSRenderItsNatDOMStdEventImpl render = JSRenderItsNatDOMStdEventImpl.getJSItsNatDOMStdEventRender((ClientItsNatDOMStdEventImpl)event,browser);
                retEvent.append( render.getStopPropagation("event.getNativeEvent()",clientDoc) );
            }
        }

        if (retEvent.length() > 0)
        {
            ItsNatServletResponseImpl itsNatResponse = event.getItsNatServletResponseImpl();
            itsNatResponse.addCodeToSend(retEvent.toString()); // Este código no es notificado a los observers
        }
    }

    public ClientItsNatNormalEventImpl createClientItsNatNormalEvent(RequestNormalEventImpl request)
    {
        return ClientItsNatDOMStdEventFactory.createClientItsNatDOMStdEvent(this,request);
    }
}
