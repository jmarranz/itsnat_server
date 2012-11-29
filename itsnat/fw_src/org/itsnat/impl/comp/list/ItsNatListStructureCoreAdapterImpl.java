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
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ElementListStructure;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatListStructureCoreAdapterImpl implements ElementListStructure,Serializable
{
    protected ItsNatListStructure structure;
    protected ItsNatList listComp;

    /**
     * Creates a new instance of ItsNatListStructureCoreAdapterImpl
     */
    public ItsNatListStructureCoreAdapterImpl(ItsNatListStructure structure,ItsNatList listComp)
    {
        this.structure = structure;
        this.listComp = listComp;
    }

    public Element getContentElement(ElementList list, int index, Element elem)
    {
        return structure.getContentElement(listComp,index,elem);
    }

}
