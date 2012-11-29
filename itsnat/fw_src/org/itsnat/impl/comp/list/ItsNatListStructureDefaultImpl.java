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

import java.io.Serializable;
import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.impl.core.domutil.ElementListStructureDefaultImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatListStructureDefaultImpl implements ItsNatListStructure,Serializable
{
    protected final static ItsNatListStructureDefaultImpl SINGLETON = new ItsNatListStructureDefaultImpl();

    /** Creates a new instance of ItsNatListStructureDefaultImpl */
    private ItsNatListStructureDefaultImpl()
    {
    }

    public static ItsNatListStructureDefaultImpl newItsNatListStructureDefault()
    {
        // No se guarda estado, usamos el SINGLETON en este falso método factoría
        return SINGLETON;
    }

    public Element getContentElement(ItsNatList list, int index, Element parentElem)
    {
        if (parentElem == null) parentElem = list.getItsNatListUI().getElementAt(index);
        return ElementListStructureDefaultImpl.getContentElement(index,parentElem);
    }

}
