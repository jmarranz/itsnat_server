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

import java.io.Serializable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;

/**
 *
 * @author jmarranz
 */
public class ItsNatListSharedImpl implements Serializable
{
    protected ItsNatListInternal comp;

    /**
     * Creates a new instance of ItsNatListSharedImpl
     */
    public ItsNatListSharedImpl(ItsNatListInternal comp)
    {
        this.comp = comp;
    }

    public static int indexOf(Object obj,ListModel dataModel)
    {
        if (obj == null) return -1;

        if (dataModel instanceof DefaultListModel)
            return ((DefaultListModel)dataModel).indexOf(obj);
        else if (dataModel instanceof DefaultComboBoxModel)
            return ((DefaultComboBoxModel)dataModel).getIndexOf(obj);
        else
            return ListDataModelUtil.indexOf(obj,dataModel);
    }


    public void bindDataModel()
    {
        // A partir de ahora los cambios los repercutimos en el DOM por eventos
        // No se debe cambiar el DOM select por otra vía que por el objeto dataModel del usuario (si es mutable)

        ListModel dataModel = comp.getListModel();
        dataModel.addListDataListener(comp);
    }

    public void unbindDataModel()
    {
        ListModel dataModel = comp.getListModel();
        dataModel.removeListDataListener(comp);
    }

    public void initialSyncUIWithDataModel()
    {
        ItsNatListUIInternal compUI = comp.getItsNatListUIInternal();
        compUI.removeAllElements();

        ListModel dataModel = comp.getListModel();
        int size = dataModel.getSize();
        for(int i = 0; i < size; i++)
        {
            Object item = dataModel.getElementAt(i);
            compUI.insertElementAt(i,item);
        }
    }

    public void intervalAdded(ListDataEvent e)
    {
        // Sincronizamos con el DOM
        ListModel dataModel = (ListModel)e.getSource();
        int index0 = e.getIndex0();
        int index1 = e.getIndex1();
        for(int i = index0; i <= index1; i++)
        {
            Object item = dataModel.getElementAt(i);
            comp.insertElementAtInternal(i,item);
        }
    }

    public void intervalRemoved(ListDataEvent e)
    {
        // Sincronizamos con el DOM
        int index0 = e.getIndex0();
        int index1 = e.getIndex1();
        comp.removeElementRangeInternal(index0,index1);
    }

    public void contentsChanged(ListDataEvent e)
    {
        // Nada que hacer en este nivel
    }

}
