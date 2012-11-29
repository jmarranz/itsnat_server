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

package org.itsnat.impl.comp.tree;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.itsnat.comp.tree.ItsNatTreeCellUI;
import org.itsnat.comp.tree.ItsNatTreeUI;
import org.itsnat.impl.core.ItsNatUserDataImpl;
import org.itsnat.impl.core.domutil.ElementTreeNodeImpl;
import org.itsnat.impl.core.util.*;
import org.w3c.dom.Element;

/**
 * En cierto modo el par ItsNatTreeCellUI/Impl es redundante pues se podría
 * usar ElementTreeNode directanemte sin embargo nos sirve
 * para sacar información del ElementTreeNode sin exponer
 * métodos que puedan modificar el árbol, esto en los ItsNatTree es
 * fundamental pues ha de modificarse el árbol (nuevos items etc)
 * a través de los métodos normales de ItsNatTree, TreeModel etc,
 * en ese contexto ItsNatTreeCellUI nos ofrece info del nodo a modo de "sólo lectura"
 * NO debe usarse el ItsNatTreeCellUI obtenido antes de un cambio en la estructura
 *
 *
 *
 *
 *
 *
 * @author jmarranz
 */
public class ItsNatTreeCellUIImpl extends ItsNatUserDataImpl implements ItsNatTreeCellUI
{
    protected ElementTreeNodeImpl treeNode;
    protected ItsNatTreeUIImpl treeUI;
    protected boolean expandState = true;

    /**
     * Creates a new instance of ItsNatTreeCellUIImpl
     */
    private ItsNatTreeCellUIImpl(ItsNatTreeUIImpl treeUI,ElementTreeNodeImpl treeNode)
    {
        super(false);

        this.treeUI = treeUI;
        this.treeNode = treeNode;
    }

    public static ItsNatTreeCellUIImpl getItsNatTreeCellUI(ItsNatTreeUIImpl treeUI,ElementTreeNodeImpl treeNode)
    {
        if (treeNode == null)
            return null;  // puede ser el root y modo rootless
        ItsNatTreeCellUIImpl treeNodeUI = (ItsNatTreeCellUIImpl)treeNode.getAuxObject();
        if (treeNodeUI == null)
        {
            treeNodeUI = new ItsNatTreeCellUIImpl(treeUI,treeNode);
            treeNode.setAuxObject(treeNodeUI);
        }
        return treeNodeUI;
    }

    public ItsNatTreeUI getItsNatTreeUI()
    {
        return treeUI;
    }

    public ElementTreeNodeImpl getElementTreeNode()
    {
        // No publicar
        return treeNode;
    }

    public ItsNatTreeCellUI getTreeNodeUIParent()
    {
        ElementTreeNodeImpl treeNodeParent = (ElementTreeNodeImpl)treeNode.getElementTreeNodeParent();
        return getItsNatTreeCellUI(treeUI,treeNodeParent);
    }

    public Element getParentElement()
    {
        return treeNode.getParentElement();
    }

    public Element getContentElement()
    {
        return treeUI.getContentElement(treeNode);
    }

    public Element getHandleElement()
    {
        return treeUI.getHandleElement(treeNode);
    }

    public Element getIconElement()
    {
        return treeUI.getIconElement(treeNode);
    }

    public Element getLabelElement()
    {
        return treeUI.getLabelElement(treeNode);
    }

    public int getIndex()
    {
        return treeNode.getIndex();
    }

    public int getChildCount()
    {
        return treeUI.getChildCount(treeNode);
    }

    public ItsNatTreeCellUI getChildItsNatTreeCellUIAt(int index)
    {
        return treeUI.getChildItsNatTreeCellUIAt(index,treeNode);
    }

    public int getRow()
    {
        return treeUI.getRow(treeNode);
    }

    public ItsNatTreeCellUI[] getTreeNodeUIPath()
    {
        ItsNatTreeCellUI nodeInfoCurr;

        int count = 1;
        nodeInfoCurr = this;
        while(nodeInfoCurr.getTreeNodeUIParent() != null)
        {
            count++;
            nodeInfoCurr = nodeInfoCurr.getTreeNodeUIParent();
        }

        ItsNatTreeCellUI[] path = new ItsNatTreeCellUI[count];
        nodeInfoCurr = this;
        for(int i = path.length - 1; i >= 0; i--)
        {
            path[i] = nodeInfoCurr;
            nodeInfoCurr = nodeInfoCurr.getTreeNodeUIParent();
        }
        return path;
    }

    public int getDeepLevel()
    {
        return treeNode.getDeepLevel();
    }

    public TreePath getTreePath()
    {
        // No memorizamos el path calculado porque puede cambiar en cualquier momento al cambiar el árbol
        return calcTreePath();
    }

    public TreePath calcTreePath()
    {
        TreeModel dataModel = treeUI.getItsNatTree().getTreeModel();
        ItsNatTreeCellUI[] infoPath = getTreeNodeUIPath();
        int pathLen = infoPath.length;
        if (treeUI.isRootless()) pathLen++;
        Object[] path = new Object[pathLen];
        Object parentNode = dataModel.getRoot();
        path[0] = parentNode;
        int first,delta;
        if (treeUI.isRootless())
        {
            first = 0;
            delta = 1;
        }
        else
        {
            first = 1;
            delta = 0;
        }
        for(int i = first; i < infoPath.length; i++)
        {
            int index = infoPath[i].getIndex();
            parentNode = dataModel.getChild(parentNode,index);
            path[i + delta] = parentNode;
        }
        return new TreePath(path);
    }

    public void expand(boolean expandState)
    {
        this.expandState = expandState;
    }

    public boolean isExpanded()
    {
        return expandState;
    }

}
