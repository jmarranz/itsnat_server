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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.toggle.ItsNatFreeCheckBox;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class FreeCheckBoxTreeNode extends FeatureTreeNode implements EventListener,ChangeListener,ItemListener
{
    protected ItsNatFreeCheckBox buttonComp;
    protected boolean selected;

    public FreeCheckBoxTreeNode()
    {
    }

    public void startExamplePanel()
    {
        this.selected = false;

        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.buttonComp = (ItsNatFreeCheckBox)compMgr.createItsNatComponentById("buttonId","freeCheckBox",null);
        buttonComp.addEventListener("click",this);

        ToggleButtonModel dataModel = buttonComp.getToggleButtonModel();
        dataModel.addChangeListener(this);
        dataModel.addItemListener(this);
    }

    public void endExamplePanel()
    {
        this.buttonComp.dispose();
        this.buttonComp = null;
    }

    public void handleEvent(Event evt)
    {
        log(evt);
    }

    public void stateChanged(ChangeEvent e)
    {
        // This event is generated when ButtonModel is changed (armed, pressed etc).
        // the selection state change is detected only
        ButtonModel model = (ButtonModel)e.getSource();

        String fact = "";
        if (model.isSelected() && !selected)
        {
            selected = true;
            fact = "selected ";
        }
        else if (!model.isSelected() && selected)
        {
            selected = false;
            fact = "deselected ";
        }

        if (!fact.equals(""))
        {
            log(e.getClass().toString() + " " + fact + " ");
        }
    }

    public void itemStateChanged(ItemEvent e)
    {
        String fact;
        int state = e.getStateChange();
        if (state == ItemEvent.SELECTED)
            fact = "selected";
        else
            fact = "deselected";

        log(e.getClass().toString() + " " + fact + " ");

        updateDecoration();
    }

    public void updateDecoration()
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
