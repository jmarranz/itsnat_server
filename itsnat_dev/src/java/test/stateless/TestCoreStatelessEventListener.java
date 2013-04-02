/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.stateless;

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.event.ItsNatEventStateless;
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
public class TestCoreStatelessEventListener implements EventListener,Serializable
{
    protected Object parent;

    public TestCoreStatelessEventListener(Object parent)
    {
        this.parent = parent;
    }

    public void handleEvent(Event evt)
    {
        ItsNatEventStateless itsNatEvt = (ItsNatEventStateless)evt;
        
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
