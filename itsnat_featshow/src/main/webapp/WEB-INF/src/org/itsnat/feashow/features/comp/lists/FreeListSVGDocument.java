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

package org.itsnat.feashow.features.comp.lists;


import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.features.comp.shared.Circle;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class FreeListSVGDocument implements EventListener,ListDataListener,ListSelectionListener
{
    protected ItsNatDocument itsNatDoc;
    protected ItsNatFreeListMultSel listComp;
    protected ItsNatHTMLInputButton removeButton;
    protected ItsNatHTMLInputText itemComp;
    protected ItsNatHTMLInputText posComp;
    protected ItsNatHTMLInputButton updateButton;
    protected ItsNatHTMLInputButton insertButton;

    public FreeListSVGDocument(ItsNatDocument itsNatDoc)
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

        this.listComp = (ItsNatFreeListMultSel)compMgr.createItsNatComponentById("compId","freeListMultSel",null);

        listComp.setItsNatListCellRenderer(new CircleListCellRenderer());
        listComp.setItsNatListCellEditor(null); // disables in-place editing

        DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();
        dataModel.addElement(new Circle(20));
        dataModel.addElement(new Circle(30));
        dataModel.addElement(new Circle(40));
        dataModel.addElement(new Circle(50));
        dataModel.addElement(new Circle(60));

        ListSelectionModel selModel = listComp.getListSelectionModel();
        selModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        selModel.setSelectionInterval(2,3);

        listComp.addEventListener("click",this);
        dataModel.addListDataListener(this);
        selModel.addListSelectionListener(this);

        this.removeButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("removeId");
        removeButton.addEventListener("click",this);

        this.itemComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("itemId");
        itemComp.setText(listComp.getListModel().getElementAt(listComp.getSelectedIndex()).toString());

        this.posComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("posId");
        posComp.setText(Integer.toString(listComp.getSelectedIndex()));

        this.updateButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("updateId");
        updateButton.addEventListener("click",this);

        this.insertButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("insertId");
        insertButton.addEventListener("click",this);

        for(int i = 0; i < dataModel.getSize(); i++)
        {
            decorateSelection(i,selModel.isSelectedIndex(i));
        }
    }

    public void handleEvent(Event evt)
    {
        EventTarget currentTarget = evt.getCurrentTarget();
        if (currentTarget == removeButton.getHTMLInputElement())
        {
            DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();
            ListSelectionModel selModel = listComp.getListSelectionModel();
            if (!selModel.isSelectionEmpty())
            {
                // Selection Model is in SINGLE_INTERVAL_SELECTION mode
                int min = selModel.getMinSelectionIndex();
                int max = selModel.getMaxSelectionIndex();
                dataModel.removeRange(min,max);
            }
        }
        else if ((currentTarget == updateButton.getHTMLInputElement()) ||
                 (currentTarget == insertButton.getHTMLInputElement()))
        {
            int newRadio = Integer.parseInt(itemComp.getText());
            try
            {
                int pos = Integer.parseInt(posComp.getText());
                DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();
                if (currentTarget == updateButton.getHTMLInputElement())
                {
                    Circle circle = (Circle)dataModel.getElementAt(pos);
                    circle.setRadio(newRadio);
                    dataModel.setElementAt(circle,pos); // To notify this change
                }
                else
                    dataModel.insertElementAt(new Circle(newRadio),pos);
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

    public void intervalAdded(ListDataEvent e)
    {
        listChangedLog(e);
    }

    public void intervalRemoved(ListDataEvent e)
    {
        listChangedLog(e);
    }

    public void contentsChanged(ListDataEvent e)
    {
        listChangedLog(e);
    }

    public void listChangedLog(ListDataEvent e)
    {

    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        int first = e.getFirstIndex();
        int last = e.getLastIndex();

        ListSelectionModel selModel = listComp.getListSelectionModel();

        int index = listComp.getSelectedIndex(); // First selected
        if (index != -1)
        {
            Circle circle = (Circle)listComp.getListModel().getElementAt(index);
            itemComp.setText(circle.toString());
            posComp.setText(Integer.toString(index));
        }

        for(int i = first; i <= last; i++)
        {
            decorateSelection(i,selModel.isSelectedIndex(i));
        }
    }

    public void decorateSelection(int index,boolean selected)
    {
        Element circleElem = listComp.getItsNatListUI().getContentElementAt(index);
        if (circleElem == null) return;

        String fillColor;
        if (selected)
            fillColor = "#ff0000";
        else
            fillColor = "#0000ff";

        circleElem.setAttribute("fill",fillColor);
    }

}
