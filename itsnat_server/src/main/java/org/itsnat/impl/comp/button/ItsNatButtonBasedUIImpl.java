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

import org.itsnat.comp.button.ItsNatButton;
import org.itsnat.comp.button.ItsNatButtonUI;
import org.itsnat.impl.comp.ItsNatElementComponentImpl;
import org.itsnat.impl.comp.ItsNatElementComponentUIImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatButtonBasedUIImpl extends ItsNatElementComponentUIImpl implements ItsNatButtonUI
{
    /**
     * Creates a new instance of ItsNatButtonBasedUIImpl
     */
    public ItsNatButtonBasedUIImpl(ItsNatElementComponentImpl parentComp)
    {
        super(parentComp);
    }

    public ItsNatButton getItsNatButton()
    {
        return (ItsNatButton)parentComp;
    }

}
