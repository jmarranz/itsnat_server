/*
 * FreeTreeDecorator.java
 *
 * Created on 16 de marzo de 2007, 18:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comp.trees;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import org.itsnat.comp.tree.ItsNatFreeTree;

/**
 *
 * @author jmarranz
 */
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
        comp.setTreeModel(dataModel);  // resets the internal listeners, the internal TreeModelListener listener is called first
        comp.addTreeExpansionListener(this);

        TreeSelectionModel selModel = comp.getTreeSelectionModel();
        selModel.addTreeSelectionListener(this);
    }

    public void treeNodesChanged(TreeModelEvent e)
    {
    }

    public void treeNodesInserted(TreeModelEvent e)
    {
    }

    public void treeNodesRemoved(TreeModelEvent e)
    {
    }

    public void treeStructureChanged(TreeModelEvent e)
    {
    }

    public void valueChanged(TreeSelectionEvent e)
    {
    }

    public void treeExpanded(TreeExpansionEvent event)
    {
    }

    public void treeCollapsed(TreeExpansionEvent event)
    {
    }

}
