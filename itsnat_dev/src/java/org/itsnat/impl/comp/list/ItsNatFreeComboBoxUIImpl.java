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

package org.itsnat.impl.comp.list;

import org.itsnat.comp.list.ItsNatComboBox;


/**
 *
 * @author jmarranz
 */
public class ItsNatFreeComboBoxUIImpl extends ItsNatFreeListUIImpl implements ItsNatComboBoxUIInternal
{
    /**
     * Creates a new instance of ItsNatFreeComboBoxUIImpl
     */
    public ItsNatFreeComboBoxUIImpl(ItsNatFreeComboBoxImpl parentComp)
    {
        super(parentComp);
    }

    public void setSelectedIndex(int index)
    {
        // Nada que hacer, no tiene reflejo en el DOM
    }

    public ItsNatComboBox getItsNatComboBox()
    {
        return (ItsNatComboBox)parentComp;
    }
}
