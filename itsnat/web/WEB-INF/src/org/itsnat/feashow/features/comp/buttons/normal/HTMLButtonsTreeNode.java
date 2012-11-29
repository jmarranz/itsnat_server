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

package org.itsnat.feashow.features.comp.buttons.normal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.ItsNatButton;
import org.itsnat.comp.button.normal.ItsNatHTMLAnchor;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.button.normal.ItsNatHTMLInputImage;
import org.itsnat.comp.button.normal.ItsNatHTMLInputReset;
import org.itsnat.comp.button.normal.ItsNatHTMLInputSubmit;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class HTMLButtonsTreeNode extends FeatureTreeNode implements EventListener,ActionListener,ChangeListener
{
    protected ItsNatHTMLInputButton inputButton;
    protected ItsNatHTMLInputImage inputImage;
    protected ItsNatHTMLInputSubmit inputSubmit;
    protected ItsNatHTMLInputReset inputReset;
    protected ItsNatHTMLButton buttonComp;
    protected ItsNatHTMLAnchor linkComp;

    public HTMLButtonsTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.inputButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("inputButtonId");
        addListeners(inputButton);

        this.inputImage = (ItsNatHTMLInputImage)compMgr.createItsNatComponentById("inputImageId");
        addListeners(inputImage);

        this.inputSubmit = (ItsNatHTMLInputSubmit)compMgr.createItsNatComponentById("inputSubmitId");
        addListeners(inputSubmit);

        this.inputReset = (ItsNatHTMLInputReset)compMgr.createItsNatComponentById("inputResetId");
        addListeners(inputReset);

        this.buttonComp = (ItsNatHTMLButton)compMgr.createItsNatComponentById("buttonId");
        addListeners(buttonComp);

        this.linkComp = (ItsNatHTMLAnchor)compMgr.createItsNatComponentById("linkId");
        addListeners(linkComp);
    }

    public void endExamplePanel()
    {
        this.inputButton.dispose();
        this.inputButton = null;

        this.inputImage.dispose();
        this.inputImage = null;

        this.inputSubmit.dispose();
        this.inputSubmit = null;

        this.inputReset.dispose();
        this.inputReset = null;

        this.buttonComp.dispose();
        this.buttonComp = null;

        this.linkComp.dispose();
        this.linkComp = null;
    }

    public void addListeners(ItsNatButton comp)
    {
        comp.addEventListener("click",this);
        ButtonModel dataModel = comp.getButtonModel();
        dataModel.addActionListener(this);
        dataModel.addChangeListener(this);
    }

    public void handleEvent(Event evt)
    {
        log(evt);
    }

    public void actionPerformed(ActionEvent e)
    {
        log(e.getClass().toString());
    }

    public void stateChanged(ChangeEvent e)
    {
        ButtonModel model = (ButtonModel)e.getSource();

        String fact = "";
        if (model.isArmed())
            fact += "armed ";
        if (model.isPressed())
            fact += "pressed ";
        if (model.isRollover())
            fact += "rollover ";
        if (model.isSelected())
            fact += "selected ";

        if (!fact.equals(""))
        {
            log(e.getClass() + " " + fact + " ");
        }
    }

}
