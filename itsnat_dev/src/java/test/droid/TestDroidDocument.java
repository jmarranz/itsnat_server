/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid;

import java.io.Serializable;
import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.script.ScriptUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

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

        // Test inserciones
        Element frameLayoutView = doc.createElement("FrameLayout");  
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "layout_width", "match_parent");        
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "layout_height", "wrap_content");         
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "background", "#ddffdd");         
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "paddingLeft", "10dp");        
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "paddingBottom", "10dp");        
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "paddingTop", "10dp");          
        frameLayoutView.setAttributeNS("http://schemas.android.com/apk/res/android", "paddingRight", "10dp");   
        
        Element frameLayoutViewInner = doc.createElement("FrameLayout");  
        frameLayoutViewInner.setAttributeNS("http://schemas.android.com/apk/res/android", "layout_width", "match_parent");        
        frameLayoutViewInner.setAttributeNS("http://schemas.android.com/apk/res/android", "layout_height", "wrap_content");         
        frameLayoutViewInner.setAttributeNS("http://schemas.android.com/apk/res/android", "background", "#ddddff");        

        Element textViewInner = doc.createElement("TextView");        
        textViewInner.setAttributeNS("http://schemas.android.com/apk/res/android", "android:text", "Inside FrameLayout");        
        frameLayoutViewInner.appendChild(textViewInner);         
        
        frameLayoutView.appendChild(frameLayoutViewInner);        
        
        doc.getDocumentElement().insertBefore(frameLayoutView, buttonTest);        

        // Test ignorar nodos de texto
        doc.getDocumentElement().insertBefore(doc.createTextNode("IGNORE TEXT NODE"), buttonTest);     // Aunque lo insertemos, en el cálculo de paths etc se ignorará          
        
        // Test eliminación de elementos
        
        Element frameLayoutViewToRemove = doc.createElement("FrameLayout");        
        frameLayoutViewToRemove.setAttributeNS("http://schemas.android.com/apk/res/android", "layout_width", "match_parent");        
        frameLayoutViewToRemove.setAttributeNS("http://schemas.android.com/apk/res/android", "layout_height", "20dp");         
        frameLayoutViewToRemove.setAttributeNS("http://schemas.android.com/apk/res/android", "background", "#ddddff");        

        Element textViewToRemove = doc.createElement("TextView");        
        textViewToRemove.setAttributeNS("http://schemas.android.com/apk/res/android", "android:text", "MUST BE REMOVED");        
        frameLayoutViewToRemove.appendChild(textViewToRemove);         

        doc.getDocumentElement().insertBefore(frameLayoutViewToRemove, buttonTest);        
        doc.getDocumentElement().removeChild(frameLayoutViewToRemove);      
        
        //itsNatDoc.addCodeToSend("itsNatDoc.alert(\"hola\");");
        

        // Test ScriptUtil 
        String code;
        ScriptUtil codeGen = itsNatDoc.getScriptUtil();

        code = codeGen.getCallMethodCode(codeGen.createScriptExpr("itsNatDoc"),"toast",new Object[]{"Toast and getCallMethodCode OK"},true);
        itsNatDoc.addCodeToSend(code);
        
        code = "if (!" + codeGen.getNodeReference(customTextView) + ".getClass().getName().equals(\"org.itsnat.itsnatdroidtest.CustomTextView\")) itsNatDoc.alert(\"FAILED test getNodeReference:\");"; // .equals(\"org.itsnat.itsnatdroidtest.CustomTextView\")
        itsNatDoc.addCodeToSend(code);        

        // Test event listener
        frameLayoutViewInner.setAttribute("id", "frameLayoutViewInner"); 
        
        if (true)
        {
        ((EventTarget)frameLayoutView).addEventListener("click", this,false);
        ((EventTarget)frameLayoutView).addEventListener("touchstart", this,false);        
        ((EventTarget)frameLayoutView).addEventListener("touchend", this,false);        
        }
        
        // Test timeout
        if (false)
        {
        itsNatDoc.addEventListener(((EventTarget)frameLayoutView),"click", this, false,CommMode.XHR_ASYNC_HOLD,(ParamTransport[])null,(String)null,2000);  
        }
        
//addEventListener(org.w3c.dom.events.EventTarget target, java.lang.String type, org.w3c.dom.events.EventListener listener, boolean useCapture, int commMode, ParamTransport[] extraParams, java.lang.String preSendCode, long eventTimeout)        

        if (false)
        {
        ((EventTarget)frameLayoutView).removeEventListener("click", this,false);            
        ((EventTarget)frameLayoutView).removeEventListener("touchstart", this,false);        
        ((EventTarget)frameLayoutView).removeEventListener("touchend", this,false);        
        }
    }

    public void handleEvent(Event evt)
    {
        if (false) throw new RuntimeException("Test Exception from Server");
        
        if (false) itsNatDoc.addCodeToSend("BAD_CODE();");        
        
        // Test timeout
        if (false)
        {
            try { Thread.sleep(5000); }
            catch (InterruptedException ex) { throw new RuntimeException(ex);  }
        }
        
        
        itsNatDoc.addCodeToSend("itsNatDoc.alert(\"OK " + evt.getType() + "\");");
    }
}
