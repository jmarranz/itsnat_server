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
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.impl.comp.*;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.comp.list.ItsNatListCellUI;
import org.itsnat.core.NameValue;
import org.w3c.dom.Element;
import org.w3c.dom.events.MouseEvent;

/**
 * Parecido a JList
 *
 * @author jmarranz
 */
public class ItsNatFreeListMultSelImpl extends ItsNatFreeListImpl implements ItsNatFreeListMultSel,ItsNatListMultSelInternal
{
    protected ListSelectionModelMgrImpl selModelMgr;
    protected ListCellEditorProcessor editorProcessor = new ListCellEditorProcessor(this);
    protected boolean selectionUsesKeyboard;

    /**
     * Creates a new instance of ItsNatFreeListMultSelImpl
     */
    public ItsNatFreeListMultSelImpl(Element element,ItsNatListStructure structure,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(element,structure,artifacts,componentMgr);

        this.selectionUsesKeyboard = getDefaultSelectionOnComponentsUsesKeyboard();

        init();
    }

    public void init()
    {
        super.init();

        // Aunque está diseñado para el caso múltiple admitiría también no multiple
        // aunque para eso está mejor el componente ComboBox.
        ItsNatDocComponentManagerImpl compMgr = getItsNatComponentManagerImpl();
        setItsNatListCellEditor(compMgr.createDefaultItsNatListCellEditor(null));
    }

    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        editorProcessor.enableEventListenersByDoc();
    }

    public void disableEventListenersByDoc(boolean updateClient)
    {
        super.disableEventListenersByDoc(updateClient);

        editorProcessor.disableEventListeners(updateClient);
    }

    public ItsNatListMultSelSharedImpl getItsNatListMultSelShared()
    {
        return (ItsNatListMultSelSharedImpl)delegate;
    }

    public ItsNatListSharedImpl createItsNatListShared()
    {
        return new ItsNatListMultSelSharedImpl(this);
    }

    public void setDefaultModels()
    {
        super.setDefaultModels();

        getItsNatListMultSelShared().setDefaultModels();
    }

    public void unbindModels()
    {
        getItsNatListMultSelShared().unbindModels();

        super.unbindModels();
    }

    public ListCellEditorProcessor getListCellEditorProcessor()
    {
        return editorProcessor;
    }

    public void processMouseClick(MouseEvent mouseEvt,ItsNatListCellUI option)
    {
        int index = option.getIndex();
        boolean toggle;
        if (!isSelectionUsesKeyboard()) toggle = true;
        else toggle = mouseEvt.getCtrlKey();
        boolean extend = mouseEvt.getShiftKey();
        boolean selected = selModelMgr.getListSelectionModel().isSelectedIndex(index);
        selModelMgr.changeSelectionModel(index,toggle,extend,selected);
    }

    public ItsNatListMultSelUIInternal getItsNatListMultSelUIInternal()
    {
        return (ItsNatListMultSelUIInternal)compUI;
    }

    public ItsNatFreeListMultSelUIImpl getItsNatFreeListMultSelUIImpl()
    {
        return (ItsNatFreeListMultSelUIImpl)compUI;
    }
    
    public ItsNatListMultSelUIInternal createDefaultItsNatFreeListMultSelUI()
    {
        return new ItsNatFreeListMultSelUIImpl(this);
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return createDefaultItsNatFreeListMultSelUI();
    }

    public ListModel getListModel()
    {
        return (ListModel)dataModel;
    }

    public void setListModel(ListModel dataModel)
    {
        setDataModel(dataModel);
    }

    public void	setListData(Object[] listData)
    {
        getItsNatListMultSelShared().setListData(listData);
    }

    public void	setListData(List<Object> listData)
    {
        getItsNatListMultSelShared().setListData(listData);
    }

    public ListSelectionModelMgrImpl getListSelectionModelMgr()
    {
        return selModelMgr;
    }

    public ListSelectionModel getListSelectionModel()
    {
        if (selModelMgr == null)
            return null;
        return selModelMgr.getListSelectionModel();
    }

    public void unsetListSelectionModel()
    {
        if (selModelMgr != null)
            selModelMgr.dispose();
        // No anulamos el selModelMgr para que se pueda recuperar el ListSelectionModel después de un disposeEffective
    }

    public void setListSelectionModel(ListSelectionModel selectionModel)
    {
        this.selModelMgr = getItsNatListMultSelShared().setListSelectionModel(selectionModel);
    }

    public int getSelectedIndex()
    {
        return getListSelectionModel().getMinSelectionIndex();
    }

    public void setSelectedIndex(int index)
    {
        getListSelectionModel().setSelectionInterval(index,index);
    }

    public int[] getSelectedIndices()
    {
        return selModelMgr.getSelectedIndices();
    }

    public void setSelectedIndices(int[] indices)
    {
        selModelMgr.setSelectedIndices(indices);
    }

    public void setSelectionMode(int selectionMode)
    {
        getListSelectionModel().setSelectionMode(selectionMode);
    }

    public Object createDefaultModelInternal()
    {
        return createDefaultListModel();
    }

    public ListModel createDefaultListModel()
    {
        return new DefaultListModel();
    }

    public void initialSyncUIWithDataModel()
    {
        super.initialSyncUIWithDataModel();

        getItsNatListMultSelShared().initialSyncSelModelWithDataModel();
    }

    public void insertElementAtInternal(int index,Object anObject)
    {
        super.insertElementAtInternal(index,anObject);

        selModelMgr.insertElementUpdateModel(index);
    }

    public void removeElementRangeInternal(int fromIndex,int toIndex)
    {
        super.removeElementRangeInternal(fromIndex,toIndex);

        selModelMgr.removeRangeUpdateModel(fromIndex,toIndex);
    }

    public ItsNatListCellEditor getItsNatListCellEditor()
    {
        ListCellEditorProcessor editorProcessor = getListCellEditorProcessor();
        return editorProcessor.getItsNatListCellEditor();
    }

    public void setItsNatListCellEditor(ItsNatListCellEditor cellEditor)
    {
        editorProcessor.setItsNatListCellEditor(cellEditor);
    }

    public void startEditingAt(int index)
    {
        getListCellEditorProcessor().startEdition(index);
    }

    public boolean isEditing()
    {
        return getListCellEditorProcessor().isEditing();
    }

    public int getEditingIndex()
    {
        return getListCellEditorProcessor().getIndex();
    }

    public String getEditorActivatorEvent()
    {
        return getListCellEditorProcessor().getEditorActivatorEvent();
    }

    public void setEditorActivatorEvent(String editorActivatorEvent)
    {
        getListCellEditorProcessor().setEditorActivatorEvent(editorActivatorEvent);
    }

    public boolean isEditingEnabled()
    {
        return getListCellEditorProcessor().isEditingEnabled();
    }

    public void setEditingEnabled(boolean value)
    {
        getListCellEditorProcessor().setEditingEnabled(value);
    }

    public void setSelectionUsesKeyboard(boolean value)
    {
        this.selectionUsesKeyboard = value;
    }

    public boolean isSelectionUsesKeyboard()
    {
        return selectionUsesKeyboard;
    }
}
