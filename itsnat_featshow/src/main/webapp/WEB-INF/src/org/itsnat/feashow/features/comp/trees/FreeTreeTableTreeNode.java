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

package org.itsnat.feashow.features.comp.trees;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.tree.ItsNatFreeTree;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.NameValue;
import org.itsnat.feashow.FeatureTreeNode;
import org.itsnat.feashow.FreeTreeDecorator;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class FreeTreeTableTreeNode extends FeatureTreeNode implements EventListener,TreeModelListener,TreeSelectionListener,ItemListener
{
    protected ItsNatHTMLInputCheckBox useSingleClickComp;
    protected ItsNatFreeTree treeComp;
    protected ItsNatHTMLInputButton removeButton;
    protected ItsNatHTMLInputText itemPrincipalComp;
    protected ItsNatHTMLInputText itemSecondaryComp;
    protected ItsNatHTMLInputText posComp;
    protected ItsNatHTMLInputButton updateButton;
    protected ItsNatHTMLInputButton insertBeforeButton;
    protected ItsNatHTMLInputButton insertAfterButton;
    protected ItsNatHTMLInputButton insertChildButton;
    protected ItsNatHTMLInputCheckBox joystickModeComp;

    public FreeTreeTableTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.useSingleClickComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("useSingleClickId");
        useSingleClickComp.getToggleButtonModel().addItemListener(this);

        NameValue[] artifacts = new NameValue[]{new NameValue("treeTable","true")};
        this.treeComp = (ItsNatFreeTree)compMgr.createItsNatComponentById("compId","freeTree",artifacts);

        treeComp.setItsNatTreeCellRenderer(new TreeTableItemRenderer(itsNatDoc));
        treeComp.setItsNatTreeCellEditor(new TreeTableItemEditor(treeComp.getItsNatTreeCellEditor(),isOperaMini()));

        new FreeTreeDecorator(treeComp).bind();

        DefaultTreeModel dataModel = (DefaultTreeModel)treeComp.getTreeModel();

        TreeTableItem rootItem = new TreeTableItem("Grey's Anatomy","Famous TV series");
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootItem);
        dataModel.setRoot(rootNode);

        DefaultMutableTreeNode parentNode;

            parentNode = addNode(new TreeTableItem("Characters","Actors"),rootNode);

                addNode(new TreeTableItem("Meredith Grey","Ellen Pompeo"),parentNode);
                addNode(new TreeTableItem("Cristina Yang","Sandra Oh"),parentNode);
                addNode(new TreeTableItem("Alex Karev","Justin Chambers"),parentNode);
                addNode(new TreeTableItem("George O'Malley","T.R. Knight"),parentNode);

            parentNode = addNode(new TreeTableItem("Other","Category"),rootNode);

                addNode(new TreeTableItem("Shonda Rhimes","Creator"),parentNode);
                addNode(new TreeTableItem("USA","Country"),parentNode);
                addNode(new TreeTableItem("English","Language"),parentNode);

        TreeSelectionModel selModel = treeComp.getTreeSelectionModel();
        selModel.setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);

        selModel.addSelectionPath(new TreePath(parentNode.getPath())); // Other

        treeComp.addEventListener("click",this);
        dataModel.addTreeModelListener(this);
        selModel.addTreeSelectionListener(this);

        this.removeButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("removeId");
        removeButton.addEventListener("click",this);

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selModel.getSelectionPath().getLastPathComponent();
        TreeTableItem selectedItem = (TreeTableItem)selectedNode.getUserObject();
        this.itemPrincipalComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("itemPrincipalId");
        itemPrincipalComp.setText(selectedItem.getPrincipal());
        this.itemSecondaryComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("itemSecondaryId");
        itemSecondaryComp.setText(selectedItem.getSecondary());

        this.posComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("posId");
        posComp.setText(Integer.toString(selModel.getMinSelectionRow()));

        this.updateButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("updateId");
        updateButton.addEventListener("click",this);

        this.insertBeforeButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("insertBeforeId");
        insertBeforeButton.addEventListener("click",this);

        this.insertAfterButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("insertAfterId");
        insertAfterButton.addEventListener("click",this);

        this.insertChildButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("insertChildId");
        insertChildButton.addEventListener("click",this);

        this.joystickModeComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("joystickModeId");
        joystickModeComp.getToggleButtonModel().addItemListener(this);
        joystickModeComp.setSelected(isJoystickModePreferred());

    }

    public DefaultMutableTreeNode addNode(Object userObject,DefaultMutableTreeNode parentNode)
    {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(userObject);
        DefaultTreeModel dataModel = (DefaultTreeModel)treeComp.getTreeModel();
        int count = dataModel.getChildCount(parentNode);
        dataModel.insertNodeInto(childNode,parentNode,count);
        return childNode;
    }

    public void endExamplePanel()
    {
        useSingleClickComp.dispose();
        this.useSingleClickComp = null;

        this.treeComp.dispose();
        this.treeComp = null;

        this.removeButton.dispose();
        this.removeButton = null;

        this.itemPrincipalComp.dispose();
        this.itemPrincipalComp = null;

        this.itemSecondaryComp.dispose();
        this.itemSecondaryComp = null;

        this.posComp.dispose();
        this.posComp = null;

        this.updateButton.dispose();
        this.updateButton = null;

        this.insertBeforeButton.dispose();
        this.insertBeforeButton = null;

        this.insertAfterButton.dispose();
        this.insertAfterButton = null;

        this.insertChildButton.dispose();
        this.insertChildButton = null;

        this.joystickModeComp.dispose();
        this.joystickModeComp = null;
    }

    public static boolean isInTree(MutableTreeNode node,DefaultTreeModel dataModel)
    {
        if (node == null) return false;
        if (node == dataModel.getRoot()) return true;
        return isInTree((MutableTreeNode)node.getParent(),dataModel);
    }

    public void handleEvent(Event evt)
    {
        log(evt);

        EventTarget currentTarget = evt.getCurrentTarget();
        if (currentTarget == removeButton.getHTMLInputElement())
        {
            DefaultTreeModel dataModel = (DefaultTreeModel)treeComp.getTreeModel();
            TreeSelectionModel selModel = treeComp.getTreeSelectionModel();
            if (!selModel.isSelectionEmpty())
            {
                // Selection Model is in CONTIGUOUS_TREE_SELECTION mode
                TreePath[] paths = selModel.getSelectionPaths();
                for(int i = 0; i < paths.length; i++)
                {
                    MutableTreeNode node = (MutableTreeNode)paths[i].getLastPathComponent();
                    if (node == dataModel.getRoot())
                        dataModel.setRoot(null);
                    else if (isInTree(node,dataModel)) // If false is already removed (a previous removed node was the parent)
                        dataModel.removeNodeFromParent(node);
                }
            }
        }
        else if ((currentTarget == updateButton.getHTMLInputElement()) ||
                 (currentTarget == insertBeforeButton.getHTMLInputElement()) ||
                 (currentTarget == insertAfterButton.getHTMLInputElement()) ||
                 (currentTarget == insertChildButton.getHTMLInputElement()) )
        {
            String newPrincipalItem = itemPrincipalComp.getText();
            String newSecondaryItem = itemSecondaryComp.getText();
            int row;
            try
            {
                row = Integer.parseInt(posComp.getText());
                DefaultTreeModel dataModel = (DefaultTreeModel)treeComp.getTreeModel();
                if (currentTarget == updateButton.getHTMLInputElement())
                {
                    TreePath path = treeComp.getTreePathForRow(row);
                    if (path != null)
                    {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
                        TreeTableItem newItem = (TreeTableItem)node.getUserObject();
                        newItem.setPrincipal(newPrincipalItem);
                        newItem.setSecondary(newSecondaryItem);
                        dataModel.nodeChanged(node); // To notify
                    }
                    else getItsNatDocument().addCodeToSend("alert('Bad Position');");
                }
                else if ((currentTarget == insertBeforeButton.getHTMLInputElement()) ||
                         (currentTarget == insertAfterButton.getHTMLInputElement()) ||
                         (currentTarget == insertChildButton.getHTMLInputElement()))
                {
                    TreeTableItem newItem = new TreeTableItem(newPrincipalItem,newSecondaryItem);
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newItem);
                    TreePath refPath = treeComp.getTreePathForRow(row);
                    if (refPath != null)
                    {
                        DefaultMutableTreeNode refNode = (DefaultMutableTreeNode)refPath.getLastPathComponent();
                        if ((currentTarget == insertBeforeButton.getHTMLInputElement()) ||
                            (currentTarget == insertAfterButton.getHTMLInputElement()))
                        {
                            if (row == 0)
                            {
                                getItsNatDocument().addCodeToSend("alert('Bad Position');");
                            }
                            else
                            {
                                TreePath parentPath = refPath.getParentPath();
                                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)parentPath.getLastPathComponent();
                                int childIndex = dataModel.getIndexOfChild(parentNode,refNode);
                                if (currentTarget == insertAfterButton.getHTMLInputElement())
                                    childIndex = childIndex + 1;
                                dataModel.insertNodeInto(newNode,parentNode,childIndex);
                            }
                        }
                        else // insert child
                        {
                            // refNode is the parent node
                            int childIndex = dataModel.getChildCount(refNode);
                            dataModel.insertNodeInto(newNode,refNode,childIndex);
                        }
                    }
                    else
                    {
                        if ((row == 0) && (dataModel.getRoot() == null))
                            dataModel.setRoot(newNode);
                        else
                            getItsNatDocument().addCodeToSend("alert('Bad Position');");
                    }
                }

            }
            catch(NumberFormatException ex)
            {
                getItsNatDocument().addCodeToSend("alert('Bad Position');");
            }
            catch(ArrayIndexOutOfBoundsException ex)
            {
                getItsNatDocument().addCodeToSend("alert('Bad Position');");
            }
        }
    }

    public void treeNodesChanged(TreeModelEvent e)
    {
        treeChangedLog(e);
    }

    public void treeNodesInserted(TreeModelEvent e)
    {
        treeChangedLog(e);
    }

    public void treeNodesRemoved(TreeModelEvent e)
    {
        treeChangedLog(e);
    }

    public void treeStructureChanged(TreeModelEvent e)
    {
        treeChangedLog(e);
    }

    public void treeChangedLog(TreeModelEvent e)
    {
        log(e.toString());
    }

    public void valueChanged(TreeSelectionEvent e)
    {
        TreeSelectionModel selModel = treeComp.getTreeSelectionModel();

        TreePath[] paths = e.getPaths();
        String fact = "";
        for(int i = 0; i < paths.length; i++)
        {
            TreePath path = paths[i];
            boolean selected = selModel.isPathSelected(path);
            if (selected)
                fact += ", selected ";
            else
                fact += ", deselected ";
            fact += path.getLastPathComponent();
        }

        log(e.getClass().toString() + " " + fact);

        int row = selModel.getMinSelectionRow(); // First selected
        if (row != -1)
        {
            TreePath path = treeComp.getTreePathForRow(row);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
            TreeTableItem value = (TreeTableItem)node.getUserObject();
            itemPrincipalComp.setText(value.getPrincipal());
            itemSecondaryComp.setText(value.getSecondary());
            posComp.setText(Integer.toString(row));
        }
    }

    public void itemStateChanged(ItemEvent e)
    {
        boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
        ButtonModel model = (ButtonModel)e.getSource();
        if (model == joystickModeComp.getToggleButtonModel())
            treeComp.setJoystickMode(selected);
        else if (model == useSingleClickComp.getToggleButtonModel())
            treeComp.setEditorActivatorEvent(selected? "click" : "dblclick");
    }
}
