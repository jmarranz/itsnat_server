/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.xul;

import java.io.Serializable;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestXULIncludeFragment implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element includeFragElem;

    public TestXULIncludeFragment(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        Document doc = itsNatDoc.getDocument();
        this.includeFragElem = doc.getElementById("includeFragId");
        ((EventTarget)includeFragElem).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        ItsNatDocFragmentTemplate template = servlet.getItsNatDocFragmentTemplate("test_xul_fragment_2");
        DocumentFragment fragment = template.loadDocumentFragment(itsNatDoc);
        Element rootElem = itsNatDoc.getDocument().getDocumentElement();
        rootElem.appendChild(fragment);
    }
}
