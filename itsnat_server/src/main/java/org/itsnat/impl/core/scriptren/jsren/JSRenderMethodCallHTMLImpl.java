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

package org.itsnat.impl.core.scriptren.jsren;

import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.event.server.ServerItsNatNormalEventImpl;
import org.itsnat.impl.core.scriptren.jsren.event.JSRenderEventImpl;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderMethodCallHTMLImpl extends JSRenderMethodCallImpl
{
    public static final JSRenderMethodCallHTMLImpl SINGLETON = new JSRenderMethodCallHTMLImpl();

    @Override
    public String getCallBlurFocusFormControlCode(Element elem,String elemRef,String methodName,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // En estos navegadores las llamadas a focus() y blur() suelen ser ignoradas,
        // el problema es que si focus() es ignorada blur() lo será también, pues el
        // navegador no tiene constancia de que el elemento tenga el foco y por tanto
        // no emite un evento blur.
        // La razón seguramente de que focus() (y blur()) sea ignorado es que
        // solemos llamarlos en la respuesta a una petición AJAX asíncrona, seguramente
        // el hilo que lo procesa no es el del GUI y hay que tener en cuenta que focus() supone
        // un cambio visual. La solución podría ser hacer que la llamada fuera síncrona (comprobado en el iPhone 2.0),
        // pero aparte de forzar algo que no queremos, el focus y el blur interesan en el ámbito
        // de la edición inplace y en dicho ámbito lo que nos interesa realmente es que sen envíen
        // los eventos sobre todo el blur aunque sea al detectar un click fuera del control,
        // Obligamos a que el usuario pulse el control
        // si quiere verdadero foco (para que salga el teclado en móviles etc) pero esto
        // no es ninguna tragedia.
        ClientDocumentStfulDelegateWebImpl clientDocWeb = (ClientDocumentStfulDelegateWebImpl)clientDoc; 
        BrowserWeb browser = clientDocWeb.getBrowserWeb();
        if (browser.isFocusOrBlurMethodWrong(methodName,(HTMLElement)elem))
            return getCallFormControlFocusBlurWithW3CEventCode((HTMLElement)elem,elemRef,methodName,clientDocWeb);
        else
            return super.getCallBlurFocusFormControlCode(elem,elemRef,methodName,clientDoc);
    }

    private static String getCallFormControlFocusBlurWithW3CEventCode(HTMLElement elem,String elemRef,String methodName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // methodName es "blur" o "focus"

        ServerItsNatNormalEventImpl evt = ServerItsNatNormalEventImpl.createServerNormalEvent("HTMLEvents",clientDoc.getItsNatStfulDocument());
                clientDoc.getItsNatStfulDocument().createEvent("HTMLEvents");
        evt.initEvent(methodName, true, true);
        evt.setTarget((EventTarget)elem);

        JSRenderEventImpl render = JSRenderEventImpl.getJSEventRender(evt, clientDoc.getBrowserWeb());
        StringBuilder code = new StringBuilder();
        code.append( render.getDispatchEvent(elemRef, evt, "evtTmp", clientDoc) );
        code.append("evtTmp = null;"); // El null es para que quede claro que ya no se usa

        return code.toString();
    }

    @Override
    public boolean isFocusOrBlurMethodWrong(String methodName,Element elem,BrowserWeb browser)
    {
        return browser.isFocusOrBlurMethodWrong(methodName,(HTMLElement)elem);
    }

}
