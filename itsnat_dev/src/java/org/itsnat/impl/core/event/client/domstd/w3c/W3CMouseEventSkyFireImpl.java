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

import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class W3CMouseEventSkyFireImpl extends W3CMouseEventImpl
{
    /**
     * Creates a new instance of W3CMouseEventImpl
     */
    public W3CMouseEventSkyFireImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public EventTarget getTarget()
    {
        // SkyFire v1.0 tiene un error raro en el evento click en <input type="checkbox">
        // resulta que cuando se pulsa este elemento se genera un evento con target=document
        // cuando debería ser el propio elemento input.
        // Detectamos este fallo al menos en el servidor, puesto que si este evento
        // se está despachando a este listener con currentTarget el <input> es que
        // el target DEBE ser el propio <input> puesto que un <input> no tiene hijos.
        // Esto sólo ocurre en el evento click, puesto que el mouseup y mousedown
        // no son disparados al menos en el caso de <input type="checkbox">
        EventTarget target = super.getTarget();
        EventTarget currTarget = getCurrentTarget();
        if (getType().equals("click") && (target instanceof Document) &&
           (currTarget instanceof Element) && DOMUtilHTML.isHTMLInputCheckBox((Element)currTarget))
            return currTarget;
        else
            return target;
    }
}
