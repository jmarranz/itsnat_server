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

import org.itsnat.core.CommMode;
import org.itsnat.impl.core.CommModeImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.resp.shared.ResponseDelegateStfulLoadDocImpl;
import org.w3c.dom.Document;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegStfulLoadDocByBOperaImpl extends ResponseDelegStfulLoadDocByBW3CImpl
{
    public ResponseDelegStfulLoadDocByBOperaImpl(ResponseDelegateStfulLoadDocImpl parent)
    {
        super(parent);
    }

    public static ResponseDelegStfulLoadDocByBOperaImpl createResponseDelegStfulLoadDocByBOpera(ResponseDelegateStfulLoadDocImpl parent)
    {
        return new ResponseDelegStfulLoadDocByBOperaImpl(parent);
    }

    public String getOnInitScriptContentCodeFixDOMCode()
    {
        // Solucionamos el problema del Opera Desktop de que el evento load no se ejecuta cuando se usan
        // los botones back/forward, pero el evento unload sigue sin ejecutarse (bug mio:  bug-311047@bugs.opera.com)
        // Tampoco soluciona que la página destino del back/forward no sea recargada (aunque el load se ejecute),
        // el evento load lo usaremos precisamente para solucionar este problema
        // http://www.opera.com/support/search/view/827/
        // La única "solución" razonable encontrada:
        // http://www.experts-exchange.com/Programming/Languages/Scripting/JavaScript/Q_21907326.html
        // En Opera Mini a día de hoy no sirve para nada (se acepta) pero por si acaso sirve en el futuro para eliminar el cacheado.

        StringBuffer code = new StringBuffer();

        code.append("window.opera.setOverrideHistoryNavigationMode(\"compatible\");\n");
        code.append("window.history.navigationMode = \"compatible\";\n");

        return code.toString();
    }

    public void afterLoadRewriteClientUIControlProperties()
    {
        final ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        if (!clientDoc.isScriptingEnabled())
            return;

        // En Opera Desktop 9 el autofill no es el habitual,
        // cuando termina de ejecutarse el evento load inmediatamente después
        // autorellena los forms con valores cacheados,
        // por lo que el "forzado" ha de realizarse DESPUES del autofill de Opera
        // Otro problema es que este llenado genera eventos "change" simulando que lo hubiera hecho el usuario,
        // aparte de que en componentes ItsNat checkbox y radio no se procesa el change,
        // nos interesa que el cliente muestre el estado inicial de la página no un estado
        // anterior (aunque la máxima prioridad es que cliente y servidor estén sincronizados)
        // Por ello antes del autofill de Opera evitamos los eventos "change" para que
        // los cambios no lleguen al servidor (total, vamos a imponer los valores del servidor de nuevo)
        // y los volvemos a restaurar el modo normal tras el autofill.

        // Haremos por tanto dos "revertimientos" (en cierto modo es ejecutarlo dos veces):
        // 1) Los cambios hechos por el usuario al cargar, como en los demás navegadores (con el fin de que el comportamiento sea el mismo que en los demás)
        // 2) Los cambios hechos por el autofill de Opera tras el load.

        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();

        EventListener listener = new RewriteClientUIControlPropsOperaLoadListenerImpl(clientDoc);

        int commMode;
        int syncDefaultMode = clientDoc.getCommMode();
        if (CommModeImpl.isXHRMode(syncDefaultMode))
            commMode = CommMode.XHR_SYNC;
        else
            commMode = syncDefaultMode; // Caso SCRIPT o SCRIPT_HOLD, no se si funcionará correctamente (asíncronos), hay que probar
        clientDoc.addEventListener((EventTarget)view,"load",listener,false,commMode); //
        // Nota: este evento load es registrado/ejecutado el ÚLTIMO y DEBE ser así pues se desactivan los eventos temporalmente
    }

}
