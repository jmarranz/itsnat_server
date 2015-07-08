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

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ElementTree;
import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class ElementTreeImpl implements ElementTree,Serializable
{
    protected ElementTreeNodeListImpl rootContainerList;  // Sirve para crear y borrar el único posible nodo: el root
    protected boolean usePatternMarkupToRender;

    /**
     * Creates a new instance of ElementTreeImpl
     */
    public ElementTreeImpl(ItsNatDocumentImpl itsNatDoc,boolean treeTable,Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        ElementGroupManagerImpl factory = itsNatDoc.getElementGroupManagerImpl();
        this.rootContainerList = factory.createElementTreeNodeListInternal(treeTable,parentElement,removePattern,structure,renderer);

        if (!removePattern && (rootContainerList.getLength() > 1)) // Si es 1 es el nodo patrón que pasa a ser el root pero no debería haber nada más
            throw new ItsNatException("A tree only can have a root node",this);

        this.usePatternMarkupToRender = itsNatDoc.isUsePatternMarkupToRender();
    }

    public boolean isUsePatternMarkupToRender()
    {
        return usePatternMarkupToRender;
    }

    public void setUsePatternMarkupToRender(boolean usePatternMarkupToRender)
    {
        this.usePatternMarkupToRender = usePatternMarkupToRender;
    }

    public Element getTreeContainerElement()
    {
        return rootContainerList.getTreeContainerElement();
    }

    public ItsNatDocument getItsNatDocument()
    {
        return rootContainerList.getItsNatDocument();
    }

    public Element getParentElement()
    {
        return rootContainerList.getParentElement();
    }

    public int getRowCount()
    {
        return rootContainerList.getRowCount();
    }

    public boolean hasTreeNodeRoot()
    {
        return !rootContainerList.isEmpty();
    }

    public ElementTreeNode getRootNode()
    {
        if (hasTreeNodeRoot())
            return rootContainerList.getFirstTreeNode();
        else
            return null;
    }

    public ElementTreeNode addRootNode()
    {
        if (hasTreeNodeRoot())
            throw new ItsNatException("Already has a root node",this);

        return rootContainerList.addTreeNode();
    }

    public ElementTreeNode addRootNode(Object value)
    {
        if (hasTreeNodeRoot())
            throw new ItsNatException("Already has a root node",this);

        return rootContainerList.addTreeNode(value);
    }

    public void removeRootNode()
    {
        if (!hasTreeNodeRoot())    // Ya está quitado
            return;

        rootContainerList.removeAllTreeNodes();
    }

    public Element getRootPatternElement()
    {
        return rootContainerList.getChildPatternElement();
    }

    public ElementTreeNode getElementTreeNodeFromNode(Node node)
    {
        return getElementTreeNodeFromNode(node,getTreeContainerElement());
    }

    public ElementTreeNode getElementTreeNodeFromNode(Node node,Element treeContainerElem)
    {
        ElementTreeNodeImpl rootNode = (ElementTreeNodeImpl)getRootNode();
        if (rootNode == null) return null;

        return rootNode.getElementTreeNodeFromNode(node,treeContainerElem);
    }

    public ElementTreeNode getElementTreeNodeFromPath(int[] path)
    {
        // No se usa, para un posible uso recuperar.
        ElementTreeNodeImpl rootNode = (ElementTreeNodeImpl)getRootNode();
        if (rootNode == null) return null;

        return rootNode.getElementTreeNodeFromPath(path);
    }

    public ElementTreeNode getElementTreeNodeFromPath(int[] path, int fromIndex)
    {
        // No se usa, para un posible uso recuperar.
        ElementTreeNodeImpl rootNode = (ElementTreeNodeImpl)getRootNode();
        if (rootNode == null) return null;

        return rootNode.getElementTreeNodeFromPath(path,fromIndex);
    }

    public ElementTreeNode getElementTreeNodeFromRow(int row)
    {
        ElementTreeNodeImpl rootNode = (ElementTreeNodeImpl)getRootNode();
        if (rootNode == null) return null; // No tiene root (el root es row = 0)

        if (row == 0)
            return rootNode;

        return rootNode.getElementTreeNodeFromRow(row);
    }

    public ElementTreeNodeRenderer getElementTreeNodeRenderer()
    {
        return rootContainerList.getElementTreeNodeRenderer();
    }

    public void setElementTreeNodeRenderer(ElementTreeNodeRenderer renderer)
    {
        rootContainerList.setElementTreeNodeRenderer(renderer);
    }

    public ElementTreeNodeStructure getElementTreeNodeStructure()
    {
        return rootContainerList.getElementTreeNodeStructure();
    }

    public void setElementTreeNodeStructure(ElementTreeNodeStructure structure)
    {
        rootContainerList.setElementTreeNodeStructure(structure);
    }

    public boolean containsUserValueName(String name)
    {
        return rootContainerList.containsUserValueName(name);
    }

    public Object removeUserValue(String name)
    {
        return rootContainerList.removeUserValue(name);
    }

    public Object getUserValue(String name)
    {
        return rootContainerList.getUserValue(name);
    }

    public Object setUserValue(String name, Object value)
    {
        return rootContainerList.setUserValue(name,value);
    }

    public String[] getUserValueNames()
    {
        return rootContainerList.getUserValueNames();
    }

}
