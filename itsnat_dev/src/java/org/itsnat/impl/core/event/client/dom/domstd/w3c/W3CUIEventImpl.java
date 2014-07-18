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

package org.itsnat.impl.core.event.client.dom.domstd.w3c;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;

/**
 *
 * @author jmarranz
 */
public abstract class W3CUIEventImpl extends W3CEventImpl implements UIEvent
{

    /**
     * Creates a new instance of W3CUIEventImpl
     */
    public W3CUIEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public void initUIEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, AbstractView viewArg, int detailArg)
    {
        throw new ItsNatException("Not implemented",this);
    }

    public boolean isCacheIfPossibleTarget()
    {
        // No hay problemas de eliminación de nodos en el cliente antes
        // que en el servidor (cosa de los mutation events)
        return true;
    }
    
    public AbstractView getView()
    {
        return getAbstractView();
    }

    public int getDetail()
    {
        return getParameterInt("detail");
    }

}
