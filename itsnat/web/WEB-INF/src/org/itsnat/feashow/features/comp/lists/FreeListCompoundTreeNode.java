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
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.feashow.FeatureTreeNode;
import org.itsnat.feashow.features.comp.shared.Person;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

public class FreeListCompoundTreeNode extends FeatureTreeNode implements EventListener,ListDataListener,ListSelectionListener,ItemListener
{
    protected ItsNatFreeListMultSel listComp;
    protected ItsNatHTMLInputButton removeButton;
    protected ItsNatHTMLInputText personFirstNameComp;
    protected ItsNatHTMLInputText personLastNameComp;
    protected ItsNatHTMLInputText posComp;
    protected ItsNatHTMLInputButton updateButton;
    protected ItsNatHTMLInputButton insertButton;
    protected ItsNatHTMLInputCheckBox joystickModeComp;
    protected EventListener globalListener;

    public FreeListCompoundTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.listComp = (ItsNatFreeListMultSel)compMgr.createItsNatComponentById("compId","freeListMultSel",null);

        listComp.setItsNatListCellRenderer(new PersonRenderer());
        listComp.setItsNatListCellEditor(null);// disable

        DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();

        dataModel.addElement(new Person("Meredith","Grey"));
        dataModel.addElement(new Person("Cristina","Yang"));
        dataModel.addElement(new Person("Izzie","Stevens"));
        dataModel.addElement(new Person("Alex","Karev"));
        dataModel.addElement(new Person("George","O'Malley"));
        dataModel.addElement(new Person("Derek","Shepherd"));

        ListSelectionModel selModel = listComp.getListSelectionModel();
        selModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        selModel.setSelectionInterval(2,3);

        listComp.addEventListener("click",this);
        selModel.addListSelectionListener(this);
        dataModel.addListDataListener(this);

        this.removeButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("removeId");
        removeButton.addEventListener("click",this);

        Person person = (Person)listComp.getListModel().getElementAt(listComp.getSelectedIndex());
        this.personFirstNameComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("personFirstNameId");
        personFirstNameComp.setText(person.getFirstName());

        this.personLastNameComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("personLastNameId");
        personLastNameComp.setText(person.getLastName());

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

        this.joystickModeComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("joystickModeId");
        joystickModeComp.getToggleButtonModel().addItemListener(this);
        joystickModeComp.setSelected(isJoystickModePreferred());

        this.globalListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                EventTarget target = evt.getTarget();
                String type = evt.getType();
                if (type.equals("click") && target instanceof HTMLInputElement &&
                    "text".equals(((HTMLInputElement)target).getAttribute("type")))
                {
                    // Avoids item selection when modifying text fields,
                    // fix weird behavior in UCWEB.
                    ((ItsNatEvent)evt).getItsNatEventListenerChain().stop();
                }
            }
        };
        itsNatDoc.addEventListener(globalListener);
        
        if (isUCWEB())
        {
            Element ucwebElem = itsNatDoc.getDocument().getElementById("ucwebId");
            ucwebElem.removeAttribute("style");
        }
    }

    public void endExamplePanel()
    {
        Element parentElem = listComp.getElement();
        listComp.getItsNatComponentManager().removeItsNatComponents(parentElem,true); // Frees remaining person related components (text boxes)

        this.listComp.dispose();
        this.listComp = null;

        this.removeButton.dispose();
        this.removeButton = null;

        this.personFirstNameComp.dispose();
        this.personFirstNameComp = null;

        this.personLastNameComp.dispose();
        this.personLastNameComp = null;

        this.posComp.dispose();
        this.posComp = null;

        this.updateButton.dispose();
        this.updateButton = null;

        this.insertButton.dispose();
        this.insertButton = null;

        this.joystickModeComp.dispose();
        this.joystickModeComp = null;

        ItsNatDocument itsNatDoc = getItsNatDocument();
        itsNatDoc.removeEventListener(globalListener);
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
            String firstName = personFirstNameComp.getText();
            String lastName = personLastNameComp.getText();
            try
            {
                int pos = Integer.parseInt(posComp.getText());
                DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();
                if (currentTarget == updateButton.getHTMLInputElement())
                {
                    Person person = (Person)dataModel.getElementAt(pos);
                    person.setFirstName(firstName);
                    person.setLastName(lastName);
                    dataModel.setElementAt(person,pos); // To notify the changes (fires an event)
                }
                else
                {
                    Person person = new Person(firstName,lastName);
                    dataModel.insertElementAt(person,pos);
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
            Person person = (Person)listComp.getListModel().getElementAt(index);
            personFirstNameComp.setText(person.getFirstName());
            personLastNameComp.setText(person.getLastName());
            posComp.setText(Integer.toString(index));
        }

        for(int i = first; i <= last; i++)
        {
            decorateSelection(i,selModel.isSelectedIndex(i));
        }
    }

    public void decorateSelection(int index,boolean selected)
    {
        Element elem = listComp.getItsNatListUI().getContentElementAt(index);
        if (elem == null) return;

        if (selected)
            setAttribute(elem,"style","background:rgb(0,0,255); color:white;");
        else
            elem.removeAttribute("style");
    }

    public static void setAttribute(Element elem,String name,String value)
    {
        String old = elem.getAttribute(name);
        if (old.equals(value)) return; // Avoids redundant operations

        elem.setAttribute(name,value);
    }

    public void itemStateChanged(ItemEvent e)
    {
        boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
        listComp.setJoystickMode(selected);
    }
}
