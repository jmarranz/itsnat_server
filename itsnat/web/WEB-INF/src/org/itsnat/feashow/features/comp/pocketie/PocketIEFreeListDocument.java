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


import javax.swing.DefaultListModel;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class PocketIEFreeListDocument implements EventListener,ListSelectionListener,ChangeListener
{
    protected ItsNatDocument itsNatDoc;
    protected ItsNatFreeListMultSel listComp;
    protected ItsNatHTMLInputButton removeButton;
    protected ItsNatHTMLInputText itemComp;
    protected ItsNatHTMLInputText posComp;
    protected ItsNatHTMLInputButton updateButton;
    protected ItsNatHTMLInputButton insertButton;
    protected ItsNatHTMLInputCheckBox editInplaceButton;

    public PocketIEFreeListDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        Document doc = itsNatDoc.getDocument();

        Element compElem = doc.getElementById("compId");
        this.listComp = compMgr.createItsNatFreeListMultSel(compElem, null, null);
        listComp.setSelectionUsesKeyboard(false);

        listComp.setEditorActivatorEvent("click");

        DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();
        dataModel.addElement("Madrid");
        dataModel.addElement("Sevilla");
        dataModel.addElement("Segovia");
        dataModel.addElement("Barcelona");
        dataModel.addElement("Oviedo");
        dataModel.addElement("Valencia");

        ListSelectionModel selModel = listComp.getListSelectionModel();
        selModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        selModel.addListSelectionListener(new PocketIEFreeListSelectionDecorator(listComp));

        selModel.setSelectionInterval(2,3);

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

        this.editInplaceButton = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("editInplaceId");
        ToggleButtonModel inPlaceModel = editInplaceButton.getToggleButtonModel();
        inPlaceModel.setSelected(false);
        inPlaceModel.addChangeListener(this);

        listComp.setEditingEnabled(inPlaceModel.isSelected());
    }

    public ItsNatDocument getItsNatDocument()
    {
        return itsNatDoc;
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
            String newItem = itemComp.getText();
            try
            {
                int pos = Integer.parseInt(posComp.getText());
                DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();
                if (currentTarget == updateButton.getHTMLInputElement())
                    dataModel.setElementAt(newItem,pos);
                else
                    dataModel.insertElementAt(newItem,pos);
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

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        int index = listComp.getSelectedIndex(); // First selected
        if (index != -1)
        {
            Object value = listComp.getListModel().getElementAt(index);
            itemComp.setText(value.toString());
            posComp.setText(Integer.toString(index));
        }
    }

    public void stateChanged(ChangeEvent e)
    {
        ToggleButtonModel dataModel = (ToggleButtonModel)e.getSource();
        listComp.setEditingEnabled(dataModel.isSelected());
    }

}

