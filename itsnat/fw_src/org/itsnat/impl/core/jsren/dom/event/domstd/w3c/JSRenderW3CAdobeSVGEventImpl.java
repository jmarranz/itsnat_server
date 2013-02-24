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
package org.itsnat.impl.core.jsren.dom.event.domstd.w3c;


import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.event.HTMLEventInternal;
import org.itsnat.impl.core.path.NodeLocationImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.events.UIEvent;

/**
 * La versión v6 del Adobe SVG Viewer soporta más o menos creación/distribución de eventos.
 *
 * La versión ASV v3 NO soportan la creación/despacho
 * de eventos manual pero lo simulamos.
 *
 * @author jmarranz
 */
public abstract class JSRenderW3CAdobeSVGEventImpl extends JSRenderW3CEventImpl
{
    public JSRenderW3CAdobeSVGEventImpl()
    {
    }

    public static JSRenderW3CAdobeSVGEventImpl getJSRenderW3CAdobeSVGEvent(ItsNatDOMStdEvent event)
    {
        if (event instanceof MouseEvent)
            return JSRenderW3CAdobeSVGMouseEventImpl.SINGLETON;
        else if (event instanceof ItsNatKeyEvent)
            return JSRenderW3CAdobeSVGKeyEventImpl.SINGLETON;
        else if (event instanceof UIEvent)
            return JSRenderW3CAdobeSVGUIEventGenericImpl.SINGLETON;
        else if (event instanceof HTMLEventInternal)
            return JSRenderW3CAdobeSVGEventDefaultImpl.SINGLETON;
        else if (event instanceof MutationEvent)
            return JSRenderW3CAdobeSVGMutationEventImpl.SINGLETON;
        else
            return JSRenderW3CAdobeSVGEventDefaultImpl.SINGLETON;
    }

    protected String getEventTypeGroup(Event evt)
    {
        // ASV v6
        // Increíble pero esta la única forma en el que conseguimos
        // hacer disparar eventos. El type pasado al document.createEvent
        // será el valor del atributo "type".
        // Como dice la documentación del ASV v6 sólo podemos crear
        // eventos "custom", afortunadamente el método Element.dispatchEvent
        // admite estos eventos custom por lo que simulamos que son eventos
        // nativos "de verdad".
        return evt.getType();
    }

    public String getInitEventSystem(ClientDocumentStfulImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        code.append( super.getInitEventSystem(clientDoc) ); // Por si acaso

        // En el caso de ASV v6 añadiremos métodos que NO necesitamos
        // pero no podemos distinguir en el servidor cual estamos usando.
        code.append(JSRenderManualDispatchImpl.getInitEventSystem(clientDoc,true));

        // En el caso de ASV v3 simulamos el createEvent de la v6
        // La variable temporal asv6 la utilizaremos en más sitios.
        code.append("var asv6 = (navigator.appVersion.indexOf(\"6.\") == 0);"); // Similar: window.getSVGViewerVersion()

        return code.toString();
    }

    public String getCreateEventInstance(Event evt,ClientDocumentStfulImpl clientDoc)
    {
        return "asv6 ? " + super.getCreateEventInstance(evt, clientDoc) + " : new Object()";
    }

    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        // ASV v6:
        // No podemos hacer nada aquí, el atributo "type" afortunadamente
        // está definido via document.createEvent, pero los
        // demás atributos "bubbles" y "cancelable", son sólo lectura
        // pues ya están definidos en el objeto evento "custom".
        // ASV v3:
        // Como el evento es creado manualmente, el type está ya definido
        // pero no bubble ni cancelabe. Para que sea compatible con la v6
        // que no admite escritura, añadimos try/catch
        StringBuilder code = new StringBuilder();
        code.append( "if (!asv6)" );
        code.append( "{\n" );
        code.append(   JSRenderManualDispatchImpl.getInitEvent(evt, evtVarName) );
        code.append( "}\n" );
        return code.toString();
    }


    public String getCallDispatchEvent(String varResName,NodeLocationImpl nodeLoc,Event evt,String evtVarName)
    {
        StringBuilder code = new StringBuilder();

        code.append("var " + varResName + ";");
        code.append("if (asv6)");
        code.append("{");
        code.append(   super.getCallDispatchEvent(varResName,nodeLoc,evt,evtVarName));
        code.append("}");
        code.append("else");
        code.append("{");
        code.append(   varResName + " = " + JSRenderManualDispatchImpl.getCallDispatchEvent(varResName, nodeLoc, evt, evtVarName) + ";" );
        code.append("}");

        return code.toString();
    }

    public String getCallDispatchEvent(String targetRef,Event evt,String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        code.append("if (asv6)");
        code.append("{");
        code.append(   super.getCallDispatchEvent(targetRef,evt,evtVarName,clientDoc) );
        code.append("}");
        code.append("else");
        code.append("{");
        code.append( JSRenderManualDispatchImpl.getCallDispatchEvent(targetRef,evt,evtVarName,clientDoc) + ";");
        code.append("}");

        return code.toString();
    }
}
