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

import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.event.client.dom.domstd.w3c.W3CKeyboardEventImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderW3CKeyboardEventImpl extends JSRenderW3CKeyEventImpl
{
    /**
     * Creates a new instance of JSWebKitDefaultKeyEventRenderImpl
     */
    public JSRenderW3CKeyboardEventImpl()
    {
    }

    public abstract String toKeyIdentifierByBrowser(int keyCode);

    public String toKeyIdentifier(int keyCode)
    {
        String keyIdentifier = toKeyIdentifierByBrowser(keyCode);
        if (keyIdentifier != null)
            return keyIdentifier;
        // Formato: U+XXXX  donde XXXX es hexadecimal
        keyIdentifier = Integer.toString(keyCode,16);
        if (keyIdentifier.length() < 4)
        {
            int len = keyIdentifier.length();
            for(int i = 1; i <= 4 - len; i++)
                keyIdentifier = '0' + keyIdentifier;
        }
        keyIdentifier = "U+" + keyIdentifier;
        return keyIdentifier;
    }

    public abstract boolean needsAuxiliarCharCode();

    public int renderCharCodeCalcKeyCode(ItsNatKeyEvent keyEvt,String evtVarName,StringBuilder code)
    {
        int keyCode;
        if (keyEvt.getType().equals("keypress"))
        {
            int charCode = keyEvt.getCharCode();
            if (needsAuxiliarCharCode())
                code.append(evtVarName + ".itsnat_charCode = " + charCode + ";\n");

            // Cuando se pulsa la tecla 'a' el keyCode es el código ASCII de la 'A'
            if (Character.isLowerCase((char)charCode))
                keyCode = Character.toUpperCase((char)charCode);
            else
                keyCode = charCode;
        }
        else // keydown y keyup
        {
            keyCode = keyEvt.getKeyCode();
        }

        return keyCode;
    }

    @Override    
    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        ItsNatKeyEvent keyEvt = (ItsNatKeyEvent)evt;

        StringBuilder code = new StringBuilder();

        String keyIdentifier;
        int keyLocation;
        int keyCode = renderCharCodeCalcKeyCode(keyEvt,evtVarName,code);

        if (keyEvt instanceof W3CKeyboardEventImpl) // Evento del cliente
        {
            W3CKeyboardEventImpl dom3KeyEvt = (W3CKeyboardEventImpl)keyEvt;
            keyIdentifier = dom3KeyEvt.getKeyIdentifier();
            keyLocation = dom3KeyEvt.getKeyLocation();
        }
        else // Del servidor
        {
            keyIdentifier = toKeyIdentifier(keyCode);
            keyLocation = 0; // No hay manera de saberlo.
        }

        return getInitKeyboardEvent(code,keyEvt,evtVarName,keyIdentifier,keyLocation,keyCode,clientDoc);
    }

    public abstract String getInitKeyboardEvent(StringBuilder code,ItsNatKeyEvent keyEvt,String evtVarName,String keyIdentifier,int keyLocation,int keyCode,ClientDocumentStfulDelegateWebImpl clientDoc);

}
