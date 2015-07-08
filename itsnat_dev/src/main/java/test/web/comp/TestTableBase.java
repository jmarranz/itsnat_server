/*
 * TestTable.java
 *
 * Created on 6 de diciembre de 2006, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp;

import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import test.web.shared.TestBaseHTMLDocument;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestTableBase extends TestBaseHTMLDocument implements EventListener,TableModelListener,ListSelectionListener
{
    protected ItsNatTable comp;
    protected ItsNatHTMLButton addButton;
    protected ItsNatHTMLButton removeButton;
    protected ItsNatHTMLInputCheckBox joystickModeCheck;

    /**
     * Creates a new instance of TestTable
     */
    public TestTableBase(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
    }

    public void initTable(ItsNatTable comp,String addButtonId,String removeButtonId,String joystickCheckboxId)
    {
        this.comp = comp;

        DefaultTableModel dataModel = new DefaultTableModel();
        comp.setTableModel(dataModel);

        dataModel.addColumn("Column 1"); // Se añade al patrón pues no hay filas
        dataModel.addColumn("Column 2"); // "
        dataModel.addRow(new String[]{"Item 1,1","Item 1,2"}); // Fila con dos columnas
        dataModel.addRow(new String[]{"Item 2,1","Item 2,2"}); // "
        dataModel.addColumn("Column 3",new String[]{"Item 1,3","Item 2,3"}); // Añade una columna a las dos filas
        dataModel.addRow(new String[]{"Item 3,1","Item 3,2","Item 3,3"}); // Nueva fila con tres columnas
        TestUtil.checkError(dataModel.getColumnCount() == 3);
        TestUtil.checkError(dataModel.getRowCount() == 3);
        TestUtil.checkError(dataModel.getValueAt(2,2) != null);

        dataModel.moveRow(0,1,1); // Desplaza las dos primeras filas a ser las dos finales
        dataModel.moveRow(1,2,0); // Las deja como estaba

        dataModel.setColumnCount(5);
        TestUtil.checkError(dataModel.getColumnCount() == 5);

        dataModel.setColumnCount(3); // restauramos quitando las dos últimas
        TestUtil.checkError(dataModel.getColumnCount() == 3);

        dataModel.setRowCount(5);
        TestUtil.checkError(dataModel.getRowCount() == 5);

        dataModel.setRowCount(3); // restauramos quitando las dos últimas
        TestUtil.checkError(dataModel.getRowCount() == 3);

        //comp.setRowSelectionModel(null);
        //comp.setColumnSelectionModel(null);

        ListSelectionModel rowSelModel = comp.getRowSelectionModel();
        ListSelectionModel columnSelModel = comp.getColumnSelectionModel();

        rowSelModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Ya es esto por defecto pero para que se vea
        columnSelModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Ya es esto por defecto pero para que se vea

        //comp.setRowSelectionAllowed(false);
        //comp.setColumnSelectionAllowed(true);
        comp.setCellSelectionEnabled(true);

        rowSelModel.addSelectionInterval(1,1);
        columnSelModel.addSelectionInterval(1,1);

        rowSelModel.addListSelectionListener(this);
        columnSelModel.addListSelectionListener(this);
        dataModel.addTableModelListener(this);


        comp.addEventListener("click",this);

        decorateSelection();

        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        this.addButton = (ItsNatHTMLButton)componentMgr.findItsNatComponentById(addButtonId);
        addButton.addEventListener("click",this);

        this.removeButton = (ItsNatHTMLButton)componentMgr.findItsNatComponentById(removeButtonId);
        removeButton.addEventListener("click",this);

        this.joystickModeCheck = (ItsNatHTMLInputCheckBox)componentMgr.findItsNatComponentById(joystickCheckboxId);
        joystickModeCheck.addEventListener("click",this);
        joystickModeCheck.setSelected(itsNatDoc.isJoystickMode());
    }

    public void decorateSelection()
    {
        int rowCount = comp.getTableModel().getRowCount();
        for(int i = 0; i < rowCount; i++)
        {
            decorateRowSelection(i);
        }

        int columnCount = comp.getTableModel().getColumnCount();
        for(int i = 0; i < columnCount; i++)
        {
            decorateColumnSelection(i);
        }
    }

    public void decorateRowSelection(int row)
    {
        int cols = comp.getTableModel().getColumnCount();
        for(int i = 0; i < cols; i++)
        {
            Element cellContentElem = comp.getItsNatTableUI().getCellContentElementAt(row,i);
            if (cellContentElem == null) continue;
            decorateCellSelection(row,i,cellContentElem);
        }
    }

    public void decorateColumnSelection(int column)
    {
        int rows = comp.getTableModel().getRowCount();
        for(int i = 0; i < rows; i++)
        {
            Element cellContentElem = comp.getItsNatTableUI().getCellContentElementAt(i,column);
            decorateCellSelection(i,column,cellContentElem);
        }
    }

    public void decorateCellSelection(int row,int column,Element cell)
    {
        boolean selected = comp.isCellSelected(row,column); // Es el que decide en última instancia si se está seleccionado o no teniendo en cuenta los modos de selección
        TestSelectionUtil.decorateSelection(cell,selected);
    }

    public void handleEvent(Event evt)
    {
        if (evt.getCurrentTarget() == addButton.getElement())
        {
            DefaultTableModel dataModel = (DefaultTableModel)comp.getTableModel();
            int rows = dataModel.getRowCount();
            rows++;
            // dataModel.addRow(new String[]{ new Integer(rows).toString(),new Integer(rows+1).toString(),new Integer(rows+2).toString()});  // Para ver si se manifiesta en el navegador y genera un evento TableDataEvent
            dataModel.insertRow(0,new String[]{ new Integer(rows).toString(),new Integer(rows+1).toString(),new Integer(rows+2).toString()});  // Para ver si se manifiesta en el navegador y genera un evento TableDataEvent
        }
        else if (evt.getCurrentTarget() == removeButton.getElement())
        {
            DefaultTableModel dataModel = (DefaultTableModel)comp.getTableModel();
            int rows = dataModel.getRowCount();
            if (rows > 0)
            {
                // dataModel.removeRow(rows - 1);
                dataModel.removeRow(0);
            }
        }
        else if (evt.getCurrentTarget() == joystickModeCheck.getElement())
            comp.setJoystickMode(joystickModeCheck.isSelected());
        else
        {
            ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
            ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();

            int[] selectedRows = comp.getSelectedRows();
            int[] selectedCols = comp.getSelectedColumns();

            outText("OK " + evt.getType() + " (rows.sel. " + selectedRows.length + ", cols.sel. " + selectedCols.length + " ) "); // Para que se vea
        }
    }

    public void valueChanged(ListSelectionEvent e)
    {
        ListSelectionModel selModel = (ListSelectionModel)e.getSource();
        if (selModel.getValueIsAdjusting())
            return;

        int first = e.getFirstIndex();
        int last = e.getLastIndex();

        boolean isRow = (selModel == comp.getRowSelectionModel());
        boolean isCol = (selModel == comp.getColumnSelectionModel());
        String rowOrColStr;
        if (isRow) rowOrColStr = "row";
        else rowOrColStr = "col";

        String msg = "OK ";
        for(int i = first; i <= last; i++)
        {
            boolean selected = selModel.isSelectedIndex(i);
            if (selected)
                msg += "(sel " + rowOrColStr + " " + i + ") ";
            else
                msg += "(desel " + rowOrColStr + " " + i + ") ";
        }

        outText(msg);

        if (isRow)
        {
            for(int i = first; i <= last; i++)
                decorateRowSelection(i);
        }
        else if (isCol)
        {
            for(int i = first; i <= last; i++)
                decorateColumnSelection(i);
        }
    }

    public void tableChanged(TableModelEvent e)
    {
        // HACER
    }
}
