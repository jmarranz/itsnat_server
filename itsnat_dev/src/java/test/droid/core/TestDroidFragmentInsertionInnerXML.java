/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

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

/**
 *
 * @author jmarranz
 */
public class TestDroidFragmentInsertionInnerXML extends TestDroidBase implements EventListener
{
   
    public TestDroidFragmentInsertionInnerXML(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testFragmentInsertionInnerXMLId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
    }
    
    public void handleEvent(Event evt)
    {     
        Document doc = getDocument();
        Element testLauncherHidden = doc.getElementById("testFragmentInsertionInnerXMLHiddenId");  
        
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        DocumentFragment docFrag = servlet.getItsNatDocFragmentTemplate("test_droid_core_fragment").loadDocumentFragment(itsNatDoc); 
        
        
        final Element frameLayoutViewToRemove = ItsNatTreeWalker.getFirstChildElement(docFrag);

        // Sabemos con seguridad que el fragment se insertará (parcialmente) via markup, nos aseguramos de todas formas que está activado
        if (!BSRenderElementImpl.SUPPORT_INSERTION_AS_MARKUP) throw new RuntimeException("CANNOT TEST");
        
        testLauncherHidden.getParentNode().insertBefore(frameLayoutViewToRemove, testLauncherHidden);        
        
        ((EventTarget)frameLayoutViewToRemove).addEventListener("click",new EventListener(){
            public void handleEvent(Event evt)
            {
                frameLayoutViewToRemove.getParentNode().removeChild(frameLayoutViewToRemove);
            }            
        },false);   
  
        // En el template fragment hay un <script> que DEBE desaparecer
        NodeList scripts = doc.getElementsByTagName("script");
        if (scripts.getLength() > 0) throw new RuntimeException("Unexpected <string> element");         
    }
    
}
