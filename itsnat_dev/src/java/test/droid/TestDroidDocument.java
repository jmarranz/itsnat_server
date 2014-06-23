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
        Element textView = doc.getElementById("textViewTest2");
        textView.setAttributeNS("http://schemas.android.com/apk/res/android", "background", "#ffdddd");
        textView.removeAttributeNS("http://schemas.android.com/apk/res/android", "background");        
        textView.setAttribute("id", "BAD ID");  
        textView.removeAttribute("id");         
        textView.setAttribute("id", "textViewTest2");
        //((EventTarget)elem).addEventListener("click", this,false);

        Element customTextView = doc.createElement("org.itsnat.itsnatdroidtest.CustomTextView");
        customTextView.setAttributeNS("http://schemas.android.com/apk/res/android", "id", "@id/textViewTest3");
        customTextView.setAttributeNS("http://schemas.android.com/apk/res/android", "text", "@string/hello_world3");
        
        Element buttonTest = doc.getElementById("buttonTest");
        doc.getDocumentElement().insertBefore(customTextView, buttonTest);
        
        
/*        
    <org.itsnat.itsnatdroidtest.CustomTextView
              android:id="@id/textViewTest3"
              android:text="@string/hello_world3"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:background="#ffdddd"
              android:layout_below="@id/textViewTest2"/>        
*/        
    }

    public void handleEvent(Event evt)
    {
        Document doc = itsNatDoc.getDocument();
        Element infoElem = doc.createElement("div");
        infoElem.appendChild(doc.createTextNode("clicked"));
        //logElem.appendChild(infoElem);
    }
}
