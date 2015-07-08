/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.asyncforms;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import test.web.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestAutoSyncFormsDocument extends TestBaseHTMLDocument implements EventListener
{
    public TestAutoSyncFormsDocument(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        super(itsNatDoc);

        Document doc = itsNatDoc.getDocument();

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.setMarkupDrivenComponents(true);

        compMgr.buildItsNatComponents(doc.getDocumentElement());

        new TestHTMLInputText(itsNatDoc);
        new TestHTMLTextArea(itsNatDoc);
        new TestHTMLSelectMultiple(itsNatDoc,request);
        new TestHTMLSelectComboBox(itsNatDoc,request);
        new TestHTMLInputCheckBox(itsNatDoc);
        new TestHTMLInputRadio(itsNatDoc);
    }

    public void handleEvent(Event evt)
    {

    }
}
