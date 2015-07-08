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

import org.itsnat.core.ItsNatException;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataEvent;

/**
 *
 * @author jmarranz
 */
public class ItsNatComboBoxSharedImpl extends ItsNatListSharedImpl
{

    /**
     * Creates a new instance of ItsNatComboBoxSharedImpl
     */
    public ItsNatComboBoxSharedImpl(ItsNatComboBoxInternal comp)
    {
        super(comp);
    }

    public ItsNatComboBoxInternal getItsNatComboBox()
    {
        return (ItsNatComboBoxInternal)comp;
    }

    public void syncWithDataModel()
    {
        ItsNatComboBoxInternal comp = getItsNatComboBox();
        comp.setSelectedItemReminder(comp.getSelectedItem());
    }

    public void initialSyncUIWithDataModel()
    {
        super.initialSyncUIWithDataModel();

        copyCurrentSelectionToUI();
    }

    public static void checkMutableComboBoxModel(ComboBoxModel model)
    {
        if (!(model instanceof MutableComboBoxModel))
            throw new ItsNatException("Expected a MutableComboBoxModel",model);
    }

    public void removeAllElements()
    {
        ItsNatComboBoxInternal comp = getItsNatComboBox();
        MutableComboBoxModel dataModel = (MutableComboBoxModel)comp.getComboBoxModel();

        if (dataModel instanceof DefaultComboBoxModel)
        {
            ((DefaultComboBoxModel)dataModel).removeAllElements();
        }
        else
        {
            while(dataModel.getSize() > 0)
            {
                Object element = dataModel.getElementAt( 0 );
                dataModel.removeElement( element );
            }
        }
    }

    public int getSelectedIndex()
    {
        ItsNatComboBoxInternal comp = getItsNatComboBox();
        ComboBoxModel dataModel = comp.getComboBoxModel();
        Object selected = dataModel.getSelectedItem();
        return indexOf(selected,dataModel);
    }

    public void setSelectedIndex(int index)
    {
        // Se propagará el cambio al DOM via evento
        ItsNatComboBoxInternal comp = getItsNatComboBox();
        ComboBoxModel dataModel = comp.getComboBoxModel();
        setSelectedIndex(dataModel,index);
    }

    public static void setSelectedIndex(ComboBoxModel dataModel,int index)
    {
        Object selected = dataModel.getElementAt(index);
        dataModel.setSelectedItem(selected); // Notar que como haya elementos repetidos puede fallar
    }

    public Object getSelectedItem()
    {
        return getItsNatComboBox().getComboBoxModel().getSelectedItem();
    }

    public void setSelectedItem(Object anObject)
    {
        ItsNatComboBoxInternal comp = getItsNatComboBox();
        comp.getComboBoxModel().setSelectedItem(anObject);
    }

    public Object[] getSelectedObjects()
    {
        ItsNatComboBoxInternal comp = getItsNatComboBox();
        ComboBoxModel dataModel = comp.getComboBoxModel();
        Object selectedObject = dataModel.getSelectedItem();
        if ( selectedObject == null )
            return new Object[0];
        else
            return new Object[] { selectedObject };
    }

    public void fireItemStateChanged(ItemEvent e)
    {
        ItsNatComboBoxInternal comp = getItsNatComboBox();
        if (comp.hasItemListeners())
        {
            ItemListener[] listeners = comp.getItemListeners();
            for (int i = 0; i < listeners.length; i++ )
            {
                listeners[i].itemStateChanged(e);
            }
        }
    }

    public void contentsChanged(ListDataEvent e)
    {
        super.contentsChanged(e);

        // No necesitamos el objeto evento
        copyCurrentSelectionToUI();

        // Lanzamos el evento a los ItemListener del cambio de selección
        selectedItemChanged();
    }

    public void intervalAdded(ListDataEvent e)
    {
        super.intervalAdded(e);

        // Lanzamos el evento a los ItemListener del cambio de selección (posible)
        selectedItemChanged();
    }

    public void intervalRemoved(ListDataEvent e)
    {
        super.intervalRemoved(e);

        // Lanzamos el evento a los ItemListener del cambio de selección (posible)
        selectedItemChanged();
    }

    public void copyCurrentSelectionToUI()
    {
        ItsNatComboBoxInternal comp = getItsNatComboBox();
        ComboBoxModel dataModel = comp.getComboBoxModel();

        // Copiamos la selección actual
        Object selectedObj = dataModel.getSelectedItem();
        int index = indexOf(selectedObj,dataModel);

        comp.setUISelectedIndex(index);
    }

    public void selectedItemChanged()
    {
        ItsNatComboBoxInternal comp = getItsNatComboBox();

	Object oldSelection = comp.getSelectedItemReminder();
	Object newSelection = comp.getComboBoxModel().getSelectedItem();
        if (oldSelection == newSelection)
            return;

	if (oldSelection != null )
	    comp.fireItemStateChanged(new ItemEvent(comp,ItemEvent.ITEM_STATE_CHANGED,
					       oldSelection,
					       ItemEvent.DESELECTED));

	// set the new selected item.
	comp.setSelectedItemReminder(newSelection);

	if (newSelection != null )
	    comp.fireItemStateChanged(new ItemEvent(comp,ItemEvent.ITEM_STATE_CHANGED,
					       newSelection,
					       ItemEvent.SELECTED));
    }

}
