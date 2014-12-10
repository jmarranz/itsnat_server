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

import java.util.EventObject;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.tree.ItsNatTree;
import org.itsnat.comp.tree.ItsNatTreeCellEditor;
import org.w3c.dom.Element;

public class TreeTableItemEditor implements ItsNatTreeCellEditor
{
    protected ItsNatTreeCellEditor defaultEditor;
    protected TreeTableItem currentItem;
    protected boolean operaMini;
    
    public TreeTableItemEditor(ItsNatTreeCellEditor defaultEditor,boolean operaMini)
    {
        this.defaultEditor = defaultEditor;     
        this.operaMini = operaMini;
    }

    public ItsNatComponent getTreeCellEditorComponent(ItsNatTree tree,int row, Object value, boolean isSelected, boolean expanded, boolean leaf, Element labelElem)
    {
        if (!operaMini) labelElem.setAttribute("style","width:100%;height:100%"); // The editor fills the parent element   
        DefaultMutableTreeNode dataNode = (DefaultMutableTreeNode)value;
        this.currentItem = (TreeTableItem)dataNode.getUserObject();
        return defaultEditor.getTreeCellEditorComponent(tree,row,currentItem.getPrincipal(),isSelected,expanded,leaf,labelElem);
    }

    public Object getCellEditorValue()
    {
        currentItem.setPrincipal((String)defaultEditor.getCellEditorValue());
        return currentItem;
    }

    public boolean isCellEditable(EventObject anEvent)
    {
        return defaultEditor.isCellEditable(anEvent);
    }

    public boolean shouldSelectCell(EventObject anEvent)
    {
        return defaultEditor.shouldSelectCell(anEvent);
    }

    public boolean stopCellEditing()
    {
        return defaultEditor.stopCellEditing();
    }

    public void cancelCellEditing()
    {
        defaultEditor.cancelCellEditing();
    }

    public void addCellEditorListener(CellEditorListener l)
    {
        defaultEditor.addCellEditorListener(l);
    }

    public void removeCellEditorListener(CellEditorListener l)
    {
        defaultEditor.removeCellEditorListener(l);
    }

}
