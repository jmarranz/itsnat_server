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

import org.itsnat.impl.comp.ItsNatElementComponentUIImpl;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.comp.tree.ItsNatTreeCellUI;
import org.itsnat.impl.comp.tree.DefaultRowMapperImpl;
import javax.swing.tree.RowMapper;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.itsnat.comp.tree.ItsNatTree;
import org.itsnat.comp.tree.ItsNatTreeCellRenderer;
import org.itsnat.comp.tree.ItsNatTreeStructure;
import org.itsnat.comp.tree.ItsNatTreeUI;
import org.itsnat.core.domutil.ElementTreeNodeList;
import org.itsnat.impl.comp.tree.ItsNatTreeImpl;
import org.itsnat.impl.core.domutil.ElementGroupManagerImpl;
import org.itsnat.impl.core.domutil.ElementTreeNodeImpl;
import org.itsnat.impl.core.domutil.ElementTreeNodeListImpl;
import org.itsnat.impl.core.domutil.ElementTreeVersatileImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class ItsNatTreeUIImpl extends ItsNatElementComponentUIImpl implements ItsNatTreeUI
{
    protected boolean enabled = true;
    protected ElementTreeVersatileImpl elementTree;

    /**
     * Creates a new instance of ItsNatTreeUIImpl
     */
    public ItsNatTreeUIImpl(ItsNatTreeImpl parentComp)
    {
        super(parentComp);

        Element parentElement = getElement();

        ItsNatTreeStructure structure = parentComp.getItsNatTreeStructure();
        ItsNatTreeStructureCoreAdapterImpl structAdapter;
        structAdapter = new ItsNatTreeStructureCoreAdapterImpl(structure,this);

        ElementGroupManagerImpl factory = getItsNatDocumentImpl().getElementGroupManagerImpl();
        this.elementTree = factory.createElementTreeVersatileInternal(parentComp.isRootless(),parentComp.isTreeTable(),parentElement,true,structAdapter,null);
    }

    public ItsNatTree getItsNatTree()
    {
        return (ItsNatTree)parentComp;
    }

    public ItsNatTreeImpl getItsNatTreeImpl()
    {
        return (ItsNatTreeImpl)parentComp;
    }

    public RowMapper getRowMapper()
    {
        return getItsNatTree().getRowMapper();
    }

    public DefaultRowMapperImpl getDefaultRowMapper()
    {
        return getItsNatTreeImpl().getDefaultRowMapper();
    }

    public boolean isRootless()
    {
        return elementTree.isRootless();
    }

    public ItsNatTreeCellRenderer getItsNatTreeCellRenderer()
    {
        return getItsNatTree().getItsNatTreeCellRenderer();
    }

    public int logicToMarkupRow(int row)
    {
        // Método no público
        if (isRootless()) row--; // Porque el root es el 0 pero como no tiene markup la row del markup es una menor
        return row;
    }

    public int markupToLogicRow(int row)
    {
        // Método no público
        if (isRootless()) row++; // Porque el root es el 0 pero como no tiene markup la row del markup es una menor
        return row;
    }

/*
    public boolean isEmpty()
    {
        return elementTree.isEmpty();
    }
*/
    public int getChildCount(ElementTreeNodeImpl treeNode)
    {
        return treeNode.getChildTreeNodeList().getLength();
    }

    public ItsNatTreeCellUI getChildItsNatTreeCellUIAt(int index,ElementTreeNodeImpl treeNode)
    {
        ElementTreeNodeImpl childTreeNode = (ElementTreeNodeImpl)treeNode.getChildTreeNodeList().getTreeNodeAt(index);
        return getItsNatTreeCellUI(childTreeNode);
    }

    public Element getParentElementFromRow(int row)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromRow(row);
        if (treeNode == null)
            return null;
        return treeNode.getParentElement();
    }

    public Element getContentElement(ElementTreeNodeImpl treeNode)
    {
        ItsNatTree tree = getItsNatTree();
        ItsNatTreeStructure structure = tree.getItsNatTreeStructure();
        return structure.getContentElement(tree,getRow(treeNode),treeNode.getParentElement());
    }

    public Element getHandleElement(ElementTreeNodeImpl treeNode)
    {
        ItsNatTree tree = getItsNatTree();
        ItsNatTreeStructure structure = tree.getItsNatTreeStructure();
        return structure.getHandleElement(tree,getRow(treeNode),treeNode.getParentElement());
    }

    public Element getIconElement(ElementTreeNodeImpl treeNode)
    {
        ItsNatTree tree = getItsNatTree();
        ItsNatTreeStructure structure = tree.getItsNatTreeStructure();
        return structure.getIconElement(tree,getRow(treeNode),treeNode.getParentElement());
    }

    public Element getLabelElement(ElementTreeNodeImpl treeNode)
    {
        ItsNatTree tree = getItsNatTree();
        ItsNatTreeStructure structure = tree.getItsNatTreeStructure();
        return structure.getLabelElement(tree,getRow(treeNode),treeNode.getParentElement());
    }

    public Element getContentElementFromRow(int row)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromRow(row);
        if (treeNode == null)
            return null;
        return getContentElement(treeNode);
    }

    public Element getHandleElementFromRow(int row)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromRow(row);
        if (treeNode == null)
            return null;
        return getHandleElement(treeNode);
    }

    public Element getIconElementFromRow(int row)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromRow(row);
        if (treeNode == null)
            return null;
        return getIconElement(treeNode);
    }

    public Element getLabelElementFromRow(int row)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromRow(row);
        if (treeNode == null)
            return null;
        return getLabelElement(treeNode);
    }

    public Element getContentElementFromTreePath(TreePath path)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromTreePath(path);
        if (treeNode == null)
            return null;
        return getContentElement(treeNode);
    }

    public Element getParentElementFromTreePath(TreePath path)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromTreePath(path);
        if (treeNode == null)
            return null;
        return treeNode.getParentElement();
    }

    public Element getHandleElementFromTreePath(TreePath path)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromTreePath(path);
        if (treeNode == null)
            return null;
        return getHandleElement(treeNode);
    }

    public Element getIconElementFromTreePath(TreePath path)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromTreePath(path);
        if (treeNode == null)
            return null;
        return getIconElement(treeNode);
    }

    public Element getLabelElementFromTreePath(TreePath path)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromTreePath(path);
        if (treeNode == null)
            return null;
        return getLabelElement(treeNode);
    }

    public int getRow(ElementTreeNodeImpl treeNode)
    {
        // A día de hoy todos los nodos son visibles respecto al framework
        int row = treeNode.getRow();
        return markupToLogicRow(row);
    }

    public int getRowCount(ElementTreeNodeImpl treeNode)
    {
        // NO se llama a markupToLogicRow(row) porque treeNode es
        // un nodo concreto y el número de nodos del subárbol no está influido
        // por el asunto del rootless.
        return treeNode.getRowCount();
    }

    public int getRowCount()
    {
        int count = elementTree.getRowCount();
        return markupToLogicRow(count);  // Hay que tener en cuenta que en el caso rootless el root "está" aunque no tenga markup
    }

    public int getRowCount(TreePath path)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromTreePath(path);
        return getRowCount(treeNode);
    }

    public int getRow(TreePath path)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromTreePath(path);
        return getRow(treeNode);
    }

    public int getRow(int index,TreePath parentPath)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromTreePath(index,parentPath);
        return getRow(treeNode);
    }

    public int getRowCount(int index,TreePath parentPath)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromTreePath(index,parentPath);
        return getRowCount(treeNode);
    }

    public ElementTreeNodeImpl getElementTreeNodeFromRow(int row)
    {
        row = logicToMarkupRow(row);

        return elementTree.getElementTreeNodeFromRow(row);
    }

    public ItsNatTreeCellUI getItsNatTreeCellUIFromNode(Node node)
    {
        ElementTreeNodeImpl treeNode = elementTree.getElementTreeNodeFromNode(node);
        return getItsNatTreeCellUI(treeNode);
    }

    public ItsNatTreeCellUI getItsNatTreeCellUIFromTreePath(TreePath path)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromTreePath(path);
        return getItsNatTreeCellUI(treeNode);
    }

    public ItsNatTreeCellUI getItsNatTreeCellUIFromRow(int row)
    {
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromRow(row);
        return getItsNatTreeCellUI(treeNode);
    }

    public ItsNatTreeCellUIImpl getItsNatTreeCellUI(ElementTreeNodeImpl treeNode)
    {
        if (treeNode == null) return null;
        return ItsNatTreeCellUIImpl.getItsNatTreeCellUI(this,treeNode);
    }

    public ElementTreeNodeImpl getElementTreeNodeFromTreePath(TreePath path)
    {
        if (path == null) return null;
        return getElementTreeNodeFromRow(getDefaultRowMapper().getRowForPath(path));
    }

    public ElementTreeNodeImpl getElementTreeNodeFromTreePath(int index,TreePath parentPath)
    {
        ElementTreeNodeListImpl childList = getElementTreeNodeChildList(parentPath);
        return (ElementTreeNodeImpl)childList.getTreeNodeAt(index);
    }


    public ElementTreeNodeListImpl getElementTreeNodeChildList(TreePath path)
    {
        ElementTreeNodeListImpl treeNodeList;
        if ((path.getParentPath() == null) && isRootless()) // es el root y el árbol no tiene root con markup
        {
            return elementTree.getChildListRootless();
        }
        else
        {
            ElementTreeNodeImpl treeNode = getElementTreeNodeFromTreePath(path);
            return (ElementTreeNodeListImpl)treeNode.getElementTreeNodeList();
        }
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Inserts a new child tree node at the position specified by its path.
     *
     * @param path path of the new tree node.
     * @see #insertTreeNodeAt(int,TreePath)
     */   
    public void insertTreeNodeAt(TreePath path)
    {
        TreePath parentPath = path.getParentPath();
        if (parentPath == null) // es el null
            addRootNode(path);
        else
        {
            TreeModel dataModel = getItsNatTree().getTreeModel();
            int index = dataModel.getIndexOfChild(parentPath.getLastPathComponent(),path.getLastPathComponent());
            ElementTreeNode parentTreeNode = getElementTreeNodeFromTreePath(parentPath);
            insertElementAt(index,path,parentTreeNode);
        }
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     * 
     * Inserts a new child tree node at the specified position below the parent tree node
     * specified by its path.
     *
     * @param index 0 based index of the new tree node relative to the parent.
     * @param parentPath the path of the parent tree node.
     * @see org.itsnat.core.domutil.ElementTreeNodeList#insertTreeNodeAt(int,Object)
     */
    public void insertTreeNodeAt(int index,TreePath parentPath)
    {
        ElementTreeNodeList treeNodeListParent = getElementTreeNodeChildList(parentPath);
        TreeModel dataModel = getItsNatTree().getTreeModel();
        Object dataNode = dataModel.getChild(parentPath.getLastPathComponent(),index);
        TreePath path = parentPath.pathByAddingChild(dataNode);
        insertElementAt(index,path,treeNodeListParent);
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Removes the child tree node specified by its index relative to the tree node parent
     * specified by its path.
     *
     * <p>This method uses the data model (to locate the parent) but can be called including when
     * the specified child node is missing in the data model (for instance was removed before).
     * </p>
     *
     * @param index 0 based index of the child tree node to remove.
     * @param parentPath the path of the parent tree node.
     * @see org.itsnat.core.domutil.ElementTreeNodeList#removeTreeNodeAt(int)
     */
    public void removeTreeNodeAt(int index,TreePath parentPath)
    {
        ElementTreeNodeListImpl treeNodeListParent = getElementTreeNodeChildList(parentPath);
        removeElementAt(index,treeNodeListParent);
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Removes all tree nodes below the specified parent tree node by its path.
     *
     * <p>This method uses the data model (to locate the specified parent tree node) but can be called including when
     * the child nodes are missing in the data model (for instance were removed before).
     * </p>
     *
     * @param parentPath the path of the parent tree node.
     */
    public void removeAllChildTreeNodes(TreePath parentPath)
    {
        if (parentPath == null) // Es el caso de eliminar el root
            removeRootNode();
        else
        {
            ElementTreeNode parentTreeNode = getElementTreeNodeFromTreePath(parentPath);
            ElementTreeNodeListImpl treeNodeListParent = (ElementTreeNodeListImpl)parentTreeNode.getChildTreeNodeList();
            int len = treeNodeListParent.getLength();
            for(int i = 0; i < len; i++)
                removeElementAt(0,treeNodeListParent); // Es 0 porque al eliminar cambia la numeración claro
        }
    }

    public void setNodeValueAt(TreePath path,boolean hasFocus,ElementTreeNodeImpl treeNode,boolean isNew)
    {
        int row = getDefaultRowMapper().getRowForPath(path);
        if (row < 0) throw new ItsNatException("Tree node is not shown: " + path,treeNode);

        Object dataNode = path.getLastPathComponent();
        setNodeValueAt(row,dataNode,hasFocus,treeNode,isNew);
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Renders the submitted data value of the child tree node specified
     * by its index relative to the tree node parent specified by its path.
     *
     * @param index 0 based index of the child tree node to render.
     * @param parentPath the path of the parent tree node.
     * @param hasFocus if this child tree node has the focus. Current ItsNat implementation ever passes false.
     * @see org.itsnat.core.domutil.ElementTreeNode#setValue(Object)
     */
    public void setTreeNodeValueAt(int index,TreePath parentPath,boolean hasFocus)
    {
        TreePath path = getItsNatTreeImpl().toTreePath(index,parentPath);
        setTreeNodeValueAt(path,hasFocus);
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Renders the submitted data value of the tree node specified by its path.
     *
     * @param path path of the tree node.
     * @param hasFocus if this child tree node has the focus. Current ItsNat implementation ever passes false.
     * @see #setTreeNodeValueAt(int,TreePath,boolean)
     */
    public void setTreeNodeValueAt(TreePath path,boolean hasFocus)
    {
        int row = getDefaultRowMapper().getRowForPath(path);
        if ((row == 0) && isRootless()) return; // El root no tiene visualización
        ElementTreeNodeImpl treeNode = getElementTreeNodeFromRow(row);
        Object value = path.getLastPathComponent();
        setNodeValueAt(row,value,hasFocus,treeNode,false);
    }

    public void setNodeValueAt(int row,Object value,boolean hasFocus,ElementTreeNodeImpl treeNode,boolean isNew)
    {
        ItsNatTree tree = getItsNatTree();
        TreeModel dataModel = tree.getTreeModel();
        boolean isSelected = tree.getTreeSelectionModel().isRowSelected(row);
        boolean isLeaf = dataModel.isLeaf(value);
        boolean expanded = getItsNatTreeCellUI(treeNode).isExpanded();
        Element treeNodeLabelElem = treeNode.getLabelElement();

        treeNode.setUsePatternMarkupToRender(isUsePatternMarkupToRender()); // Asegura que tiene el último valor pues cada tree node tiene su propio estado
        treeNode.prepareRendering(treeNodeLabelElem,isNew);

        ItsNatTreeCellRenderer renderer = getItsNatTreeCellRenderer();
        if (renderer != null)
            renderer.renderTreeCell(tree,row,value,isSelected,expanded,isLeaf,hasFocus,treeNodeLabelElem,isNew);
    }

    public void removeElementAt(int index,ElementTreeNodeListImpl treeNodeListParent)
    {
        unrenderTreeNode(index,treeNodeListParent);

        treeNodeListParent.removeTreeNodeAt(index);
    }

    public void insertElementAt(int index,TreePath path,ElementTreeNode treeNodeParent)
    {
        insertElementAt(index,path,treeNodeParent.getChildTreeNodeList());
    }

    public void insertElementAt(int index,TreePath path,ElementTreeNodeList treeNodeList)
    {
        ElementTreeNode newTreeNode = treeNodeList.insertTreeNodeAt(index);

        setNodeValueAt(path,false,(ElementTreeNodeImpl)newTreeNode,true);

        // Procesamos los hijos de forma recursiva
        insertChildren(path,newTreeNode.getChildTreeNodeList());
    }

    public void insertChildren(TreePath path,ElementTreeNodeList treeNodeList)
    {
        // Procesamos los hijos de forma recursiva
        TreeModel dataModel = getItsNatTree().getTreeModel();
        Object dataNode = path.getLastPathComponent();
        int count = dataModel.getChildCount(dataNode);
        for(int i = 0; i < count; i++)
        {
            Object childNode = dataModel.getChild(dataNode,i);
            TreePath childPath = path.pathByAddingChild(childNode);
            insertElementAt(i,childPath,treeNodeList);
        }
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Adds a root tree node and renders the value using the
     * current structure and renderer.
     *
     * <p>If the tree already has a root node an exception is thrown.</p>
     *
     * @param dataNodeRoot the data value of the new root node.
     * @see org.itsnat.core.domutil.ElementTree#addRootNode()
     */
    public void addRootNode(Object dataNodeRoot)
    {
        TreePath rootPath = new TreePath(dataNodeRoot);
        addRootNode(rootPath);
    }

    public void addRootNode(TreePath rootPath)
    {
        // Interna, se cumple aquí que rootPath no tiene parent (es verdaderamente root)
        if (!isRootless()) // Si es root less no tiene markup
        {
            ElementTreeNodeImpl rootNode = elementTree.addRootNode(); // Si ya había root dará error
            setNodeValueAt(rootPath,false,rootNode,true);
        }

        ElementTreeNodeList treeNodeList = getElementTreeNodeChildList(rootPath);

        // Procesamos los hijos de forma recursiva
        insertChildren(rootPath,treeNodeList);
    }

    public void unrenderTreeNode(int index,ElementTreeNodeListImpl childList)
    {
        ElementTreeNodeImpl treeNode = (ElementTreeNodeImpl)childList.getTreeNodeAt(index);
        unrenderTreeNode(treeNode);
    }

    public void unrenderTreeNode(ElementTreeNodeImpl treeNode)
    {
        if (treeNode == null) return;

        ItsNatTreeCellRenderer renderer = getItsNatTreeCellRenderer();
        if (renderer != null)
        {
            Element treeNodeLabelElem = treeNode.getLabelElement();
            renderer.unrenderTreeCell(getItsNatTree(),treeNode.getRow(),treeNodeLabelElem);
        }
        // Es posible que el padre pueda no tener renderer pero los hijos sí.
        ElementTreeNodeListImpl childList = treeNode.getElementTreeNodeList();
        unrenderTreeNode(childList);
    }

    public void unrenderTreeNode(ElementTreeNodeListImpl childList)
    {
        if (childList == null) return;
        int len = childList.getLength();
        for(int i = 0; i < len; i++)
            unrenderTreeNode(i,childList);
    }

    /**
     * ESTE METODO FUE PUBLICO
     * 
     * Removes the current root tree node, if the tree is empty does nothing.
     * If the tree is rootless the tree is completely removed.
     *
     * @see org.itsnat.core.domutil.ElementTree#removeRootNode()
     */
    public void removeRootNode()
    {
        if (!isRootless())
            unrenderTreeNode(elementTree.getRootNode()); // puede ser null el root (no tiene)
        else
            unrenderTreeNode(elementTree.getChildListRootless());

        elementTree.removeRootNode();
    }

    public boolean isUsePatternMarkupToRender()
    {
        return elementTree.isUsePatternMarkupToRender();
    }

    public void setUsePatternMarkupToRender(boolean value)
    {
        elementTree.setUsePatternMarkupToRender(value);
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
