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

package org.itsnat.impl.core.scriptren.jsren.dom.event.domstd.w3c;

import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.event.client.domstd.w3c.BlackBerryOldKeyEventImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class JSRenderW3CKeyEventBlackBerryOldImpl extends JSRenderW3CKeyboardEventImpl
{
    public static final JSRenderW3CKeyEventBlackBerryOldImpl SINGLETON = new JSRenderW3CKeyEventBlackBerryOldImpl();

    /**
     * Creates a new instance of JSWebKitDefaultKeyEventRenderImpl
     */
    public JSRenderW3CKeyEventBlackBerryOldImpl()
    {
    }

    public boolean needsAuxiliarCharCode()
    {
        return true;
    }

    public String toKeyIdentifierByBrowser(int keyCode)
    {
        return BlackBerryOldKeyEventImpl.toKeyIdentifier(keyCode);
    }

    protected String getEventTypeGroup(Event evt)
    {
        return "KeyboardEvents"; // "KeyboardEvent" NO VALE, es con "s" al final
    }

    @Override    
    public String getInitKeyboardEvent(StringBuilder code,ItsNatKeyEvent keyEvt,String evtVarName,String keyIdentifier,int keyLocation,int keyCode,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // La documentación oficial (JDE 4.6) documenta initKeyboardEvent como:
        // KeyboardEvent.initKeyboardEvent( typeArg, canBubbleArg, cancelableArg, viewArg,
        //      keyIdentifierArg, keyLocationArg, modifiersList )
        // Esta versión es la última del DOM 3 salvo keyLocationArg que
        // en DOM 3 es un entero y BlackBerry documenta como String, pero
        // es una herrata de la documentación porque creando un evento
        // y haciendo "typeof evt.keyLocation" devuelve un number.
        // Aparte del error tonto de viewArg que debe ser null (caso de
        // todos los eventos derivados UIEvent), la implementación es
        // DIFERENTE a la documentación y al DOM 3: HAY UN PARAMETRO MÁS
        // Esto lo adiviné llamando y capturando el error (try/catch) y mostrando el objeto exception con
        // un alert, el cual decía que se esperaban 8 parámetros.
        // La verdadera interface es:
        // initKeyboardEvent( keyLocationArg, keyCodeArg, modifiersList )
        // Este keyCodeArg define la propiedad "keyCode" que es un entero.
        // Como este error se solucionará (el keyCode puede obtenerse del keyIdentifier pasado)
        // usamos las dos versiones para evitar
        // que una versión posterior del motor nos haga fallar.

        StringBuilder modifiersListArg = new StringBuilder();
        if (keyEvt.getCtrlKey())  addModifier("Control",modifiersListArg);
        if (keyEvt.getAltKey())   addModifier("Alt",modifiersListArg);
        if (keyEvt.getShiftKey()) addModifier("Shift",modifiersListArg);
        if (keyEvt.getMetaKey())  addModifier("Meta",modifiersListArg);
        // AltGraph no podemos obtenerlo

        code.append("try {" );
        // Sin el parámetro keyCode
        code.append( evtVarName + ".initKeyboardEvent("
                + "\"" + keyEvt.getType() + "\","
                + keyEvt.getBubbles() + ","
                + keyEvt.getCancelable() + ","
                + getViewPath(keyEvt.getView(),clientDoc) + ","
                + "\"" + keyIdentifier + "\","
                + keyLocation + ","
                + "\"" + modifiersListArg + "\""
                + ");\n" );
        code.append("}catch(e){" );
        // Con el parámetro keyCode
        code.append( evtVarName + ".initKeyboardEvent("
                + "\"" + keyEvt.getType() + "\","
                + keyEvt.getBubbles() + ","
                + keyEvt.getCancelable() + ","
                + getViewPath(keyEvt.getView(),clientDoc) + ","
                + "\"" + keyIdentifier + "\","
                + keyLocation + ","
                + keyCode + ","
                + "\"" + modifiersListArg + "\""
                + ");\n" );
        code.append("}");

        return code.toString();
    }


    public static void addModifier(String name,StringBuilder code)
    {
        if (code.length() > 0) code.append(" " + name);
        else code.append(name);
    }
}
