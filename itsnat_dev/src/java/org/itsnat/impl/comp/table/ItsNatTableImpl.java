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
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableCellEditor;
import org.itsnat.comp.table.ItsNatTableCellRenderer;
import org.itsnat.comp.table.ItsNatTableCellUI;
import org.itsnat.comp.table.ItsNatTableHeader;
import org.itsnat.comp.table.ItsNatTableHeaderCellUI;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.comp.table.ItsNatTableUI;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.*;
import org.itsnat.impl.comp.list.ListSelectionModelMgrImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByClientImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByClientJoystickImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByDocImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByDocJoystickImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersJoystick;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersJoystickSharedImpl;
import org.itsnat.impl.comp.listener.JoystickModeComponent;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MouseEvent;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatTableImpl extends ItsNatElementComponentImpl implements ItsNatTable,TableModelListener,JoystickModeComponent
{
    protected boolean enabled = true;
    protected ItsNatTableHeaderImpl header;
    protected ItsNatTableCellRenderer renderer;
    protected ListSelectionModelMgrImpl rowSelModelMgr;
    protected ListSelectionModelMgrImpl columnSelModelMgr;
    protected boolean rowSelectionAllowed = true;  // Por defecto
    protected boolean columnSelectionAllowed = false;   // Por defecto
    protected TableCellEditorProcessor editorProcessor = new TableCellEditorProcessor(this);
    protected boolean selectionUsesKeyboard;
    protected ItsNatTableStructure structure;

    /**
     * Creates a new instance of ItsNatTableImpl
     */
    public ItsNatTableImpl(Element element,ItsNatTableStructure structure,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        this.selectionUsesKeyboard = getDefaultSelectionOnComponentsUsesKeyboard();

        this.structure = structure != null ? structure : (ItsNatTableStructure)getDeclaredStructure(ItsNatTableStructure.class);

        setItsNatTableCellRenderer(componentMgr.createDefaultItsNatTableCellRenderer());
    }

    public void init()
    {
        ItsNatTableStructure structure = getItsNatTableStructure();
        Element tableElem = getElement();
        Element headerElem = structure.getHeadElement(this,tableElem);
        if (headerElem != null)
        {
            // Es necesario crear antes de que se cree el UI, DataModel etc y cuando ya tenemos la estructura
            this.header = createItsNatTableHeader(headerElem);
        }

        super.init();

        ItsNatDocComponentManagerImpl compMgr = getItsNatComponentManagerImpl();
        setItsNatTableCellEditor(compMgr.createDefaultItsNatTableCellEditor(null));
    }

    public ItsNatCompDOMListenersByDocImpl createItsNatCompDOMListenersByDoc()
    {
        return new ItsNatCompDOMListenersByDocJoystickImpl(this);
    }

    public ItsNatCompDOMListenersByClientImpl createItsNatCompDOMListenersByClient(ClientDocumentImpl clientDoc)
    {
        return new ItsNatCompDOMListenersByClientJoystickImpl(this,clientDoc);
    }

    @Override
    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        enableEventListener("click");

        editorProcessor.enableEventListenersByDoc();
    }

    @Override
    public void disableEventListenersByDoc(boolean updateClient)
    {
        super.disableEventListenersByDoc(updateClient);

        editorProcessor.disableEventListeners(updateClient);
    }

    public boolean isJoystickMode()
    {
        return getItsNatCompDOMListenersByDocJoystick().isJoystickEnabled();
    }

    public void setJoystickMode(boolean value)
    {
        getItsNatCompDOMListenersByDocJoystick().setJoystickEnabled(value);
    }

    /* Esté método se hará público en el futuro */
    public boolean isJoystickMode(ClientDocument clientDoc)
    {
        return getItsNatCompDOMListenersByClientJoystick((ClientDocumentImpl)clientDoc).isJoystickEnabled();
    }

    /* Esté método se hará público en el futuro */
    public void setJoystickMode(ClientDocument clientDoc,boolean value)
    {
        getItsNatCompDOMListenersByClientJoystick((ClientDocumentImpl)clientDoc).setJoystickEnabled(value);
    }

    public ItsNatCompDOMListenersByDocJoystickImpl getItsNatCompDOMListenersByDocJoystick()
    {
        return (ItsNatCompDOMListenersByDocJoystickImpl)domListenersByDoc;
    }

    public ItsNatCompDOMListenersByClientJoystickImpl getItsNatCompDOMListenersByClientJoystick(ClientDocumentImpl clientDoc)
    {
        return (ItsNatCompDOMListenersByClientJoystickImpl)getItsNatCompDOMListenersByClient(clientDoc);
    }

    @Override
    public void setDefaultItsNatComponentUI()
    {
        if (header != null) // Si tiene header el objeto ya fue creado
            header.setDefaultItsNatComponentUI();

        super.setDefaultItsNatComponentUI();
    }

    @Override
    public void setDefaultModels()
    {
        super.setDefaultModels();

        if (header != null) // Si tiene header el objeto ya fue creado
            header.setDefaultModels();  // El header no tiene modelo pero por si acaso

        setRowSelectionModel(new DefaultListSelectionModel());
        setColumnSelectionModel(new DefaultListSelectionModel());
    }

    @Override
    public void unbindModels()
    {
        if (header != null)
            header.unsetListSelectionModel();

        unsetRowListSelectionModel();
        unsetColumnListSelectionModel();

        super.unbindModels();
    }

    public void unbindDataModel()
    {
        TableModel dataModel = (TableModel)getDataModel();
        dataModel.removeTableModelListener(this);
    }

    public TableCellEditorProcessor getTableCellEditorProcessor()
    {
        return editorProcessor;
    }

    public abstract ItsNatTableHeaderImpl createItsNatTableHeader(Element headerElem);

    public ItsNatTableHeader getItsNatTableHeader()
    {
        return getItsNatTableHeaderImpl();
    }

    public ItsNatTableHeaderImpl getItsNatTableHeaderImpl()
    {
        return header;
    }

    public ItsNatTableUI getItsNatTableUI()
    {
        return (ItsNatTableUI)compUI;
    }

    public ItsNatTableUIImpl getItsNatTableUIImpl()
    {
        return (ItsNatTableUIImpl)compUI;
    }

    public Object createDefaultModelInternal()
    {
        return createDefaultTableModel();
    }

    public TableModel createDefaultTableModel()
    {
        return new DefaultTableModel();
    }

    public void bindDataModel()
    {
        // A partir de ahora los cambios los repercutimos en el DOM por eventos
        // No se debe cambiar el DOM por otra vía que por el objeto dataModel
        TableModel dataModel = (TableModel)getDataModel();
        dataModel.addTableModelListener(this);
    }

    @Override
    public void initialSyncWithDataModel()
    {
        super.initialSyncWithDataModel();

        TableModel dataModel = getTableModel();
        int columns = dataModel.getColumnCount();
        int rows = dataModel.getRowCount();

        updateSelectionModelAddedRemoved(columns,columnSelModelMgr);
        updateSelectionModelAddedRemoved(rows,rowSelModelMgr);
    }

    public void syncWithDataModel()
    {
        initialSyncWithDataModel(); // En este caso no exige que el UI esté vacío, lo actualiza al estado del modelo
    }

    public void insertDOMRow(int row,Object[] values)
    {
        ItsNatTableUIImpl compUI = getItsNatTableUIImpl();
        compUI.insertRowAt(row,values);

        addInternalEventListenerJoystickModeRow(row);

        rowSelModelMgr.insertElementUpdateModel(row);
    }

    public void removeDOMRow(int row)
    {
        removeInternalEventListenerJoystickModeRow(row);

        ItsNatTableUIImpl compUI = getItsNatTableUIImpl();
        compUI.removeRowAt(row);

        rowSelModelMgr.removeRangeUpdateModel(row,row);
    }

    public void insertDOMColumn(int column,Object[] columData)
    {
        ItsNatTableUIImpl compUI = getItsNatTableUIImpl();
        TableModel dataModel = getTableModel();
        compUI.insertColumnAt(column,columData);

        addInternalEventListenerJoystickModeColumn(column);

        ItsNatTableHeaderImpl header = getItsNatTableHeaderImpl();
        if (header != null)
            header.insertDOMColumn(column,dataModel.getColumnName(column));

        columnSelModelMgr.insertElementUpdateModel(column);
    }

    public void removeDOMColumn(int column)
    {
        removeInternalEventListenerJoystickModeColumn(column);

        ItsNatTableUIImpl compUI = getItsNatTableUIImpl();
        compUI.removeColumnAt(column);

        ItsNatTableHeaderImpl header = getItsNatTableHeaderImpl();
        if (header != null)
            header.removeDOMColumn(column);

        columnSelModelMgr.removeRangeUpdateModel(column,column);
    }

    public void updateDOMRowValues(int row,Object[] rowValues)
    {
        ItsNatTableUIImpl compUI = getItsNatTableUIImpl();
        boolean[] selected = getRowSelectionList(row,getTableModel().getColumnCount());
        boolean[] hasFocus = new boolean[selected.length];
        compUI.setRowValuesAt(row,rowValues,selected,hasFocus);
    }

    public void setDOMRowCount(int rows)
    {
        ItsNatTableUIImpl compUI = getItsNatTableUIImpl();
        int oldRows = compUI.getRowCount();

        if (rows < oldRows)
            removeInternalEventListenerJoystickModeRowRange(rows,oldRows - 1);

        compUI.setRowCount(rows);

        if (rows > oldRows)
            addInternalEventListenerJoystickModeRowRange(oldRows,rows - 1);

        updateSelectionModelAddedRemoved(rows,rowSelModelMgr);
    }

    public void setDOMColumnCount(int cols)
    {
        ItsNatTableUIImpl compUI = getItsNatTableUIImpl();
        int oldCols = compUI.getColumnCount();

        if (cols < oldCols)
            removeInternalEventListenerJoystickModeColumnRange(cols,oldCols - 1);

        compUI.setColumnCount(cols);

        if (cols > oldCols)
            addInternalEventListenerJoystickModeColumnRange(oldCols,cols - 1);

        ItsNatTableHeaderImpl header = getItsNatTableHeaderImpl();
        if (header != null)
            header.setDOMColumnCount(cols);

        updateSelectionModelAddedRemoved(cols,columnSelModelMgr);
    }

    public void updateSelectionModelAddedRemoved(int size,ListSelectionModelMgrImpl selModelMgr)
    {
        if (selModelMgr != null)
            selModelMgr.setSize(size);
    }

    public void tableChanged(TableModelEvent e)
    {
        // Sincronizamos con el modelo padre
        TableModel dataModel = (TableModel)e.getSource();
        int firstRow = e.getFirstRow();
        int lastRow = e.getLastRow();
        int column = e.getColumn();
        int type = e.getType();

        if (column == TableModelEvent.ALL_COLUMNS)
        {
            if (firstRow == TableModelEvent.HEADER_ROW)
            {
                // Ha cambiado la estructura de la tabla totalmente
                syncWithDataModel();
                return;
            }
            else if (lastRow == Integer.MAX_VALUE) // Han cambiado todas las filas (en número y contenido), rehacemos las filas
            {
                syncRowsWithDataModel();
                return;
            }
        }

        switch(type)
        {
            case TableModelEvent.INSERT:
                if (column == TableModelEvent.ALL_COLUMNS) // nuevas filas insertadas
                {
                    int colCount = dataModel.getColumnCount();
                    for(int i = firstRow; i <= lastRow; i++)
                    {
                        Object[] rowValues = getRowValues(i,colCount);
                        insertDOMRow(i,rowValues);
                    }
                }
                else // Se ha insertado una columna concreta
                {
                    Object[] colValues = getColumnValues(column);
                    insertDOMColumn(column,colValues);
                }
                break;
            case TableModelEvent.UPDATE:
                if (column == TableModelEvent.ALL_COLUMNS) // Caso de movimiento de filas
                {
                    int colCount = dataModel.getColumnCount();
                    for(int i = firstRow; i <= lastRow; i++)
                    {
                        Object[] rowValues = getRowValues(i,colCount);
                        updateDOMRowValues(i,rowValues);
                    }
                }
                else // Caso de actualización de una columna concreta (una o varias filas)
                {
                    // En teoría con DefaultTableModel no se puede cambiar el nombre de una columna (el nombre se define al insertar la columna únicamente)
                    //Object[] colValues = getColumnValues(column);
                    //updateDOMColumnValues(column,colValues);

                    ItsNatTableUIImpl compUI = getItsNatTableUIImpl();
                    for (int row = firstRow; row <= lastRow; row++)
                    {
                        Object obj = dataModel.getValueAt(row,column);
                        boolean isSelected = isCellSelected(row,column);
                        compUI.setCellValueAt(row,column,obj,isSelected,false);
                    }
                }
                break;
            case TableModelEvent.DELETE:
                if (column == TableModelEvent.ALL_COLUMNS) // Caso de eliminación de varias filas
                {
                    for(int i = firstRow; i <= lastRow; i++)
                        removeDOMRow(firstRow); // NO usar i pues al borrar se desplazan las siguientes
                }
                else // Caso de eliminación de una columna concreta
                {
                    removeDOMColumn(column);
                }
                break;
        }
    }

    public void initialSyncUIWithDataModel()
    {
        // El sistema de eventos usado por DefaultTableModel ante cambios
        // es muy pobre, da poca información, por ejemplo, ante la adición
        // de una columna se envía un evento indicando que TODO ha cambiado
        // En vez de destruir todas las columnas y añadir de nuevo cuando apenas
        // una habrá cambiado, lo hacemos más inteligente, hay que tener en cuenta
        // que la eliminación/adición de nodos es un proceso lento.
        TableModel dataModel = getTableModel();

        int columns = dataModel.getColumnCount();
        setDOMColumnCount(columns);

        int rows = dataModel.getRowCount();
        setDOMRowCount(rows);

        copyRowValuesFromDataModelToUI();

        if (header != null)
            header.copyHeaderValuesFromDataModelToUI();
    }

    public void syncRowsWithDataModel()
    {
        // Leer notas de syncWithDataModel
        // Suponemos que el número de columnas no ha cambiado
        // pero el número de filas y contenido sí
        TableModel dataModel = getTableModel();
        int rows = dataModel.getRowCount();
        setDOMRowCount(rows);

        copyRowValuesFromDataModelToUI();
    }

    public void copyRowValuesFromDataModelToUI()
    {
        ItsNatTableUIImpl compUI = getItsNatTableUIImpl();
        TableModel dataModel = getTableModel();
        int rows = dataModel.getRowCount();
        int columns = dataModel.getColumnCount();

        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < columns; j++)
            {
                Object obj = dataModel.getValueAt(i,j);
                boolean isSelected = isCellSelected(i,j);
                compUI.setCellValueAt(i,j,obj,isSelected,false);
            }
        }
    }

    public TableModel getTableModel()
    {
        return (TableModel)dataModel;
    }

    public void setTableModel(TableModel dataModel)
    {
        setDataModel(dataModel);
    }

    public ListSelectionModelMgrImpl getRowSelectionModelMgr()
    {
        return rowSelModelMgr;
    }

    public ListSelectionModel getRowSelectionModel()
    {
        if (rowSelModelMgr == null)
            return null;
        return rowSelModelMgr.getListSelectionModel();
    }

    public void unsetRowListSelectionModel()
    {
        if (rowSelModelMgr != null)
            rowSelModelMgr.dispose();
        // No anulamos el selModelMgr para que se pueda recuperar el ListSelectionModel después de un disposeEffective
    }

    public void setRowSelectionModel(ListSelectionModel rowSelectionModel)
    {
        unsetRowListSelectionModel();

        int size = getTableModel().getRowCount();

        this.rowSelModelMgr = ListSelectionModelMgrImpl.newListSelectionModelMgr(rowSelectionModel,size);

        // No hay listener del componente
    }

    public ListSelectionModelMgrImpl getColumnSelectionModelMgr()
    {
        return columnSelModelMgr;
    }

    public ListSelectionModel getColumnSelectionModel()
    {
        if (columnSelModelMgr == null)
            return null;
        return columnSelModelMgr.getListSelectionModel();
    }

    public void unsetColumnListSelectionModel()
    {
        if (columnSelModelMgr != null)
            columnSelModelMgr.dispose();
        // No anulamos el selModelMgr para que se pueda recuperar el ListSelectionModel después de un disposeEffective
    }

    public void setColumnSelectionModel(ListSelectionModel columnSelectionModel)
    {
        unsetColumnListSelectionModel();

        int size = getTableModel().getColumnCount();

        this.columnSelModelMgr = ListSelectionModelMgrImpl.newListSelectionModelMgr(columnSelectionModel,size);

        // No hay listener del componente
    }

    @Override
    public void processNormalEvent(Event evt)
    {
        String type = evt.getType();
        if (type.equals("click") || type.equals("mouseup"))
        {
            Node nodeClicked = (Node)evt.getTarget(); // Puede ser un nodo interior del elemento pulsado
            MouseEvent mouseEvt = (MouseEvent)evt;
            boolean toggle;
            if (!isSelectionUsesKeyboard()) toggle = true;
            else toggle = mouseEvt.getCtrlKey();
            boolean extend = mouseEvt.getShiftKey();

            ItsNatTableHeaderCellUI headerCellInfo = null;
            if (header != null)
            {
                // Vemos si ha sido pulsada la cabecera
                headerCellInfo = header.processEvent(nodeClicked,toggle,extend);
                if ((headerCellInfo != null) && (isRowSelectionAllowed() || isColumnSelectionAllowed()))
                {
                    // Además seleccionamos la columna de la tabla
                    int column = headerCellInfo.getIndex();
                    changeColumnSelection(column,toggle,extend);
                }
            }

            if ((headerCellInfo == null) && (isRowSelectionAllowed() || isColumnSelectionAllowed()))
            {
                // Ha sido seguramente pulsado el cuerpo de la tabla
                ItsNatTableUI bodyUI = getItsNatTableUI();
                ItsNatTableCellUI cellInfo = bodyUI.getItsNatTableCellUIFromNode(nodeClicked);
                if (cellInfo != null)
                {
                    int row = cellInfo.getRowIndex();
                    int column = cellInfo.getColumnIndex();
                    changeSelection(row,column,toggle,extend);
                }
            }
        }

        super.processNormalEvent(evt);
    }

    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend)
    {
        // Idem JTable y segun http://developer.classpath.org/doc/javax/swing/JTable-source.html#line.4945
	boolean selected = isCellSelected(rowIndex, columnIndex);

        columnSelModelMgr.changeSelectionModel(columnIndex, toggle, extend, selected);
        rowSelModelMgr.changeSelectionModel(rowIndex, toggle, extend, selected);
    }

    public void changeColumnSelection(int columnIndex, boolean toggle, boolean extend)
    {
        ListSelectionModel csm = getColumnSelectionModel();

	boolean selected = csm.isSelectedIndex(columnIndex);

        columnSelModelMgr.changeSelectionModel(columnIndex, toggle, extend, selected);
    }

    public void clearSelection()
    {
        getRowSelectionModel().clearSelection();
        getColumnSelectionModel().clearSelection();
    }

    public void setSelectionMode(int selectionMode)
    {
        clearSelection();
        getRowSelectionModel().setSelectionMode(selectionMode);
        getColumnSelectionModel().setSelectionMode(selectionMode);
    }

    public boolean isRowSelectionAllowed()
    {
        return rowSelectionAllowed;
    }

    public void setRowSelectionAllowed(boolean rowSelectionAllowed)
    {
        this.rowSelectionAllowed = rowSelectionAllowed;
    }

    public boolean isColumnSelectionAllowed()
    {
        return columnSelectionAllowed;
    }

    public void setColumnSelectionAllowed(boolean columnSelectionAllowed)
    {
        this.columnSelectionAllowed = columnSelectionAllowed;
    }

    public boolean isCellSelectionEnabled()
    {
        return isRowSelectionAllowed() && isColumnSelectionAllowed();
    }

    public void setCellSelectionEnabled(boolean cellSelectionEnabled)
    {
	setRowSelectionAllowed(cellSelectionEnabled);
	setColumnSelectionAllowed(cellSelectionEnabled);
    }

    public boolean isRowSelected(int row)
    {
	return getRowSelectionModel().isSelectedIndex(row);
    }

    public boolean isColumnSelected(int column)
    {
        return getColumnSelectionModel().isSelectedIndex(column);
    }

    public boolean isCellSelected(int row, int column)
    {
        /* Casuística:
         * Filas y columnas NO seleccionables: no celda seleccionada
         * Fila O columna seleccionables (no las dos): celda seleccionada si está en una fila o columna seleccionada
         * Fila Y columna seleccionables: celda seleccionada SOLO si la celda está en una fila Y columna seleccionadas (ambas),
         *    en este caso se trata de evitar que al seleccionar una celda seleccione la fila y la columna enteras (formando una cruz)
         *    este caso sirve para seleccionar celdas individuales.
         */
	if (!isRowSelectionAllowed() && !isColumnSelectionAllowed())
	    return false;

        return ( (!isRowSelectionAllowed() || isRowSelected(row)) &&
                 (!isColumnSelectionAllowed() || isColumnSelected(column)) );
    }

    public void setRowSelectionInterval(int index0, int index1)
    {
        getRowSelectionModel().setSelectionInterval(index0,index1);
    }

    public void setColumnSelectionInterval(int index0, int index1)
    {
        getColumnSelectionModel().setSelectionInterval(index0,index1);
    }

    public int[] getSelectedRows()
    {
        return rowSelModelMgr.getSelectedIndices();
    }

    public void setSelectedRows(int[] indices)
    {
        rowSelModelMgr.setSelectedIndices(indices);
    }

    public int[] getSelectedColumns()
    {
        return columnSelModelMgr.getSelectedIndices();
    }

    public void setSelectedColumns(int[] indices)
    {
        columnSelModelMgr.setSelectedIndices(indices);
    }

    public int getSelectedColumn()
    {
        return getColumnSelectionModel().getMinSelectionIndex();
    }

    public int getSelectedRow()
    {
	return getRowSelectionModel().getMinSelectionIndex();
    }

    public void selectAll()
    {
        int rows = getTableModel().getRowCount();
        int columns = getTableModel().getColumnCount();
        if ((rows > 0) && (columns > 0))
        {
	    setRowSelectionInterval(0, rows - 1);
	    setColumnSelectionInterval(0, columns - 1);
	}
    }

    public Object[] getRowValues(int row,int colCount)
    {
        TableModel dataModel = getTableModel();
        Object[] rowValues = new Object[colCount];
        for(int j = 0; j < colCount; j++)
        {
            rowValues[j] = dataModel.getValueAt(row,j);
        }
        return rowValues;
    }

    public Object[] getColumnValues(int column)
    {
        TableModel dataModel = getTableModel();
        int rowCount = dataModel.getRowCount();
        Object[] colValues = new Object[rowCount];
        for(int i = 0; i < rowCount; i++)
        {
            colValues[i] = dataModel.getValueAt(i,column);
        }
        return colValues;
    }

    public boolean[] getRowSelectionList(int row,int colCount)
    {
        boolean[] rowSelList = new boolean[colCount];
        for(int j = 0; j < colCount; j++)
        {
            rowSelList[j] = isCellSelected(row,j);
        }
        return rowSelList;
    }

    public boolean[] getColumnSelectionList(int column)
    {
        TableModel dataModel = getTableModel();
        int rowCount = dataModel.getRowCount();
        boolean[] colSelList = new boolean[rowCount];
        for(int i = 0; i < rowCount; i++)
        {
            colSelList[i] = isCellSelected(i,column);
        }
        return colSelList;
    }

    public ItsNatTableCellRenderer getItsNatTableCellRenderer()
    {
        return renderer;
    }

    public void setItsNatTableCellRenderer(ItsNatTableCellRenderer renderer)
    {
        this.renderer = renderer;
    }

    public ItsNatTableCellEditor getItsNatTableCellEditor()
    {
        TableCellEditorProcessor editorProcessor = getTableCellEditorProcessor();
        return editorProcessor.getItsNatTableCellEditor();
    }

    public void setItsNatTableCellEditor(ItsNatTableCellEditor cellEditor)
    {
        editorProcessor.setItsNatTableCellEditor(cellEditor);
    }

    public void startEditingAt(int row,int column)
    {
        getTableCellEditorProcessor().startEdition(row,column);
    }

    public boolean isEditing()
    {
        return getTableCellEditorProcessor().isEditing();
    }

    public int getEditingRow()
    {
        return getTableCellEditorProcessor().getRow();
    }

    public int getEditingColumn()
    {
        return getTableCellEditorProcessor().getColumn();
    }

    public String getEditorActivatorEvent()
    {
        return getTableCellEditorProcessor().getEditorActivatorEvent();
    }

    public void setEditorActivatorEvent(String editorActivatorEvent)
    {
        getTableCellEditorProcessor().setEditorActivatorEvent(editorActivatorEvent);
    }

    public boolean isEditingEnabled()
    {
        return getTableCellEditorProcessor().isEditingEnabled();
    }

    public void setEditingEnabled(boolean value)
    {
        getTableCellEditorProcessor().setEditingEnabled(value);
    }

    public ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        return null;
    }

    public ItsNatTableStructure getItsNatTableStructure()
    {
        return structure;
    }

    public Object createDefaultStructure()
    {
        return getItsNatComponentManager().createDefaultItsNatTableStructure();
    }

    public void setSelectionUsesKeyboard(boolean value)
    {
        this.selectionUsesKeyboard = value;
    }

    public boolean isSelectionUsesKeyboard()
    {
        return selectionUsesKeyboard;
    }

    public Element[] getContentElementList()
    {
        // Toda la tabla incluido el header
        ItsNatTableUI compUI = getItsNatTableUI();
        int rowCount = compUI.getRowCount();
        int colCount = compUI.getColumnCount();

        int headColCount = 0;
        ItsNatTableHeaderImpl header = getItsNatTableHeaderImpl();
        if (header != null) headColCount = colCount;

        Element[] elemList = new Element[rowCount * colCount + headColCount];
        for(int i = 0; i < rowCount; i++)
        {
            int firstCellInRow = i*colCount;
            for(int j = 0; j < colCount; j++)
                elemList[firstCellInRow + j] = compUI.getCellContentElementAt(i,j);
        }

        if (header != null)
            elemList = header.getContentElementList(elemList, headColCount);

        return elemList;
    }

    public Element[] getContentElementListOfRow(int row)
    {
        ItsNatTableUI compUI = getItsNatTableUI();
        TableModel dataModel = getTableModel();
        int colCount = dataModel.getColumnCount();
        Element[] elemList = new Element[colCount];
        for(int i = 0; i < colCount; i++)
            elemList[i] = compUI.getCellContentElementAt(row,i);
        return elemList;
    }

    public Element[] getContentElementListOfColumn(int column)
    {
        ItsNatTableUI compUI = getItsNatTableUI();
        TableModel dataModel = getTableModel();
        int rowCount = dataModel.getRowCount();
        Element[] elemList = new Element[rowCount];
        for(int i = 0; i < rowCount; i++)
            elemList[i] = compUI.getCellContentElementAt(i,column);
        return elemList;
    }

    public void addInternalEventListenerJoystickModeRow(int row)
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        Element[] elemList = getContentElementListOfRow(row);

        ItsNatCompDOMListenersJoystickSharedImpl.addEventListenerJoystick(domListeners, elemList);
    }

    public void removeInternalEventListenerJoystickModeRow(int row)
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        Element[] elemList = getContentElementListOfRow(row);

        ItsNatCompDOMListenersJoystickSharedImpl.removeEventListenerJoystick(domListeners, elemList);
    }

    public void addInternalEventListenerJoystickModeRow(ArrayList<ItsNatCompDOMListenersJoystick> domListeners,int row)
    {
        Element[] elemList = getContentElementListOfRow(row);

        ItsNatCompDOMListenersJoystickSharedImpl.addEventListenerJoystick(domListeners, elemList);
    }

    public void removeInternalEventListenerJoystickModeRow(ArrayList<ItsNatCompDOMListenersJoystick> domListeners,int row)
    {
        Element[] elemList = getContentElementListOfRow(row);

        ItsNatCompDOMListenersJoystickSharedImpl.removeEventListenerJoystick(domListeners, elemList);
    }

    public void addInternalEventListenerJoystickModeColumn(int column)
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        Element[] elemList = getContentElementListOfColumn(column);

        ItsNatCompDOMListenersJoystickSharedImpl.addEventListenerJoystick(domListeners, elemList);
    }

    public void removeInternalEventListenerJoystickModeColumn(int column)
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        Element[] elemList = getContentElementListOfColumn(column);

        ItsNatCompDOMListenersJoystickSharedImpl.removeEventListenerJoystick(domListeners, elemList);
    }

    public void addInternalEventListenerJoystickModeColumn(ArrayList<ItsNatCompDOMListenersJoystick> domListeners,int column)
    {
        Element[] elemList = getContentElementListOfColumn(column);

        ItsNatCompDOMListenersJoystickSharedImpl.addEventListenerJoystick(domListeners, elemList);
    }

    public void removeInternalEventListenerJoystickModeColumn(ArrayList<ItsNatCompDOMListenersJoystick> domListeners,int column)
    {
        Element[] elemList = getContentElementListOfColumn(column);

        ItsNatCompDOMListenersJoystickSharedImpl.removeEventListenerJoystick(domListeners, elemList);
    }

    public void addInternalEventListenerJoystickModeRowRange(int fromRow,int toRow)
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        for(int i = fromRow; i <= toRow; i++)
            addInternalEventListenerJoystickModeRow(domListeners,i);
    }

    public void removeInternalEventListenerJoystickModeRowRange(int fromRow,int toRow)
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        for(int i = fromRow; i <= toRow; i++)
            removeInternalEventListenerJoystickModeRow(domListeners,i);
    }

    public void addInternalEventListenerJoystickModeColumnRange(int fromColumn,int toColumn)
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        for(int i = fromColumn; i <= toColumn; i++)
            addInternalEventListenerJoystickModeColumn(domListeners,i);
    }

    public void removeInternalEventListenerJoystickModeColumnRange(int fromColumn,int toColumn)
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        for(int i = fromColumn; i <= toColumn; i++)
            removeInternalEventListenerJoystickModeColumn(domListeners,i);
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
