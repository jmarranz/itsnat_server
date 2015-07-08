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

package org.itsnat.impl.comp.button;

import java.io.Serializable;
import org.itsnat.impl.comp.ItsNatHTMLFormCompValueBasedImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLFormCompButtonSharedImpl implements Serializable
{
    protected ItsNatHTMLFormCompValueBasedImpl comp;

    /**
     * Creates a new instance of ItsNatHTMLFormCompButtonSharedImpl
     */
    public ItsNatHTMLFormCompButtonSharedImpl(ItsNatHTMLFormCompValueBasedImpl comp)
    {
        this.comp = comp;
    }

    public boolean handleEvent(Event evt)
    {
        // En los botones toggle la pulsación supone que el modelo cambie la selección, evitamos de
        // la siguiente manera que el cambio que se produjo ya en el cliente se propague de nuevo al cliente por mutation events
        // En los botones normales no hará para nada
        boolean res;

        ItsNatButtonInternal compButton = (ItsNatButtonInternal)comp;

        comp.setServerUpdatingFromClient(true);

        try
        {
            res = compButton.getItsNatButtonShared().handleEvent(evt);
        }
        finally
        {
            comp.setServerUpdatingFromClient(false);
        }

        return res;
    }
}
