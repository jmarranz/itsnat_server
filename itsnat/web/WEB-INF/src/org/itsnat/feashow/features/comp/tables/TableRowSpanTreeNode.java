/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.comp.tables;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.table.ItsNatHTMLTable;
import org.itsnat.comp.table.ItsNatHTMLTableUI;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLTableCellElement;

public class TableRowSpanTreeNode extends FeatureTreeNode implements EventListener,TableModelListener,ListSelectionListener,ItemListener
{
    protected ItsNatHTMLInputCheckBox useSingleClickComp;
    protected ItsNatHTMLTable tableComp;
    protected ItsNatHTMLInputButton removeButton;
    protected ItsNatHTMLInputText[] newItemComp;
    protected ItsNatHTMLInputText posComp;
    protected ItsNatHTMLInputButton updateButton;
    protected ItsNatHTMLInputButton insertButton;
    protected ItsNatHTMLInputCheckBox joystickModeComp;

    public TableRowSpanTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.useSingleClickComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("useSingleClickId");
        useSingleClickComp.getToggleButtonModel().addItemListener(this);

        this.tableComp = (ItsNatHTMLTable)compMgr.createItsNatComponentById("compId");

        DefaultTableModel dataModel = (DefaultTableModel)tableComp.getTableModel();
        dataModel.addTableModelListener(this);
        tableComp.setTableModel(dataModel); // resets the internal listeners, the internal TableModelListener listener is called first

        dataModel.addColumn("City");
        dataModel.addColumn("Turistic Place/Monument");
        dataModel.addRow(new String[] {"Madrid","Plaza Mayor"});
        dataModel.addRow(new String[] {"Madrid","Palacio Real"});
        dataModel.addRow(new String[] {"Sevilla","Plaza de España"});
        dataModel.addRow(new String[] {"Sevilla","Parque de María Luisa"});
        dataModel.addRow(new String[] {"Segovia","Plaza del Azoguejo"});
        dataModel.addRow(new String[] {"Segovia","Alcázar"});


        ListSelectionModel rowSelModel = tableComp.getRowSelectionModel();
        ListSelectionModel columnSelModel = tableComp.getColumnSelectionModel();

        rowSelModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        // columnSelModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        tableComp.setRowSelectionAllowed(true);
        tableComp.setColumnSelectionAllowed(false);

        rowSelModel.addListSelectionListener(new TableRowSelectionDecoration(tableComp));

        rowSelModel.setSelectionInterval(1,1);

        rowSelModel.addListSelectionListener(this);
        columnSelModel.addListSelectionListener(this);

        this.removeButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("removeId");
        removeButton.addEventListener("click",this);

        this.newItemComp = new ItsNatHTMLInputText[dataModel.getColumnCount()];
        for(int i = 0; i < newItemComp.length; i++)
        {
            this.newItemComp[i] = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("itemId" + i);
            newItemComp[i].setText(dataModel.getValueAt(rowSelModel.getMinSelectionIndex(), i).toString());
        }

        this.posComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("posId");
        posComp.setText(Integer.toString(rowSelModel.getMinSelectionIndex()));

        this.updateButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("updateId");
        updateButton.addEventListener("click",this);

        this.insertButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("insertId");
        insertButton.addEventListener("click",this);

        this.joystickModeComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("joystickModeId");
        joystickModeComp.getToggleButtonModel().addItemListener(this);
        joystickModeComp.setSelected(isJoystickModePreferred());
    }

    public void endExamplePanel()
    {
        useSingleClickComp.dispose();
        this.useSingleClickComp = null;

        this.tableComp.dispose();
        this.tableComp = null;

        this.removeButton.dispose();
        this.removeButton = null;

        for(int i = 0; i < newItemComp.length; i++)
        {
            this.newItemComp[i].dispose();
            this.newItemComp[i] = null;
        }

        this.posComp.dispose();
        this.posComp = null;

        this.updateButton.dispose();
        this.updateButton = null;

        this.insertButton.dispose();
        this.insertButton = null;

        this.joystickModeComp.dispose();
        this.joystickModeComp = null;
    }

    public void handleEvent(Event evt)
    {
        log(evt);

        EventTarget currentTarget = evt.getCurrentTarget();
        if (currentTarget == removeButton.getHTMLInputElement())
        {
            DefaultTableModel dataModel = (DefaultTableModel)tableComp.getTableModel();
            ListSelectionModel rowSelModel = tableComp.getRowSelectionModel();
            if (!rowSelModel.isSelectionEmpty())
            {
                // Selection Model is in SINGLE_INTERVAL_SELECTION mode
                int min = rowSelModel.getMinSelectionIndex();
                int max = rowSelModel.getMaxSelectionIndex();
                for(int i = 0; i <= max - min; i++)
                    dataModel.removeRow(min);
            }
        }
        else if ((currentTarget == updateButton.getHTMLInputElement()) ||
                 (currentTarget == insertButton.getHTMLInputElement()))
        {
            String[] newItem = new String[newItemComp.length];
            for(int i = 0; i < newItemComp.length; i++)
                newItem[i] = newItemComp[i].getText();

            int row;
            try
            {
                row = Integer.parseInt(posComp.getText());
                DefaultTableModel dataModel = (DefaultTableModel)tableComp.getTableModel();
                if (currentTarget == updateButton.getHTMLInputElement())
                {
                    for(int i = 0; i < newItemComp.length; i++)
                        dataModel.setValueAt(newItem[i],row,i);
                }
                else
                    dataModel.insertRow(row,newItem);
            }
            catch(NumberFormatException ex)
            {
                getItsNatDocument().addCodeToSend("alert('Bad Position');");
            }
            catch(ArrayIndexOutOfBoundsException ex)
            {
                getItsNatDocument().addCodeToSend("alert('Bad Position');");
            }
        }
    }

    public void tableChanged(TableModelEvent e)
    {
        int firstRow = e.getFirstRow();
        int lastRow = e.getLastRow();

        String action = "";
        int type = e.getType();
        switch(type)
        {
            case TableModelEvent.INSERT: action = "added"; break;
            case TableModelEvent.DELETE: action = "removed"; break;
            case TableModelEvent.UPDATE: action = "changed"; break;
        }

        String interval = "";
        if (firstRow != -1)
            interval = "interval " + firstRow + "-" + lastRow;

        log(e.getClass().toString() + " " + action + " " + interval);

        // This listener is called *after* the effective UI change
        switch(type)
        {
            case TableModelEvent.INSERT:
                for(int i = firstRow - 1; i <= lastRow + 1; i++)
                    updateRowSpanPrevContiguous(i);
                break;
            case TableModelEvent.DELETE:
                updateRowSpanPrevContiguous(firstRow - 1);
                updateRowSpanPrevContiguous(firstRow);
                break;
            case TableModelEvent.UPDATE:
                for(int i = firstRow - 1; i <= lastRow + 1; i++)
                    updateRowSpanPrevContiguous(i);
                break;
        }
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        ListSelectionModel rowSelModel = tableComp.getRowSelectionModel();
        if (e.getSource() != rowSelModel) return; // may be the column selector

        int first = e.getFirstIndex();
        int last = e.getLastIndex();

        String fact = "";
        for(int i = first; i <= last; i++)
        {
            boolean selected = rowSelModel.isSelectedIndex(i);
            if (selected)
                fact += ", selected ";
            else
                fact += ", deselected ";
            fact += i;
        }

        log(e.getClass().toString() + " " + fact);

        int row = rowSelModel.getMinSelectionIndex(); // First selected
        if (row != -1)
        {
            for(int i = 0; i < newItemComp.length; i++)
            {
                Object value = tableComp.getTableModel().getValueAt(row,i);
                newItemComp[i].setText(value.toString());
            }
            posComp.setText(Integer.toString(row));
        }
    }

    public static int getFirstContiguousEqual(int row, int col,TableModel dataModel)
    {
        if (row == 0) return -1;
        Object value = dataModel.getValueAt(row,col);
        int prevIndex = -1;
        for(int i = row - 1; i >= 0; i--)
        {
            Object currValue = dataModel.getValueAt(i,col);
            if (!value.equals(currValue)) return prevIndex;
            prevIndex = i;
        }
        return prevIndex;
    }

    public static int getLastContiguousEqual(int row, int col,TableModel dataModel)
    {
        if (row == dataModel.getRowCount() - 1) return -1;
        Object value = dataModel.getValueAt(row,col);
        int nextIndex = -1;
        for(int i = row + 1; i < dataModel.getRowCount(); i++)
        {
            Object currValue = dataModel.getValueAt(i,col);
            if (!value.equals(currValue)) return nextIndex;
            nextIndex = i;
        }
        return nextIndex;
    }

    public void updateRowSpanPrevContiguous(int row)
    {
        if (row < 0) return;
        TableModel dataModel = tableComp.getTableModel();
        if (row >= dataModel.getRowCount()) return;

        ItsNatHTMLTableUI tableUI = tableComp.getItsNatHTMLTableUI();
        int first = getFirstContiguousEqual(row,0,dataModel);
        if (first == -1)
            first = row;

        int last = getLastContiguousEqual(first,0,dataModel);
        if (last == -1)
            last = first;
        int rowSpan = last - first + 1;

        HTMLTableCellElement cell = tableUI.getHTMLTableCellElementAt(first,0);
        if (cell.getParentNode() == null)
        {
            HTMLTableCellElement cellNext = tableUI.getHTMLTableCellElementAt(first, 1 );
            cellNext.getParentNode().insertBefore(cell,cellNext);
        }

        if (rowSpan > 1)
            cell.setAttribute("rowspan",Integer.toString(rowSpan));
        else if (cell.hasAttribute("rowspan"))
            cell.removeAttribute("rowspan");

        if (first != last)
        {
            for(int i = first + 1; i <= last; i++)
            {
                cell = tableUI.getHTMLTableCellElementAt(i,0);
                if (cell.getParentNode() != null)
                    cell.getParentNode().removeChild(cell);
            }
        }
    }

    public void itemStateChanged(ItemEvent e)
    {
        boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
        ButtonModel model = (ButtonModel)e.getSource();
        if (model == joystickModeComp.getToggleButtonModel())
            tableComp.setJoystickMode(selected);
        else if (model == useSingleClickComp.getToggleButtonModel())
            tableComp.setEditorActivatorEvent(selected? "click" : "dblclick");
    }
}
