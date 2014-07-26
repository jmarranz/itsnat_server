/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidViewTreeRemoving extends TestDroidBase implements EventListener
{
   
    public TestDroidViewTreeRemoving(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testViewTreeRemovingId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
    }
    
    public void handleEvent(Event evt)
    {     
        Document doc = getDocument();
        Element testLauncherHidden = doc.getElementById("testViewTreeRemovingHiddenId");  
        
        final Element frameLayoutViewToRemove = doc.createElement("FrameLayout");        
        frameLayoutViewToRemove.setAttributeNS(ANDROID_NS, "layout_width", "match_parent");        
        frameLayoutViewToRemove.setAttributeNS(ANDROID_NS, "layout_height", "wrap_content");         
        frameLayoutViewToRemove.setAttributeNS(ANDROID_NS, "background", "#ddddff");        

        Element textViewToRemove = doc.createElement("TextView"); 
        textViewToRemove.setAttributeNS(ANDROID_NS, "layout_width", "match_parent");        
        textViewToRemove.setAttributeNS(ANDROID_NS, "layout_height", "50dp");         
        textViewToRemove.setAttributeNS(ANDROID_NS, "text", "CLICK HERE TO REMOVE");        
        frameLayoutViewToRemove.appendChild(textViewToRemove);

        testLauncherHidden.getParentNode().insertBefore(frameLayoutViewToRemove, testLauncherHidden);        
        
        ((EventTarget)frameLayoutViewToRemove).addEventListener("click",new EventListener(){
            public void handleEvent(Event evt)
            {
                frameLayoutViewToRemove.getParentNode().removeChild(frameLayoutViewToRemove);
            }            
        },false);   
  
    }
    
}
