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

import javax.swing.AbstractCellEditor;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.label.ItsNatFreeLabel;
import org.itsnat.comp.label.ItsNatLabel;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.feashow.features.comp.shared.Person;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;


public class PersonCustomLabelEditor extends AbstractCellEditor implements ItsNatLabelEditor,EventListener
{
    protected PersonEditorComponent editorComp;
    protected ItsNatFreeLabel parentComp;
    protected String eventType;

    public PersonCustomLabelEditor(ItsNatFreeLabel parentComp)
    {
        this.parentComp = parentComp;
    }


    public ItsNatComponent getLabelEditorComponent(ItsNatLabel label, Object value, Element labelElem)
    {
        ItsNatComponentManager compMgr = label.getItsNatComponentManager();
        ItsNatDocument itsNatDoc = compMgr.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        ItsNatDocFragmentTemplate docFragTemplate = servlet.getItsNatDocFragmentTemplate("feashow.comp.shared.personEditor");
        DocumentFragment editorFrag = docFragTemplate.loadDocumentFragment(itsNatDoc);

        labelElem.appendChild(editorFrag);

        Element editorElem = doc.getElementById("personEditorId");

        this.editorComp = new PersonEditorComponent((Person)value,editorElem,compMgr);

        editorComp.getOKButton().addEventListener("click",this);
        editorComp.getCancelButton().addEventListener("click",this);

        this.eventType = parentComp.getEditorActivatorEvent();
        parentComp.setEditorActivatorEvent(null); // To avoid recreation of a new editing instance by clicking the component
        // Alternative:
        // parentComp.setEditingEnabled(false);

        return editorComp;
    }

    public Object getCellEditorValue()
    {
        return editorComp.getPerson();
    }

    public void handleEvent(Event evt)
    {
        if (evt.getCurrentTarget() == editorComp.getOKButton().getHTMLInputElement())
        {
            editorComp.updatePerson();

            editorComp.dispose(); // Before the DOM sub tree is removed from the tree by the editor manager
            stopCellEditing();
        }
        else
        {
            editorComp.dispose(); // "
            cancelCellEditing();
        }

        ClientDocument clientDoc = ((ItsNatEvent)evt).getClientDocument();
        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                // Executed as a "continue" event to avoid registering in the client a new event listener
                // while is processing the same event type (click) => new listener is executed too (undesired).
                parentComp.setEditorActivatorEvent(eventType); // Restore
                // Alternative:
                // parentComp.setEditingEnabled(true);
            }
        };
        clientDoc.addContinueEventListener(null,listener);

        this.editorComp = null;
    }

}
