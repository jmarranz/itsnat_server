/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.scriptren.bsren.node.BSRenderElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestDroidFragmentInsertionUsingDOMAPI extends TestDroidBase implements EventListener
{
   
    public TestDroidFragmentInsertionUsingDOMAPI(ItsNatDocument itsNatDoc)
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
        DocumentFragment docFrag = servlet.getItsNatDocFragmentTemplate("test_droid_core_fragment").loadDocumentFragment(itsNatDoc); 
        
        
        final Element elementViewToRemove = ItsNatTreeWalker.getFirstChildElement(docFrag);

        // En el template fragment hay un <script> que DEBE desaparecer
        NodeList scripts = elementViewToRemove.getElementsByTagName("script");
        if (scripts.getLength() != 2) throw new RuntimeException("Expected 2 <string> elements");         
        
        boolean old = BSRenderElementImpl.SUPPORT_INSERTION_AS_MARKUP;
        BSRenderElementImpl.SUPPORT_INSERTION_AS_MARKUP = false;
        
        try
        {
            testLauncherHidden.getParentNode().insertBefore(elementViewToRemove, testLauncherHidden);        
        }
        finally
        {
            BSRenderElementImpl.SUPPORT_INSERTION_AS_MARKUP = old;
        }
        
        Element frameLayoutViewToRemove2 = (Element)testLauncherHidden.getPreviousSibling();
        if (elementViewToRemove != frameLayoutViewToRemove2) throw new RuntimeException("TEST ERROR");

        String layout_width = elementViewToRemove.getAttributeNS(ANDROID_NS,"layout_width");
        if (!"250dp".equals(layout_width))
            throw new RuntimeException("TEST FAIL");            
        
        ((EventTarget)elementViewToRemove).addEventListener("click",new EventListenerSerial(){
            @Override
            public void handleEvent(Event evt)
            {
                elementViewToRemove.getParentNode().removeChild(elementViewToRemove);
            }            
        },false);   
  
        // En el template fragment hay un <script> que DEBE desaparecer
        scripts = doc.getElementsByTagName("script");
        if (scripts.getLength() > 0) throw new RuntimeException("Unexpected <string> element");        
    }
    
}
