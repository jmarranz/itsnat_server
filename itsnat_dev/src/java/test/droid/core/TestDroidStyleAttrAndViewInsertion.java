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
public class TestDroidStyleAttrAndViewInsertion extends TestDroidBase implements EventListener
{
   
    public TestDroidStyleAttrAndViewInsertion(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);
        Document doc = itsNatDoc.getDocument();
        Element testStyleAttr = doc.getElementById("testStyleAttrId");
        ((EventTarget)testStyleAttr).addEventListener("click", this, false);
    }
    
    public void handleEvent(Event evt)
    {
        Document doc = itsNatDoc.getDocument();        
        Element testStyleAttrHidden = doc.getElementById("testStyleAttrHiddenId");  
        
        Element textView = doc.createElement("TextView");        
        // Test definir atributos antes de insertar
        textView.setAttribute("android:text", "OK if text has left/right padding, background pink and width is match_parent");         
        textView.setAttribute("style","@style/test");
        
        testStyleAttrHidden.getParentNode().insertBefore(textView, testStyleAttrHidden);
        
        // Test definir atributos después de insertar            
        textView.setAttribute("android:layout_width", "match_parent");        
        textView.setAttribute("android:layout_height", "wrap_content");        
        textView.setAttribute("android:background", "#ffdddd");         
        textView.removeAttribute("android:background");   
        String nodeRef = itsNatDoc.getScriptUtil().getNodeReference(textView);
        itsNatDoc.addCodeToSend("if (" + nodeRef + ".getBackground() != null) itsNatDoc.alert(\"FAIL removeAttribute\");");
        textView.setAttribute("android:background", "#ffdddd");  // Rosa   
        
        // Test uso del atributo DOM id
        textView.setAttribute("id", "BAD_ID");  
        textView.removeAttribute("id");         
        itsNatDoc.addCodeToSend("if (itsNatDoc.findViewByXMLId(\"BAD_ID\") != null) itsNatDoc.alert(\"FAIL removeAttribute XML Id\");");        
        
        textView.setAttribute("id", "testStyleAttrTextId");
        itsNatDoc.addCodeToSend("if (" + nodeRef + "!= itsNatDoc.findViewByXMLId(\"testStyleAttrTextId\")) itsNatDoc.alert(\"FAIL setAttribute XML Id\");");        
        
    }
    
}
