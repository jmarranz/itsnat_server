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
package org.itsnat.impl.core.resp.shared.bybrow;

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.itsnat.impl.core.resp.shared.ResponseDelegateStfulLoadDocImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class RewriteClientUIControlPropsOperaLoadListenerImpl implements EventListener,Serializable
{
    protected ClientDocumentStfulImpl clientDoc;

    public RewriteClientUIControlPropsOperaLoadListenerImpl(ClientDocumentStfulImpl clientDoc)
    {
        this.clientDoc = clientDoc;
    }

    public void handleEvent(Event evt)
    {
        StringBuilder code = new StringBuilder();

        // Registramos antes de que se ejecute el auto-complete
        // Filtramos los eventos change generados por el navegador por el autocomplete
        code.append("var filter = function (evt)\n");
        code.append("{\n");
        code.append("  var evtList = evt.getListenerWrapper(); \n");
        code.append("  if (!evtList.getType) return true;\n");
        code.append("  return (evtList.getType() != \"change\");\n"); // Si devuelve false entonces no enviar al servidor este evento
        code.append("};\n");
        code.append("itsNatDoc.addGlobalEventListener(filter);\n");

        code.append("var listener = function ()\n");
        code.append("{\n");
        code.append("  var itsNatDoc = arguments.callee.itsNatDoc;\n");
        code.append("  itsNatDoc.removeGlobalEventListener(arguments.callee.filter);\n"); // Restauramos tras el auto complete

        // Reconstuirmos así el objeto de utilidad response en fase load aunque ya no estamos en esa fase
        // y evitamos así problemas de serialización si memorizáramos el objeto original como un atributo (o por captura de contexto ocurre serialización indirecta)
        ResponseLoadStfulDocumentValid response = new ResponseLoadStfulDocumentValidFakeForOpera(clientDoc);
        ResponseDelegateStfulLoadDocImpl responseDeleg = ResponseDelegateStfulLoadDocImpl.createResponseDelegateStfulLoadDoc(response);
        code.append( responseDeleg.rewriteClientUIControlProperties(true) );
 
        code.append("};\n");
        code.append("listener.filter = filter;\n");
        code.append("listener.itsNatDoc = itsNatDoc;\n");

        code.append("itsNatDoc.setTimeout(listener,0);"); // El listener se ejecutará *después* del auto-complete

        ClientDocument clientDoc = ((ItsNatEvent)evt).getClientDocument();
        clientDoc.addCodeToSend(code.toString());

        // Antes de ejecutarse el rewriteClientUIControlProperties(), Opera hace el autofill
    }
}
