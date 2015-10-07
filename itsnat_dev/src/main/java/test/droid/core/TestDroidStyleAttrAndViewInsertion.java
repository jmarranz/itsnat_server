/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
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
    protected Element testStyleAttr;
    
    public TestDroidStyleAttrAndViewInsertion(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);
        Document doc = itsNatDoc.getDocument();
        this.testStyleAttr = doc.getElementById("testStyleAttrId");
        ((EventTarget)testStyleAttr).addEventListener("click", this, false);
    }
    
    @Override
    public void handleEvent(Event evt)
    {
        ((EventTarget)testStyleAttr).removeEventListener("click", this, false); // Evitamos ejecutar varias veces para evitar que falle textView.setAttributeNS(ANDROID_NS,"android:id","@+id/testStyleAttrTextId"); pues al reutilizarse el id varios elementos tendrán el mismo id (lo cual es correcto) y se devuelve el primero en el test y por tanto fallará
        
        
        Document doc = itsNatDoc.getDocument();        
        Element testStyleAttrHidden = doc.getElementById("testStyleAttrHiddenId");  
        
        Element textView = doc.createElement("TextView");        
        // Test definir atributos antes de insertar
        textView.setAttributeNS(ANDROID_NS,"android:text", "OK if text has left/right padding, background pink and width is match_parent");         
        textView.setAttribute("style","@style/test");
        
        testStyleAttrHidden.getParentNode().insertBefore(textView, testStyleAttrHidden);
        
        // Test definir atributos después de insertar            
        textView.setAttributeNS(ANDROID_NS,"android:layout_width", "match_parent");        
        textView.setAttributeNS(ANDROID_NS,"android:layout_height", "wrap_content");        
        textView.setAttributeNS(ANDROID_NS,"android:background", "#ffdddd");         
        textView.removeAttributeNS(ANDROID_NS,"background");   
        String nodeRef = itsNatDoc.getScriptUtil().getNodeReference(textView);
        itsNatDoc.addCodeToSend("var view = " + nodeRef + ";");        
        itsNatDoc.addCodeToSend("if (view.getBackground() != null) alert(\"FAIL removeAttribute\");");
        textView.setAttributeNS(ANDROID_NS,"android:background", "#ffdddd");  // Rosa   
        
        // Test uso del atributo DOM id
        textView.setAttribute("id", "BAD_ID");  
        textView.removeAttribute("id");         
        itsNatDoc.addCodeToSend("if (itsNatDoc.findViewByXMLId(\"BAD_ID\") != null) alert(\"FAIL removeAttribute XML Id\");");        
        
        textView.setAttribute("id", "testStyleAttrTextId");
        itsNatDoc.addCodeToSend("if (view != itsNatDoc.findViewByXMLId(\"testStyleAttrTextId\")) alert(\"FAIL setAttribute XML Id\");");        
        
        textView.setAttributeNS(ANDROID_NS,"android:id","@+id/testStyleAttrTextId");
        itsNatDoc.addCodeToSend("if (view != view.getParent().findViewById(view.getId())) alert(\"FAIL setAttribute XML Id native \");");         
    }
    
}
