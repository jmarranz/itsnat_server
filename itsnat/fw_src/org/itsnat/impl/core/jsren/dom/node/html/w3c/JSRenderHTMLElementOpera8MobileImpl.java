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
public class JSRenderHTMLElementOpera8MobileImpl extends JSRenderHTMLElementOperaImpl
{
    public static final JSRenderHTMLElementOpera8MobileImpl SINGLETON = new JSRenderHTMLElementOpera8MobileImpl();

    /**
     * Creates a new instance of JSOperaDefaultHTMLElementRenderImpl
     */
    public JSRenderHTMLElementOpera8MobileImpl()
    {
        // A partir de la lista de MSIE probados uno a uno

        tagNamesWithoutInnerHTML.add("table");
        tagNamesWithoutInnerHTML.add("tbody");
        tagNamesWithoutInnerHTML.add("tfoot");
        tagNamesWithoutInnerHTML.add("thead");
        tagNamesWithoutInnerHTML.add("tr");

        tagNamesWithoutInnerHTML.add("html");
        tagNamesWithoutInnerHTML.add("select");  // Con innerHTML los cerradores </option> los considera nuevos elementos (de tag desconocido)

        // No probado: tagNamesWithoutInnerHTML.add("frameset");

        /* Caso <style> dentro de un innerHTML: el elemento se inserta pero el
         * estilo NO se aplica.
         */
        tagNamesNotValidInsideInnerHTML.add("style");
    }

    public static void fixTreeHTMLInputCheckBoxFocusBlur(Node node,ClientDocumentStfulImpl clientDoc)
    {
        if (!clientDoc.canReceiveALLNormalEvents())
            return; // No merece la pena

        if (!clientDoc.isSendCodeEnabled())
            return;

        StringBuffer code = fixTreeHTMLInputCheckBoxFocusBlur(node,null,clientDoc);

        if ((code != null) && (code.length() > 0))
            clientDoc.addCodeToSend(code.toString());
    }

    private static StringBuffer fixTreeHTMLInputCheckBoxFocusBlur(Node node,StringBuffer code,ClientDocumentStfulImpl clientDoc)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE) return code;

        Element elem = (Element)node;

        if (DOMUtilHTML.isHTMLInputCheckBox(elem))
        {
            if (code == null) code = new StringBuffer();

            String elemRef = clientDoc.getNodeReference(elem,true,true);

            String methodName = "opera8MobFixInputCheckBoxFocusBlur";
            if (!clientDoc.isClientMethodBounded(methodName))
                code.append(bindFixTreeHTMLInputCheckBoxFocusBlurMethod(methodName,clientDoc));

            code.append("itsNatDoc." + methodName + "(" + elemRef + ");\n");

            // Un <input type=checkbox> no puede tener hijos, no seguimos obviamente bajando
        }
        else
        {
            Node child = elem.getFirstChild();
            while (child != null)
            {
                code = fixTreeHTMLInputCheckBoxFocusBlur(child,code,clientDoc);
                child = child.getNextSibling();
            }
        }

        return code;
    }

    private static String bindFixTreeHTMLInputCheckBoxFocusBlurMethod(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        // El problema que se resuelve es un fallo raro que se da en Opera Mobile
        // v8.6 y 8.65 WinCE y UIQ, excepto en 8.65 WinCE, consiste en que al llegar
        // a un <input type=checkbox> a través de los cursores del emulador (no los del teclado del PC, es diferente)
        // se lanzan los siguientes eventos: focus,blur,focus es decir se emite un par
        // focus/blur innecesario. Esto no ocurre si se hace click con el ratón (stylus en el disp. real).
        // La solución es algo complicada, la base es que la terna focus/blur/focus se realiza
        // de forma seguida por lo que un timer con window.setTimeout se ejecutará *después*
        // a través de variables "begin" y "end" detectamos cuando el ciclo de eventos
        // termina, mientras tanto evitamos procesar los eventos focus y blur y la reentrada del segundo focus,
        // al final emitimos nuestro propio evento focus que sabemos que no tiene este problema.
        // Hay que tener en cuenta que la solución debe ser compatible con el pulsado
        // normal del control sin cursores.

        // Podríamos vincular este código cuando se inserta el elemento
        // pero los listeners "focus" y "blur" en un checkbox son poco habituales
        // salvo en edición inplace.

        StringBuffer code = new StringBuffer();

        code.append( "var func = function (elem)\n" );
        code.append( "{\n" );
        code.append( "  var itsNatDoc = this;\n" );
        code.append( "  var listener = function(evt)\n" );
        code.append( "  {\n" );
        code.append( "    var elem = arguments.callee.elem;\n" );
        code.append( "    if (elem.itsNatFocusBegin && !elem.itsNatFocusEnd) return;\n" );
        code.append( "    elem.itsNatFocusBegin = true;\n" );
        code.append( "    elem.itsNatFocusEnd = false;\n" );

        code.append( "    var filter = function(evt)\n" );
        code.append( "    {\n" );
        code.append( "      var elem = arguments.callee.elem;\n" );
        code.append( "      if (evt.getCurrentTarget() != elem) return true;\n" );
        code.append( "      var type = (evt.getNativeEvent) ? evt.getNativeEvent().type : null;\n" );
        code.append( "      if ((type!=null) && ((type==\"focus\")||(type==\"blur\"))) return false;\n" );
        code.append( "      return true;\n" );
        code.append( "    };\n" );
        code.append( "    filter.elem = elem;\n" );
        code.append( "    itsNatDoc.addGlobalEventListener(filter);\n" );
        code.append( "    elem.fixInputCheckBoxFocusBlurFilter = filter;\n" );

        code.append( "    var timer = function()\n" );
        code.append( "    {\n" );
        code.append( "      var elem = arguments.callee.elem;\n" );
        code.append( "      itsNatDoc.removeGlobalEventListener(elem.fixInputCheckBoxFocusBlurFilter);\n" );
        code.append( "      var evtTmp = itsNatDoc.doc.createEvent(\"Events\");\n" );
        code.append( "      evtTmp.initEvent(\"focus\",true,true);\n" );
        code.append( "      elem.dispatchEvent(evtTmp);\n" );
        code.append( "      elem.itsNatFocusBegin = false;\n" );
        code.append( "      elem.itsNatFocusEnd = true;\n" );
        code.append( "    };\n" );
        code.append( "    timer.elem = elem;\n" );
        code.append( "    itsNatDoc.setTimeout(timer,0);\n" );

        code.append( "  };\n" );
        code.append( "  listener.elem = elem;\n" );
        code.append( "  elem.addEventListener(\"focus\",listener,false);\n" );
        code.append( "};\n" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

}
