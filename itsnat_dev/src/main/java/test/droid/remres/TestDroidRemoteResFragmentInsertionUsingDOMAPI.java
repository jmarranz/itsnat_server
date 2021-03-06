/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.remres;

import test.droid.shared.TestDroidBase;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.scriptren.bsren.node.BSRenderElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestDroidRemoteResFragmentInsertionUsingDOMAPI extends TestDroidBase implements EventListener
{
   
    public TestDroidRemoteResFragmentInsertionUsingDOMAPI(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testFragmentInsertionUsingDOMAPIId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
    }
    
    @Override
    public void handleEvent(Event evt)
    {     
        Document doc = getDocument();
        Element testLauncherHidden = doc.getElementById("testFragmentInsertionUsingDOMAPIHiddenId");  
        
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        DocumentFragment docFrag = servlet.getItsNatDocFragmentTemplate("test_droid_remote_resources_fragment").loadDocumentFragment(itsNatDoc); 
        
        
        final Element elemToRemove = ItsNatTreeWalker.getLastChildElement(docFrag);
           
        boolean old = BSRenderElementImpl.SUPPORT_INSERTION_AS_MARKUP;
        BSRenderElementImpl.SUPPORT_INSERTION_AS_MARKUP = false;
        
        try
        {
            testLauncherHidden.getParentNode().insertBefore(docFrag, testLauncherHidden);        
        }
        finally
        {
            BSRenderElementImpl.SUPPORT_INSERTION_AS_MARKUP = old;
        }
        
        ((EventTarget)elemToRemove).addEventListener("click",new EventListenerSerial(){
            @Override
            public void handleEvent(Event evt)
            {
                Element firstElem = ItsNatTreeWalker.getPreviousSiblingElement(elemToRemove);
                elemToRemove.getParentNode().removeChild(firstElem);
                elemToRemove.getParentNode().removeChild(elemToRemove);
            }            
        },false);   
       
    }
    
}
