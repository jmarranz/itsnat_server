/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.extjsexam.tab;

import inexp.extjsexam.ExtJSExampleDocument;
import inexp.extjsexam.model.DBSimulator;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.table.ItsNatFreeTable;
import org.itsnat.core.NameValue;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class TabContainingTable extends TabBase implements EventListener,TableModelListener,ItemListener
{
    protected Element addItemElem;
    protected Element removeItemElem;
    protected ItsNatHTMLInputCheckBox editInPlaceComp;
    protected ItsNatHTMLInputCheckBox joystickModeComp;
    protected ItsNatFreeTable tableComp;
    protected Element nameElem;
    protected boolean ascending = false;

    public TabContainingTable(ExtJSExampleDocument parent)
    {
        super(parent);
    }

    public String getFragmentName()
    {
        return "extjsexample_paneltable";
    }

    public DBSimulator getDataModel()
    {
        return extJSDoc.getDataModel();
    }

    public abstract void fillTableWithData();
    
    public void render()
    {
        ItsNatHTMLDocument itsNatDoc = getItsNatHTMLDocument();
        Document doc = itsNatDoc.getDocument();

        this.addItemElem = doc.getElementById("addItemId");
        ((EventTarget)addItemElem).addEventListener("click",this, false);
        this.removeItemElem = doc.getElementById("removeItemId");
        ((EventTarget)removeItemElem).addEventListener("click",this, false);

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.editInPlaceComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("editInPlaceId");
        boolean mobile = extJSDoc.isMobileBrowser();
        editInPlaceComp.setSelected(!mobile);
        editInPlaceComp.getToggleButtonModel().addItemListener(this);

        this.joystickModeComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("joystickModeId");
        joystickModeComp.setSelected(itsNatDoc.isJoystickMode());
        joystickModeComp.getToggleButtonModel().addItemListener(this);

        Element tableElem = doc.getElementById("tableId");
        this.tableComp = compMgr.createItsNatFreeTable(tableElem, CustomTableStructure.SINGLETON,null);
        tableComp.setJoystickMode(joystickModeComp.isSelected());

        DefaultTableModel tableModel = (DefaultTableModel)tableComp.getTableModel();
        tableModel.addColumn("Name"); // not shown (no header)
        tableModel.addColumn("Description"); // not shown (no header)
        fillTableWithData();

        tableComp.setColumnSelectionAllowed(false);
        tableComp.setRowSelectionAllowed(true);
        ListSelectionModel rowSel = tableComp.getRowSelectionModel();
        rowSel.addListSelectionListener(new RowSelectionDecoration(tableComp,this));

        tableComp.setItsNatTableCellEditor(new NameValueInplaceEditor(tableComp.getItsNatTableCellEditor()));
        tableComp.setEditingEnabled(editInPlaceComp.isSelected());
        if (mobile) tableComp.setEditorActivatorEvent("click");
        tableComp.getTableModel().addTableModelListener(this);

        this.nameElem = doc.getElementById("nameId");
        ((EventTarget)nameElem).addEventListener("click", this,false);
    }

    public Element getAddItemElement()
    {
        return addItemElem;
    }

    public Element getRemoveItemElement()
    {
        return removeItemElem;
    }

    public void remove()
    {
        ItsNatHTMLDocument itsNatDoc = getItsNatHTMLDocument();

        ((EventTarget)addItemElem).removeEventListener("click",this, false);
        this.addItemElem = null;
        ((EventTarget)removeItemElem).removeEventListener("click",this, false);
        this.removeItemElem = null;

        tableComp.dispose();
        this.tableComp = null;

        ((EventTarget)nameElem).removeEventListener("click", this,false);
        this.nameElem = null;
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == nameElem)
        {
            this.ascending = !ascending;
            decorateNameTitle();
            orderByName();
        }
        else if (currTarget == addItemElem)
        {
            new ModalAddNewItem(this);
        }
        else if (currTarget == removeItemElem)
        {
            int row = tableComp.getSelectedRow();
            NameValue value = (NameValue)tableComp.getTableModel().getValueAt(row,0);
            new ModalConfirmRemoveItem(this,row,value.getName());
        }
    }

    public void decorateNameTitle()
    {
        Element td = (Element)nameElem.getParentNode();
        String cssClass = td.getAttribute("class");
        if (cssClass.indexOf("x-grid3-hd-over") == -1)
            cssClass = cssClass + " x-grid3-hd-over";

        // Changing the icon
        if (ascending)
        {
            cssClass = cssClass.replaceFirst(" sort-desc", "");
            cssClass = cssClass + " sort-asc";
        }
        else
        {
            cssClass = cssClass.replaceFirst(" sort-asc", "");
            cssClass = cssClass + " sort-desc";
        }

        td.setAttribute("class",cssClass);
    }

    public void orderByName()
    {
        // If called while editing in place, the new value in control is lost because the editor element is removed when sorting (no blur/change event are sent)

        DefaultTableModel tableModel = (DefaultTableModel)tableComp.getTableModel();
        int rowCount = tableModel.getRowCount();
        List<Object[]> rows = new ArrayList<Object[]>(rowCount);
        for(int row = 0; row < rowCount; row++)
        {
            NameValue name = (NameValue)tableModel.getValueAt(row,0);
            NameValue desc = (NameValue)tableModel.getValueAt(row,1);
            rows.add(new Object[] { name, desc });
        }

        Collections.sort(rows, new Comparator<Object[]>()
            {
              public int compare(Object[] o1, Object[] o2)
              {
                  NameValue name1 = (NameValue)o1[0];
                  NameValue name2 = (NameValue)o2[0];
                  int order = name1.getName().compareTo(name2.getName());
                  if (!ascending) order = -order;
                  return order;
              }
            }
        );

        tableModel.setRowCount(0);
        for(int row = 0; row < rowCount; row++)
        {
            Object[] rowData = (Object[])rows.get(row);
            tableModel.addRow(rowData);
        }
    }

    public void tableChanged(TableModelEvent e)
    {
        int firstRow = e.getFirstRow();
        //int lastRow = e.getLastRow();

        int type = e.getType();
        switch(type)
        {
            case TableModelEvent.INSERT: break;
            case TableModelEvent.DELETE: break;
            case TableModelEvent.UPDATE:
                int column = e.getColumn();
                NameValue value = (NameValue)tableComp.getTableModel().getValueAt(firstRow,column);
                if (column == 0) // name
                    updateName(value.getName(),value.getValue());
                else // description
                    updateDescription(value.getName(),value.getValue());
                break;
        }
    }

    public void removeItem(int row)
    {
        DefaultTableModel tableModel = (DefaultTableModel)tableComp.getTableModel();
        NameValue value = (NameValue)tableModel.getValueAt(row,0);
        Object modelObj = value.getValue();

        removeItemDB(modelObj);
        tableModel.removeRow(row);
    }

    public void itemStateChanged(ItemEvent e)
    {
        int state = e.getStateChange();
        boolean selected = (state == ItemEvent.SELECTED);
        if (editInPlaceComp.getToggleButtonModel() == e.getItemSelectable())
            tableComp.setEditingEnabled(selected);
        else
        {
            tableComp.setJoystickMode(selected);
        }
    }

    public abstract void updateName(String name,Object modelObj);

    public abstract void updateDescription(String desc,Object modelObj);

    public abstract void addNewItem(String name,String desc);

    public abstract void removeItemDB(Object modelObj);
}
