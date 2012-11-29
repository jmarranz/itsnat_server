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

import org.itsnat.impl.comp.inplace.EditorProcessorBaseImpl;
import org.itsnat.comp.tree.ItsNatTreeCellEditor;
import org.itsnat.comp.tree.ItsNatTreeCellUI;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.tree.ItsNatTreeUI;
import org.itsnat.core.ItsNatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class TreeCellEditorProcessor extends EditorProcessorBaseImpl
{
    protected ItsNatTreeCellUI cellInfo;
    protected TreePath path;
    protected int row;

    public TreeCellEditorProcessor(ItsNatTreeImpl compParent)
    {
        super(compParent);
    }

    public ItsNatTreeImpl getItsNatTree()
    {
        return (ItsNatTreeImpl)compParent;
    }

    public ItsNatTreeCellEditor getItsNatTreeCellEditor()
    {
        return (ItsNatTreeCellEditor)cellEditor;
    }

    public void setItsNatTreeCellEditor(ItsNatTreeCellEditor cellEditor)
    {
        setCellEditor(cellEditor);
    }

    public void setCurrentContext(ItsNatTreeCellUI cellInfo,TreePath path,int row)
    {
        this.cellInfo = cellInfo;
        this.path = path;
        this.row = row;
    }

    public TreePath getTreePath()
    {
        return path; // El actualmente editado
    }

    public int getRow()
    {
        return row; // El actualmente editado
    }

    public void startEdition(TreePath path)
    {
        if (prepareEdition())
        {
            openEditor(path);
        }
    }

    public void startEdition(int row)
    {
        if (prepareEdition())
        {
            openEditor(row);
        }
    }

    private void openEditor(TreePath path)
    {
        ItsNatTreeUI compUI = getItsNatTree().getItsNatTreeUIImpl();
        ItsNatTreeCellUI cellInfo = (ItsNatTreeCellUI)compUI.getItsNatTreeCellUIFromTreePath(path);
        if (cellInfo == null) throw new ItsNatException("This tree node cannot be edited, may be is the root node in a rootless tree");
        openEditor(path,cellInfo.getRow(),cellInfo);
    }

    private void openEditor(int row)
    {
        ItsNatTreeUI compUI = getItsNatTree().getItsNatTreeUIImpl();
        ItsNatTreeCellUI cellInfo = (ItsNatTreeCellUI)compUI.getItsNatTreeCellUIFromRow(row);
        openEditor(cellInfo.getTreePath(),row,cellInfo);
    }

    private void openEditor(ItsNatTreeCellUI cellInfo)
    {
        openEditor(cellInfo.getTreePath(),cellInfo.getRow(),cellInfo);
    }

    private void openEditor(TreePath path,int row,ItsNatTreeCellUI cellInfo)
    {
        if (row >= 0)
        {
            setCurrentContext(cellInfo,path,row);

            ItsNatTreeImpl tree = getItsNatTree();
            TreeModel dataModel = tree.getTreeModel();
            Element labelElem = cellInfo.getLabelElement();
            TreeSelectionModelMgrImpl selModelMgr = tree.getTreeSelectionModelMgr();
            boolean isSelected = selModelMgr.isRowSelected(row);
            Object dataNode = path.getLastPathComponent();
            boolean isLeaf = dataModel.isLeaf(dataNode);
            boolean expanded = cellInfo.isExpanded();
            beforeShow(labelElem);
            ItsNatTreeCellEditor cellEditor = getItsNatTreeCellEditor();
            ItsNatComponent compEditor = cellEditor.getTreeCellEditorComponent(tree,row,dataNode,isSelected, expanded,isLeaf,labelElem);
            afterShow(compEditor);
        }
    }

    protected void openEditor(Event evt)
    {
        Node nodeClicked = (Node)evt.getTarget(); // Puede ser un nodo interior del elemento pulsado

        ItsNatTreeUI compUI = getItsNatTree().getItsNatTreeUIImpl();
        ItsNatTreeCellUI cellInfo = (ItsNatTreeCellUI)compUI.getItsNatTreeCellUIFromNode(nodeClicked);
        if (cellInfo != null) // Se ha pulsado un elemento verdaderamente
        {
            if (DOMUtilInternal.isChildOrSame(nodeClicked,cellInfo.getLabelElement(),cellInfo.getParentElement()))
            {
                // Se ha pulsado el nodo contenido (el handle y el icon no valen para editar)
                openEditor(cellInfo);
            }
        }
    }

    public void acceptNewValue(Object value)
    {
        TreeModel dataModel = getItsNatTree().getTreeModel();
        dataModel.valueForPathChanged(path,value); // Oportunidad del modelo de actualizarse, ver por ejemplo en DefaultTreeModel
    }

    public void clearCurrentContext()
    {
        setCurrentContext(null,null,-1);
    }
}
