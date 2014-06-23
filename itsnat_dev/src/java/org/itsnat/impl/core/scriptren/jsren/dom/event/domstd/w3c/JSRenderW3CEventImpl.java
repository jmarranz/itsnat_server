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

package org.itsnat.impl.core.scriptren.jsren.dom.event.domstd.w3c;

import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.impl.core.browser.web.BrowserAdobeSVG;
import org.itsnat.impl.core.browser.web.BrowserBatik;
import org.itsnat.impl.core.browser.web.BrowserBlackBerryOld;
import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.event.HTMLEventInternal;
import org.itsnat.impl.core.scriptren.jsren.dom.event.domstd.JSRenderItsNatDOMStdEventImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderW3CEventImpl extends JSRenderItsNatDOMStdEventImpl
{

    /**
     * Creates a new instance of JSRenderW3CEventImpl
     */
    public JSRenderW3CEventImpl()
    {
    }

    protected abstract String getEventTypeGroup(Event evt);

    public static JSRenderW3CEventImpl getJSW3CEventRender(ItsNatDOMStdEvent event,BrowserW3C browser)
    {
        // Primero procesamos los navegadores cuyo soporte de W3C DOM Events es muy pobre
        if (browser instanceof BrowserAdobeSVG)
            return JSRenderW3CAdobeSVGEventImpl.getJSRenderW3CAdobeSVGEvent(event);

        // Ahora los navegadores con mayor soporte de W3C DOM Events clasificados por tipo de evento
        if (event instanceof MouseEvent)
            return JSRenderW3CMouseEventImpl.getJSW3CMouseEventRender(browser);
        else if (event instanceof ItsNatKeyEvent)
            return JSRenderW3CKeyEventImpl.getJSW3CKeyEventRender(browser);
        else if (event instanceof UIEvent)
            return JSRenderW3CUIEventGenericDefaultImpl.SINGLETON;
        else if (event instanceof HTMLEventInternal)
            return JSRenderW3CHTMLEventImpl.getJSRenderW3CHTMLEvent(browser);
        else if (event instanceof MutationEvent)
            return JSRenderW3CMutationEventImpl.SINGLETON;
        else
            return JSRenderW3CEventDefaultImpl.SINGLETON; // Desconocidos
    }

    public String getCreateEventInstance(Event evt,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return "itsNatDoc.doc.createEvent(\"" + getEventTypeGroup(evt) + "\")";
    }

    public String getInitEventDefault(Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return evtVarName + ".initEvent(\"" + evt.getType() + "\"," + evt.getBubbles() + "," + evt.getCancelable() + ");\n";
    }

    public String getStopPropagation(String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();
        code.append( evtVarName + ".stopPropagation();\n" );
        return code.toString();
    }

    public String getPreventDefault(String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return evtVarName + ".preventDefault();\n";
    }

    public static String getViewPath(AbstractView view,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        BrowserWeb browser = clientDoc.getBrowserWeb();
        if (browser instanceof BrowserBlackBerryOld)
            return "null";  // Error tonto de BlackBerry 4.6, no admite el "window" de verdad
        else if (browser instanceof BrowserBatik)
            return "itsNatDoc.doc.defaultView"; // Esto es porque "window" es un objeto especial (WindowWrapper) definido por Batik que NO es un AbstractView pues hay que tener en cuenta que "document" es el objeto nativo DOM y por tanto createEvent crea un objeto nativo DOM que necesita AbstractView como parámetro

        return clientDoc.getNodeReference((Node)view,true,true);
    }
}
