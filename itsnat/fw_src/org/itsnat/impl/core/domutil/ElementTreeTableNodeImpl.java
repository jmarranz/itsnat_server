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

package org.itsnat.impl.core.domutil;

import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ElementTreeTableNodeImpl extends ElementTreeNodeImpl
{

    /**
     * Creates a new instance of ElementTreeTableNodeImpl
     */
    public ElementTreeTableNodeImpl(ItsNatDocumentImpl itsNatDoc,ElementTreeTableNodeListImpl parentList,int index,Element parentElement,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        super(itsNatDoc,parentList,index,parentElement,structure,renderer);

        this.childElemList = new ElementTreeTableNodeListNormalImpl(itsNatDoc,getGlobalChildList(),this,structure,renderer);
    }

    public boolean isTreeTable()
    {
        return true;
    }

    public ElementListImpl getGlobalChildList()
    {
        return getElementTreeTableNodeList().getGlobalChildList();
    }

    public ElementTreeTableNodeListImpl getElementTreeTableNodeList()
    {
        return (ElementTreeTableNodeListImpl)parentList;
    }

    public ElementTreeNodeListImpl getElementTreeNodeList()
    {
        return childElemList;
    }
}
