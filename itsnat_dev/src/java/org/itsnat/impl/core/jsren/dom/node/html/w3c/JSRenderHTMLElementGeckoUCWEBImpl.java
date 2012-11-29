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

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.browser.BrowserGeckoUCWEB;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLAnchorElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementGeckoUCWEBImpl extends JSRenderHTMLElementGeckoImpl
{
    public static final JSRenderHTMLElementGeckoUCWEBImpl SINGLETON = new JSRenderHTMLElementGeckoUCWEBImpl();

    /** Creates a new instance of JSMSIEHTMLElementRenderImpl */
    public JSRenderHTMLElementGeckoUCWEBImpl()
    {
    }

    public static void fixTreeHTMLElements(boolean inserted,Node node,ClientDocumentStfulImpl clientDoc)
    {
        if (!clientDoc.canReceiveALLNormalEvents())
            return; // No merece la pena

        if (!clientDoc.isSendCodeEnabled())
            return;

        StringBuffer code = fixTreeHTMLElements(inserted,node,null,clientDoc);

        if ((code != null) && (code.length() > 0))
            clientDoc.addCodeToSend(code.toString());
    }

    private static StringBuffer fixTreeHTMLElements(boolean inserted,Node node,StringBuffer code,ClientDocumentStfulImpl clientDoc)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE) return code;

        Element elem = (Element)node;

        BrowserGeckoUCWEB browser = (BrowserGeckoUCWEB)clientDoc.getBrowser();

        if (browser.isHTMLTextControlPassive(elem))
        {
            code = fixHTMLTextControlPassiveElement(inserted,elem,code,clientDoc);
            // Un <input> o <textarea> no puede tener hijos, no seguimos obviamente bajando
            return code;
        }
        else if ((elem instanceof HTMLAnchorElement) &&
                  browser.isHTMLAnchorOnClickNotExec())
        {
            code = fixHTMLAnchorHRef(inserted,(HTMLAnchorElement)elem,code,clientDoc);
            // Un <a> no puede tener hijos, no seguimos obviamente bajando
            return code;
        }
        else if (browser.isOnlyOnClickExecuted(elem)) // Los casos anteriores no se dan para este caso, no hay problema por la casuistica excluyente
        {
            code = fixOnlyOnClickExecutedElement(inserted,elem,code,clientDoc);
            // SI puede tener hijos
        }

        Node child = elem.getFirstChild();
        while (child != null)
        {
            code = fixTreeHTMLElements(inserted,child,code,clientDoc);
            child = child.getNextSibling();
        }

        return code;
    }

    private static StringBuffer fixHTMLTextControlPassiveElement(boolean inserted,Element elem,StringBuffer code,ClientDocumentStfulImpl clientDoc)
    {
        // El problema es el siguiente:
        // UCWEB (6.3 Win CE y 6.0 Java), cuando se modifica un input text o password o file o un textarea, no emite
        // ni focus, ni blur, ni change, ni click, lo que hace es que cuando se pulsa cualquier otro control
        // de formulario o link (con o sin listeners asociados) el navegador cliente proxy
        // se comunica con el servidor de UCWEB y le envía el estado actual de los
        // controles de texto, en el servidor se actualizan automáticamente
        // dichos valores (se actualiza la propiedad "value") antes de que se procese cualquier evento (se ejecute el JavaScript)
        // después se dispara el evento asociado a la acción del usuario,
        // luego al final de todo, si el control de texto tiene listeners registrados,
        // a continuación dispara en este orden los eventos focus,click y blur en este
        // orden en todos los elementos controles de texto (con listeners de cada tipo) incluso aunque no haya habido ningun cambio de value,
        // aunque esto último NO ocurre en las versiones de Java, sólo en Win CE.
        // Hay notar que el evento change NO se emite.
        // El problema es que dicho disparo de focus/click/blur para el control de texto, si se hace, se hace DESPUES
        // de disparar el evento que motivó la comunicación con el servidor, por ejemplo
        // pulsamos un botón, por lo que se procesarán los listeners asociados al botón
        // antes que los listeners del control de texto.
        // Por otro lado tenemos el problema del change no emitido, podríamos emitir un "change" manual
        // al procesarse el blur sin embargo esto no soluciona el problema de que el evento
        // causante de la comunicación haya sido disparado antes, dicho listener es posible
        // que espere que el dato del cliente esté ya en el servidor en Java cuando
        // no es así porque normalmente el change se usa para transportar el "value".
        // La solución es interceptar usando un capture en el documento los eventos típicos
        // que provocan la comunicación con el servidor, focus, click y change, sea cual sea el elemento emisor
        // y emitir los eventos focus/click/change/blur manualmente a todos controles de
        // de texto, así se procesarán antes de que se procese el evento "motivador" de la
        // la comunicación con el servidor UCWEB.
        // El evento focus se considera como "motivador" porque si se emite se emite antes que click, idem con change
        // por ejemplo en el caso de cambio en un <select> (se emite antes el change
        // que el click), no usamos otros tal y como blur porque siempre estará unido
        // a un focus y click puesto que los únicos elementos que admitirían un blur
        // "desconectado" del focus son los controles de texto, los cuales no emiten
        // estos eventos cuando el usuario actúa en ellos (se emiten despúes cuando
        // hay comunicación provocada por otros controles).
        // Los eventos mouseup y mousedown no son disparados por UCWEB, sólo click,
        // parece que tampoco keydown, keyup y keypress.

        // Como existe el riesgo de emitir varias veces el pack de eventos
        // a los controles de texto (caso de la versión Win CE), como no sabemos cual es el primer
        // evento en la comunicación con UCWEB servidor ni cuantos hay,
        // por ello sólo emitimos el pack cuando hay un cambio en la propiedad
        // value pues así detectamos que fue modificado por el usuario desde la
        // última vez (aunque para ser estrictos pudo ser modificado via JavaScript también pero en fin).

        // Otro problema es que en tiempo de carga antes de enviar al cliente se emite al final
        // de la carga eventos a los controles de texto que tengan asociados listeners,
        // esto ocurre al final de todo y antes de la respuesta
        // (no en el momento en que se asocia el listener)
        // Esto ocurre en Win CE y Java.

        // Finalmente algo parecido ocurre cuando un script inserta un control de texto
        // que tiene (inline) o asocia listeners (addEventListener), automáticamente se emiten los eventos asociados
        // a dichos listeners, esto ocurre al final de todo y antes de la respuesta
        // (no en el momento en que se asocia el listener).
        // Esto ocurre en Win CE y Java.

        // Para evitar el pack de eventos que el propio UCWEB emite a los controles
        // al final de todo (antes de devolver la respuesta), intentamos filtrar dichos eventos que no fueron disparados
        // manualmente (stopPropagation()), usando un capture desde el documento.

        if (code == null) code = new StringBuffer();

        String elemRef = clientDoc.getNodeReference(elem,true,true);

        if (inserted)
        {
            String methodName = "ucwebFixHTMLTextControlRegister";
            if (!clientDoc.isClientMethodBounded(methodName))
            {
                // Se ha de enlazar y llamar una sola vez por página cliente
                // lo hacemos antes de la primera vez que se inserta un elemento
                code.append(bindFixHTMLTextControlMethodRegistry(methodName,clientDoc));
                code.append("itsNatDoc." + methodName + "();\n");
            }

            methodName = "ucwebFixHTMLTextControlInsert";
            if (!clientDoc.isClientMethodBounded(methodName))
                code.append(bindFixHTMLTextControlMethodInsert(methodName,clientDoc));

            code.append("itsNatDoc." + methodName + "(" + elemRef + ");\n");
        }
        else
        {
            String methodName = "ucwebFixHTMLTextControlRemove";
            if (!clientDoc.isClientMethodBounded(methodName))
                code.append(bindFixHTMLTextControlMethodRemove(methodName,clientDoc));

            code.append("itsNatDoc." + methodName + "(" + elemRef + ");\n");
        }

        return code;
    }


    private static String bindFixHTMLTextControlMethodRegistry(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        code.append( "var func = function ()\n" );
        code.append( "{\n" );
        code.append( "  this.htmlTextControls = new itsnat.List();\n" );
        code.append( "  var itsNatDoc = this;\n" );

        code.append( "  function dispEvent(elem,type,familly)\n" );
        code.append( "  {\n" );
        code.append( "    var evtTmp = itsNatDoc.doc.createEvent(familly);\n" );
        code.append( "    if (type==\"click\") evtTmp.initMouseEvent(\"click\",true,true,itsNatDoc.win,0,0,0,0,0,false,false,false,false,0,null);\n" );
        code.append( "    else evtTmp.initEvent(type,true,true);\n" );
        code.append( "    elem.dispatchEvent(evtTmp);\n" );
        code.append( "  }\n" );

        code.append( "  var func = function(evt)\n" );
        code.append( "  {\n" );
        code.append( "    if (itsNatDoc.fixingHTMLTextControls) return;\n" ); // evita la reentrada
        code.append( "    var list = itsNatDoc.htmlTextControls;\n" );
        code.append( "    if (list.isEmpty()) return;\n" );
        code.append( "    list = list.getArrayCopy();\n" ); // por si acaso al procesar los eventos se añaden/quitan nuevos controles
        // Filtramos los posibles eventos lanzados por el UCWEB a los controles de texto
        code.append( "    for(var i = 0; i < list.length; i++)\n" );
        code.append( "    {\n" );
        code.append( "      var elem = list[i];\n" );
        code.append( "      if (elem == evt.target) { evt.stopPropagation(); return; }\n" );
        code.append( "    }\n" );
        // Ahora tenemos la seguridad de que el evento no es de un control de texto
        // y lo más seguro es que sea un evento del UCWEB cuando se establece comunicación del cliente con el servidor
        code.append( "    itsNatDoc.fixingHTMLTextControls = true;\n" );
        code.append( "    for(var i = 0; i < list.length; i++)\n" );
        code.append( "    {\n" );
        code.append( "      var elem = list[i];\n" );
        code.append( "      var changed = (elem.value != elem.itsnat_value);\n" );
        code.append( "      elem.itsnat_value = elem.value;\n" );
        code.append( "      dispEvent(elem,\"focus\",\"HTMLEvents\");\n" );
        code.append( "      dispEvent(elem,\"click\",\"MouseEvents\");\n" );
        code.append( "      if (changed) \n" );
        code.append( "         dispEvent(elem,\"change\",\"HTMLEvents\");\n" );
        code.append( "      dispEvent(elem,\"blur\",\"HTMLEvents\");\n" );
        code.append( "    }\n" );
        code.append( "    itsNatDoc.fixingHTMLTextControls = false;\n" );
        code.append( "  };\n" );
        code.append( "  var evtList = [\"focus\",\"click\",\"change\",\"blur\"];\n" ); // El "blur" no es un evento "motivador/iniciador" de comunicación cliente/servidor, pero en este código tiene la misión también de filtrar los eventos enviados a los controles de texto por UCWEB
        code.append( "  for(var i = 0; i < evtList.length; i++)\n" );
        code.append( "    itsNatDoc.doc.addEventListener(evtList[i],func,true);\n" ); 
        code.append( "};\n" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    private static String bindFixHTMLTextControlMethodInsert(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        code.append( "var func = function (node)\n" );
        code.append( "{" );
        code.append( "  node.itsnat_value = node.value;\n" );
        code.append( "  this.htmlTextControls.add(node);\n" );
        code.append( "};\n" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }


    private static String bindFixHTMLTextControlMethodRemove(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        code.append( "var func = function (node)\n" );
        code.append( "{" );
        code.append( "  this.htmlTextControls.remove(node);\n" );
        code.append( "};\n" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    private static StringBuffer fixHTMLAnchorHRef(boolean inserted,HTMLAnchorElement elem,StringBuffer code,ClientDocumentStfulImpl clientDoc)
    {
        // Esto es debido a que los links cuando son pulsados no emiten
        // ni click, ni focus ni nada ni siquiera con handler onclick.

        if (code == null) code = new StringBuffer();

        if (inserted)
        {
            String methodName = JSRenderHTMLAttributeGeckoUCWEBImpl.getHTMLAnchorHRefCallMethodName();
            if (!clientDoc.isClientMethodBounded(methodName))
            {
                // Se ha de enlazar una sola vez por página cliente
                // aprovechamos la primera vez que se inserta un elemento
                code.append(bindFixHTMLAnchorHRefProcess(methodName,clientDoc));
            }

            String elemRef = clientDoc.getNodeReference(elem,true,true);
            code.append("var elem = " + elemRef + ";\n");

            JSRenderNodeRegistryByClientImpl registry = JSRenderHTMLAttributeGeckoUCWEBImpl.getHTMLAnchorHRefRegistry(clientDoc);
            String id = registry.registerNode(elem);

            String href = elem.getAttribute("href");
            href = JSRenderHTMLAttributeGeckoUCWEBImpl.transformHTMLAnchorHRef(href, id);

            // Modificamos el atributo en el cliente sólo, NO en el servidor.
            code.append("elem.setAttribute(\"href\",\"" + href + "\");\n");

            methodName = "ucwebFixHTMLAnchorHRefInsert";
            if (!clientDoc.isClientMethodBounded(methodName))
                code.append(bindFixHTMLAnchorHRefInsert(methodName,clientDoc));

            code.append("itsNatDoc." + methodName + "(\"" + id + "\",elem);\n");
        }
        else
        {
            // Sabemos que en el cliente el onclick está modificado siguiendo el patrón del insert
            JSRenderNodeRegistryByClientImpl registry = JSRenderHTMLAttributeGeckoUCWEBImpl.getHTMLAnchorHRefRegistry(clientDoc);
            String id = registry.unRegisterNode(elem);

            String methodName = "ucwebFixHTMLAnchorHRefRemove";
            if (!clientDoc.isClientMethodBounded(methodName))
                code.append(bindFixHTMLAnchorHRefRemove(methodName,clientDoc));

            code.append("itsNatDoc." + methodName + "(\"" + id + "\");\n");
        }

        return code;
    }

    private static String bindFixHTMLAnchorHRefProcess(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        code.append( "var func = function (id)\n" );
        code.append( "{\n" );
        code.append( "  var node = this.htmlAnchors[id];\n" );
        code.append( "  var evtTmp;\n" );
        code.append( "  evtTmp = itsNatDoc.doc.createEvent(\"HTMLEvents\");\n" );
        code.append( "  evtTmp.initEvent(\"focus\",true,true);\n" );
        code.append( "  node.dispatchEvent(evtTmp);\n" );
        code.append( "  evtTmp = itsNatDoc.doc.createEvent(\"MouseEvents\");\n" );
        code.append( "  evtTmp.initMouseEvent(\"click\",true,true,itsNatDoc.win,0,0,0,0,0,false,false,false,false,0,null);\n" );
        code.append( "  node.dispatchEvent(evtTmp);\n" );
        code.append( "};\n" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    private static String bindFixHTMLAnchorHRefInsert(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        code.append( "var func = function (id,node)\n" );
        code.append( "{\n" );
        code.append( "  if (!this.htmlAnchors) this.htmlAnchors = new Object();\n" );
        code.append( "  this.htmlAnchors[id] = node;\n" );
        code.append( "};\n" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    private static String bindFixHTMLAnchorHRefRemove(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        code.append( "var func = function (id)\n" );
        code.append( "{\n" );
        code.append( "  delete this.htmlAnchors[id];\n" );
        code.append( "};\n" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    private static StringBuffer fixOnlyOnClickExecutedElement(boolean inserted,Element elem,StringBuffer code,ClientDocumentStfulImpl clientDoc)
    {
        // En las versiones Windows Mobile los elementos no form ignoran los addEventListener
        // e incluso la propiedad onclick, sólo el handler inline onclick es ejecutado,
        // cuando hay un click del usuario con la correspondiente comunicación con el servidor.
        // Sin embargo esta ejecución del handler inline ni siquiera es normal,
        // inspeccionando con arguments.callee vemos que no hay parámetro "event" y
        // el "this" no funciona, es más si usamos "event" y/o "this" el servidor UCWEB
        // en tiempo de carga parsea dichos handler y evita que sean pulsables en el cliente
        // pues detecta que darán error si se ejecutan. La buena noticia es que
        // un dispatchEvent de un evento manualmente creado funciona bien, es decir
        // ejecuta los listeners registrados con addEventListener y la función
        // de la propiedad onclick haciendo correctamente el bubble/capture (a fin de cuentas es un FireFox).
        // Por tanto SOLO se ejecuta la función del atributo cuando se pulsa el elemento,
        // lo cual nos permitirá trucar dicho atributo para lanzar un click normal via JavaScript.
        // El código original del atributo onclick estará en la propiedad (cuando
        // se cambia el atributo se cambia la propiedad automáticamente pero no al revés)
        // el cual se ejecutará normalmente al procesarse el evento manual, habrá
        // que tener cuidado cuando queramos cambiar el atributo onclick, pues habrá
        // que cambiar realmente la propiedad onclick.
        // Aparte de solucionar el problema de asociar addEventListener también
        // solucionamos indirectamente el problema del uso de "event" y "this" pues
        // ahora el código original está en la propiedad onclick y es ejecutado normalmente
        // (no la ejecución extraña del handler inline cuando se pulsa el elemento), como
        // el cambio del onclick lo hacemos cuando se inserta el elemento (si tiene atributo onclick),
        // el servidor UCWEB no detecta que estuvo "mal" (lo cual debe de hacer seguramente cuando
        // va a servir la página al cliente) y pasa a ser "pulsable".

        // En Windows Mobile (6.1 al menos) sólo unos pocos elementos responden al onclick,
        // he detectado al menos SPAN,TBODY,TR,TD, quizás haya más, pero no funciona
        // ni TABLE, ni DIV ni P ni A. Afortunadamente con <a> podemos usar
        // similar técnica con href (se hace en otro sitio).

        if (inserted)
        {
            JSRenderNodeRegistryByClientImpl registry = JSRenderHTMLAttributeGeckoUCWEBImpl.getFixOnlyOnClickExecRegistry(clientDoc);
            String id = registry.getIdByNode(elem);
            if ((id == null) &&  // Si id == null no fue procesado anteriormente, pudo ser procesado por ejemplo al añadir un listener click en el servidor (en fast load se ejectua antes que el fix sistemático que se hace al final de la carga)
                (elem.hasAttribute("onclick") || elem instanceof HTMLAnchorElement) )
            {
                // Sólo procesamos aquellos que tengan un handler onclick
                // pues de otra manera procesaríamos prácticamente todos los elementos
                // en aquellos elementos en los que se añadan listeners ya se hará la mejora del elemento.
                // Así arreglamos el problema de la presencia de "this" y/o "event" en el handler
                // Procesamos siempre los anchor aunque no tengan onclick para generar un evento click
                // de esta manera podemos usar los anchor para generar clicks que son procesados
                // por elementos más arriba (bubble/capture) que por si mismo no generan click (ej. <div> o <p> en 6.3 Win CE),
                // este es el mismo tipo de técnica que se usa en Pocket IE o MotoWebKit,
                // además permite que funcionen ejemplos de código diseñados para Pocket IE o MotoWebKit
                // en donde se usan anchors "desnudos" (que no hacen nada por sí mismos pues sirven para generar eventos),
                // pues de otra manera estos anchors son los pulsados (no nodos padre con onclick) pero
                // al no tener el onclick especial ItsNat no se genera un evento normal click para ser procesado por nodos padre.

                if (code == null) code = new StringBuffer();

                String elemRef = clientDoc.getNodeReference(elem,true,true);
                code.append("var elem = " + elemRef + ";\n");

                code.append( fixOnlyOnClickExecutedElementInsert(elem,"elem",clientDoc) );
            }
        }
        else
        {
            JSRenderNodeRegistryByClientImpl registry = JSRenderHTMLAttributeGeckoUCWEBImpl.getFixOnlyOnClickExecRegistry(clientDoc);
            String id = registry.getIdByNode(elem);
            if (id == null) return code; // No estaba registrado

            if (code == null) code = new StringBuffer();

            code.append( fixOnlyOnClickExecutedElementRemove(elem,id,clientDoc) );
        }

        return code;
    }

    public static String fixOnlyOnClickExecutedElementInsert(Element elem,String elemVarName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        String methodName = JSRenderHTMLAttributeGeckoUCWEBImpl.getFixOnlyOnClickExecProcessMethodName();
        if (!clientDoc.isClientMethodBounded(methodName))
        {
            // Se ha de enlazar una sola vez por página cliente
            code.append(JSRenderHTMLElementGeckoUCWEBImpl.bindFixOnlyOnClickExecProcess(methodName,clientDoc));
        }

        JSRenderNodeRegistryByClientImpl registry = JSRenderHTMLAttributeGeckoUCWEBImpl.getFixOnlyOnClickExecRegistry(clientDoc);
        String id = registry.registerNode(elem);

        // Modificamos el atributo en el cliente sólo, NO en el servidor.

        String onclickAttr = JSRenderHTMLAttributeGeckoUCWEBImpl.getFixOnlyOnClickExecMethodCall(id);
        code.append(elemVarName + ".setAttribute(\"onclick\",\"" + onclickAttr + "\");\n");

        String onclickHandler = elem.getAttribute("onclick");
        code.append(elemVarName + ".onclick = function (event) { " + onclickHandler + " };\n");

        methodName = "ucwebFixOnlyOnClickExecInsert";
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(JSRenderHTMLElementGeckoUCWEBImpl.bindFixOnlyOnClickExecInsert(methodName,clientDoc));

        code.append("itsNatDoc." + methodName + "(\"" + id + "\"," + elemVarName + ");\n");

        return code.toString();
    }

    public static String fixOnlyOnClickExecutedElementRemove(Element elem,String id,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        // Sabemos que en el cliente el onclick está modificado siguiendo el patrón del insert

        JSRenderNodeRegistryByClientImpl registry = JSRenderHTMLAttributeGeckoUCWEBImpl.getFixOnlyOnClickExecRegistry(clientDoc);
        String id2 = registry.unRegisterNode(elem);
        if (!id.equals(id2)) throw new ItsNatException("INTERNAL ERROR");

        String methodName = "ucwebFixFixOnlyOnClickExecRemove";
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindFixOnlyOnClickExecRemove(methodName,clientDoc));

        code.append("itsNatDoc." + methodName + "(\"" + id + "\");\n");

        return code.toString();
    }

    public static String bindFixOnlyOnClickExecProcess(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        code.append( "var func = function (id)\n" );
        code.append( "{\n" );
        code.append( "  var node = this.onlyOnClickExecElems[id];\n" );
        code.append( "  var evtTmp = itsNatDoc.doc.createEvent(\"MouseEvents\");\n" );
        code.append( "  evtTmp.initMouseEvent(\"click\",true,true,itsNatDoc.win,0,0,0,0,0,false,false,false,false,0,null);\n" );
        code.append( "  node.dispatchEvent(evtTmp);\n" );
        code.append( "};\n" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    public static String bindFixOnlyOnClickExecInsert(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        code.append( "var func = function (id,node)\n" );
        code.append( "{\n" );
        code.append( "  if (!this.onlyOnClickExecElems) this.onlyOnClickExecElems = new Object();\n" );
        code.append( "  this.onlyOnClickExecElems[id] = node;\n" );
        code.append( "};\n" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    private static String bindFixOnlyOnClickExecRemove(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();

        code.append( "var func = function (id)\n" );
        code.append( "{\n" );
        code.append( "  delete this.onlyOnClickExecElems[id];\n" );
        code.append( "};\n" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

}

