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

package org.itsnat.impl.core.scriptren.jsren.event.domstd;

import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.event.EventInternal;
import org.itsnat.impl.core.scriptren.jsren.event.domstd.msie.JSRenderMSIEOldEventImpl;
import org.itsnat.impl.core.scriptren.jsren.event.domstd.w3c.JSRenderW3CEventImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.scriptren.jsren.event.JSRenderNormalEventImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderItsNatDOMStdEventImpl extends JSRenderNormalEventImpl
{

    /**
     * Creates a new instance of JSRenderItsNatDOMStdEventImpl
     */
    public JSRenderItsNatDOMStdEventImpl()
    {
    }

    public static JSRenderItsNatDOMStdEventImpl getJSItsNatDOMStdEventRender(ItsNatDOMStdEvent event,Browser browser)
    {
        if (browser instanceof BrowserMSIEOld)
            return JSRenderMSIEOldEventImpl.getJSRenderMSIEOldEvent(event,(BrowserMSIEOld)browser);
        else
            return JSRenderW3CEventImpl.getJSW3CEventRender(event,(BrowserW3C)browser);
    }

    public abstract String getCreateEventInstance(Event evt,ClientDocumentStfulDelegateWebImpl clientDoc);

    // http://developer.mozilla.org/en/docs/DOM:document.createEvent
    public abstract String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc);

    public abstract String getStopPropagation(String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc);

    public abstract String getPreventDefault(String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc);
   
    public String getInitEventSystem(ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return "";
    }
    
    public String getCreateEventCode(Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // http://msdn2.microsoft.com/en-us/library/ms536423.aspx
        // Sobre las limitaciones de fireEvent en MSIE:
        // http://groups.google.com.au/group/comp.lang.javascript/browse_frm/thread/27e7c70e51ff8a99/98cea9cdf065a524?lnk=gst&q=dispatchEvent+winter&rnum=1&hl=en#98cea9cdf065a524
        // http://lists.evolt.org/archive/Week-of-Mon-20040301/156228.html
        // Por ejemplo un evento "click" en un checkbox no lo seleccionará o en un link no navegará
        // aunque se ejecutarán los handlers, es mejor usar el método
        // click() que funciona exactamente igual que un click de ratón.

        StringBuilder code = new StringBuilder();

        code.append( getInitEventSystem(clientDoc) );

        code.append( "var " + evtVarName + " = " + getCreateEventInstance(evt,clientDoc) + ";\n" );

        code.append( getInitEvent(evt,evtVarName,clientDoc) );

        return code.toString();
    }

    @Override    
    public String getDispatchEvent(String varResName,NodeLocationImpl nodeLoc,Event evt,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        code.append( getCreateEventCode(evt,"evt",clientDoc) );

        code.append( getCallDispatchEvent(varResName,nodeLoc,evt,"evt",clientDoc) );

        return code.toString();
    }

    @Override    
    public String getDispatchEvent(String targetRef,Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        code.append( getCreateEventCode(evt,evtVarName,clientDoc) );

        code.append( getCallDispatchEvent(targetRef,evt,evtVarName,clientDoc) );

        return code.toString();
    }

    public String getCallDispatchEvent(String varResName,NodeLocationImpl nodeLoc,Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return "var " + varResName + " = itsNatDoc.dispatchEvent2(" + nodeLoc.toScriptNodeLocation(true) + ",\"" + evt.getType() + "\"," + evtVarName + ");\n";
    }

    public String getCallDispatchEvent(String targetRef,Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return "itsNatDoc.dispatchEvent(" + targetRef + ",\"" + evt.getType() + "\"," + evtVarName + ");\n";
    }
}
