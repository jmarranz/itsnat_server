/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.include;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import test.web.shared.EventListenerSerial;

public class TestDroidIncludeSlowLoadModeLayoutDocument implements Serializable
{
    protected ItsNatDocument itsNatDoc;

    public TestDroidIncludeSlowLoadModeLayoutDocument(ItsNatDocument itsNatDoc)
    {      
        this.itsNatDoc = itsNatDoc;
       
        Document doc = itsNatDoc.getDocument();
        Element testLauncherHidden = doc.getElementById("testViewTreeRemovingHiddenId");

        // Al ser fastLoadMode = false estos View generan código BeanShell
        final Element frameLayoutViewToRemove = doc.createElement("FrameLayout");
        frameLayoutViewToRemove.setAttribute("android:layout_width", "match_parent");
        frameLayoutViewToRemove.setAttribute("android:layout_height", "wrap_content");
        frameLayoutViewToRemove.setAttribute("android:background", "#ddddff");

        Element textViewToRemove = doc.createElement("TextView");
        textViewToRemove.setAttribute("android:layout_width", "match_parent");
        textViewToRemove.setAttribute("android:layout_height", "50dp");
        textViewToRemove.setAttribute("android:textSize", "@remote:dimen/droid/res/values/test_values_remote.xml:test_dimen_textSize");         

        frameLayoutViewToRemove.appendChild(textViewToRemove);
        
        testLauncherHidden.getParentNode().insertBefore(frameLayoutViewToRemove, testLauncherHidden);

        textViewToRemove.setAttribute("android:textColor", "@remote:color/droid/res/values/test_values_remote.xml:test_color_textColor");   // Lo llamamos después para liar, nada más     
        textViewToRemove.setAttribute("android:text", "CLICK HERE TO REMOVE (text color is red, text size is small)"); // Lo llamamos después para liar, nada más         
        // En el código beanshell generado hay de todo, al menos un setInnerXML (via InnerMarkupCodeImpl) y un setAttribute*  con metadata al contener un atributo remoto 
        ((EventTarget)frameLayoutViewToRemove).addEventListener("click",new EventListenerSerial(){
            @Override
            public void handleEvent(Event evt)
            {
                frameLayoutViewToRemove.getParentNode().removeChild(frameLayoutViewToRemove);
            }
        },false);
        
    }

}
