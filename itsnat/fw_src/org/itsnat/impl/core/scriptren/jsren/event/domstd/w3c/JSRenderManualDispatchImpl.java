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

package org.itsnat.impl.core.scriptren.jsren.event.domstd.w3c;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.web.ItsNatStfulWebDocumentImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;
import org.itsnat.impl.core.registry.ItsNatNormalEventListenerListSameTarget;
import org.itsnat.impl.core.registry.ItsNatNormalEventListenerListSameTarget.Pair;
import org.itsnat.impl.core.registry.dom.domstd.ItsNatDOMStdEventListenerRegistryImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class JSRenderManualDispatchImpl
{
    public JSRenderManualDispatchImpl()
    {
    }

    public static String getDispatchMethodName()
    {
        return "dispatchEventManual";
    }

    public static String getDispatchMethodName2()
    {
        return "dispatchEventManual2";
    }

    public static String getInitEventSystem(ClientDocumentStfulDelegateWebImpl clientDoc,boolean useAttrInHandler)
    {
        StringBuilder code = new StringBuilder();

        String methodName1 = getDispatchMethodName();
        if (!clientDoc.isClientMethodBounded(methodName1))
            code.append(bindDispatchEventManualMethod(methodName1,useAttrInHandler));

        String methodName2 = getDispatchMethodName2();
        if (!clientDoc.isClientMethodBounded(methodName2))
            code.append(bindDispatchEventManualMethod2(methodName2));

        return code.toString();
    }

    public static String getInitEvent(Event evt,String evtVarName)
    {
        StringBuilder code = new StringBuilder();
        code.append(   evtVarName + ".itsnat_stopPropagation = false;\n" );
        code.append(   evtVarName + ".stopPropagation = function(){ this.itsnat_stopPropagation = true; };\n" );
        code.append(   evtVarName + ".itsnat_preventDefault = false;\n" );
        code.append(   evtVarName + ".preventDefault = function(){ this.itsnat_preventDefault = true; };\n" );

        code.append(   evtVarName + ".type = \"" + evt.getType() + "\";\n" );
        code.append(   evtVarName + ".bubbles = " + evt.getBubbles() + ";\n" );
        code.append(   evtVarName + ".cancelable = " + evt.getCancelable() + ";\n" );
        return code.toString();
    }

    public static String getCallDispatchEvent(String varResName,NodeLocationImpl nodeLoc,Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        EventTarget target = evt.getTarget();
        LinkedList<String> captureList = new LinkedList<String>();
        LinkedList<String> atTargetList = new LinkedList<String>();
        LinkedList<String> bubbleList = new LinkedList<String>();
        getEventListenerIds(target,evt,captureList,atTargetList,bubbleList,clientDoc);

        String captureIds = idListToString(captureList);
        String atTargetIds = idListToString(atTargetList);
        String bubbleIds = idListToString(bubbleList);

        return "itsNatDoc." + getDispatchMethodName2() + "(" + nodeLoc.toScriptNodeLocation(true) + "," + evtVarName + "," + captureIds + "," + atTargetIds + "," + bubbleIds + ")";
    }

    public static String getCallDispatchEvent(String targetRef,Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        EventTarget target = evt.getTarget();
        LinkedList<String> captureList = new LinkedList<String>();
        LinkedList<String> atTargetList = new LinkedList<String>();
        LinkedList<String> bubbleList = new LinkedList<String>();
        getEventListenerIds(target,evt,captureList,atTargetList,bubbleList,clientDoc);

        String captureIds = idListToString(captureList);
        String atTargetIds = idListToString(atTargetList);
        String bubbleIds = idListToString(bubbleList);

        return "itsNatDoc." + getDispatchMethodName() + "(" + targetRef + "," + evtVarName + "," + captureIds + "," + atTargetIds + "," + bubbleIds + ")";
    }

    private static String bindDispatchEventManualMethod(String methodName,boolean useAttrInHandler)
    {
        StringBuilder code = new StringBuilder();

        code.append("var func = function (node,evt,captureIds,atTargetIds,bubbleIds)\n");
        code.append("{\n");
        code.append("  function processPhase(idList,itsNatDoc)\n");
        code.append("  {\n");
        code.append("    if (idList == null) return evt.itsnat_stopPropagation;\n");
        code.append("    for(var i = 0; i < idList.length; i++)\n");
        code.append("    {\n");
        code.append("      var idOrNode = idList[i];\n");
        code.append("      if (typeof idOrNode == \"string\") \n"); // id
        code.append("      {\n");
        code.append("        var listener = itsNatDoc.domListeners.get(idOrNode);\n");
        code.append("        if (!listener) continue;\n"); // por si acaso
        code.append("        evt.currentTarget = listener.getCurrentTarget();\n");
        code.append("        listener.dispatchEvent(evt);\n");
        code.append("      }\n");
        code.append("      else\n"); // node
        code.append("      {\n");
        code.append("        evt.currentTarget = idOrNode;\n");
        if (useAttrInHandler)
        {
          code.append("        var handler = new Function(idOrNode.getAttribute(\"on\" + evt.type));\n");
          code.append("        var res = handler.call(idOrNode);\n");
        }
        else
        {
          code.append("        var res = idOrNode[\"on\" + evt.type].call(idOrNode,evt);\n");
        }
        code.append("        if ((typeof res == \"boolean\") && !res) evt.preventDefault();\n");
        code.append("      }\n");
        code.append("      if (evt.itsnat_stopPropagation) break;\n");  // no es del todo correcto, pero lo habitual es un listener por nodo
        code.append("    }\n");
        code.append("    return evt.itsnat_stopPropagation;\n");
        code.append("  }\n");
        code.append("  evt.target = node;\n");
        code.append("  evt.eventPhase = 1; if (processPhase(captureIds,this)) return !evt.itsnat_preventDefault;\n");
        code.append("  evt.eventPhase = 2; if (processPhase(atTargetIds,this)) return !evt.itsnat_preventDefault;\n");
        code.append("  evt.eventPhase = 3; if (processPhase(bubbleIds,this)) return !evt.itsnat_preventDefault;\n");
        code.append("  return !evt.itsnat_preventDefault;\n");
        code.append("};\n");

        code.append("itsNatDoc." + methodName + " = func;\n");

        return code.toString();
    }

    private static String bindDispatchEventManualMethod2(String methodName)
    {
        StringBuilder code = new StringBuilder();

        code.append("var func = function (idObj,evt,captureIds,atTargetIds,bubbleIds)\n");
        code.append("{\n");
        code.append("  var node = this.getNode(idObj);\n");
        code.append("  return this." + getDispatchMethodName() + "(node,evt,captureIds,atTargetIds,bubbleIds);\n");
        code.append("};\n");

        code.append("itsNatDoc." + methodName + " = func;\n");

        return code.toString();
    }


    private static String idListToString(LinkedList<String> idList)
    {
        if (idList.isEmpty()) return "null";
        StringBuilder code = new StringBuilder();
        code.append("[");
        for(Iterator<String> it = idList.iterator(); it.hasNext(); )
        {
            String idOrNode = it.next();
            code.append(idOrNode);
            if (it.hasNext())
                code.append(",");
        }
        code.append("]");
        return code.toString();
    }

    private static void getEventListenerIds(EventTarget target,Event evt,LinkedList<String> captureList,LinkedList<String> atTargetList,LinkedList<String> bubbleList,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // NetFront:  (YA NO VALE NO ESTA SOPORTADO, REVISAR ESTO)
        // El método EventTarget.dispatchEvent() de NetFront y en general los métodos que lanzan eventos
        // tal y como focus() y blur() se ejecuta asíncronamente, más exactamente después
        // de que termine el código que lo ejecuta. Por otra parte dispatchEvent() no soporta
        // eventos HTML tal y como "change", "blur", "focus" etc, es decir, los ignora
        // Además la llamada a focus() "bloquea" el componente por lo que un cambio seguido a la propiedad
        // "value" por ejemplo es ignorado.
        // Por estas razones evitamos usar dispatchEvent(), focus() y blur() y los substituimos
        // por nuestro propio dispatching de eventos.
        // En el JavaScript cliente no hay suficiente información para obtener
        // rápidamente los listeners afectados por el evento por lo que los
        // obtenemos desde el servidor, además el dispatchEventManual no es usado
        // para despachar eventos generados por NetFront (lo cual sí ocurre en MSIE).

        // Ver ServerItsNatNormalEventImpl para saber más sobre el proceso de eventos DOM W3C

        // En el caso de Adobe SVG Viewer v3 el problema es que
        // aunque existe dispatchEvent (en ASV v3 al menos) no existe document.createEvent
        // por lo que no he encontrado

        Node targetNode = (Node)target;

        ItsNatStfulWebDocumentImpl itsNatDoc = clientDoc.getItsNatStfulWebDocument();

        ItsNatDOMStdEventListenerRegistryImpl globalRegistry = null;
        if (itsNatDoc.hasDOMStdEventListeners())
            globalRegistry = itsNatDoc.getDOMStdEventListenerRegistry();

        ItsNatDOMStdEventListenerRegistryImpl clientRegistry = null;
        if (clientDoc.hasDOMStdEventListeners())
            clientRegistry = clientDoc.getDOMStdEventListenerRegistry();

        int capturingCount = 0;
        if (globalRegistry != null) capturingCount += globalRegistry.getCapturingCount();
        if (clientRegistry != null) capturingCount += clientRegistry.getCapturingCount();
        boolean someoneCaptures = capturingCount > 0;

        ArrayList<Node> parentList = null;
        if (someoneCaptures || evt.getBubbles())
        {
            parentList = new ArrayList<Node>();
            Node parent = targetNode.getParentNode();
            while (parent != null)
            {
                parentList.add(parent);
                parent = parent.getParentNode();
            }
        }

        String type = evt.getType();
        if (someoneCaptures)
        {
            // Fase Event.CAPTURING_PHASE, desde el más alto hasta el target
            for (int i = parentList.size() - 1; i >= 0; i--)
            {
                EventTarget currentTarget = (EventTarget)parentList.get(i);
                getEventListenerIds(currentTarget,type,true,globalRegistry,clientRegistry,captureList,clientDoc);
            }
        }

        // Event.AT_TARGET
        getEventListenerIds(target,type,false,globalRegistry,clientRegistry,atTargetList,clientDoc);

        if (evt.getBubbles())
        {
            // Event.BUBBLING_PHASE
            for (int i = 0; i < parentList.size(); i++)
            {
                EventTarget currentTarget = (EventTarget)parentList.get(i);
                getEventListenerIds(currentTarget,type,false,globalRegistry,clientRegistry,bubbleList,clientDoc);
            }
        }
    }

    private static void getEventListenerIds(EventTarget currentTarget,String type,boolean useCapture,ItsNatDOMStdEventListenerRegistryImpl globalRegistry,ItsNatDOMStdEventListenerRegistryImpl clientRegistry,LinkedList<String> idList,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        if (currentTarget instanceof Element)
        {
            if (((Element)currentTarget).hasAttribute("on" + type))
            {
                String nodeRef = clientDoc.getNodeReference((Node)currentTarget, true,true); // Tenemos la garantía de que se ejecuta la referencia pues se ejecuta en el propio array
                idList.add(nodeRef);
            }
        }
        if (globalRegistry != null)
        {
            ItsNatNormalEventListenerListSameTarget listSameTarget = globalRegistry.getItsNatNormalEventListenersByTarget(currentTarget);
            getEventListenerIds(type,useCapture,listSameTarget,idList);
        }
        if (clientRegistry != null)
        {
            ItsNatNormalEventListenerListSameTarget listSameTarget = clientRegistry.getItsNatNormalEventListenersByTarget(currentTarget);
            getEventListenerIds(type,useCapture,listSameTarget,idList);
        }
    }

    private static void getEventListenerIds(String type,boolean useCapture,ItsNatNormalEventListenerListSameTarget listSameTarget,LinkedList<String> idList)
    {
        if (listSameTarget == null) return;

        LinkedList<Pair> listeners = listSameTarget.getItsNatNormalEventListeners(type, useCapture);
        if (listeners == null) return;

        for(Pair pair : listeners)
        {
            ItsNatNormalEventListenerWrapperImpl listener = pair.getListenerWrapper();
            idList.add("\"" + listener.getId() + "\"");
        }
    }
}
