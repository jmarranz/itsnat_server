/*
 * TestInsertFragment.java
 *
 * Created on 9 de enero de 2007, 9:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.free;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.inc.ItsNatFreeInclude;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLInputElement;
import test.web.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestFreeInclude extends TestBaseHTMLDocument implements EventListener
{
    protected ItsNatHTMLInputButton button;
    protected ItsNatFreeInclude includeComp;

    /** Creates a new instance of TestInsertFragment */
    public TestFreeInclude(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        HTMLInputElement elem = (HTMLInputElement)doc.getElementById("addRemoveIncludeId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.button = (ItsNatHTMLInputButton)componentMgr.findItsNatComponent(elem);
        this.includeComp = (ItsNatFreeInclude)componentMgr.findItsNatComponentById("freeIncludeId");

        removeInclude();

        button.addEventListener("click",this);
    }

    public void handleEvent(final Event evt)
    {
//        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
//        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();

        if (includeComp.isIncluded())
            removeInclude();
        else
            include();
    }

    public void include()
    {
        boolean buildComp = !itsNatDoc.getItsNatDocumentTemplate().isAutoBuildComponents();
        includeComp.includeFragment("test_html_fragment_fragment",buildComp);
        button.getHTMLInputElement().setValue("Remove Included");
    }

    public void removeInclude()
    {
        includeComp.removeFragment(); // Si no hay nada incluido no hace nada
        button.getHTMLInputElement().setValue("Include");
    }
}
