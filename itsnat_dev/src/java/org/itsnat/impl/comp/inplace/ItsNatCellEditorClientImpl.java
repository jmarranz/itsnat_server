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

package org.itsnat.impl.comp.inplace;

import org.itsnat.comp.ItsNatComponent;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserBlackBerryOld;
import org.itsnat.impl.core.browser.BrowserGecko;
import org.itsnat.impl.core.browser.opera.BrowserOperaMini;
import org.itsnat.impl.core.browser.webkit.BrowserWebKitIOS;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.jsren.JSRenderMethodCallImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLOptionElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatCellEditorClientImpl
{
    public ItsNatCellEditorClientImpl()
    {
    }

    public static ItsNatCellEditorClientImpl getItsNatHTMLCellEditorClient(Browser browser,ItsNatComponent compEditor)
    {
        if (compEditor.getNode() instanceof HTMLElement)
        {
            if (browser instanceof BrowserOperaMini)
                return ItsNatHTMLCellEditorClientOperaMiniImpl.SINGLETON;
            else
                return ItsNatCellEditorClientDefaultImpl.SINGLETON;
        }
        else
            return ItsNatCellEditorClientDefaultImpl.SINGLETON;
    }

    public void handleGlobalEvent(Event evt,ItsNatCellEditorImpl parent)
    {
        // No consideramos eventos no estándar DOM pues en teoría no tendrían porqué
        // provocar un blur. De hecho el ejemplo del test funcional del Feat. Show lanza un continue
        // event para recoger el valor de retorno del dispatch, si lo consideráramos para el blur
        // cerraríamos la edición inplace erróneamente antes de que se envíe el blur.
        // Es el caso también de un timer, no debería cerrar el editor.
        if (evt instanceof ItsNatDOMStdEvent)
        {
            ItsNatComponent compEditor = parent.getCellEditorComponent();
            Node nodeEditor = compEditor.getNode();
            EventTarget target = evt.getTarget();
            if (target == nodeEditor) return;

            if ((target instanceof HTMLOptionElement) &&
                (((Node)target).getParentNode() == nodeEditor))
            {
                // En BlackBerry un <option> es el target cuando una opción de un select es pulsada (curioso)
                ClientDocumentStfulImpl clientDoc = (ClientDocumentStfulImpl)((ItsNatEvent)evt).getClientDocument();
                Browser browser = clientDoc.getBrowser();
                if (browser instanceof BrowserBlackBerryOld) return;
            }

            parent.stopCellEditing();
        }
    }

    public void handleEvent(Event evt,ItsNatCellEditorImpl parent,ClientDocumentStfulImpl clientDoc)
    {
    }

    public void registerEventListeners(ItsNatCellEditorImpl compParent,ClientDocumentStfulImpl clientDoc)
    {
        ItsNatComponent compEditor = compParent.getCellEditorComponent();

        Browser browser = clientDoc.getBrowser();
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)clientDoc.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element nodeEditor = (Element)compEditor.getNode(); // Sólo admitimos elementos por ahora

        clientDoc.addCodeToSend("var nodeEditor = " + clientDoc.getNodeReference(nodeEditor,true,true) + ";\n");

        StringBuilder codeListener = new StringBuilder();
        codeListener.append( "event.setMustBeSent(false);\n" ); // Sirve para evitar que se envíe el evento click, ya se envía un evento blur
        codeListener.append( "try{" );
        codeListener.append( "var node = arguments.callee.nodeEditor;\n" );
        codeListener.append( "var target = event.getTarget();\n" );
        codeListener.append( "if (node == target) return;\n" ); // Es un click dirigido al propio elemento editándose.

        // El método getCallBlurFocusFormControlCode llamará a blur() o enviará un evento "blur" manualmente según si
        // blur() es procesado correctamente o ignorado/erróneo, si focus() no lo es lo normal es que el blur() es ignorado salvo que el usuario
        // haya fijado el foco manualmente. En dichos casos (focus() no ejecutado) se envía un evento "blur" que asegura que el editor se quita
        // aunque el control no haya tenido nunca el foco (ni por focus() ni pulsando el usuario).
        JSRenderMethodCallImpl render = JSRenderMethodCallImpl.getJSRenderMethodCall(nodeEditor);
        codeListener.append(render.getCallBlurFocusFormControlCode(nodeEditor,"node","blur",clientDoc));
        codeListener.append( "}catch(e){}\n" ); // el try/catch es por si el nodo se hubiera eliminado antes y el evento está pendiente todavía

        String bindToListener = "nodeEditor = nodeEditor";

        clientDoc.addEventListener((EventTarget)doc,"click", compParent, true,clientDoc.getCommMode(),null, codeListener.toString(),clientDoc.getEventTimeout(),bindToListener);

        if (browser instanceof BrowserWebKitIOS)
        {
            // En iPhone los eventos de ratón no llegan al document, <body> o <html>
            // si el elemento pulsado "no es clickable" es decir si no tiene un listener
            // asociado (el propio <body> aunque tenga listener es ignorado si se pulsa en su área).
            // Una alternativa es usar los eventos "touch" introducidos en iPhone 2.0 que funcionan
            // casi igual que los eventos de ratón en un browser de desktop
            // Sin embargo no pueden substituir a los eventos de ratón porque hay diferencias por ejemplo al pulsar
            // un link no se emiten (quizás porque el comportamiento normal de un link es abandonar la página)
            // de otra manera podríamos usar estos eventos en el cliente y engañar al servidor haciendo creer
            // que son mouse events.
            // Por ahora el "touchend" será tratado como un evento unknown aunque
            // en futuro podríamos hacerlo público con interfases propias, envío desde el servidor etc
            // pues por ejemplo tiene información del punto pulsado etc
            // (al igual que los demás "touchstart", "touchmove" "touchcancel" etc).

            // http://www.sitepen.com/blog/2008/07/10/touching-and-gesturing-on-the-iphone/
            // http://rossboucher.com/2008/08/19/iphone-touch-events-in-javascript/
            // http://developer.yahoo.com/yui/3/event/
            clientDoc.addEventListener((EventTarget)doc,"touchend", compParent, true,clientDoc.getCommMode(),null, codeListener.toString(),clientDoc.getEventTimeout(),bindToListener);
        }
    }

    public void unregisterEventListeners(ItsNatCellEditorImpl parent,ClientDocumentStfulImpl clientDoc)
    {
        Browser browser = clientDoc.getBrowser();
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)clientDoc.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        clientDoc.removeEventListener((EventTarget)doc,"click", parent, true);

        if (browser instanceof BrowserWebKitIOS)
            clientDoc.removeEventListener((EventTarget)doc,"touchend", parent, true);
    }
}
