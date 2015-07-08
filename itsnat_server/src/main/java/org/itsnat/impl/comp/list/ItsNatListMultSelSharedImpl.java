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

import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;

/**
 *
 * @author jmarranz
 */
public class ItsNatListMultSelSharedImpl extends ItsNatListSharedImpl
{

    /**
     * Creates a new instance of ItsNatListMultSelSharedImpl
     */
    public ItsNatListMultSelSharedImpl(ItsNatListMultSelInternal comp)
    {
        super(comp);
    }

    public ItsNatListMultSelInternal getItsNatListMultSel()
    {
        return (ItsNatListMultSelInternal)comp;
    }

    public ListSelectionModelMgrImpl setListSelectionModel(ListSelectionModel selectionModel)
    {
        ItsNatListMultSelInternal comp = getItsNatListMultSel();
        int size = comp.getListModel().getSize();
        return ListSelectionModelMgrImpl.newListSelectionModelMgr(selectionModel,size);
    }

    public void initialSyncSelModelWithDataModel()
    {
        ItsNatListMultSelInternal comp = getItsNatListMultSel();
        ListSelectionModelMgrImpl selModelMgr = comp.getListSelectionModelMgr();
        if (selModelMgr != null)
        {
            ListModel dataModel = comp.getListModel();
            selModelMgr.syncWithDataModel(dataModel.getSize());
        }
    }

    public void contentsChanged(ListDataEvent e)
    {
        super.contentsChanged(e);

        // Es lanzado por el dataModel del usuario
        // El ListModel y concretamente el DefaultListModel sí soportan
        // el cambio de valor de un elemento, y sin embargo al contrario
        // que en el caso ComboBox este evento no debe ser debido a cambios
        // en la selección pues para eso está el SelectionModel.
        ItsNatListMultSelInternal comp = getItsNatListMultSel();
        ItsNatListMultSelUIInternal compUI = comp.getItsNatListMultSelUIInternal();

        int index0 = e.getIndex0();
        int index1 = e.getIndex1();

        ListModel dataModel = (ListModel)e.getSource();
        ListSelectionModel selModel = comp.getListSelectionModel();
        // Necesariamente es index0 <= index1
        for(int i = index0; i <= index1; i++)
        {
            Object obj = dataModel.getElementAt(i);
            boolean isSelected = selModel.isSelectedIndex(i);
            compUI.setElementValueAt(i,obj,isSelected,false);
        }
    }

    public void setListData(final Object[] listData)
    {
        // Similar a JList
        ItsNatListMultSelInternal comp = getItsNatListMultSel();
        AbstractListModel dataModel = new AbstractListModel()
        {
            public int getSize() { return listData.length; }
            public Object getElementAt(int i) { return listData[i]; }
        };
        comp.setListModel(dataModel);
    }

    public void setListData(final List<Object> listData)
    {
        // Similar a JList
        ItsNatListMultSelInternal comp = getItsNatListMultSel();
        AbstractListModel dataModel = new AbstractListModel()
        {
            public int getSize() { return listData.size(); }
            public Object getElementAt(int i) { return listData.get(i); }
        };
        comp.setListModel(dataModel);
    }

    public void setDefaultModels()
    {
        ItsNatListMultSelInternal comp = getItsNatListMultSel();

        // Después de la iniciación del data model
        comp.setListSelectionModel(new DefaultListSelectionModel());
    }

    public void unbindModels()
    {
        ItsNatListMultSelInternal comp = getItsNatListMultSel();
        comp.unsetListSelectionModel();
    }
}
