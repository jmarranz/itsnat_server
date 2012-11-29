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

package org.itsnat.impl.comp.list;

import org.itsnat.impl.comp.inplace.EditorProcessorBaseImpl;
import org.itsnat.impl.comp.*;
import org.itsnat.comp.list.ItsNatListCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.list.ItsNatListCellUI;
import org.itsnat.comp.list.ItsNatListUI;
import org.itsnat.impl.comp.list.ItsNatFreeListMultSelImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class ListCellEditorProcessor extends EditorProcessorBaseImpl
{
    protected ItsNatListCellUI cellInfo;

    public ListCellEditorProcessor(ItsNatFreeListMultSelImpl compParent)
    {
        super(compParent);
    }

    public ItsNatFreeListMultSelImpl getItsNatFreeListMultSel()
    {
        return (ItsNatFreeListMultSelImpl)compParent;
    }

    public ItsNatListCellEditor getItsNatListCellEditor()
    {
        return (ItsNatListCellEditor)cellEditor;
    }

    public void setItsNatListCellEditor(ItsNatListCellEditor cellEditor)
    {
        setCellEditor(cellEditor);
    }

    public void setCurrentContext(ItsNatListCellUI cellInfo)
    {
        this.cellInfo = cellInfo;
    }

    public int getIndex()
    {
        if (cellInfo == null)
            return -1;
        return cellInfo.getIndex();
    }

    public void startEdition(int index)
    {
        if (prepareEdition())
        {
            openEditor(index);
        }
    }

    protected void openEditor(int index)
    {
        ItsNatListUI compUI = getItsNatFreeListMultSel().getItsNatListUI();
        ItsNatListCellUI cellInfo = compUI.getItsNatListCellUIAt(index);
        openEditor(cellInfo);
    }

    protected void openEditor(Event evt)
    {
        Node nodeClicked = (Node)evt.getTarget(); // Puede ser un nodo interior del elemento pulsado

        ItsNatListUI compUI = getItsNatFreeListMultSel().getItsNatListUI();
        ItsNatListCellUI cellInfo = compUI.getItsNatListCellUIFromNode(nodeClicked);
        openEditor(cellInfo);
    }

    private void openEditor(ItsNatListCellUI cellInfo)
    {
        if (cellInfo != null) // Se ha pulsado un elemento verdaderamente
        {
            setCurrentContext(cellInfo);

            int index = cellInfo.getIndex();
            ItsNatFreeListMultSelImpl list = getItsNatFreeListMultSel();
            ListModel dataModel = list.getListModel();
            Element cellContentElem = cellInfo.getContentElement();
            Object value = dataModel.getElementAt(index);
            boolean isSelected = list.getListSelectionModel().isSelectedIndex(index);
            beforeShow(cellContentElem);
            ItsNatListCellEditor cellEditor = getItsNatListCellEditor();
            ItsNatComponent compEditor = cellEditor.getListCellEditorComponent(list,index,value,isSelected,cellContentElem);
            afterShow(compEditor);
        }
    }

    public void acceptNewValue(Object value)
    {
        ListModel dataModel = getItsNatFreeListMultSel().getListModel();
        if (dataModel instanceof DefaultListModel)
        {
            int index = cellInfo.getIndex();
            ((DefaultListModel)dataModel).setElementAt(value,index); // Si se ha cambiado se renderiza el cambio
        }
        // En el caso de que el ListModel del usuario no fuera un DefaultListModel
        // no sabemos como actualizar el modelo por lo que será el usuario
        // el que tenga que hacer su propio CellEditorListener y recibir el evento en
        // editingStopped
    }

    public void clearCurrentContext()
    {
        setCurrentContext(null);
    }
}
