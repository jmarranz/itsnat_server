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

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.event.client.dom.domstd.NodeContainerImpl;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;

/**
 *
 * @author jmarranz
 */
public class MSIEOldMouseEventImpl extends MSIEOldUIEventImpl implements MouseEvent
{
    protected NodeContainerImpl relatedTarget;

    /**
     * Creates a new instance of MSIEOldMouseEventImpl
     */
    public MSIEOldMouseEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public void initMouseEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, AbstractView viewArg, int detailArg, int screenXArg, int screenYArg, int clientXArg, int clientYArg, boolean ctrlKeyArg, boolean altKeyArg, boolean shiftKeyArg, boolean metaKeyArg, short buttonArg, EventTarget relatedTargetArg)
    {
        throw new ItsNatException("Not implemented",this);
    }
/*
    public EventTarget getCurrentTarget()
    {
        String type = getType();
        if (type.equals("mouseover"))
            return (EventTarget)originalEvt.getToElement();
        else
            return super.getCurrentTarget();
    }
*/
    public int getDetail()
    {
        if (this.detail == 0) // No ha sido inicializado
        {
            // Es lo más aproximado porque salvo el propio dblclick no sabemos si ha habido un doble click y estamos procesando el segundo click
            if (getType().equals("dblclick"))
                this.detail = 2; // Dos clicks
            else
                this.detail = 1;
        }
        return super.getDetail();
    }

    public int getScreenX()
    {
        return originalEvt.getScreenX();
    }

    public int getScreenY()
    {
        return originalEvt.getScreenY();
    }

    public int getClientX()
    {
        return originalEvt.getClientX();
    }

    public int getClientY()
    {
        return originalEvt.getClientY();
    }

    public boolean getCtrlKey()
    {
        return originalEvt.getCtrlKey();
    }

    public boolean getShiftKey()
    {
        return originalEvt.getShiftKey();
    }

    public boolean getAltKey()
    {
        return originalEvt.getAltKey();
    }

    public boolean getMetaKey()
    {
        return false; // Ver el idem en MSIEKeyEventImpl
    }

    public short getButton()
    {
        short button = originalEvt.getButton();
        // Los valores de button de MSIE no son los estándar
        // como es un evento de ratón nunca será cero
        switch(button)
        {
            case 1: return 0; // botón izquierdo
            case 2: return 2; // botón derecho
            case 4: return 1; // botón de enmedio
        }

        return button; // Combinaciones de varios botones pulsados, DOM level 3 lo menciona pero no estandariza los valores exactos
    }

    public EventTarget getRelatedTarget()
    {
        if (relatedTarget == null)
        {
            // Existe en el caso de mouseover/mouseout
            Node target;
            String type = getType();
            if (type.equals("mouseover"))
                target = originalEvt.getFromElement();
            else if (type.equals("mouseout"))
                target = originalEvt.getToElement();
            else
                target = null;

            this.relatedTarget = new NodeContainerImpl(target);
        }
        return (EventTarget)relatedTarget.get();
    }

    public void resolveNodePaths()
    {
        super.resolveNodePaths();

        getRelatedTarget();
    }
}
