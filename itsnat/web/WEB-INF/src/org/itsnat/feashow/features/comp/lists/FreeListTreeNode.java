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
import javax.swing.ButtonModel;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class FreeListTreeNode extends FeatureTreeNode implements EventListener,ListDataListener,ListSelectionListener,ItemListener
{
    protected ItsNatHTMLInputCheckBox useSingleClickComp;
    protected ItsNatFreeListMultSel listComp;
    protected ItsNatHTMLInputButton removeButton;
    protected ItsNatHTMLInputText itemComp;
    protected ItsNatHTMLInputText posComp;
    protected ItsNatHTMLInputButton updateButton;
    protected ItsNatHTMLInputButton insertButton;
    protected ItsNatHTMLInputCheckBox joystickModeComp;

    public FreeListTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.useSingleClickComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("useSingleClickId");
        useSingleClickComp.getToggleButtonModel().addItemListener(this);

        this.listComp = (ItsNatFreeListMultSel)compMgr.createItsNatComponentById("compId","freeListMultSel",null);

        DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();
        dataModel.addElement("Madrid");
        dataModel.addElement("Sevilla");
        dataModel.addElement("Segovia");
        dataModel.addElement("Barcelona");
        dataModel.addElement("Oviedo");
        dataModel.addElement("Valencia");

        ListSelectionModel selModel = listComp.getListSelectionModel();
        selModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        selModel.addListSelectionListener(new ListSelectionDecorator(listComp));

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

        this.joystickModeComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("joystickModeId");
        joystickModeComp.getToggleButtonModel().addItemListener(this);
        joystickModeComp.setSelected(isJoystickModePreferred());

        if (isUCWEB())
        {
            Element ucwebElem = itsNatDoc.getDocument().getElementById("ucwebId");
            ucwebElem.removeAttribute("style");
        }
    }

    public void endExamplePanel()
    {
        useSingleClickComp.dispose();
        this.useSingleClickComp = null;

        this.listComp.dispose();
        this.listComp = null;

        this.removeButton.dispose();
        this.removeButton = null;

        this.itemComp.dispose();
        this.itemComp = null;

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

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        int first = e.getFirstIndex();
        int last = e.getLastIndex();

        ListSelectionModel selModel = listComp.getListSelectionModel();
        String fact = "";
        for(int i = first; i <= last; i++)
        {
            boolean selected = selModel.isSelectedIndex(i);
            if (selected)
                fact += ", selected ";
            else
                fact += ", deselected ";
            fact += i;
        }

        log(e.getClass().toString() + " " + fact);

        int index = listComp.getSelectedIndex(); // First selected
        if (index != -1)
        {
            Object value = listComp.getListModel().getElementAt(index);
            itemComp.setText(value.toString());
            posComp.setText(Integer.toString(index));
        }
    }

    public void itemStateChanged(ItemEvent e)
    {
        boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
        ButtonModel model = (ButtonModel)e.getSource();
        if (model == joystickModeComp.getToggleButtonModel())
            listComp.setJoystickMode(selected);
        else if (model == useSingleClickComp.getToggleButtonModel())
            listComp.setEditorActivatorEvent(selected? "click" : "dblclick");
    }

}
