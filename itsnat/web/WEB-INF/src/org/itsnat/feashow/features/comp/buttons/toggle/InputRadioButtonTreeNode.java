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
import org.itsnat.comp.button.toggle.ItsNatHTMLInputRadio;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;


public class InputRadioButtonTreeNode extends FeatureTreeNode implements EventListener,ActionListener,ItemListener
{
    protected ItsNatHTMLInputRadio inputComp1;
    protected ItsNatHTMLInputRadio inputComp2;

    public InputRadioButtonTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.inputComp1 = (ItsNatHTMLInputRadio)compMgr.createItsNatComponentById("inputId1");
        inputComp1.addEventListener("click",this);

        this.inputComp2 = (ItsNatHTMLInputRadio)compMgr.createItsNatComponentById("inputId2");
        inputComp2.addEventListener("click",this);

        // In this case this code is not really needed because "name" attribute is used to group:
        ItsNatButtonGroup itsNatGrp1 = inputComp1.getItsNatButtonGroup();
        ItsNatButtonGroup itsNatGrp2 = inputComp2.getItsNatButtonGroup();
        if ( ((itsNatGrp1 == null) || (itsNatGrp2 == null)) ||
             (itsNatGrp1.getButtonGroup() != itsNatGrp2.getButtonGroup()) )
        {
            ButtonGroup group = new ButtonGroup();
            ItsNatButtonGroup htmlGroup = compMgr.getItsNatButtonGroup(group);
            htmlGroup.addButton(inputComp1);
            htmlGroup.addButton(inputComp2);
        }

        ToggleButtonModel dataModel1 = inputComp1.getToggleButtonModel();
        dataModel1.addActionListener(this);
        dataModel1.addItemListener(this);

        ToggleButtonModel dataModel2 = inputComp2.getToggleButtonModel();
        dataModel2.addActionListener(this);
        dataModel2.addItemListener(this);
    }

    public void endExamplePanel()
    {
        this.inputComp1.dispose();
        this.inputComp1 = null;

        this.inputComp2.dispose();
        this.inputComp2 = null;
    }

    public void handleEvent(Event evt)
    {
        EventTarget currentTarget = evt.getCurrentTarget();
        String button;
        if (currentTarget == inputComp1.getHTMLInputElement())
            button = "button 1";
        else
            button = "button 2";

        log(evt,button);
    }

    public void actionPerformed(ActionEvent e)
    {
        String button;
        if (e.getSource() == inputComp1.getToggleButtonModel())
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

        if (e.getItem() == inputComp1.getToggleButtonModel())
            fact += " 1";
        else
            fact += " 2";

        log(e.getClass().toString() + " " + fact);
    }

}
