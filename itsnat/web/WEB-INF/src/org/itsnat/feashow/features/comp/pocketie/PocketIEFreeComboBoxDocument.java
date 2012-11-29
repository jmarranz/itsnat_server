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
import javax.swing.DefaultComboBoxModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class PocketIEFreeComboBoxDocument implements EventListener,ItemListener
{
    protected ItsNatDocument itsNatDoc;
    protected ItsNatFreeComboBox comboComp;
    protected ItsNatHTMLInputButton removeButton;
    protected ItsNatHTMLInputText itemComp;
    protected ItsNatHTMLInputText posComp;
    protected ItsNatHTMLInputButton insertButton;

    public PocketIEFreeComboBoxDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        Document doc = itsNatDoc.getDocument();

        Element compElem = doc.getElementById("compId");
        this.comboComp = compMgr.createItsNatFreeComboBox(compElem, null, null);

        DefaultComboBoxModel dataModel = (DefaultComboBoxModel)comboComp.getComboBoxModel();
        dataModel.addElement("Madrid");
        dataModel.addElement("Sevilla");
        dataModel.addElement("Segovia");
        dataModel.addElement("Barcelona");

        comboComp.addItemListener(new PocketIEFreeComboBoxSelectionDecorator(comboComp));

        dataModel.setSelectedItem("Segovia");

        comboComp.addItemListener(this);

        this.removeButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("removeId");
        removeButton.addEventListener("click",this);

        this.itemComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("itemId");
        itemComp.setText(dataModel.getSelectedItem().toString());

        this.posComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("posId");
        posComp.setText(Integer.toString(comboComp.getSelectedIndex()));

        this.insertButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("insertId");
        insertButton.addEventListener("click",this);
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
            DefaultComboBoxModel dataModel = (DefaultComboBoxModel)comboComp.getComboBoxModel();
            Object selected = dataModel.getSelectedItem();
            if (selected != null)
                dataModel.removeElement(selected);
        }
        else if (currentTarget == insertButton.getHTMLInputElement())
        {
            String newItem = itemComp.getText();
            if (comboComp.indexOf(newItem) != -1)
                getItsNatDocument().addCodeToSend("alert('Duplicated Element');");
            else
            {
                try
                {
                    int pos = Integer.parseInt(posComp.getText());
                    DefaultComboBoxModel dataModel = (DefaultComboBoxModel)comboComp.getComboBoxModel();
                    dataModel.insertElementAt(newItem,pos);
                    comboComp.setSelectedIndex(pos);
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
    }

    public void itemStateChanged(ItemEvent e)
    {
        if (e.getStateChange() == ItemEvent.SELECTED)
        {
            itemComp.setText(e.getItem().toString());
            posComp.setText(Integer.toString(comboComp.getSelectedIndex()));
        }
    }

}

