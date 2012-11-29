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

import java.util.ArrayList;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByClientImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByDocImpl;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.comp.*;
import org.itsnat.impl.comp.list.ListSelectionModelMgrImpl;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableHeader;
import org.itsnat.comp.table.ItsNatTableHeaderCellRenderer;
import org.itsnat.comp.table.ItsNatTableHeaderCellUI;
import org.itsnat.comp.table.ItsNatTableHeaderUI;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersJoystickSharedImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatTableHeaderImpl extends ItsNatElementComponentImpl implements ItsNatTableHeader
{
    protected boolean enabled = true;
    protected ItsNatTableHeaderCellRenderer renderer;
    protected ItsNatTableImpl tableComp;
    protected ListSelectionModelMgrImpl selModelMgr;


    /**
     * Creates a new instance of ItsNatTableHeaderImpl
     */
    public ItsNatTableHeaderImpl(ItsNatTableImpl tableComp, Element headerElem)
    {
        // El element puede ser nulo (no hay header)
        super(headerElem, null, tableComp.getItsNatComponentManagerImpl());

        this.tableComp = tableComp;

        ItsNatDocComponentManagerImpl componentMgr = getItsNatComponentManagerImpl();

        setItsNatTableHeaderCellRenderer(componentMgr.createDefaultItsNatTableHeaderCellRenderer());
    }

    public ItsNatCompDOMListenersByDocImpl createItsNatCompDOMListenersByDoc()
    {
        return null;
    }

    public ItsNatCompDOMListenersByClientImpl createItsNatCompDOMListenersByClient(ClientDocumentImpl clientDoc)
    {
        return null;
    }

    public void setDefaultModels()
    {
        super.setDefaultModels();

        // Después de la iniciación del data model
        setDefaultListSelectionModel();
    }

    public void setDefaultListSelectionModel()
    {
        ListSelectionModel selModel = new DefaultListSelectionModel();
        selModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setListSelectionModel(selModel);
    }

    public Object createDefaultStructure()
    {
        return null; // La estructura se debe obtener a través de ItsNatTable
    }

    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        // Por ahora nada porque los eventos (click de selección de la columna)
        // pues en el header se procesan en la clase Table asociada
    }

    public ItsNatTable getItsNatTable()
    {
        return tableComp;
    }

    public ItsNatTableImpl getItsNatTableImpl()
    {
        return tableComp;
    }

    public ItsNatTableHeaderUI getItsNatTableHeaderUI()
    {
        return (ItsNatTableHeaderUI)compUI;
    }

    public ItsNatTableHeaderUIImpl getItsNatTableHeaderUIImpl()
    {
        return (ItsNatTableHeaderUIImpl)compUI;
    }    

    public ItsNatTableHeaderCellRenderer getItsNatTableHeaderCellRenderer()
    {
        return renderer;
    }

    public void setItsNatTableHeaderCellRenderer(ItsNatTableHeaderCellRenderer renderer)
    {
        this.renderer = renderer;
    }

    public void bindDataModel()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public void unbindDataModel()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public void initialSyncUIWithDataModel()
    {
    }

    public Object createDefaultModelInternal()
    {
        return null;
    }

    public ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        return null;
    }

    public ListSelectionModel getListSelectionModel()
    {
        if (selModelMgr == null)
        {
            return null;
        }
        return selModelMgr.getListSelectionModel();
    }

    public void unsetListSelectionModel()
    {
        if (selModelMgr != null)
        {
            selModelMgr.dispose();
        // No anulamos el selModelMgr para que se pueda recuperar el ListSelectionModel después de un disposeEffective
        }
    }

    public void setListSelectionModel(ListSelectionModel selectionModel)
    {
        unsetListSelectionModel();

        int size = getItsNatTable().getTableModel().getColumnCount();
        this.selModelMgr = ListSelectionModelMgrImpl.newListSelectionModelMgr(selectionModel, size);
    }

    public int getSelectedIndex()
    {
        return getListSelectionModel().getMinSelectionIndex();
    }

    public void setSelectedIndex(int index)
    {
        getListSelectionModel().setSelectionInterval(index, index);
    }

    public ItsNatTableHeaderCellUI processEvent(Node nodeClicked, boolean toggle, boolean extend)
    {
        // Es llamado desde ItsNatTableImpl
        // Vemos si ha sido pulsada la cabecera

        ItsNatTableHeaderUI headerUI = getItsNatTableHeaderUI();
        ItsNatTableHeaderCellUI colCellInfo = headerUI.getItsNatTableHeaderCellUIFromNode(nodeClicked);
        if (colCellInfo == null)
        {
            return null;
        }
        int column = colCellInfo.getIndex();
        changeColumnSelection(column, toggle, extend);

        return colCellInfo;
    }

    public void changeColumnSelection(int columnIndex, boolean toggle, boolean extend)
    {
        ListSelectionModel csm = getListSelectionModel();

        boolean selected = csm.isSelectedIndex(columnIndex);

        selModelMgr.changeSelectionModel(columnIndex, toggle, extend, selected);
    }

    public void copyHeaderValuesFromDataModelToUI()
    {
        ItsNatTableHeaderUIImpl compUI = getItsNatTableHeaderUIImpl();
        TableModel dataModel = getItsNatTable().getTableModel();
        int columns = dataModel.getColumnCount();
        for(int i = 0; i < columns; i++)
        {
            String columnName = dataModel.getColumnName(i);
            compUI.setElementValueAt(i,columnName,false,false);
        }
    }

    public void setDOMColumnCount(int cols)
    {
        ItsNatTableHeaderUIImpl compUI = getItsNatTableHeaderUIImpl();
        int oldCols = compUI.getLength();

        if (cols < oldCols)
            removeInternalEventListenerJoystickMode(cols,oldCols - 1);

        compUI.setLength(cols);

        if (cols > oldCols)
            addInternalEventListenerJoystickMode(oldCols,cols - 1);
    }

    public void insertDOMColumn(int index,Object value)
    {
        Element elem = getItsNatTableHeaderUIImpl().insertElementAt(index,value);

        addInternalEventListenerJoystickMode(index,elem);
    }

    public void removeDOMColumn(int index)
    {
        removeInternalEventListenerJoystickMode(index);

        getItsNatTableHeaderUIImpl().removeElementAt(index);
    }

    public Element[] getContentElementList(Element[] elemList,int colCount)
    {
        ItsNatTableHeaderUI compUI = getItsNatTableHeaderUI();
        int from = elemList.length - colCount;
        for(int i = 0; i < colCount; i++)
            elemList[i + from] = compUI.getContentElementAt(i);
        return elemList;
    }

    public void addInternalEventListenerJoystickMode(int index,Element elem)
    {
        ItsNatTableImpl tableComp = getItsNatTableImpl();
        ArrayList domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(tableComp);
        if (domListeners.isEmpty())
            return;

        Element contentElem = tableComp.getItsNatTableStructure().getHeaderColumnContentElement(this, index, elem);

        ItsNatCompDOMListenersJoystickSharedImpl.addEventListenerJoystick(domListeners, contentElem);
    }

    public void addInternalEventListenerJoystickMode(int index)
    {
        ItsNatTableImpl tableComp = getItsNatTableImpl();
        ArrayList domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(tableComp);
        if (domListeners.isEmpty())
            return;

        addInternalEventListenerJoystickMode(domListeners,index);
    }

    public void removeInternalEventListenerJoystickMode(int index)
    {
        ItsNatTableImpl tableComp = getItsNatTableImpl();
        ArrayList domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(tableComp);
        if (domListeners.isEmpty())
            return;

        removeInternalEventListenerJoystickMode(domListeners,index);
    }

    public void addInternalEventListenerJoystickMode(ArrayList domListeners,int index)
    {
        ItsNatTableHeaderUI compUI = getItsNatTableHeaderUI();
        Element contentElem = compUI.getContentElementAt(index);

        ItsNatCompDOMListenersJoystickSharedImpl.addEventListenerJoystick(domListeners, contentElem);
    }

    public void removeInternalEventListenerJoystickMode(ArrayList domListeners,int index)
    {
        ItsNatTableHeaderUI compUI = getItsNatTableHeaderUI();
        Element contentElem = compUI.getContentElementAt(index);

        ItsNatCompDOMListenersJoystickSharedImpl.removeEventListenerJoystick(domListeners, contentElem);
    }


    public void addInternalEventListenerJoystickMode(int fromIndex,int toIndex)
    {
        ItsNatTableImpl tableComp = getItsNatTableImpl();
        ArrayList domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(tableComp);
        if (domListeners.isEmpty())
            return;

        for(int i = fromIndex; i <= toIndex; i++)
            addInternalEventListenerJoystickMode(domListeners,i);
    }

    public void removeInternalEventListenerJoystickMode(int fromIndex,int toIndex)
    {
        ItsNatTableImpl tableComp = getItsNatTableImpl();
        ArrayList domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(tableComp);
        if (domListeners.isEmpty())
            return;

        for(int i = fromIndex; i <= toIndex; i++)
            removeInternalEventListenerJoystickMode(domListeners,i);
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean b)
    {
        this.enabled = b;
    }

}
