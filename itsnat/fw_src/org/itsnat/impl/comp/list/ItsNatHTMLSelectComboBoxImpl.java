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

import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.ItsNatComponentUI;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.EventListenerList;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.comp.mgr.web.ItsNatStfulWebDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLSelectElement;

/**
 * Similar a JComboBox
 * @author jmarranz
 */
public class ItsNatHTMLSelectComboBoxImpl extends ItsNatHTMLSelectImpl implements ItsNatHTMLSelectComboBox,ItsNatComboBoxInternal
{
    protected Object selectedItemReminder;
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * Creates a new instance of ItsNatHTMLSelectComboBoxImpl
     */
    public ItsNatHTMLSelectComboBoxImpl(HTMLSelectElement element,NameValue[] artifacts,ItsNatStfulWebDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        // No imponemos así podríamos en teoría asociar al tipo de select que queramos
        //getHTMLSelectElement().setMultiple(false);

        init();
    }

    public ItsNatComboBoxSharedImpl getItsNatComboBoxShared()
    {
        return (ItsNatComboBoxSharedImpl)listDelegate;
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

    public ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        if (changeBasedDelegate.isChangeEvent(type,clientDoc))
        {
            CustomParamTransport selectedIndex = new CustomParamTransport("selectedIndex","event.getCurrentTarget().selectedIndex");
            return new ParamTransport[]{selectedIndex};
        }
        else
            return null;
    }

    public void handleEventOnChange(Event evt)
    {
        // Ejecutado como respuesta al evento "change" en el SELECT en el navegador
        ItsNatEvent itsNatEvent = (ItsNatEvent)evt;
        int selectedIndex = Integer.parseInt((String)itsNatEvent.getExtraParam("selectedIndex"));

        setServerUpdatingFromClient(true); // Pues el evento viene del navegador y no se necesita enviar actualizaciones (salvo observers para que vean el cambio del cliente)

        try
        {
            setSelectedIndex(selectedIndex);
        }
        finally
        {
            setServerUpdatingFromClient(false);
        }
    }

    public ItsNatComboBoxUIInternal getItsNatComboBoxUIInternal()
    {
        return (ItsNatComboBoxUIInternal)compUI;
    }

    public ItsNatHTMLSelectComboBoxUIImpl getItsNatHTMLSelectComboBoxUIImpl()
    {
        return (ItsNatHTMLSelectComboBoxUIImpl)compUI;
    }

    public ItsNatComboBoxUIInternal createDefaultItsNatHTMLSelectComboBoxUI()
    {
        return new ItsNatHTMLSelectComboBoxUIImpl(this);
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        // Por defecto es mutable
        return createDefaultItsNatHTMLSelectComboBoxUI();
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

    public boolean isCombo()
    {
        return true;
    }

    public void setUISelectedIndex(int index)
    {
        ItsNatHTMLSelectComboBoxUIImpl compUI = getItsNatHTMLSelectComboBoxUIImpl();

        boolean wasDisabled = disableSendCodeToRequesterIfServerUpdating(); // Evitamos así que llegue al requester si no debe llegar (pero sí a los observadores)
        try
        {
            compUI.setSelectedIndex(index);
        }
        finally
        {
            if (wasDisabled) enableSendCodeToRequester();
        }
    }

}

