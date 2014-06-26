/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class TestDroidDocument implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element elem;


    public TestDroidDocument(ItsNatDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;

        Document doc = itsNatDoc.getDocument();
        Element textView2 = doc.getElementById("textViewTest2");
        String bgTextViewTest2 = textView2.getAttributeNS("http://schemas.android.com/apk/res/android", "background");
        textView2.setAttributeNS("http://schemas.android.com/apk/res/android", "background", "#ffdddd");
        textView2.removeAttributeNS("http://schemas.android.com/apk/res/android", "background");        
        textView2.setAttributeNS("http://schemas.android.com/apk/res/android", "background", bgTextViewTest2);       
        textView2.setAttribute("id", "BAD ID");  
        textView2.removeAttribute("id");         
        textView2.setAttribute("id", "textViewTest2");
        //((EventTarget)elem).addEventListener("click", this,false);

        Element customTextView = doc.createElement("org.itsnat.itsnatdroidtest.CustomTextView");
        // El style debe definirse si se quiere ANTES de insertar
        customTextView.setAttribute("style", "@style/test");
        customTextView.setAttributeNS("http://schemas.android.com/apk/res/android", "text", "@string/hello_world3");    // Definimos otro antes de insertar para probar
        Element buttonTest = doc.getElementById("buttonTest");
        
        doc.getDocumentElement().insertBefore(customTextView, buttonTest);
        
        customTextView.setAttributeNS("http://schemas.android.com/apk/res/android", "id", "@id/textViewTest3");      
        customTextView.setAttributeNS("http://schemas.android.com/apk/res/android", "layout_width", "match_parent");        
        customTextView.setAttributeNS("http://schemas.android.com/apk/res/android", "layout_height", "wrap_content");        
        customTextView.setAttributeNS("http://schemas.android.com/apk/res/android", "background", "#ffdddd");        

        Element frameLayoutView = doc.createElement("FrameLayout");  
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "layout_width", "match_parent");        
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "layout_height", "wrap_content");         
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "background", "#ddffdd");         
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "paddingLeft", "10dp");        
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "paddingBottom", "10dp");        
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "paddingTop", "10dp");          
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "paddingRight", "10dp");   
        
        Element frameLayoutView2 = doc.createElement("FrameLayout");  
        frameLayoutView2.setAttributeNS("http://schemas.android.com/apk/res/android", "layout_width", "match_parent");        
        frameLayoutView2.setAttributeNS("http://schemas.android.com/apk/res/android", "layout_height", "wrap_content");         
        frameLayoutView2.setAttributeNS("http://schemas.android.com/apk/res/android", "background", "#ddddff");        

        Element textView4 = doc.createElement("TextView");        
        textView4.setAttributeNS("http://schemas.android.com/apk/res/android", "android:text", "Inside FrameLayout");        
        frameLayoutView2.appendChild(textView4);         
        
        frameLayoutView.appendChild(frameLayoutView2);        
        
        doc.getDocumentElement().insertBefore(frameLayoutView, buttonTest);        

        doc.getDocumentElement().insertBefore(doc.createTextNode("IGNORE TEXT NODE"), buttonTest);        
        doc.getDocumentElement().insertBefore(doc.createComment("IGNORE COMMENT"), buttonTest);       
    }

    public void handleEvent(Event evt)
    {
        Document doc = itsNatDoc.getDocument();
        Element infoElem = doc.createElement("div");
        infoElem.appendChild(doc.createTextNode("clicked"));
        //logElem.appendChild(infoElem);
    }
}
