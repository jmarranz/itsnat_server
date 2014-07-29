/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidToDOM extends TestDroidBase implements EventListener
{
   
    public TestDroidToDOM(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testToDOMId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
    }
    
    public void handleEvent(Event evt)
    {     
        Document doc = getDocument();
        Element testLauncherHidden = doc.getElementById("testToDOMHiddenId");  
        
        StringBuilder markup = new StringBuilder();
        
        // Sólo se admite "android:" como prefijo exactamente
        
        markup.append("<TextView ");
        markup.append("  android:layout_width=\"match_parent\" \n");
        markup.append("  android:layout_height=\"25dp\" \n");
        markup.append("  android:background=\"#ddddff\" \n");        
        markup.append("  style=\"@style/test\" \n");        
        markup.append("  android:text=\"OK if purple and left/right padding\"> \n");
        markup.append("</TextView>");         
        
        markup.append("<TextView ");
        markup.append("  android:layout_width=\"match_parent\" \n");
        markup.append("  android:layout_height=\"25dp\" \n");
        markup.append("  android:background=\"#ddffdd\" \n");
        markup.append("  style=\"@style/test\" \n");        
        markup.append("  android:text=\"OK if green and left/right padding\"> \n");
        markup.append("</TextView>");        
        
        DocumentFragment docFrag = itsNatDoc.toDOM(markup.toString());       

        testLauncherHidden.getParentNode().insertBefore(docFrag, testLauncherHidden);  
    }
    
}
