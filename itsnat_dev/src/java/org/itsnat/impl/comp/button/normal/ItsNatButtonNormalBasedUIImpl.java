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

package org.itsnat.impl.comp.button.normal;

import org.itsnat.comp.button.normal.ItsNatButtonNormal;
import org.itsnat.impl.comp.ItsNatElementComponentImpl;
import org.itsnat.impl.comp.button.ItsNatButtonBasedUIImpl;

/**
 * Los métodos isSelected,setSelected,getSelectedObjects de la clase base
 * no tienen ningún valor en los botones normales (que no se quedan pulsados)
 *
 * @author jmarranz
 */
public class ItsNatButtonNormalBasedUIImpl extends ItsNatButtonBasedUIImpl
{
    /**
     * Creates a new instance of ItsNatButtonNormalBasedUIImpl
     */
    public ItsNatButtonNormalBasedUIImpl(ItsNatButtonNormal parentComp)
    {
        super((ItsNatElementComponentImpl)parentComp);
    }

    public ItsNatButtonNormal getItsNatButtonNormal()
    {
        return (ItsNatButtonNormal)parentComp;
    }
}
