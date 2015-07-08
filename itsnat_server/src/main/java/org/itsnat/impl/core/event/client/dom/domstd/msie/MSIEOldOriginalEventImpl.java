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

package org.itsnat.impl.core.event.client.dom.domstd.msie;

import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class MSIEOldOriginalEventImpl
{
    /*
    http://msdn.microsoft.com/library/default.asp?url=/workshop/author/dhtml/reference/events.asp
    http://msdn.microsoft.com/library/default.asp?url=/workshop/author/dhtml/reference/objects.asp

    http://msdn.microsoft.com/workshop/browser/mshtml/reference/ifaces/eventobj/eventobj.asp
    http://msdn.microsoft.com/workshop/browser/mshtml/reference/ifaces/eventobj2/eventobj2.asp
    http://msdn.microsoft.com/workshop/browser/mshtml/reference/ifaces/eventobj3/ihtmleventobj3.asp
    http://msdn.microsoft.com/workshop/browser/mshtml/reference/ifaces/eventobj4/ihtmleventobj4.asp

    Lista completa de atributos:

    altKey, altLeft, button, clientX, clientY, ctrlKey, ctrlLeft, dataFld,
    offsetX, offsetY, propertyName, qualifier, reason, repeat, screenX,
    screenY, shiftKey, shiftLeft, srcUrn, type, x, y
    returnValue, cancelBubble, keyCode, fromElement, toElement, button
    offsetX, offsetY, srcFilter, dataTransfer, contentOverflow, behaviorCookie
    behaviorPart, nextPage, wheelDelta

    Atributos presentes pero no documentados por Microsoft:

    imeCompositionChange,imeNotifyCommand,imeNotifyData,imeRequest,imeRequestData
    keyboardLayout
    **/

    protected MSIEOldEventImpl event;

    /** Creates a new instance of MSIEOldOriginalEventImpl */
    public MSIEOldOriginalEventImpl(MSIEOldEventImpl event)
    {
        this.event = event;
    }

    public boolean getAltKey()
    {
        return event.getParameterBoolean("altKey");
    }

    public short getButton()
    {
        return event.getParameterShort("button");
    }

    public boolean getCancelBubble()
    {
        return event.getParameterBoolean("cancelBubble");
    }

    public int getClientX()
    {
        return event.getParameterInt("clientX");
    }

    public int getClientY()
    {
        return event.getParameterInt("clientY");
    }

    public boolean getCtrlKey()
    {
        return event.getParameterBoolean("ctrlKey");
    }

    public Node getFromElement()
    {
        return event.getParameterNode("fromElement");
    }

    public int getKeyCode()
    {
        return event.getParameterInt("keyCode");
    }

    public int getOffsetX()
    {
        return event.getParameterInt("offsetX");
    }

    public int getOffsetY()
    {
        return event.getParameterInt("offsetY");
    }

    public String getQualifier()
    {
        return event.getParameter("qualifier");
    }

    public int getReason()
    {
        return event.getParameterInt("reason");
    }

    public boolean getReturnValue()
    {
        // En realidad debería ser Object (Variant) pero su uso habitual es true/false como únicos valores
        // returnValue es especial porque es una propiedad del evento
        // que puede no existir pero que si se define tiene significado
        // para MSIE
        String value = event.getParameter("returnValue");
        if (value.equals("undefined"))
            return true; // No está definida por lo que el retorno de true suele significa que se siga con el comportamiento normal
        else
            return Boolean.valueOf(value).booleanValue(); // Si no es "true" devuelve false, admitiendo por tanto cualquier valor.
    }

    public int getScreenX()
    {
        return event.getParameterInt("screenX");
    }

    public int getScreenY()
    {
        return event.getParameterInt("screenY");
    }

    public boolean getShiftKey()
    {
        return event.getParameterBoolean("shiftKey");
    }

    public Node getSrcElement()
    {
        return event.getParameterNode("srcElement");
    }

    public Node getSrcFilter()
    {
        return event.getParameterNode("srcFilter");
    }

    public Node getToElement()
    {
        return event.getParameterNode("toElement");
    }

    public String getType()
    {
        return event.getParameter("type");
    }

    public int getX()
    {
        return event.getParameterInt("x");
    }

    public int getY()
    {
        return event.getParameterInt("y");
    }

}
