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
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateHTMLLoadDocWebKitBoltImpl extends ResponseDelegateHTMLLoadDocWebKitImpl
{
    public ResponseDelegateHTMLLoadDocWebKitBoltImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public void dispatchRequestListeners()
    {
        fixDOMHTMLSelectWithSizeOrMultipleElements();

        super.dispatchRequestListeners();
    }

    public void fixDOMHTMLSelectWithSizeOrMultipleElements()
    {
        // El comportamiento de las cajas de texto <input> y los <textarea>
        // es complejo (y diferente a como fue originalmente en v1.0).
        // Bolt trata de evitar la comunicación con el servidor cada vez que el usuario
        // cambia un input text o un textarea, hasta el punto de que el evento change NO ES EMITIDO
        // y no es cosa de existir o no un handler inline.
        // En cierto modo sigue una idea similar a UCWEB, es decir cuando se cambia un control
        // de texto el nuevo valor es enviado al servidor EN LA SIGUIENTE comunicación
        // la propiedad "value" cambia pero sin provocar evento change.
        // El problema es que si se dispara un click (o mouseup/down) o un focus asociado al control (o a elementos padres)
        // dichos eventos se disparan después de editar el elemento sin embargo el valor
        // puesto no es enviado al servidor, es más dicho valor en el control visual se pierde si el evento produce un cambio
        // visual en el documento, es decir si por el proceso de edición se produce un evento, el valor del combo nuevo no se envía
        // en ese momento y puede llegar a perderse.
        // El evento blur es también complicado, el blur NO se envía al salir del editor del control,
        // sin embargo el Bolt cliente sabe que un control de texto ha sido tocado (aunque no haya sido cambiado)
        // tal que en la siguiente comunicación con el servidor por otra razón el servidor se entera de que
        // ha sido tocado y si hay un listener dispara ese blur. El proceso del blur puede ocurrir
        // cuando tocamos otro control de texto, en ese caso el cliente se comunica con el servidor para disparar el blur pendiente
        // al salir del editor del segundo control, aunque existe dicha comunicación el nuevo valor
        // del segundo control no es enviado en ese momento, es más, si el blur del primero
        // produce un cambio visual en el documento el valor en el editor se pierde
        // como dijimos antes.
        // Como consecuencia NO podemos usar el evento "focus" como hacemos en el caso de SkyFire que es similar a este
        // para memorizar el "value" antes de entrar en el editor y ver si cambia al salir, porque este evento focus se produce
        // tras la edición y el nuevo valor no es enviado al servidor y puede perderse. Idem click y similares.
        // Por tanto enviamos el evento change por sistema al procesar el blur.

        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        if (!clientDoc.isScriptingEnabled())
            return;

        StringBuffer code = new StringBuffer();

        code.append("var func = function (evt)");
        code.append("{");
        code.append("  var targetName = evt.target.localName;");
        code.append("  if ((\"input\" != targetName)&&(\"textarea\" != targetName)) return;");
        code.append("  if (\"input\" == targetName)");
        code.append("  {");
        code.append("    var type = evt.target.getAttribute(\"type\");");
        code.append("    if ((\"text\" != type)&&(\"password\" != type)&&(\"file\" != type)) return;");
        code.append("  }");
        code.append("  var evtTmp = document.createEvent(\"UIEvents\");");
        code.append("  evtTmp.initUIEvent(\"change\",true,true,window,0);");
        code.append("  evt.target.dispatchEvent(evtTmp);");
        code.append("};");
        code.append("document.addEventListener(\"blur\",func,true);"); // Notar que es capture para asegurar que se ejecuta el primero de todo

        addFixDOMCodeToSend(code.toString());
    }
}
