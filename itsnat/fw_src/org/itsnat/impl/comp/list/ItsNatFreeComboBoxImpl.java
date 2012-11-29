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

import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.comp.list.ItsNatListCellEditor;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.core.ItsNatException;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.EventListenerList;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.comp.list.ItsNatListCellUI;
import org.itsnat.core.NameValue;
import org.w3c.dom.Element;
import org.w3c.dom.events.MouseEvent;

/**
 *
 * @author jmarranz
 */
public class ItsNatFreeComboBoxImpl extends ItsNatFreeListImpl implements ItsNatFreeComboBox,ItsNatComboBoxInternal
{
    protected Object selectedItemReminder;
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * Creates a new instance of ItsNatFreeComboBoxImpl
     */
    public ItsNatFreeComboBoxImpl(Element element,ItsNatListStructure structure,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(element,structure,artifacts,componentMgr);

        init();
    }

    public ItsNatComboBoxSharedImpl getItsNatComboBoxShared()
    {
        return (ItsNatComboBoxSharedImpl)delegate;
    }

    public ItsNatListSharedImpl createItsNatListShared()
    {
        return new ItsNatComboBoxSharedImpl(this);
    }

    public Object getSelectedItemReminder()
    {
        return selectedItemReminder;
    }

    public void setSelectedItemReminder(Object obj)
    {
        this.selectedItemReminder = obj;
    }

    public void processMouseClick(MouseEvent mouseEvt,ItsNatListCellUI option)
    {
        int selectedIndex = option.getIndex();
        setSelectedIndex(selectedIndex);
    }

    public ItsNatComboBoxUIInternal getItsNatComboBoxUIInternal()
    {
        return (ItsNatComboBoxUIInternal)compUI;
    }

    public ItsNatFreeComboBoxUIImpl getItsNatFreeComboBoxUIImpl()
    {
        return (ItsNatFreeComboBoxUIImpl)compUI;
    }
    
    public ItsNatComboBoxUIInternal createDefaultItsNatFreeComboBoxUI()
    {
        return new ItsNatFreeComboBoxUIImpl(this);
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return createDefaultItsNatFreeComboBoxUI();
    }

    public void initialSyncWithDataModel()
    {
        super.initialSyncWithDataModel();

        getItsNatComboBoxShared().syncWithDataModel();
    }

    public ComboBoxModel getComboBoxModel()
    {
        return (ComboBoxModel)dataModel;
    }

    public void setComboBoxModel(ComboBoxModel dataModel)
    {
        setDataModel(dataModel);
    }

    public ListModel getListModel()
    {
        return getComboBoxModel();
    }

    public void setListModel(ListModel dataModel)
    {
        setComboBoxModel((ComboBoxModel)dataModel);
    }

    public void checkMutableComboBoxModel()
    {
        ItsNatComboBoxSharedImpl.checkMutableComboBoxModel(getComboBoxModel());
    }

    public void addElement(Object anObject)
    {
        checkMutableComboBoxModel();
        MutableComboBoxModel model = (MutableComboBoxModel)getComboBoxModel();
        model.addElement(anObject);
    }

    public void insertElementAt(int index,Object anObject)
    {
        checkMutableComboBoxModel();
        MutableComboBoxModel model = (MutableComboBoxModel)getComboBoxModel();
        model.insertElementAt(anObject,index);
    }

    public void removeElementAt(int anIndex)
    {
        checkMutableComboBoxModel();
        MutableComboBoxModel model = (MutableComboBoxModel)getComboBoxModel();
        model.removeElementAt(anIndex);
    }

    public void removeAllElements()
    {
        checkMutableComboBoxModel();
        getItsNatComboBoxShared().removeAllElements();
    }

    public void removeElement(Object anObject)
    {
        checkMutableComboBoxModel();
        MutableComboBoxModel model = (MutableComboBoxModel)getComboBoxModel();
        model.removeElement(anObject);
    }

    public int getSelectedIndex()
    {
        return getItsNatComboBoxShared().getSelectedIndex();
    }

    public void setSelectedIndex(int index)
    {
        getItsNatComboBoxShared().setSelectedIndex(index);
    }

    public Object getSelectedItem()
    {
        return getItsNatComboBoxShared().getSelectedItem();
    }

    public void setSelectedItem(Object anObject)
    {
        getItsNatComboBoxShared().setSelectedItem(anObject);
    }

    public Object[] getSelectedObjects()
    {
        return getItsNatComboBoxShared().getSelectedObjects();
    }

    public void addItemListener(ItemListener l)
    {
        listenerList.add(ItemListener.class,l);
    }

    public void removeItemListener(ItemListener l)
    {
        listenerList.remove(ItemListener.class,l);
    }

    public boolean hasItemListeners()
    {
        return listenerList.getListenerCount() > 0;
    }

    public ItemListener[] getItemListeners()
    {
        return (ItemListener[])listenerList.getListeners(ItemListener.class);
    }

    public void fireItemStateChanged(ItemEvent e)
    {
        getItsNatComboBoxShared().fireItemStateChanged(e);
    }

    public Object createDefaultModelInternal()
    {
        return createDefaultListModel();
    }

    public ListModel createDefaultListModel()
    {
        return new DefaultComboBoxModel();
    }

    public ItsNatListCellEditor getItsNatListCellEditor()
    {
        return null;
    }

    public void setItsNatListCellEditor(ItsNatListCellEditor cellEditor)
    {
        throw new ItsNatException("Combo box items can not be graphically edited",this);
    }

    public void startEditingAt(int index)
    {
        throw new ItsNatException("Combo box items can not be graphically edited",this);
    }

    public boolean isEditing()
    {
        return false;
    }

    public int getEditingIndex()
    {
        return -1;
    }

    public String getEditorActivatorEvent()
    {
        throw new ItsNatException("Combo box items can not be graphically edited",this);
    }

    public void setEditorActivatorEvent(String event)
    {
        throw new ItsNatException("Combo box items can not be graphically edited",this);
    }

    public boolean isEditingEnabled()
    {
        return false;
    }

    public void setEditingEnabled(boolean value)
    {
        throw new ItsNatException("Combo box items can not be graphically edited",this);
    }

    public void setUISelectedIndex(int index)
    {
        ItsNatFreeComboBoxUIImpl compUI = getItsNatFreeComboBoxUIImpl();
        compUI.setSelectedIndex(index);
    }
}
