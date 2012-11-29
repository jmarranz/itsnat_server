/*
 * PersonCustomLabelEditor.java
 *
 * Created on 10 de junio de 2007, 12:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comp.labels;

import javax.swing.AbstractCellEditor;
import manual.comp.shared.Person;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.label.ItsNatLabel;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class PersonCustomLabelEditor extends AbstractCellEditor implements ItsNatLabelEditor,EventListener
{
    protected PersonEditorComponent editorComp;

    public PersonCustomLabelEditor()
    {
    }

    public ItsNatComponent getLabelEditorComponent(ItsNatLabel label, Object value, Element labelElem)
    {
        ItsNatComponentManager compMgr = label.getItsNatComponentManager();
        ItsNatDocument itsNatDoc = compMgr.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        ItsNatDocFragmentTemplate docFragTemplate = servlet.getItsNatDocFragmentTemplate("feashow.components.shared.personEditor");
        DocumentFragment editorFrag = docFragTemplate.loadDocumentFragment(itsNatDoc);

        labelElem.appendChild(editorFrag);

        Element editorElem = doc.getElementById("personEditorId");

        this.editorComp = new PersonEditorComponent((Person)value,editorElem,compMgr);

        editorComp.getOKButton().addEventListener("click",this);
        editorComp.getCancelButton().addEventListener("click",this);

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

        this.editorComp = null;
    }
}
