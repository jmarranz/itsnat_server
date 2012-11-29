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

import org.itsnat.comp.list.ItsNatListCellUI;
import org.itsnat.comp.list.ItsNatListUI;
import org.itsnat.impl.core.domutil.ListElementInfoMasterImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatListCellUIImpl extends ItsNatListBasedCellUIImpl implements ItsNatListCellUI
{
    /**
     * Creates a new instance of ItsNatListCellUIImpl
     */
    public ItsNatListCellUIImpl(ListElementInfoMasterImpl elementInfo,ItsNatListUIImpl listUI)
    {
        super(elementInfo,listUI);
    }

    public ItsNatListUI getItsNatListUI()
    {
        return (ItsNatListUI)listUI;
    }

    public Element getContentElement()
    {
        return getItsNatListUI().getContentElementAt(getIndex());
    }
}
