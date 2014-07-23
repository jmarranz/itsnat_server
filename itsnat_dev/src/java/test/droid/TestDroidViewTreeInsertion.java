/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid;

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
public class TestDroidViewTreeInsertion extends TestDroidBase implements EventListener
{
   
    public TestDroidViewTreeInsertion(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testViewTreeInsertionId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
    }
    
    public void handleEvent(Event evt)
    {     
        Document doc = getDocument();
        Element testLauncherHidden = doc.getElementById("testViewTreeInsertionHiddenId");  
        
        Element frameLayoutView = doc.createElement("FrameLayout");  
        frameLayoutView.setAttributeNS(ANDROID_NS, "layout_width", "match_parent");        
        frameLayoutView.setAttributeNS(ANDROID_NS, "layout_height", "wrap_content");         
        frameLayoutView.setAttributeNS(ANDROID_NS, "background", "#ddffdd");         
        frameLayoutView.setAttributeNS(ANDROID_NS, "paddingLeft", "10dp");        
        frameLayoutView.setAttributeNS(ANDROID_NS, "paddingBottom", "10dp");        
        frameLayoutView.setAttributeNS(ANDROID_NS, "paddingTop", "10dp");          
        frameLayoutView.setAttributeNS(ANDROID_NS, "paddingRight", "10dp");   
        
        Element frameLayoutViewInner = doc.createElement("FrameLayout");  
        frameLayoutViewInner.setAttributeNS(ANDROID_NS, "layout_width", "match_parent");        
        frameLayoutViewInner.setAttributeNS(ANDROID_NS, "layout_height", "wrap_content");         
        frameLayoutViewInner.setAttributeNS(ANDROID_NS, "background", "#ddddff");        

        Element textViewInner = doc.createElement("TextView");        
        textViewInner.setAttributeNS(ANDROID_NS, "text", "OK if two anidated rects (green/purple)");        
        frameLayoutViewInner.appendChild(textViewInner);         
        
        frameLayoutView.appendChild(frameLayoutViewInner);        
       
        
        // Test ignorar nodos de texto: este test lo ponemos aquí por ponerlo en algún sitio
        testLauncherHidden.getParentNode().insertBefore(doc.createTextNode("IGNORE TEXT NODE"), testLauncherHidden);     // Aunque lo insertemos, en el cálculo de paths etc se ignorará          
        
        
        testLauncherHidden.getParentNode().insertBefore(frameLayoutView, testLauncherHidden);   
    }
    
}
