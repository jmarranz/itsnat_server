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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class FreeComboBoxTreeNode extends FeatureTreeNode implements EventListener,ListDataListener,ItemListener
{
    protected ItsNatFreeComboBox comboComp;
    protected ItsNatHTMLInputButton removeButton;
    protected ItsNatHTMLInputText itemComp;
    protected ItsNatHTMLInputText posComp;
    protected ItsNatHTMLInputButton insertButton;
    protected ItsNatHTMLInputCheckBox joystickModeComp;

    public FreeComboBoxTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.comboComp = (ItsNatFreeComboBox)compMgr.createItsNatComponentById("compId","freeComboBox",null);

        DefaultComboBoxModel dataModel = (DefaultComboBoxModel)comboComp.getComboBoxModel();
        dataModel.addElement("Madrid");
        dataModel.addElement("Sevilla");
        dataModel.addElement("Segovia");
        dataModel.addElement("Barcelona");
        dataModel.addElement("Oviedo");
        dataModel.addElement("Valencia");

        comboComp.addItemListener(new ComboBoxSelectionDecorator(comboComp));

        dataModel.setSelectedItem("Segovia");

        comboComp.addEventListener("click",this);
        dataModel.addListDataListener(this);
        comboComp.addItemListener(this);

        this.removeButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("removeId");
        removeButton.addEventListener("click",this);

        this.itemComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("itemId");
        itemComp.setText(dataModel.getSelectedItem().toString());

        this.posComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("posId");
        posComp.setText(Integer.toString(comboComp.getSelectedIndex()));

        this.insertButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("insertId");
        insertButton.addEventListener("click",this);

        this.joystickModeComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("joystickModeId");
        joystickModeComp.getToggleButtonModel().addItemListener(this);
        joystickModeComp.setSelected(isJoystickModePreferred());
    }

    public void endExamplePanel()
    {
        this.comboComp.dispose();
        this.comboComp = null;

        this.removeButton.dispose();
        this.removeButton = null;

        this.itemComp.dispose();
        this.itemComp = null;

        this.posComp.dispose();
        this.posComp = null;

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
        int index0 = e.getIndex0();
        int index1 = e.getIndex1();

        String action = "";
        int type = e.getType();
        switch(type)
        {
            case ListDataEvent.INTERVAL_ADDED:   action = "added"; break;
            case ListDataEvent.INTERVAL_REMOVED: action = "removed"; break;
            case ListDataEvent.CONTENTS_CHANGED: action = "changed"; break;
        }

        String interval = "";
        if (index0 != -1)
            interval = "interval " + index0 + "-" + index1;

        log(e.getClass().toString() + " " + action + " " + interval);
    }

    public void itemStateChanged(ItemEvent e)
    {
        boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
        if (e.getSource() == joystickModeComp.getToggleButtonModel())
        {
            comboComp.setJoystickMode(selected);
        }
        else
        {
            String fact;
            if (selected)
                fact = "selected";
            else
                fact = "deselected";

            log(e.getClass().toString() + " " + fact + " " + e.getItem());

            if (selected)
            {
                itemComp.setText(e.getItem().toString());
                posComp.setText(Integer.toString(comboComp.getSelectedIndex()));
            }
        }
    }
}
