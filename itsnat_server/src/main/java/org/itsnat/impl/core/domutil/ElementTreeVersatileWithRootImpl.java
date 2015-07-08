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
public class ElementTreeVersatileWithRootImpl extends ElementTreeVersatileImpl
{
    protected ElementTreeImpl tree;

    /** Creates a new instance of ElementTreeVersatileWithRootImpl */
    public ElementTreeVersatileWithRootImpl(ItsNatDocumentImpl itsNatDoc,boolean treeTable,Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        ElementGroupManagerImpl factory = itsNatDoc.getElementGroupManagerImpl();
        this.tree = factory.createElementTreeInternal(treeTable,parentElement,removePattern,structure,renderer);
    }

    public boolean isRootless()
    {
        return false;
    }

    public int getRowCount()
    {
        return tree.getRowCount();
    }

    public ElementTreeNodeImpl getRootNode()
    {
        return (ElementTreeNodeImpl)tree.getRootNode();
    }

    public ElementTreeNodeImpl addRootNode()
    {
        return (ElementTreeNodeImpl)tree.addRootNode();
    }

    public void removeRootNode()
    {
        tree.removeRootNode();
    }

    public ElementTreeNodeImpl getElementTreeNodeFromRow(int row)
    {
        return (ElementTreeNodeImpl)tree.getElementTreeNodeFromRow(row);
    }

    public ElementTreeNodeImpl getElementTreeNodeFromNode(Node node)
    {
        return (ElementTreeNodeImpl)tree.getElementTreeNodeFromNode(node);
    }

    public ElementTreeNodeListImpl getChildListRootless()
    {
        throw new ItsNatException("Tree with root");
    }

    public boolean isUsePatternMarkupToRender()
    {
        return tree.isUsePatternMarkupToRender();
    }

    public void setUsePatternMarkupToRender(boolean value)
    {
        tree.setUsePatternMarkupToRender(value);
    }
}
