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

import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.impl.core.browser.web.webkit.BrowserWebKit;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.event.client.dom.domstd.w3c.WebKitKeyEventImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class JSRenderW3CKeyEventWebKitImpl extends JSRenderW3CKeyboardEventImpl
{
    public static final JSRenderW3CKeyEventWebKitImpl SINGLETON = new JSRenderW3CKeyEventWebKitImpl();
    
    /**
     * Creates a new instance of JSRenderW3CKeyEventWebKitDefaultImpl
     */
    public JSRenderW3CKeyEventWebKitImpl()
    {
    }

    public static JSRenderW3CKeyEventWebKitImpl getJSRenderW3CKeyEventWebKit(BrowserWebKit browser)
    {
        return JSRenderW3CKeyEventWebKitImpl.SINGLETON;
    }

    public String toKeyIdentifierByBrowser(int keyCode)
    {
        return WebKitKeyEventImpl.toKeyIdentifier(keyCode);
    }
    public boolean needsAuxiliarCharCode()
    {
        return true;
    }

    protected String getEventTypeGroup(Event evt)
    {
        return "KeyboardEvent"; // Yo creo que vale también "KeyboardEvents" (en plural)
    }

    @Override    
    public String getInitKeyboardEvent(StringBuilder code,ItsNatKeyEvent keyEvt,String evtVarName,String keyIdentifier,int keyLocation,int keyCode,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // http://webkit.org/docs/a00095.html
        // http://www.w3.org/TR/2003/WD-DOM-Level-3-Events-20030331/events.html#Events-KeyboardEvents-Interfaces
        // http://lists.webkit.org/pipermail/webkit-unassigned/2007-April/035697.html
        // http://trac.webkit.org/projects/webkit/browser/trunk/WebCore/dom/KeyboardEvent.cpp
        // http://trac.webkit.org/projects/webkit/browser/trunk/WebCore/dom/KeyboardEvent.h
        // http://www.koders.com/cpp/fid07000D08025239E54B79ABC802B84ED8C27FA066.aspx (PlatformKeyboardEvent.h)
        // http://www.koders.com/cpp/fidCA6C5BC570CBFE6A97F78D2A65217CBECDB4ED88.aspx?s=PlatformKeyboardEvent#L27 ( KeyEventWin.cpp )
        // http://lists.macosforge.org/pipermail/webkit-changes/2005-September/000914.html
        // Las técnicas que usan UIEvent no funcionan (en Safari 3) porque los atributos ctrlKey etc
        // son "readonly" de acuerdo con DOM 3 pero no hay problema pues
        // se pasan por initKeyboardEvent
        // Ejemplo: http://developer.yahoo.com/yui/docs/UserAction.js.html

        // El problema keyCode y charCode es que son read only porque se supone que lo genera Safari,
        // keyCode no hay problema pues se obtiene de keyIdentifier pero charCode no.
        // Sin embargo Safari tiene la propiedad
        // de que el objeto evento creado por createEvent es el mismo que reciben
        // los listeners por lo que podemos asociar una propiedad itsnat_charCode
        // al evento. De otra manera tendríamos que utilizar el shiftKey como única
        // manera de poder calcular el charCode al recibir el evento.

        code.append( evtVarName + ".initKeyboardEvent("
                + "\"" + keyEvt.getType() + "\","
                + keyEvt.getBubbles() + ","
                + keyEvt.getCancelable() + ","
                + getViewPath(keyEvt.getView(),clientDoc) + ","
                + "\"" + keyIdentifier + "\"," // keyIdentifier
                + keyLocation + "," // keyLocation
                + keyEvt.getCtrlKey() + ","
                + keyEvt.getAltKey() + ","
                + keyEvt.getShiftKey() + ","
                + keyEvt.getMetaKey() + ","
                + false  // altGraph
                + ");\n" );

        return code.toString();
    }
}
