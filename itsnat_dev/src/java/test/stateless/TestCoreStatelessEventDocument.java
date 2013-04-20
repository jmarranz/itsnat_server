/*
 * TestCoreLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.stateless;

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatEventDOMStateless;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.core.tmpl.ItsNatHTMLDocFragmentTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class TestCoreStatelessEventDocument implements Serializable,EventListener
{
    protected ItsNatHTMLDocument itsNatDoc;

    /**
     * Creates a new instance of TestCoreLoadListener
     */
    public TestCoreStatelessEventDocument(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = itsNatDoc;
        
        itsNatDoc.addEventListener(this);
    }

    public void handleEvent(Event evt)
    {
        ItsNatEventDOMStateless itsNatEvt = (ItsNatEventDOMStateless)evt;
        
        ItsNatDocument itsNatDoc = itsNatEvt.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element elemParent = doc.getElementById("testElemId");
        ScriptUtil scriptGen = itsNatDoc.getScriptUtil();
        String elemRef = scriptGen.getNodeReference(elemParent);
        ClientDocument clientDoc = itsNatEvt.getClientDocument();
        clientDoc.addCodeToSend(elemRef + ".innerHTML = '';");        
        clientDoc.addCodeToSend("alert('Removed current children');");        
                
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();  
        ItsNatHTMLDocFragmentTemplate docFragTemplate = (ItsNatHTMLDocFragmentTemplate)servlet.getItsNatDocFragmentTemplate("test_html_fragment_fragment");  

        DocumentFragment docFrag = docFragTemplate.loadDocumentFragmentBody(itsNatDoc);  
  
        elemParent.appendChild(docFrag); // docFrag is empty now  
        
        
        clientDoc.addCodeToSend("alert('OK');");
    }    
}
