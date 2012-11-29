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
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementWebKitMotoImpl extends JSRenderHTMLElementWebKitFilteredCommentsImpl
{
    public static final JSRenderHTMLElementWebKitMotoImpl SINGLETON = new JSRenderHTMLElementWebKitMotoImpl();

    /** Creates a new instance of JSRenderHTMLElementWebKitMotoImpl */
    public JSRenderHTMLElementWebKitMotoImpl()
    {
    }

    public static void fixTreeHTMLElements(Node node,ClientDocumentStfulImpl clientDoc)
    {
        if (!clientDoc.canReceiveALLNormalEvents())
            return; // No merece la pena

        if (!clientDoc.isSendCodeEnabled())
            return;

        StringBuffer code = fixTreeHTMLElements(node,null,clientDoc);

        if ((code != null) && (code.length() > 0))
            clientDoc.addCodeToSend(code.toString());
    }

    private static StringBuffer fixTreeHTMLElements(Node node,StringBuffer code,ClientDocumentStfulImpl clientDoc)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE) return code;

        Element elem = (Element)node;

        if (elem instanceof HTMLTextAreaElement)
        {
            if (code == null) code = new StringBuffer();

            // En los <textarea> no se emite el evento change, sin embargo emiten
            // el evento blur.

            // El evento "focus" y "blur" en textarea son de los pocos que admiten addEventListener
            // El initUIEvent no está definido
            // El evento change se lanzará en teoría después del blur, pero esto es
            // correcto en MotoWebKit, así ocurre en <input type="text"> pues el WebKit es muy antiguo.

            // correcto en MotoWebKit, así ocurre en <input type="text"> pues el WebKit es muy antiguo.
            String elemRef = clientDoc.getNodeReference(elem,true,true);
            code.append( "var elem = " + elemRef + ";\n" );

            code.append( "var listener = function(evt)\n" );
            code.append( "{\n" );
            code.append( "  var elem = evt.currentTarget;\n" );
            code.append( "  elem.itsnat_value = elem.value;\n" );
            code.append( "};\n" );
            code.append( "elem.addEventListener(\"focus\",listener,false);\n" );

            code.append( "var listener = function(evt)\n" );
            code.append( "{\n" );
            code.append( "  var elem = evt.currentTarget;\n" );
            code.append( "  if (elem.itsnat_value == elem.value) return;\n" ); // No ha cambiado
            code.append( "  var evtTmp = itsNatDoc.doc.createEvent(\"UIEvents\");\n" );
            code.append( "  evtTmp.initEvent(\"change\",true,true);\n" );
            code.append( "  itsNatDoc.dispatchEvent(elem,\"change\",evtTmp);\n" );
            code.append( "};\n" );
            code.append( "elem.addEventListener(\"blur\",listener,false);\n" );

            // Un <textarea> no puede tener elementos hijo, no seguimos obviamente bajando
        }
        else if (DOMUtilHTML.isHTMLInputCheckBox(elem))
        {
            if (code == null) code = new StringBuffer();

            // El evento click no se lanza pero el change sí, sin embargo es el click
            // el que es usado para detectar el cambio de estado en los checkboxes en el componente checkbox.

            // El evento "change" (en input) es de los pocos que admiten addEventListener
            // El initMouseEvent no está definido.
            code.append( "var listener = function(evt)\n" );
            code.append( "{\n" );
            code.append( "  var elem = evt.currentTarget;\n" );
            code.append( "  if (!elem.itsNatFireClick) return;\n" );
            code.append( "  var evtTmp = itsNatDoc.doc.createEvent(\"UIEvents\");\n" );
            code.append( "  evtTmp.initEvent(\"click\",true,true);\n" );
            code.append( "  itsNatDoc.dispatchEvent(elem,\"click\",evtTmp);\n" );
            code.append( "};\n" );

            String elemRef = clientDoc.getNodeReference(elem,true,true);
            code.append( "var elem = " + elemRef + ";\n" );
            code.append( "elem.addEventListener(\"change\",listener,false);\n" );
            code.append( "elem.itsNatFireClick = true;\n");

            // Un <input> no puede tener hijos, no seguimos obviamente bajando
        }
        else if (DOMUtilHTML.isHTMLInputImage(elem))
        {
            if (code == null) code = new StringBuffer();

            // El evento click no se lanza pero el keydown y keyup sí, sin embargo es el click
            // el interesante en un botón. Podríamos simular también mouseup y mousedown
            // pero estos eventos no suelen ser disparados por MotoWebKit en ningún elemento.
            // Usamos keyup pues se ejecuta al levantar el botón al igual que el "click"

            // El evento "keyup" (en input) es de los pocos que admiten addEventListener
            // El initMouseEvent no está definido.
            code.append( "var listener = function(evt)\n" );
            code.append( "{\n" );
            code.append( "  var elem = evt.currentTarget;\n" );
            code.append( "  var evtTmp = itsNatDoc.doc.createEvent(\"UIEvents\");\n" );
            code.append( "  evtTmp.initEvent(\"click\",true,true);\n" );
            code.append( "  itsNatDoc.dispatchEvent(elem,\"click\",evtTmp);\n" );
            code.append( "};\n" );

            String elemRef = clientDoc.getNodeReference(elem,true,true);
            code.append( "var elem = " + elemRef + ";\n" );
            code.append( "elem.addEventListener(\"keyup\",listener,false);\n" );

            // Un <input> no puede tener hijos, no seguimos obviamente bajando
        }
        else
        {
            Node child = elem.getFirstChild();
            while (child != null)
            {
                code = fixTreeHTMLElements(child,code,clientDoc);
                child = child.getNextSibling();
            }
        }

        return code;
    }

    public String getCurrentStyleObject(String itsNatDocVar,String elemName,ClientDocumentStfulImpl clientDoc)
    {
        // window.getComputedStyle no está definido
        return elemName + ".style";
    }
}

