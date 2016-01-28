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
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatEventDOMStateless;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import test.droid.shared.TestDroidBase;

/**
 *
 * @author jmarranz
 */
public class TestDroidStatelessCoreEventDocument extends TestDroidBase implements Serializable,EventListener
{
    /**
     * Creates a new instance of TestCoreLoadListener
     */
    public TestDroidStatelessCoreEventDocument(ItsNatDocument itsNatDoc,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        super(itsNatDoc);
        
        if (!itsNatDoc.isCreatedByStatelessEvent())
            throw new RuntimeException("Only to test stateless, must be loaded by a stateless event");
        
        itsNatDoc.getDocument();
        
        itsNatDoc.addEventListener(this);
    }

    @Override
    public void handleEvent(Event evt)
    {
  
        ItsNatEventDOMStateless itsNatEvt = (ItsNatEventDOMStateless)evt;
        
        ItsNatDocument itsNatDoc = itsNatEvt.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element elemParent = doc.getElementById("testElemId");
        ScriptUtil scriptGen = itsNatDoc.getScriptUtil();
        String elemParentRef = scriptGen.getNodeReference(elemParent);
        ClientDocument clientDoc = itsNatEvt.getClientDocument();
        clientDoc.addCodeToSend("View view = (View)" + elemParentRef + "; ");        
        clientDoc.addCodeToSend("view.removeAllViews();");         
        
        Element logElem = doc.getElementById("testElem_text_Id");
        logToTextView(logElem, "Removed current children before insertion");    
                
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();  
        ItsNatDocFragmentTemplate docFragTemplate = servlet.getItsNatDocFragmentTemplate("test_droid_core_fragment");  

        DocumentFragment docFrag = docFragTemplate.loadDocumentFragment(itsNatDoc);  
  
        elemParent.appendChild(docFrag); // docFrag is empty now  
        
        // Cambiamos el texto del TextView del fragment que está diseñado para otro test, así ejercitamos la capacidad de modificar DOM sin problema en el doc auxiliar
        // y trasladarse los cambios al ppal sin problemas 
        Element fragmentRoot = doc.getElementById("fragmentTestId");
        fragmentRoot.removeAttribute("onclick");         
        Element textView = ItsNatTreeWalker.getFirstChildElement(fragmentRoot);
        textView.setAttribute("android:text","Some Text");
        
        Element testElemContainer = doc.getElementById("testElemContainerId");        
        testElemContainer.setAttribute("android:visibility","visible");
        
        logToTextView(logElem, "\nOK"); 
    }    
}
