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

package org.itsnat.impl.comp.button.toggle;

import org.itsnat.comp.button.toggle.ItsNatButtonCheckBox;
import org.itsnat.comp.button.toggle.ItsNatFreeCheckBox;
import org.itsnat.impl.comp.button.toggle.ItsNatFreeCheckBoxImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatFreeCheckBoxUIImpl extends ItsNatFreeButtonToggleUIImpl
{

    /**
     * Creates a new instance of ItsNatFreeCheckBoxUIImpl
     */
    public ItsNatFreeCheckBoxUIImpl(ItsNatFreeCheckBoxImpl parentComp)
    {
        super(parentComp);
    }

    public ItsNatButtonCheckBox getItsNatButtonCheckBox()
    {
        return (ItsNatButtonCheckBox)parentComp;
    }

    public ItsNatFreeCheckBox getItsNatFreeCheckBox()
    {
        return (ItsNatFreeCheckBox)parentComp;
    }

}
