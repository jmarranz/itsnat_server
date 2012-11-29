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

package org.itsnat.impl.core.event.client.domstd.w3c;

import java.util.HashMap;
import java.util.Map;

/**
 * Navegadores que soportan de alguna forma el KeyboardEvent de DOM-3
 * aunque ha de transformase en el ItsNatKeyEvent basado en FireFox
 *
 * Aunque se hable de Safari (WebKit en general) también es aplicable a BlackBerryOld
 *
 * @author jmarranz
 */
public class W3CKeyboardEventSharedImpl
{
    protected Map keyCodes = new HashMap();
    protected Map keyIdentifiers = new HashMap();

    public void addKeyCodeAndIdentifier(String keyIdentifier,int value)
    {
        Integer valueObj = new Integer(value);
        keyCodes.put(keyIdentifier,valueObj);
        keyIdentifiers.put(valueObj,keyIdentifier);
    }

    public String toKeyIdentifier(int keyCode)
    {
        return (String)keyIdentifiers.get(new Integer(keyCode));
    }

    public int toKeyCode(String keyIdentifier)
    {
        Integer keyCode = (Integer)keyCodes.get(keyIdentifier);
        if (keyCode != null)
            keyCode.intValue();

        // Se espera el formato: U+XXXX  donde XXXX es hexadecimal (16 bits)
        if (!keyIdentifier.startsWith("U+"))
            return 0; // No sabemos cual es, no provocamos error, que lo detecte el usuario

        keyIdentifier = keyIdentifier.substring(2);  // quitamos el U+
        return Integer.parseInt(keyIdentifier,16);
    }


    public int keyIdentifierToKeyCode(W3CKeyboardEventImpl event)
    {
        String keyIdentifier = getKeyIdentifier(event);
        return toKeyCode(keyIdentifier);
    }

    public int keyIdentifierToCharCode(W3CKeyboardEventImpl event)
    {
        char charCode = (char)keyIdentifierToKeyCode(event);
        // Este es el caso de evento creado con createEvent en JavaScript por el usuario
        // (si lo genera Safari ya nos da el charCode y si se genera desde el servidor se pasa el charCode de forma indirecta
        // no necesitándose el keyIdentifier para sacar el charCode).
        // Sólo disponemos de keyIdentifier y los flags concretamente shiftKey
        // El programador debería ser consciente en JavaScript de que es una simulación de tecla
        // usando shiftKey = true/false podemos calcular el carácter emitido
        // a partir del keyIdentifier/keyCode

        // El keyCode en el caso de una letra es la letra en mayúscula (ej A = 65), por tanto para obtener el charCode
        if (!event.getShiftKey())
            charCode = Character.toLowerCase(charCode);
        return charCode;
    }

    public int getCharCode(W3CKeyboardEventImpl event)
    {
        // Safari ofrece un atributo charCode readonly (realmente es un método)
        // por compatibilidad con FireFox, sin embargo si el evento se generó
        // via createEvent el charCode no es calculado y es cero (idem keyCode),
        // Si el evento proviene del servidor (server-sent) se utiliza un truco
        // para asociar un charCode al evento (con otro nombre) dicho charCode
        // lo recibiremos aquí. Si el evento fue creado por el usuario en JavaScript
        // via createEvent aparte de ItsNat entonces el charCode lo tendremos
        // que calcular a través de keyIdentifier y shiftKey
        // http://www.koders.com/cpp/fidCA6C5BC570CBFE6A97F78D2A65217CBECDB4ED88.aspx?s=PlatformKeyboardEvent#L27

        if (event.getType().equals("keypress"))
        {
            int charCode = event.getParameterInt("charCode");
            if (charCode == 0)
                charCode = keyIdentifierToCharCode(event);
            return charCode;
        }
        else
            return 0; // keydown y keyup
    }


    public int getKeyCode(W3CKeyboardEventImpl event)
    {
        if (event.getType().equals("keypress"))
            return 0;
        else
        {
            // keydown y keyup
            int keyCode = event.getParameterInt("keyCode");
            if (keyCode == 0)
                keyCode = keyIdentifierToKeyCode(event);
            return keyCode;
        }
    }

    public String getKeyIdentifier(W3CKeyboardEventImpl event)
    {
        return event.getParameter("keyIdentifier");
    }

    public int getKeyLocation(W3CKeyboardEventImpl event)
    {
        return event.getParameterInt("keyLocation");
    }

    public boolean getAltKey(W3CKeyboardEventImpl event)
    {
        return event.getParameterBoolean("altKey");
    }

    public boolean getCtrlKey(W3CKeyboardEventImpl event)
    {
        return event.getParameterBoolean("ctrlKey");
    }

    public boolean getMetaKey(W3CKeyboardEventImpl event)
    {
        return event.getParameterBoolean("metaKey");
    }

    public boolean getShiftKey(W3CKeyboardEventImpl event)
    {
        return event.getParameterBoolean("shiftKey");
    }

}
