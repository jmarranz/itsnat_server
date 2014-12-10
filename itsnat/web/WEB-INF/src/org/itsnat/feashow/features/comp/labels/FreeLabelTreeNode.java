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

package org.itsnat.feashow.features.comp.labels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.label.ItsNatFreeLabel;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.text.ItsNatHTMLInputTextFormatted;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class FreeLabelTreeNode extends FeatureTreeNode implements EventListener,PropertyChangeListener
{
    protected ItsNatHTMLInputCheckBox useSingleClickComp;
    protected ItsNatFreeLabel[] compList = new ItsNatFreeLabel[5];

    public FreeLabelTreeNode()
    {
    }

    public void startExamplePanel()
    {
        isJoystickModePreferred();
        
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)getItsNatDocument();
        ItsNatHTMLComponentManager compMgr = itsNatDoc.getItsNatHTMLComponentManager();

        this.useSingleClickComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("useSingleClickId");
        useSingleClickComp.addEventListener("click",this);

        try
        {
            ItsNatFreeLabel comp;

            comp = (ItsNatFreeLabel)compMgr.createItsNatComponentById("labelId1","freeLabel",null);
            comp.setValue("Any Text");
            ItsNatHTMLInputText textInput = compMgr.createItsNatHTMLInputText(null,null);
            shared(0,comp,textInput);

            comp = (ItsNatFreeLabel)compMgr.createItsNatComponentById("labelId2","freeLabel",null);
            comp.setValue(Boolean.TRUE);
            ItsNatHTMLInputCheckBox checkBox = compMgr.createItsNatHTMLInputCheckBox(null,null);
            shared(1,comp,checkBox);

            comp = (ItsNatFreeLabel)compMgr.createItsNatComponentById("labelId3","freeLabel",null);
            comp.setValue(new Integer(3));
            ItsNatHTMLSelectComboBox comboBox = compMgr.createItsNatHTMLSelectComboBox(null,null);
            DefaultComboBoxModel model = (DefaultComboBoxModel)comboBox.getComboBoxModel();
            for(int i=0; i < 5; i++) model.addElement(new Integer(i));
            shared(2,comp,comboBox);

            comp = (ItsNatFreeLabel)compMgr.createItsNatComponentById("labelId4","freeLabel",null);
            comp.setValue(new Date());
            ItsNatHTMLInputTextFormatted textInputFormatted = compMgr.createItsNatHTMLInputTextFormatted(null,null);
            shared(3,comp,textInputFormatted);

            comp = (ItsNatFreeLabel)compMgr.createItsNatComponentById("labelId5","freeLabel",null);
            comp.setValue("Any \n Text");
            ItsNatHTMLTextArea textArea = compMgr.createItsNatHTMLTextArea(null,null);
            shared(4,comp,textArea);
        }
        catch(PropertyVetoException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void endExamplePanel()
    {
        useSingleClickComp.dispose();
        this.useSingleClickComp = null;

        for(int i = 0; i < compList.length; i++)
        {
            compList[i].dispose();
            compList[i] = null;
        }
    }

    public void shared(int i,ItsNatFreeLabel comp,ItsNatComponent editorComp)
    {
        if (!isOperaMini()) ((Element)editorComp.getNode()).setAttribute("style","width:100%;height:100%"); // The editor fills the parent element    
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)getItsNatDocument();
        ItsNatHTMLComponentManager compMgr = itsNatDoc.getItsNatHTMLComponentManager();
        ItsNatLabelEditor editor = compMgr.createDefaultItsNatLabelEditor(editorComp);
        comp.setItsNatLabelEditor(editor);
        comp.addEventListener("click",this);
        comp.addPropertyChangeListener("value",this);
        compList[i] = comp;
    }

    public void handleEvent(Event evt)
    {
        log(evt);
        if (evt.getCurrentTarget() == useSingleClickComp.getElement())
        {
            boolean sel = useSingleClickComp.isSelected();
            String eventType = sel? "click" : "dblclick";
            for(int i = 0; i < compList.length; i++)
                compList[i].setEditorActivatorEvent(eventType);
        }
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        log(evt.getClass() + ", old: " + evt.getOldValue() + ", new: " + evt.getNewValue());
    }
}
