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
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.tree.ItsNatFreeTree;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.itsnat.feashow.FreeTreeDecorator;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class FreeTreeTreeNode extends FeatureTreeNode implements EventListener,TreeModelListener,TreeSelectionListener,TreeWillExpandListener,ItemListener
{
    protected ItsNatHTMLInputCheckBox useSingleClickComp;
    protected ItsNatFreeTree treeComp;
    protected ItsNatHTMLInputButton removeButton;
    protected ItsNatHTMLInputText itemComp;
    protected ItsNatHTMLInputText posComp;
    protected ItsNatHTMLInputButton updateButton;
    protected ItsNatHTMLInputButton insertBeforeButton;
    protected ItsNatHTMLInputButton insertAfterButton;
    protected ItsNatHTMLInputButton insertChildButton;
    protected ItsNatHTMLInputCheckBox joystickModeComp;

    public FreeTreeTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.useSingleClickComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("useSingleClickId");
        useSingleClickComp.getToggleButtonModel().addItemListener(this);

        this.treeComp = (ItsNatFreeTree)compMgr.createItsNatComponentById("compId","freeTree",null);

        new FreeTreeDecorator(treeComp).bind();

        DefaultTreeModel dataModel = (DefaultTreeModel)treeComp.getTreeModel();

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Grey's Anatomy");
        dataModel.setRoot(rootNode);

        DefaultMutableTreeNode parentNode;

            parentNode = addNode("Characters",rootNode,dataModel);

                addNode("Meredith Grey",parentNode,dataModel);
                addNode("Cristina Yang",parentNode,dataModel);
                addNode("Alex Karev",parentNode,dataModel);
                addNode("George O'Malley",parentNode,dataModel);

            parentNode = addNode("Actors",rootNode,dataModel);

                addNode("Ellen Pompeo",parentNode,dataModel);
                addNode("Sandra Oh",parentNode,dataModel);
                addNode("Justin Chambers",parentNode,dataModel);
                addNode("T.R. Knight",parentNode,dataModel);

        TreeSelectionModel selModel = treeComp.getTreeSelectionModel();
        selModel.setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);

        selModel.addSelectionPath(new TreePath(parentNode.getPath())); // Actors

        treeComp.addEventListener("click",this);
        dataModel.addTreeModelListener(this);
        selModel.addTreeSelectionListener(this);

        treeComp.addTreeWillExpandListener(this);

        this.removeButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("removeId");
        removeButton.addEventListener("click",this);

        this.itemComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("itemId");
        itemComp.setText(selModel.getSelectionPath().getLastPathComponent().toString());

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
        
        if (isUCWEB())
        {
            Element ucwebElem = itsNatDoc.getDocument().getElementById("ucwebId");
            ucwebElem.removeAttribute("style");
        }
    }

    public void endExamplePanel()
    {
        useSingleClickComp.dispose();
        this.useSingleClickComp = null;

        this.treeComp.dispose();
        this.treeComp = null;

        this.removeButton.dispose();
        this.removeButton = null;

        this.itemComp.dispose();
        this.itemComp = null;

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

    public static DefaultMutableTreeNode addNode(Object userObject,DefaultMutableTreeNode parentNode,DefaultTreeModel dataModel)
    {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(userObject);
        int count = dataModel.getChildCount(parentNode);
        dataModel.insertNodeInto(childNode,parentNode,count);
        return childNode;
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
            String newItem = itemComp.getText();
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
                        node.setUserObject(newItem);
                        dataModel.nodeChanged(node);
                    }
                    else getItsNatDocument().addCodeToSend("alert('Bad Position');");
                }
                else if ((currentTarget == insertBeforeButton.getHTMLInputElement()) ||
                         (currentTarget == insertAfterButton.getHTMLInputElement()) ||
                         (currentTarget == insertChildButton.getHTMLInputElement()))
                {
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
        TreeSelectionModel selModel = (TreeSelectionModel)e.getSource();

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
            Object value = path.getLastPathComponent();
            itemComp.setText(value.toString());
            posComp.setText(Integer.toString(row));
        }
    }

    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException
    {
        // Will expand
    }

    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException
    {
        // Will collapse
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
