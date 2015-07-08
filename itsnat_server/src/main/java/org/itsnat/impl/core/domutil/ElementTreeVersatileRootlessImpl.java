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

import org.itsnat.core.ItsNatException;
import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class ElementTreeVersatileRootlessImpl extends ElementTreeVersatileImpl
{
    protected ElementTreeNodeListImpl treeNodeList;

    /** Creates a new instance of ElementTreeVersatileRootlessImpl */
    public ElementTreeVersatileRootlessImpl(ItsNatDocumentImpl itsNatDoc,boolean treeTable,Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        ElementGroupManagerImpl factory = itsNatDoc.getElementGroupManagerImpl();
        this.treeNodeList = factory.createElementTreeNodeListInternal(treeTable,parentElement,removePattern,structure,renderer);
    }

    public boolean isRootless()
    {
        return true;
    }

    public int getRowCount()
    {
        return treeNodeList.getRowCount();
    }

    public ElementTreeNodeImpl getRootNode()
    {
        return null;
    }

    public ElementTreeNodeImpl addRootNode()
    {
        throw new ItsNatException("Rootless tree");
    }

    public void removeRootNode()
    {
        treeNodeList.removeAllTreeNodes();
    }

    public ElementTreeNodeImpl getElementTreeNodeFromRow(int row)
    {
        return treeNodeList.getElementTreeNodeFromRow(row);
    }

    public ElementTreeNodeImpl getElementTreeNodeFromNode(Node node)
    {
        return (ElementTreeNodeImpl)treeNodeList.getElementTreeNodeFromNode(node);
    }

    public ElementTreeNodeListImpl getChildListRootless()
    {
        return treeNodeList;
    }

    public boolean isUsePatternMarkupToRender()
    {
        return treeNodeList.isUsePatternMarkupToRender();
    }

    public void setUsePatternMarkupToRender(boolean value)
    {
        treeNodeList.setUsePatternMarkupToRender(value);
    }
}
