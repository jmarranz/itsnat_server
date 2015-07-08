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
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ElementTreeNodeNormalImpl extends ElementTreeNodeImpl
{
    /**
     * Creates a new instance of ElementTreeNodeNormalImpl
     */
    public ElementTreeNodeNormalImpl(ItsNatDocumentImpl itsNatDoc,ElementTreeNormalNodeListImpl parentList,int index,Element parentElement,Element childPatternElem,boolean clonePattern,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        super(itsNatDoc,parentList,index,parentElement,structure,renderer);

        // Si tiene algún nodo hijo dicho nodo es usado como patrón
        // y se impone al posible parámetro childPatternElem
        Element childListParentElem = getChildListElement();
        if (childListParentElem != null) // Si es null es que la estructura dice que este nodo no tiene hijos
        {
            Element childPatternElemInternal = ItsNatTreeWalker.getFirstChildElement(childListParentElem); // puede ser null, en ese caso el propio parent será el patrón
            if (childPatternElemInternal != null)
                childPatternElem = childPatternElemInternal;
            else
            {
                if (childPatternElem == null)
                {
                    childPatternElem = getParentElement();  // El propio nodo padre es el patrón para los hijos
                    clonePattern = true;
                }
            }

            this.childElemList = new ElementTreeNormalNodeListNormalImpl(itsNatDoc,this,childListParentElem,childPatternElem,clonePattern,removePattern,structure,renderer);
        }
    }

    public ElementTreeNodeListImpl getElementTreeNodeList()
    {
        return childElemList; // Puede ser null (no puede tener hijos)
    }

    public boolean isTreeTable()
    {
        return false;
    }
}
