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

package org.itsnat.impl.core.jsren.dom.node.html.w3c;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementGeckoSkyFireImpl extends JSRenderHTMLElementGeckoImpl
{
    public static final JSRenderHTMLElementGeckoSkyFireImpl SINGLETON = new JSRenderHTMLElementGeckoSkyFireImpl();

    /** Creates a new instance of JSMSIEHTMLElementRenderImpl */
    public JSRenderHTMLElementGeckoSkyFireImpl()
    {
    }

    public static void fixTreeHTMLTextControlElements(Node node,ClientDocumentStfulImpl clientDoc)
    {
        // Solucionamos el error del SkyFire 1.0 de que en los <input type="text|password|file>
        // y <textarea> el evento change no se lanza, pero el blur sí.
        // Detectamos blur para lanzar el change antes de ser procesado.
        if (!clientDoc.canReceiveALLNormalEvents())
            return; // No merece la pena

        if (!clientDoc.isSendCodeEnabled())
            return;

        StringBuffer code = fixTreeHTMLTextControlElements(node,null,clientDoc);

        if ((code != null) && (code.length() > 0))
            clientDoc.addCodeToSend(code.toString());
    }

    private static StringBuffer fixTreeHTMLTextControlElements(Node node,StringBuffer code,ClientDocumentStfulImpl clientDoc)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE) return code;

        Element elem = (Element)node;

        if (DOMUtilHTML.isHTMLTextAreaOrInputTextBox(elem))
        {
            if (code == null) code = new StringBuffer();

            String elemRef = clientDoc.getNodeReference(elem,true,true);

            String methodName = "skyFireFixHTMLTextControl";
            if (!clientDoc.isClientMethodBounded(methodName))
                code.append(bindFixHTMLTextControlMethod(methodName,clientDoc));

            code.append("itsNatDoc." + methodName + "(" + elemRef + ");\n");

            // Un <input> o <textarea> no puede tener hijos, no seguimos obviamente bajando
        }
        else
        {
            Node child = elem.getFirstChild();
            while (child != null)
            {
                code = fixTreeHTMLTextControlElements(child,code,clientDoc);
                child = child.getNextSibling();
            }
        }

        return code;
    }

    private static String bindFixHTMLTextControlMethod(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        code.append( "var func = function (elem)" );
        code.append( "{" );
        code.append( "  var listener = function(evt)\n" );
        code.append( "  {\n" );
        code.append( "    var elem = evt.currentTarget;\n" );
        code.append( "    elem.itsnat_value = elem.value;\n" );
        code.append( "  };\n" );
        code.append( "  elem.addEventListener(\"focus\",listener,false);\n" );

        code.append( "  var listener = function(evt)\n" );
        code.append( "  {\n" );
        code.append( "    var elem = evt.currentTarget;\n" );
        code.append( "    if (elem.itsnat_value == elem.value) return;\n" ); // No ha cambiado
        code.append( "    var chEvt = itsNatDoc.doc.createEvent(\"Events\");\n" );
        code.append( "    chEvt.initEvent(\"change\",true,true);\n" );
        code.append( "    elem.dispatchEvent(chEvt);\n" );
        code.append( "  };\n" );
        code.append( "  elem.addEventListener(\"blur\",listener,false);\n" );
        code.append( "};" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

}

