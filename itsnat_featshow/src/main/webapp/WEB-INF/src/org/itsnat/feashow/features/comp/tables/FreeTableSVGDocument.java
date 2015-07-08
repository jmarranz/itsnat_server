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

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.table.ItsNatFreeTable;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.features.comp.shared.Circle;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class FreeTableSVGDocument implements EventListener,TableModelListener,ListSelectionListener
{
    protected ItsNatDocument itsNatDoc;

    protected ItsNatFreeTable tableComp;
    protected ItsNatHTMLInputButton removeButton;
    protected ItsNatHTMLInputText[] itemComp;
    protected ItsNatHTMLInputText posComp;
    protected ItsNatHTMLInputButton updateButton;
    protected ItsNatHTMLInputButton insertButton;

    public FreeTableSVGDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        startExample();
    }

    public ItsNatDocument getItsNatDocument()
    {
        return itsNatDoc;
    }

    public void startExample()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.tableComp = (ItsNatFreeTable)compMgr.createItsNatComponentById("compId","freeTable",null);

        tableComp.setItsNatTableCellRenderer(new CircleTableCellRenderer());

        DefaultTableModel dataModel = (DefaultTableModel)tableComp.getTableModel();
        dataModel.addColumn("Column 1");
        dataModel.addColumn("Column 2");
        dataModel.addColumn("Column 3");
        dataModel.addColumn("Column 4");
        dataModel.addRow(new Circle[] {new Circle(20),new Circle(25),new Circle(30),new Circle(35)});
        dataModel.addRow(new Circle[] {new Circle(20),new Circle(25),new Circle(30),new Circle(35)});
        dataModel.addRow(new Circle[] {new Circle(20),new Circle(25),new Circle(30),new Circle(35)});

        ListSelectionModel rowSelModel = tableComp.getRowSelectionModel();
        //ListSelectionModel columnSelModel = tableComp.getColumnSelectionModel();

        rowSelModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        // columnSelModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        tableComp.setRowSelectionAllowed(true);
        tableComp.setColumnSelectionAllowed(false);

        rowSelModel.addListSelectionListener(new SVGCircleTableRowSelectionDecoration(tableComp));

        rowSelModel.setSelectionInterval(1,1);

        dataModel.addTableModelListener(this);
        rowSelModel.addListSelectionListener(this);

        this.removeButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("removeId");
        removeButton.addEventListener("click",this);

        this.itemComp = new ItsNatHTMLInputText[dataModel.getColumnCount()];
        for(int i = 0; i < itemComp.length; i++)
        {
            this.itemComp[i] = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("itemId" + i);
            Circle circle = (Circle)dataModel.getValueAt(rowSelModel.getMinSelectionIndex(), i);
            itemComp[i].setText(circle.toString());
        }

        this.posComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("posId");
        posComp.setText(Integer.toString(rowSelModel.getMinSelectionIndex()));

        this.updateButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("updateId");
        updateButton.addEventListener("click",this);

        this.insertButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("insertId");
        insertButton.addEventListener("click",this);
    }

    public void handleEvent(Event evt)
    {
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
            int[] newRadio = new int[itemComp.length];
            for(int i = 0; i < itemComp.length; i++)
                newRadio[i] = Integer.parseInt(itemComp[i].getText());

            int row;
            try
            {
                row = Integer.parseInt(posComp.getText());
                DefaultTableModel dataModel = (DefaultTableModel)tableComp.getTableModel();
                if (currentTarget == updateButton.getHTMLInputElement())
                {
                    for(int i = 0; i < itemComp.length; i++)
                    {
                        Circle circle = (Circle)dataModel.getValueAt(row,i);
                        circle.setRadio(newRadio[i]);
                        dataModel.setValueAt(circle,row,i);// To notify this change
                    }
                }
                else
                {
                    Circle[] circles = new Circle[newRadio.length];
                    for (int i = 0; i < newRadio.length; i++)
                        circles[i] = new Circle(newRadio[i]);
                    dataModel.insertRow(row,circles);
                }
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

    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        ListSelectionModel rowSelModel = (ListSelectionModel)e.getSource();

        //int first = e.getFirstIndex();
        //int last = e.getLastIndex();

        int row = rowSelModel.getMinSelectionIndex(); // First selected
        if (row != -1)
        {
            for(int i = 0; i < itemComp.length; i++)
            {
                Circle circle = (Circle)tableComp.getTableModel().getValueAt(row,i);
                itemComp[i].setText(circle.toString());
            }
            posComp.setText(Integer.toString(row));
        }
    }

}
