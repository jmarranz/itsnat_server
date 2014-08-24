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
        Element elem = doc.getDocumentElement();

        ((EventTarget)elem).addEventListener("click", this,false);
        ((EventTarget)elem).addEventListener("touchstart", this,false);        
        ((EventTarget)elem).addEventListener("touchend", this,false);        
        
        
        ((EventTarget)elem).removeEventListener("click", this,false);            
        ((EventTarget)elem).removeEventListener("touchstart", this,false);        
        ((EventTarget)elem).removeEventListener("touchend", this,false);        
          
        itsNatDoc.addEventListener(null, "load", this, false);
        itsNatDoc.addEventListener(null, "unload", this, false);       
        
        // Hay un <script> en el template inicial que DEBE desaparecer en tiempo de carga antes de poder acceder al Document
        NodeList scripts = doc.getElementsByTagName("script");
        if (scripts.getLength() > 0) throw new RuntimeException("Unexpected <string> element");        
    }
    
    public void handleEvent(Event evt)
    {     
        String type = evt.getType();
        if (type.equals("load"))
        {
            itsNatDoc.addCodeToSend("var view = itsNatDoc.findViewByXMLId(\"testLoadListenerId\"); view.setText(view.getText() + \"OK\");");            
        }
        else if (type.equals("unload"))
        {
            itsNatDoc.addCodeToSend("var view = itsNatDoc.findViewByXMLId(\"testUnloadListenerId\"); view.setText(view.getText() + \"OK\");");            
        }        
        else
        {
            itsNatDoc.addCodeToSend("alert(\"Unexpected event\");");
        }
  
    }
    
}
