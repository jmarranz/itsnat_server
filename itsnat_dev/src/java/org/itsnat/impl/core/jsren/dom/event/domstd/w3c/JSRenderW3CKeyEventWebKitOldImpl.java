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

import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class JSRenderW3CKeyEventWebKitOldImpl extends JSRenderW3CKeyEventWebKitImpl
{
    public static final JSRenderW3CKeyEventWebKitOldImpl SINGLETON = new JSRenderW3CKeyEventWebKitOldImpl();

    /**
     * Creates a new instance of JSWebKitDefaultKeyEventRenderImpl
     */
    public JSRenderW3CKeyEventWebKitOldImpl()
    {
    }

    public boolean needsAuxiliarCharCode()
    {
        return false;
    }

    protected String getEventTypeGroup(Event evt)
    {
        // Admite el nombre "KeyboardEvents" (en plural) que crea un objeto key event sin embargo no implementa
        // initKeyBoardEvent, por lo que es contraproducente porque las propiedades
        // keyIdentifier etc están ya definidas pero sólo como lectura, en definitiva está
        // medio hecho.

        // Podríamos crear el objecto con "Event" o "Events" pero ambos no valen,
        // si creamos con UIEvent (pues el key event es un UIEvent) produce que keyCode y charCode sean de sólo lectura
        // Un truco SUCIO pero que ¡¡funciona con dispatchEvent en todos los S60WebKit!! es usar
        // "HTMLEvents" aunque en teoría es erróneo luego usar iniEvent con "keypress" etc.

        return "HTMLEvents"; // ¡¡NO es un error ver notas!!
    }

    public String getInitKeyboardEvent(StringBuffer code,ItsNatKeyEvent keyEvt,String evtVarName,String keyIdentifier,int keyLocation,int keyCode,ClientDocumentStfulImpl clientDoc)
    {
        // http://webkit.org/docs/a00095.html NO se cumple, es más moderno que la especificación de S60WebKit.

        // Leer notas de JSRenderW3CKeyEventWebKitDefaultImpl, algunas cosas son aplicables
        // aquí.

        code.append( getInitEventDefault(keyEvt,evtVarName,clientDoc) );

        code.append( evtVarName + ".view=" + getViewPath(keyEvt.getView(),clientDoc) + ";\n" );
        code.append( evtVarName + ".keyIdentifier=\"" + keyIdentifier + "\";\n" );
        code.append( evtVarName + ".keyLocation=" + keyLocation + ";\n" );
        code.append( evtVarName + ".altKey=" + keyEvt.getAltKey() + ";\n" );
        code.append( evtVarName + ".ctrlKey=" + keyEvt.getCtrlKey() + ";\n" );
        code.append( evtVarName + ".shiftKey=" + keyEvt.getShiftKey() + ";\n" );
        code.append( evtVarName + ".metaKey=" + keyEvt.getMetaKey() + ";\n" );
        code.append( evtVarName + ".altGraph=false;\n" );

        code.append( evtVarName + ".keyCode=" + keyEvt.getKeyCode() + ";\n" ); // Estrictamente no es necesario pues se puede obtener de keyIdentifier
        code.append( evtVarName + ".charCode=" + keyEvt.getCharCode() + ";\n" );  // Evitamos así la necesidad de itsnat_charCode

        return code.toString();
    }
}
