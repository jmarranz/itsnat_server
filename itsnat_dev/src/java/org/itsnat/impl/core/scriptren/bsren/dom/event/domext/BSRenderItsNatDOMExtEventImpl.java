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
package org.itsnat.impl.core.scriptren.bsren.dom.event.domext;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatDOMExtEvent;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.impl.core.scriptren.bsren.dom.event.BSRenderEventImpl;
import org.itsnat.impl.core.scriptren.bsren.dom.event.BSRenderNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public abstract class BSRenderItsNatDOMExtEventImpl extends BSRenderNormalEventImpl
{

    /** Creates a new instance of JSRenderItsNatDOMExtEventImpl */
    public BSRenderItsNatDOMExtEventImpl()
    {
    }

    public static BSRenderItsNatDOMExtEventImpl getBSRenderItsNatDOMExtEvent(ItsNatDOMExtEvent event)
    {
        if (event instanceof ItsNatUserEvent)
            return BSRenderItsNatUserEventImpl.SINGLETON;
        else
            throw new ItsNatException("This event type is not supported",event);
    }
}
