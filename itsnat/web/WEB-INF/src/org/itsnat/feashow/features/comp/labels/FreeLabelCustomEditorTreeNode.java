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
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.label.ItsNatFreeLabel;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.itsnat.feashow.features.comp.shared.Person;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;


public class FreeLabelCustomEditorTreeNode extends FeatureTreeNode implements EventListener,PropertyChangeListener
{
    protected ItsNatHTMLInputCheckBox useSingleClickComp;
    protected ItsNatFreeLabel comp;

    public FreeLabelCustomEditorTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)getItsNatDocument();
        ItsNatHTMLComponentManager compMgr = itsNatDoc.getItsNatHTMLComponentManager();

        this.useSingleClickComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("useSingleClickId");
        useSingleClickComp.addEventListener("click",this);

        this.comp = (ItsNatFreeLabel)compMgr.createItsNatComponentById("labelId","freeLabel",null);
        try { comp.setValue(new Person("Jose M.","Arranz")); }
        catch(PropertyVetoException ex) { throw new RuntimeException(ex); }

        ItsNatLabelEditor editor = new PersonCustomLabelEditor(comp);
        comp.setItsNatLabelEditor(editor);

        comp.addEventListener("click",this);
        comp.addPropertyChangeListener("value",this);
    }

    public void endExamplePanel()
    {
        useSingleClickComp.dispose();
        this.useSingleClickComp = null;

        this.comp.dispose();
        this.comp = null;
    }

    public void handleEvent(Event evt)
    {
        log(evt);
        if (evt.getCurrentTarget() == useSingleClickComp.getElement())
        {
            boolean sel = useSingleClickComp.isSelected();
            String eventType = sel? "click" : "dblclick";
            comp.setEditorActivatorEvent(eventType);
        }
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        log(evt.getClass() + ", old: " + evt.getOldValue() + ", new: " + evt.getNewValue());
    }
}
