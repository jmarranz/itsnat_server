/*
 * TestInsertFragment.java
 *
 * Created on 9 de enero de 2007, 9:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLButtonElement;

/**
 *
 * @author jmarranz
 */
public class TestInsertFragment implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected HTMLButtonElement buttonElem;

    /** Creates a new instance of TestInsertFragment */
    public TestInsertFragment(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        this.buttonElem = (HTMLButtonElement)doc.getElementById("insertFragmentId");
        ((EventTarget)buttonElem).addEventListener("click",this,false);
    }

    public void handleEvent(final Event evt)
    {
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();

        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        DocumentFragment docFrag = servlet.getItsNatDocFragmentTemplate("test_html_fragment_fragment").loadDocumentFragment(itsNatDoc); // Por defecto carga el fragmento "body"
        buttonElem.getParentNode().insertBefore(docFrag,buttonElem);
    }
}
