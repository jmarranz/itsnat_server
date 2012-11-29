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
public class ResponseDelegateHTMLLoadDocNetFrontImpl extends ResponseDelegateHTMLLoadDocW3CImpl
{
    public ResponseDelegateHTMLLoadDocNetFrontImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public void dispatchRequestListeners()
    {
        super.dispatchRequestListeners();

        fixLoadScript();
    }

    public void fixLoadScript()
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        if (!clientDoc.isScriptingEnabled())
            return;

        // NetFront tiene un error raro que yo creo que está relacionado
        // con la "autoeliminación" del árbol del <script> de inicialización
        // El caso es que se elimina pero en su lugar deja un nodo Comment vacío
        // Dicho nuevo nodo se añade (afortunadamente) tras la ejecución del script de iniciación
        // por lo que el código de iniciación del script no detecta tal nodo superfluo.

        StringBuffer code = new StringBuffer();
        code.append("var listener = function ()\n");
        code.append("{\n");
        code.append("  var childNodes = document.body.childNodes;\n");
        code.append("  var len = childNodes.length; if (len == 0) return;\n");
        code.append("  var last = childNodes[len - 1]; \n");
        code.append("  if ((last.nodeType == 8)&&(last.data == \"\")) last.parentNode.removeChild(last);\n");
        code.append("};\n");
        code.append("itsNatDoc.setTimeout(listener,0);\n");
        clientDoc.addCodeToSend(code.toString());
    }
}

