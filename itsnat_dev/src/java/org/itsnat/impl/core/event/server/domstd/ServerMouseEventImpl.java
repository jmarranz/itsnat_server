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

package org.itsnat.impl.core.event.server.domstd;

import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;

/**
 *
 * @author jmarranz
 */
public class ServerMouseEventImpl extends ServerUIEventImpl implements MouseEvent
{
    protected int screenX;
    protected int screenY;
    protected int clientX;
    protected int clientY;
    protected boolean ctrlKey;
    protected boolean altKey;
    protected boolean shiftKey;
    protected boolean metaKey;
    protected short button;
    protected EventTarget relatedTarget;

    /** Creates a new instance of ServerMouseEventImpl */
    public ServerMouseEventImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public int getScreenX()
    {
        return screenX;
    }

    public int getScreenY()
    {
        return screenY;
    }

    public int getClientX()
    {
        return clientX;
    }

    public int getClientY()
    {
        return clientY;
    }

    public boolean getCtrlKey()
    {
        return ctrlKey;
    }

    public boolean getShiftKey()
    {
        return shiftKey;
    }

    public boolean getAltKey()
    {
        return altKey;
    }

    public boolean getMetaKey()
    {
        return metaKey;
    }

    public short getButton()
    {
        return button;
    }

    public EventTarget getRelatedTarget()
    {
        return relatedTarget;
    }

    public void initMouseEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, AbstractView viewArg, int detailArg, int screenXArg, int screenYArg, int clientXArg, int clientYArg, boolean ctrlKeyArg, boolean altKeyArg, boolean shiftKeyArg, boolean metaKeyArg, short buttonArg, EventTarget relatedTargetArg)
    {
        super.initUIEvent(typeArg,canBubbleArg,cancelableArg,viewArg,detailArg);

        this.screenX = screenXArg;
        this.screenY = screenYArg;
        this.clientX = clientXArg;
        this.clientY = clientYArg;
        this.ctrlKey = ctrlKeyArg;
        this.altKey = altKeyArg;
        this.shiftKey = shiftKeyArg;
        this.metaKey = metaKeyArg;
        this.button = buttonArg;
        this.relatedTarget = relatedTargetArg;
    }

}
