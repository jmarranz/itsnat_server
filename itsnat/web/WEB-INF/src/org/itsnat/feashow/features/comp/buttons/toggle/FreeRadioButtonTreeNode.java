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

package org.itsnat.feashow.features.comp.buttons.toggle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton.ToggleButtonModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.ItsNatButtonGroup;
import org.itsnat.comp.button.toggle.ItsNatFreeRadioButton;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class FreeRadioButtonTreeNode extends FeatureTreeNode implements EventListener,ActionListener,ItemListener
{
    protected ItsNatFreeRadioButton buttonComp1;
    protected ItsNatFreeRadioButton buttonComp2;

    public FreeRadioButtonTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.buttonComp1 = (ItsNatFreeRadioButton)compMgr.createItsNatComponentById("buttonId1","freeRadioButton",null);
        buttonComp1.addEventListener("click",this);

        this.buttonComp2 = (ItsNatFreeRadioButton)compMgr.createItsNatComponentById("buttonId2","freeRadioButton",null);
        buttonComp2.addEventListener("click",this);

        ButtonGroup group = new ButtonGroup();
        ItsNatButtonGroup compGroup = compMgr.getItsNatButtonGroup(group);
        compGroup.addButton(buttonComp1);
        compGroup.addButton(buttonComp2);


        ToggleButtonModel dataModel1 = buttonComp1.getToggleButtonModel();
        dataModel1.addActionListener(this);
        dataModel1.addItemListener(this);

        ToggleButtonModel dataModel2 = buttonComp2.getToggleButtonModel();
        dataModel2.addActionListener(this);
        dataModel2.addItemListener(this);
    }

    public void endExamplePanel()
    {
        this.buttonComp1.dispose();
        this.buttonComp1 = null;

        this.buttonComp2.dispose();
        this.buttonComp2 = null;
    }

    public void handleEvent(Event evt)
    {
        EventTarget currentTarget = evt.getCurrentTarget();
        String button;
        if (currentTarget == buttonComp1.getElement())
            button = "button 1";
        else
            button = "button 2";

        log(evt,button);
    }

    public void actionPerformed(ActionEvent e)
    {
        String button;
        if (e.getSource() == buttonComp1.getToggleButtonModel())
            button = "button 1";
        else
            button = "button 2";

        log(e.getClass().toString() + " " + button);
    }

    public void itemStateChanged(ItemEvent e)
    {
        String fact;
        int state = e.getStateChange();
        if (state == ItemEvent.SELECTED)
            fact = "selected";
        else
            fact = "deselected";

        if (e.getItem() == buttonComp1.getToggleButtonModel())
            fact += " 1";
        else
            fact += " 2";

        log(e.getClass().toString() + " " + fact);

        if (e.getItem() == buttonComp1.getToggleButtonModel())
            updateDecoration(buttonComp1);
        else
            updateDecoration(buttonComp2);
    }

    public void updateDecoration(ItsNatFreeRadioButton buttonComp)
    {
        Element buttonElem = buttonComp.getElement();
        ToggleButtonModel model = buttonComp.getToggleButtonModel();
        if (model.isSelected())
            setAttribute(buttonElem,"style","background: rgb(253,147,173);"); // weak red
        else
            buttonElem.removeAttribute("style");
    }

    public static void setAttribute(Element elem,String name,String value)
    {
        String old = elem.getAttribute(name);
        if (old.equals(value)) return; // Avoids redundant operations

        elem.setAttribute(name,value);
    }
}
