/*
 * TestTable.java
 *
 * Created on 6 de diciembre de 2006, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp.free;

import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.comp.tree.ItsNatFreeTree;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.tree.ItsNatTreeCellUI;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.tree.ItsNatTreeUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.ElementCSSInlineStyle;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLImageElement;
import test.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public abstract class TestFreeTreeBase extends TestBaseHTMLDocument implements EventListener,TreeModelListener,TreeSelectionListener,TreeExpansionListener,TreeWillExpandListener
{
    protected ItsNatFreeTree comp;
    protected ItsNatHTMLButton buttonAdd;
    protected ItsNatHTMLButton buttonRemove;
    protected ItsNatHTMLButton buttonReload;
    protected ItsNatHTMLInputCheckBox joystickModeCheck;

    /**
     * Creates a new instance of TestTable
     */
    public TestFreeTreeBase(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
    }

    public void initTree(String treeId,String addButtonId,String removeButtonId,String reloadButtonId,String joystickCheckboxId)
    {
        Document doc = itsNatDoc.getDocument();
        HTMLElement rootElem = (HTMLElement)doc.getElementById(treeId);
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.comp = (ItsNatFreeTree)componentMgr.findItsNatComponent(rootElem);

        comp.setToggleClickCount(2);

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root Item");
        DefaultTreeModel dataModel = new DefaultTreeModel(rootNode);
        dataModel.addTreeModelListener(this); // Añadimos antes de meter en el componente para que el listener último sea el del componente pues se procesan los últimos primero en eventos

        comp.setTreeModel(dataModel);

        comp.addEventListener("click",this);

        DefaultMutableTreeNode childNode;
        DefaultMutableTreeNode parentNode;

        childNode = addNode("Item 1",rootNode);

            parentNode = childNode;
            childNode = addNode("Item 1.1",parentNode);

                parentNode = childNode;
                childNode = addNode("Item 1.1.1",parentNode);

        childNode = addNode("Item 2",rootNode);

            parentNode = childNode;
            childNode = addNode("Item 2.1",parentNode);
            childNode = addNode("Item 2.2",parentNode);

        // comp.setSelectionModel(null);

        TreeSelectionModel selModel = comp.getTreeSelectionModel();
        selModel.setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);

        TreePath[] paths = selModel.getSelectionPaths();
        if (paths != null)
        {
            for(int i = 0; i < paths.length; i++)
            {
                TreePath path = paths[i];
                boolean selected = selModel.isPathSelected(path);
                decorateSelection(path,selected);
            }
        }

        selModel.addTreeSelectionListener(this);


        comp.addTreeExpansionListener(this);
        comp.addTreeWillExpandListener(this);


        this.buttonAdd = (ItsNatHTMLButton)componentMgr.findItsNatComponentById(addButtonId);
        buttonAdd.addEventListener("click",this);

        this.buttonRemove = (ItsNatHTMLButton)componentMgr.findItsNatComponentById(removeButtonId);
        buttonRemove.addEventListener("click",this);

        this.buttonReload = (ItsNatHTMLButton)componentMgr.findItsNatComponentById(reloadButtonId);
        buttonReload.addEventListener("click",this);

        this.joystickModeCheck = (ItsNatHTMLInputCheckBox)componentMgr.findItsNatComponentById(joystickCheckboxId);
        joystickModeCheck.addEventListener("click",this);
        joystickModeCheck.setSelected(itsNatDoc.isJoystickMode());
    }

    public DefaultMutableTreeNode addNode(String value,DefaultMutableTreeNode parentNode)
    {
        DefaultTreeModel dataModel = (DefaultTreeModel)comp.getTreeModel();
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(value);
        int count = dataModel.getChildCount(parentNode);
        dataModel.insertNodeInto(childNode,parentNode,count);
        return childNode;
    }

    public void handleEvent(Event evt)
    {
        if (evt.getCurrentTarget() == buttonAdd.getElement())
            addNewItems();
        else if (evt.getCurrentTarget() == buttonRemove.getElement())
            removeItems();
        else if (evt.getCurrentTarget() == buttonReload.getElement())
            reloadTree();
        else if (evt.getCurrentTarget() == joystickModeCheck.getElement())
            comp.setJoystickMode(joystickModeCheck.isSelected());
        else
        {
            int[] selected = comp.getTreeSelectionModel().getSelectionRows();
            if (selected == null) selected = new int[0];
            outText("OK " + evt.getType() + " (n.sel. " + selected.length + ") "); // Para que se vea
        }

    }

    public void addNewItems()
    {
        DefaultTreeModel dataModel = (DefaultTreeModel)comp.getTreeModel();

         // La inserción lo hacemos aparte via botón para que no influya en la selección de celdas

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)dataModel.getRoot();
        if (rootNode == null)
        {
            rootNode = new DefaultMutableTreeNode("Root");
            dataModel.setRoot(rootNode);
        }

        DefaultMutableTreeNode parentNode;
        DefaultMutableTreeNode childNode;
        int count = rootNode.getChildCount();
        childNode = new DefaultMutableTreeNode("Item " + (count + 1));
        rootNode.insert(childNode,count);

        parentNode = childNode;
        childNode = new DefaultMutableTreeNode("Item " + (count + 1) + ".1");
        parentNode.insert(childNode,0);
        childNode = new DefaultMutableTreeNode("Item " + (count + 1) + ".2");
        parentNode.insert(childNode,1);

        // Notificamos la inserción del pequeño árbol

        dataModel.nodesWereInserted(rootNode,new int[]{count});
    }

    public void removeItems()
    {
        DefaultTreeModel dataModel = (DefaultTreeModel)comp.getTreeModel();

        // Eliminamos el primer elemento hijo del root o el propio root
        int rows = comp.getTreeNodeCount(); //comp.getRowCount();
        if (rows > 0) // Si rows es cero es que no hay ni siquiera root
        {
            TreePath path;
            if (rows > 1) path = comp.getTreePathForRow(1);
            else path = comp.getTreePathForRow(0); // el root
            if (path != null)
            {
                MutableTreeNode dataNode = (MutableTreeNode)path.getLastPathComponent();
                if (dataNode.getParent() != null) // No es Root
                    dataModel.removeNodeFromParent(dataNode);
                else
                    dataModel.setRoot(null);
            }
        }
    }

    public void reloadTree()
    {
        DefaultTreeModel dataModel = (DefaultTreeModel)comp.getTreeModel();

        // Hacemos un reload sistemático para todos los nodos
        if (dataModel.getRoot() != null)
        {
            TreePath path = new TreePath(dataModel.getRoot());
            do
            {
                dataModel.nodeStructureChanged((TreeNode)path.getLastPathComponent());
                path = comp.getNextPath(path);
            }
            while(path != null);
        }
    }

    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException
    {
        // Permitimos
    }

    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException
    {
        // Permitimos
    }

    public void treeNodesChanged(TreeModelEvent e)
    {
    }

    public void treeNodesInserted(TreeModelEvent e)
    {
        TreeModel dataModel = comp.getTreeModel();
        TreePath parentPath = e.getTreePath();
        updateHandleAndIconOfNode(parentPath);

        Object parentNode = parentPath.getLastPathComponent();
        int[] indices = e.getChildIndices();
        if ((indices != null)&&(indices.length > 0))
        {
            for(int i = 0; i < indices.length; i++)
            {
                int index = indices[i];
                Object childNode = dataModel.getChild(parentNode,index);
                TreePath childPath = parentPath.pathByAddingChild(childNode);
                updateNodeTree(childPath);
            }
        }
    }

    public void treeNodesRemoved(TreeModelEvent e)
    {
        TreePath parentPath = e.getTreePath();
        updateHandleAndIconOfNode(parentPath);
    }

    public void treeStructureChanged(TreeModelEvent e)
    {
        TreePath path = e.getTreePath();
        if (path == null) return; // Root case (removed)
        updateNodeTree(path);
    }

    public void updateNodeTree(TreePath path)
    {
        updateHandleAndIconOfNode(path);
        TreeModel dataModel = comp.getTreeModel();
        Object dataNode = path.getLastPathComponent();
        int len = dataModel.getChildCount(dataNode);
        for(int i = 0; i < len; i++)
        {
            Object childNode = dataModel.getChild(dataNode,i);
            TreePath childPath = path.pathByAddingChild(childNode);
            updateNodeTree(childPath);
        }
    }

    public void updateHandleAndIconOfNode(TreePath path)
    {
        ItsNatTreeCellUI nodeInfo = comp.getItsNatTreeUI().getItsNatTreeCellUIFromTreePath(path);
        if (nodeInfo == null) return; // path is root and rootless mode
        updateHandleAndIconOfNode(nodeInfo,path);
    }

    public void updateHandleAndIconOfNode(ItsNatTreeCellUI nodeInfo,TreePath path)
    {
        TreeModel dataModel = comp.getTreeModel();
        Object dataNode = path.getLastPathComponent();

        Element handle = nodeInfo.getHandleElement();
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
            decorateSelection(paths[i],selected);
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
        if (nodeInfo == null) return; // path is root and rootless mode
        updateTreeUIExpandCollapse(nodeInfo,path,expand);
    }

    public boolean childNodesAreVisible(ItsNatTreeCellUI nodeInfo,boolean visible)
    {
        return nodeInfo.isExpanded() && visible;
    }

    public void updateTreeUIExpandCollapse(ItsNatTreeCellUI nodeInfo,TreePath path,boolean visible)
    {
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

        updateHandleAndIconOfNode(nodeInfo,path);

        updateChildVisibility(path,nodeInfo.isExpanded());
    }

    public void updateChildVisibility(TreePath path,boolean childrenVisible)
    {
        // Hide/show child nodes
        TreeModel dataModel = comp.getTreeModel();
        Object parentNode = path.getLastPathComponent();
        int len = dataModel.getChildCount(parentNode);
        ItsNatTreeUI compUI = comp.getItsNatTreeUI();
        for(int i = 0; i < len; i++)
        {
            Object childNode = dataModel.getChild(parentNode,i);
            TreePath childPath = path.pathByAddingChild(childNode);
            ItsNatTreeCellUI childNodeInfo = compUI.getItsNatTreeCellUIFromTreePath(childPath);
            Element childElem = childNodeInfo.getParentElement();

            String display;
            if (childrenVisible)
                removeStyleProperty(childElem,"display"); // Show
            else
                setStyleProperty(childElem,"display","none"); // Hide

            if (comp.isTreeTable())
            {
                // Child nodes are not inside the parent's DOM HTMLElement
                // in this case
                updateChildVisibility(childPath,childNodesAreVisible(childNodeInfo,childrenVisible));
            }
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
