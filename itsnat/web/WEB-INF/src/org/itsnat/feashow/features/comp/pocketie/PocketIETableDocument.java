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

package org.itsnat.feashow.features.comp.pocketie;


import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.table.ItsNatHTMLTable;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLTableElement;

public class PocketIETableDocument implements EventListener,ListSelectionListener,ChangeListener
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected ItsNatHTMLTable tableComp;
    protected ItsNatHTMLInputButton removeButton;
    protected ItsNatHTMLInputText[] newItemComp;
    protected ItsNatHTMLInputText posComp;
    protected ItsNatHTMLInputButton updateButton;
    protected ItsNatHTMLInputButton insertButton;
    protected ItsNatHTMLInputCheckBox editInplaceButton;

    public PocketIETableDocument(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        ItsNatHTMLComponentManager compMgr = itsNatDoc.getItsNatHTMLComponentManager();
        Document doc = itsNatDoc.getDocument();

        HTMLTableElement compElem = (HTMLTableElement)doc.getElementById("compId");
        this.tableComp = compMgr.createItsNatHTMLTable(compElem,null,null);
        tableComp.setSelectionUsesKeyboard(false);

        tableComp.setEditorActivatorEvent("click");

        DefaultTableModel dataModel = (DefaultTableModel)tableComp.getTableModel();
        dataModel.addColumn("City");
        dataModel.addColumn("Public square");
        dataModel.addColumn("Monument");
        dataModel.addRow(new String[] {"Madrid","Plaza Mayor","Palacio Real"});
        dataModel.addRow(new String[] {"Sevilla","Plaza de España","Giralda"});
        dataModel.addRow(new String[] {"Segovia","Plaza del Azoguejo","Acueducto Romano"});

        ListSelectionModel rowSelModel = tableComp.getRowSelectionModel();
        // ListSelectionModel columnSelModel = tableComp.getColumnSelectionModel();

        rowSelModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        // columnSelModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        tableComp.setRowSelectionAllowed(true);
        tableComp.setColumnSelectionAllowed(false);

        rowSelModel.addListSelectionListener(new PocketIETableRowSelectionDecorator(tableComp));

        rowSelModel.setSelectionInterval(1,1);

        rowSelModel.addListSelectionListener(this);

        ListSelectionModel headSelModel = tableComp.getItsNatTableHeader().getListSelectionModel();
        headSelModel.addListSelectionListener(this);

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

        this.editInplaceButton = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("editInplaceId");
        ToggleButtonModel inPlaceModel = editInplaceButton.getToggleButtonModel();
        inPlaceModel.setSelected(false);
        inPlaceModel.addChangeListener(this);

        tableComp.setEditingEnabled(inPlaceModel.isSelected());
    }

    public ItsNatDocument getItsNatDocument()
    {
        return itsNatDoc;
    }

    public String hideComponent()
    {
        // To avoid blinking
        HTMLTableElement parentElem = (HTMLTableElement)tableComp.getElement();
        String cssValue = parentElem.getAttribute("style");
        parentElem.setAttribute("style","display:none;" + cssValue);
        return cssValue;
    }

    public void showComponent(String cssValue)
    {
        // Restore old state
        HTMLTableElement parentElem = (HTMLTableElement)tableComp.getElement();
        parentElem.setAttribute("style",cssValue);
    }

    public void handleEvent(Event evt)
    {
        String cssValue = hideComponent();

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

        showComponent(cssValue);
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        int first = e.getFirstIndex();
        int last = e.getLastIndex();

        ListSelectionModel selModel = (ListSelectionModel)e.getSource();
        if (selModel == tableComp.getRowSelectionModel())
        {
            int row = selModel.getMinSelectionIndex(); // First selected
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
        else if (selModel == tableComp.getItsNatTableHeader().getListSelectionModel())
        {
            int selected = -1;
            for(int i = first; i <= last; i++)
            {
                if (selModel.isSelectedIndex(i))
                    selected = i;
            }
            if (selected == -1) return;
            final int selectedCol = selected;
            DefaultTableModel dataModel = (DefaultTableModel)tableComp.getTableModel();
            Vector vector = dataModel.getDataVector();
            Comparator comp = new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    Vector row1 = (Vector)o1;
                    Vector row2 = (Vector)o2;
                    String item1 = (String)row1.get(selectedCol);
                    String item2 = (String)row2.get(selectedCol);
                    return item1.compareTo(item2);
                }
            };
            Collections.sort(vector,comp);
            dataModel.fireTableStructureChanged();
        }
    }

    public void stateChanged(ChangeEvent e)
    {
        ToggleButtonModel dataModel = (ToggleButtonModel)e.getSource();
        tableComp.setEditingEnabled(dataModel.isSelected());
    }

}

