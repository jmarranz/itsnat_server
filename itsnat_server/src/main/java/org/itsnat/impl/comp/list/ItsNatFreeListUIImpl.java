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

import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.comp.list.ItsNatFreeList;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatFreeListUIImpl extends ItsNatListUIImpl
{
    protected boolean enabled = true;

    /**
     * Creates a new instance of ItsNatFreeListUIImpl
     */
    public ItsNatFreeListUIImpl(ItsNatFreeListImpl parentComp)
    {
        super(parentComp);

        Element parentElement = getElement();

        ItsNatListStructure structure = parentComp.getItsNatListStructure();
        ItsNatListStructureCoreAdapterImpl structAdapter;
        structAdapter = new ItsNatListStructureCoreAdapterImpl(structure,parentComp);

        ItsNatDocumentImpl itsNatDoc = getItsNatDocumentImpl();
        this.elementList = itsNatDoc.getElementGroupManagerImpl().createElementListInternal(parentElement,true,structAdapter,null);
    }

    public ItsNatFreeList getItsNatFreeListBase()
    {
        return (ItsNatFreeList)parentComp;
    }

    public Element getContentElementAt(int index,Element optionElem)
    {
        ItsNatFreeList list = getItsNatFreeListBase();
        return list.getItsNatListStructure().getContentElement(list,index,optionElem);
    }

    public void setEnabled(boolean b)
    {
        this.enabled = b;
    }

    public boolean isEnabled()
    {
        return enabled;
    }
}
