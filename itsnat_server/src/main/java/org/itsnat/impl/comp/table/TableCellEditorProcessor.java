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

package org.itsnat.impl.comp.table;

import org.itsnat.impl.comp.inplace.EditorProcessorBaseImpl;
import org.itsnat.impl.comp.*;
import org.itsnat.comp.table.ItsNatTableCellEditor;
import javax.swing.table.TableModel;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.table.ItsNatTableCellUI;
import org.itsnat.comp.table.ItsNatTableUI;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class TableCellEditorProcessor extends EditorProcessorBaseImpl
{
    protected ItsNatTableCellUI cellInfo;

    public TableCellEditorProcessor(ItsNatTableImpl compParent)
    {
        super(compParent);
    }

    public ItsNatTableImpl getItsNatTable()
    {
        return (ItsNatTableImpl)compParent;
    }

    public ItsNatTableCellEditor getItsNatTableCellEditor()
    {
        return (ItsNatTableCellEditor)cellEditor;
    }

    public void setItsNatTableCellEditor(ItsNatTableCellEditor cellEditor)
    {
        setCellEditor(cellEditor);
    }

    public void setCurrentContext(ItsNatTableCellUI cellInfo)
    {
        this.cellInfo = cellInfo;
    }

    public int getRow()
    {
        if (cellInfo == null)
            return -1;
        return cellInfo.getRowIndex();
    }

    public int getColumn()
    {
        if (cellInfo == null)
            return -1;
        return cellInfo.getColumnIndex();
    }

    public void startEdition(int row,int column)
    {
        if (prepareEdition())
        {
            openEditor(row,column);
        }
    }

    protected void openEditor(int row,int column)
    {
        ItsNatTableUI bodyUI = getItsNatTable().getItsNatTableUI();
        ItsNatTableCellUI cellInfo = bodyUI.getItsNatTableCellUIAt(row,column);
        openEditor(cellInfo);
    }

    protected void openEditor(Event evt)
    {
        Node nodeClicked = (Node)evt.getTarget(); // Puede ser un nodo interior del elemento pulsado

        ItsNatTableUI bodyUI = getItsNatTable().getItsNatTableUI();
        ItsNatTableCellUI cellInfo = bodyUI.getItsNatTableCellUIFromNode(nodeClicked);
        openEditor(cellInfo);
    }

    protected void openEditor(ItsNatTableCellUI cellInfo)
    {
        if (cellInfo != null) // Se ha pulsado una celda verdaderamente
        {
            int row = cellInfo.getRowIndex();
            int column = cellInfo.getColumnIndex();
            ItsNatTableImpl table = getItsNatTable();
            TableModel dataModel = table.getTableModel();
            if (dataModel.isCellEditable(row,column))
            {
                setCurrentContext(cellInfo);
                Element cellContentElem = cellInfo.getCellContentElement();
                Object value = dataModel.getValueAt(row,column);
                boolean isSelected = table.isCellSelected(row,column);
                beforeShow(cellContentElem);
                ItsNatTableCellEditor cellEditor = getItsNatTableCellEditor();
                ItsNatComponent compEditor = cellEditor.getTableCellEditorComponent(table,row,column,value,isSelected,cellContentElem);
                afterShow(compEditor);
            }
        }
    }

    public void acceptNewValue(Object value)
    {
        int row = cellInfo.getRowIndex();
        int column = cellInfo.getColumnIndex();
        getItsNatTable().getTableModel().setValueAt(value,row,column); // Si se ha cambiado se renderiza el cambio
    }

    public void clearCurrentContext()
    {
        setCurrentContext(null);
    }
}