/*
 * TestCoreLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.droid.stateless.core;

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatEventDOMStateless;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class TestDroidStatelessCoreEventDocument implements Serializable,EventListener
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of TestCoreLoadListener
     */
    public TestDroidStatelessCoreEventDocument(ItsNatDocument itsNatDoc,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = itsNatDoc;
        
        if (!itsNatDoc.isCreatedByStatelessEvent())
            throw new RuntimeException("Only to test stateless, must be loaded by a stateless event");
        
        itsNatDoc.addEventListener(this);
    }

    public void handleEvent(Event evt)
    {

        
        ItsNatEventDOMStateless itsNatEvt = (ItsNatEventDOMStateless)evt;
        
        ItsNatDocument itsNatDoc = itsNatEvt.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element elemParent = doc.getElementById("testElemId");
        ScriptUtil scriptGen = itsNatDoc.getScriptUtil();
        String elemParentRef = scriptGen.getNodeReference(elemParent);
        ClientDocument clientDoc = itsNatEvt.getClientDocument();
        clientDoc.addCodeToSend("View view = (View)" + elemParentRef + "; alert(view);");        
        clientDoc.addCodeToSend("view.removeAllViews();");         
        clientDoc.addCodeToSend("alert(\"Removed current children\");");        
                
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();  
        ItsNatDocFragmentTemplate docFragTemplate = servlet.getItsNatDocFragmentTemplate("test_droid_core_fragment");  

        DocumentFragment docFrag = docFragTemplate.loadDocumentFragment(itsNatDoc);  
  
        elemParent.appendChild(docFrag); // docFrag is empty now  
        
        
        clientDoc.addCodeToSend("alert(\"OK\");");
    }    
}
