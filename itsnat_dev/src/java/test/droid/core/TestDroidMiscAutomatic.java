/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidMiscAutomatic extends TestDroidBase implements EventListener
{
   
    public TestDroidMiscAutomatic(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Document doc = getDocument();
        Element elem = getDocument().getDocumentElement();

        ((EventTarget)elem).addEventListener("click", this,false);
        ((EventTarget)elem).addEventListener("touchstart", this,false);        
        ((EventTarget)elem).addEventListener("touchend", this,false);        
        
        
        ((EventTarget)elem).removeEventListener("click", this,false);            
        ((EventTarget)elem).removeEventListener("touchstart", this,false);        
        ((EventTarget)elem).removeEventListener("touchend", this,false);        
               

        // Hay un <script> en el template inicial que DEBE desaparecer
        NodeList scripts = doc.getElementsByTagName("script");
        if (scripts.getLength() > 0) throw new RuntimeException("Unexpected <string> element");        
    }
    
    public void handleEvent(Event evt)
    {     
        itsNatDoc.addCodeToSend("itsNatDoc.alert(\"Unexpected event\");");
  
    }
    
}
