/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.itsnat.comp.tree.ItsNatFreeTree;
import org.itsnat.comp.tree.ItsNatTreeCellUI;
import org.itsnat.comp.tree.ItsNatTreeUI;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.ElementCSSInlineStyle;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLImageElement;

public class FreeTreeDecorator implements TreeModelListener,TreeSelectionListener,TreeExpansionListener
{
    protected ItsNatFreeTree comp;

    public FreeTreeDecorator(ItsNatFreeTree comp)
    {
        this.comp = comp;
    }

    public void bind()
    {
        TreeModel dataModel = comp.getTreeModel();
        dataModel.addTreeModelListener(this); // Added before to call setTreeModel again because it must be called last (the last registered is the first called, the component register a listener to add/remove DOM elements)
        comp.setTreeModel(dataModel);// Resets the internal listeners, the internal TreeModelListener listener is called first
        comp.addTreeExpansionListener(this);

        TreeSelectionModel selModel = comp.getTreeSelectionModel();
        selModel.addTreeSelectionListener(this);
    }

    public int getMaxLevelInitiallyShown()
    {
        return -1; // All
    }
    
    public void treeNodesChanged(TreeModelEvent e)
    {
    }

    public void treeNodesInserted(TreeModelEvent e)
    {
        TreeModel dataModel = comp.getTreeModel();
        TreePath parentPath = e.getTreePath();

        boolean childrenVisible = true;
        ItsNatTreeCellUI parentNodeInfo = comp.getItsNatTreeUI().getItsNatTreeCellUIFromTreePath(parentPath);
        if (parentNodeInfo != null) // if null parentPath is root and rootless mode
        {
            updateHandleAndIconOfNode(parentNodeInfo,parentPath); // visibility doesn't change
            childrenVisible = parentNodeInfo.isExpanded();
        }

        Object parentNode = parentPath.getLastPathComponent();
        int[] indices = e.getChildIndices();
        if ((indices != null)&&(indices.length > 0))
        {
            for(int i = 0; i < indices.length; i++)
            {
                int index = indices[i];
                Object childNode = dataModel.getChild(parentNode,index);
                TreePath childPath = parentPath.pathByAddingChild(childNode);
                updateNodeTree(childPath,childrenVisible);

                int maxLevel = getMaxLevelInitiallyShown();
                if ((maxLevel > 0) && (maxLevel <= childPath.getPathCount()))
                    comp.collapseNode(childPath);
            }
        }
    }

    public void treeNodesRemoved(TreeModelEvent e)
    {
        TreePath parentPath = e.getTreePath();
        updateNode(parentPath,isVisible(parentPath));
    }

    public void treeStructureChanged(TreeModelEvent e)
    {
        TreePath path = e.getTreePath();
        if (path == null) return; // Root case (removed)
        updateNodeTree(path,isVisible(path));
    }

    public void updateNodeTree(TreePath path,boolean visible)
    {
        ItsNatTreeCellUI nodeInfo = comp.getItsNatTreeUI().getItsNatTreeCellUIFromTreePath(path);
        if (nodeInfo == null) return; // parentPath is root and rootless mode
        updateNode(nodeInfo,path,visible);

        boolean childrenVisible = nodeInfo.isExpanded();
        TreeModel dataModel = comp.getTreeModel();
        Object dataNode = path.getLastPathComponent();
        int len = dataModel.getChildCount(dataNode);
        for(int i = 0; i < len; i++)
        {
            Object childNode = dataModel.getChild(dataNode,i);
            TreePath childPath = path.pathByAddingChild(childNode);
            updateNodeTree(childPath,childrenVisible);
        }
    }
  
    public void updateNode(TreePath path,boolean visible)
    {
        ItsNatTreeCellUI nodeInfo = comp.getItsNatTreeUI().getItsNatTreeCellUIFromTreePath(path);
        if (nodeInfo == null) return; // parentPath is root and rootless mode
        updateNode(nodeInfo,path,visible);
    }

    public void updateNode(ItsNatTreeCellUI nodeInfo,TreePath path,boolean visible)
    {
        updateHandleAndIconOfNode(nodeInfo,path);

        updateVisibilityOfNode(nodeInfo,path,visible);
    }

    public void updateHandleAndIconOfNode(TreePath path)
    {
        ItsNatTreeCellUI nodeInfo = comp.getItsNatTreeUI().getItsNatTreeCellUIFromTreePath(path);
        if (nodeInfo == null) return; // parentPath is root and rootless mode
        updateHandleAndIconOfNode(nodeInfo,path);
    }

    public void updateHandleAndIconOfNode(ItsNatTreeCellUI nodeInfo,TreePath path)
    {
        TreeModel dataModel = comp.getTreeModel();
        Object dataNode = path.getLastPathComponent();

        Element handle = nodeInfo.getHandleElement();
        HTMLImageElement handleImg;
        if (handle instanceof HTMLAnchorElement) 
            handleImg = (HTMLImageElement)handle.getFirstChild();
        else
            handleImg = (HTMLImageElement)handle;

        if (nodeInfo.isExpanded())
            handleImg.setSrc("img/tree/tree_node_expanded.gif");
        else
            handleImg.setSrc("img/tree/tree_node_collapse.gif");

        Element icon = nodeInfo.getIconElement();
        HTMLImageElement iconImg;
        if (icon instanceof HTMLAnchorElement) 
            iconImg = (HTMLImageElement)icon.getFirstChild();
        else
            iconImg = (HTMLImageElement)icon;

        if (dataModel.isLeaf(dataNode))
        {
            setStyleProperty(handle,"display","none");
            iconImg.setSrc("img/tree/gear.gif");
        }
        else
        {
            removeStyleProperty(handle,"display");
            if (nodeInfo.isExpanded())
                iconImg.setSrc("img/tree/tree_folder_open.gif");
            else
                iconImg.setSrc("img/tree/tree_folder_close.gif");
        }

        if (comp.isTreeTable())
        {
            Element contentParentElem = nodeInfo.getContentElement();
            int margin = 15 * nodeInfo.getDeepLevel();
            if (margin > 0)
                setStyleProperty(contentParentElem,"padding-left",margin + "px");
        }
    }
    
    public void valueChanged(TreeSelectionEvent e)
    {
        TreePath[] paths = e.getPaths();
        for(int i = 0; i < paths.length; i++)
        {
            boolean selected = e.isAddedPath(i);
            TreePath path = paths[i];
            decorateSelection(path,selected);
            if (selected)
            {
                // Expand all parents
                TreePath parentPath = path.getParentPath();
                if (parentPath != null)
                    comp.expandPath(parentPath);
            }
        }
    }

    public void decorateSelection(TreePath path,boolean selected)
    {
        if (path == null) return; // Root case (removed)
        Element labelElem = comp.getItsNatTreeUI().getLabelElementFromTreePath(path);
        if (labelElem == null) return;
        decorateSelection(labelElem,selected);
    }

    public void decorateSelection(Element elem,boolean selected)
    {
        ElementCSSInlineStyle elemCSS = (ElementCSSInlineStyle)elem;
        CSSStyleDeclaration elemStyle = elemCSS.getStyle();

        if (selected)
        {
            elemStyle.setProperty("background","rgb(0,0,255)",null);
            elemStyle.setProperty("color","white",null);
        }
        else
        {
            elemStyle.removeProperty("background");
            elemStyle.removeProperty("color");
        }
    }

    public void treeExpanded(TreeExpansionEvent event)
    {
        expandCollapse(event,true);
    }

    public void treeCollapsed(TreeExpansionEvent event)
    {
        expandCollapse(event,false);
    }

    public void expandCollapse(TreeExpansionEvent event,boolean expand)
    {
        TreePath path = event.getPath();
        ItsNatTreeUI compUI = comp.getItsNatTreeUI();
        ItsNatTreeCellUI nodeInfo = compUI.getItsNatTreeCellUIFromTreePath(path);
        if (nodeInfo == null) return; // parentPath is root and rootless mode
        updateTreeUIExpandCollapse(nodeInfo,path,expand);
    }

    public boolean isVisible(TreePath path)
    {
        TreePath parentPath = path.getParentPath();
        if (parentPath == null) return true;
        
        ItsNatTreeUI compUI = comp.getItsNatTreeUI();
        ItsNatTreeCellUI parentNodeInfo = compUI.getItsNatTreeCellUIFromTreePath(parentPath);
        if (parentNodeInfo == null) return true; // path is root and rootless mode
        return parentNodeInfo.isExpanded();
    }
   
    public void updateTreeUIExpandCollapse(ItsNatTreeCellUI nodeInfo,TreePath path,boolean expand)
    {
        updateHandleAndIconOfNode(nodeInfo,path); // visibility is not affected

        updateChildrenVisibility(path,expand);
    }

    public void updateChildrenVisibility(TreePath parentPath,boolean childrenVisible)
    {
        // Hide/show child nodes
        TreeModel dataModel = comp.getTreeModel();
        Object parentNode = parentPath.getLastPathComponent();
        int len = dataModel.getChildCount(parentNode);
        for(int i = 0; i < len; i++)
        {
            Object childNode = dataModel.getChild(parentNode,i);
            TreePath childPath = parentPath.pathByAddingChild(childNode);

            updateVisibilityOfNode(childPath,childrenVisible);
        }
    }

    public void updateVisibilityOfNode(TreePath path,boolean visible)
    {
        ItsNatTreeUI compUI = comp.getItsNatTreeUI();
        ItsNatTreeCellUI nodeInfo = compUI.getItsNatTreeCellUIFromTreePath(path);
        if (nodeInfo == null) return; // parentPath is root and rootless mode
        updateVisibilityOfNode(nodeInfo,path,visible);
    }

    public void updateVisibilityOfNode(ItsNatTreeCellUI nodeInfo,TreePath path,boolean visible)
    {
        Element elem = nodeInfo.getParentElement();

        if (visible)
            removeStyleProperty(elem,"display"); // Show
        else
            setStyleProperty(elem,"display","none"); // Hide

        if (comp.isTreeTable())
        {
            // Child nodes are not inside the parent's DOM HTMLElement
            // in this case
            boolean childrenVisible = nodeInfo.isExpanded() && visible;
            updateChildrenVisibility(path,childrenVisible);
        }
    }

    public void setStyleProperty(Element elem,String name,String value)
    {
        ElementCSSInlineStyle elemCSS = (ElementCSSInlineStyle)elem;
        CSSStyleDeclaration elemStyle = elemCSS.getStyle();
        elemStyle.setProperty(name,value,null);
    }

    public void removeStyleProperty(Element elem,String name)
    {
        ElementCSSInlineStyle elemCSS = (ElementCSSInlineStyle)elem;
        CSSStyleDeclaration elemStyle = elemCSS.getStyle();
        elemStyle.removeProperty(name);
    }
}
