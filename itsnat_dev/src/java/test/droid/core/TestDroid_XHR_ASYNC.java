/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.itsnat.core.CommMode;
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
public class TestDroid_XHR_ASYNC extends TestDroidBase implements EventListener
{
   
    public TestDroid_XHR_ASYNC(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("test_XHR_ASYNC_Id");        
        itsNatDoc.addEventListener((EventTarget)testLauncher,"click",this,false,CommMode.XHR_ASYNC);         
    }
    
    public void handleEvent(Event evt)
    {     
        // Los eventos se verán secuenciales de todas formas porque ItsNat sincroniza el ItsNatDocument 
        try { Thread.sleep(2000); }
        catch (InterruptedException ex) { ex.printStackTrace(); }
        
        Document doc = getDocument();
        Element outElem = doc.getElementById("test_XHR_ASYNC_text_Id");  
        String text = outElem.getAttributeNS(ANDROID_NS, "text");
        text += "OK XHR_ASYNC ";
        outElem.setAttributeNS(ANDROID_NS, "text",text);
        
    }
    
}
