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

package org.itsnat.impl.core.jsren.listener;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLLabelElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatDOMStdEventListenerOpera8MobileImpl extends JSRenderItsNatDOMStdEventListenerImpl
{
    public static final JSRenderItsNatDOMStdEventListenerOpera8MobileImpl SINGLETON = new JSRenderItsNatDOMStdEventListenerOpera8MobileImpl();

    /** Creates a new instance of JSRenderItsNatDOMStdEventListenerImpl */
    public JSRenderItsNatDOMStdEventListenerOpera8MobileImpl()
    {
    }

    protected boolean needsAddListenerReturnElement()
    {
        return true;
    }

    protected boolean needsRemoveListenerReturnElement()
    {
        return false;
    }

    protected String addItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        EventTarget target = itsNatListener.getCurrentTarget();
        if (target instanceof Element)
        {
            StringBuffer code = new StringBuffer();

            code.append( super.addItsNatDOMStdEventListenerCode(itsNatListener,clientDoc) );

            // Opera Mobile 8.x puede usarse en dispositivos no táctiles usando los cursores
            // Si un elemento tiene un listener del evento click será navegable al mismo
            // usando los cursores.
            // Sin embargo he detectado algún caso (al menos Opera Mobile 8.65 Win CE) en donde tras añadir un event listener click via addEventListener (página ya cargada)
            // no se entera del mismo respecto a los cursores y no se puede llegar via cursores.
            // Esto ocurre al cambiar a modo joystick en el ejemplo de tablas con ExtJS del ItsNat Experiments
            // pero se puede comprobar en cualquier otro elemento (ej. un td o un label).
            // Si en el template existiera un onclick="", por ejemplo en las celdas, esto no pasa,
            // pueden darse otras soluciones tal y como hacer un display:none y quitarlo inmediatamente
            // después, esto provoca una "refresco" visual que corrige el fallo, pero
            // la técnica onclick también vale.
            // Aunque sólo se ha detectado para el click en elementos no tipo control de formulario
            // extendemos el refresco para cualquier tipo de evento y elemento por si acaso,
            // pues el código que se envía es mínimo.
            String type = itsNatListener.getType();

            String attrName = "\"on" + type + "\"";
            code.append( "if (!elem.hasAttribute(" + attrName + ")) { elem.setAttribute(" + attrName + ",\"\"); elem.removeAttribute(" + attrName + "); } \n" );

            // Otro problema es que el elemento <label> no procesa el evento click
            // cuando es pulsado a través del "enter" de la PDA como sí ocurre
            // en otros tipos de elementos. Sin embargo dicho botón emite un evento
            // de teclado con keyCode 13 es decir el "ENTER" (41 en UIQ).
            // Esto es útil en edición "inplace" aunque NO IMPORTANTE
            // pues la edición inplace NO es recomendada con <label>
            // pues usando sólo cursores falla en Opera Mobile 8.6x

            if ("click".equals(type) && target instanceof HTMLLabelElement)
            {
                final String methodName = "opera8MobFixLabelClick";
                if (!clientDoc.isClientMethodBounded(methodName))
                {
                    code.append( "var func = function (elem)" );
                    code.append( "{" );
                    code.append( "  if (elem.itsNatFixLabelClick) return;\n" );
                    code.append( "  var listener = function(evt)\n" );
                    code.append( "  {\n" );
                    code.append( "    if ((evt.keyCode!=13)&&(evt.keyCode!=41)) return;\n" ); // Botón central: 13 en caso de Windows CE, 41 en caso de Symbian (UIQ 3.1)
                    code.append( "    var elem = evt.currentTarget;\n" );
                    code.append( "    var evtTmp = itsNatDoc.doc.createEvent(\"MouseEvents\");\n" );
                    code.append( "    evtTmp.initEvent(\"click\",true,true);\n" );
                    code.append( "    elem.dispatchEvent(evtTmp);\n" );
                    code.append( "  };\n" );
                    code.append( "  elem.addEventListener(\"keypress\",listener,false);\n" ); // Mejor keypress que keyup, pues keyup en el botón central parece que es ignorado en Symbian 8.65 (probado en edit in place)
                    code.append( "  elem.itsNatFixLabelClick = true;\n" );
                    code.append( "};" );
                    code.append( "itsNatDoc." + methodName + " = func;\n" );

                    clientDoc.bindClientMethod(methodName);
                }

                code.append("itsNatDoc." + methodName + "(elem);\n");
            }

            return code.toString();
        }
        else return super.addItsNatDOMStdEventListenerCode(itsNatListener,clientDoc);
    }

}
