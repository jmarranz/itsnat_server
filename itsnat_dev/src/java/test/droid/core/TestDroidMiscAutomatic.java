/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ClientDocument;
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
        Element rootElem = doc.getDocumentElement();

        ((EventTarget)rootElem).addEventListener("click", this,false);
        ((EventTarget)rootElem).addEventListener("touchstart", this,false);        
        ((EventTarget)rootElem).addEventListener("touchend", this,false);        
        
        
        ((EventTarget)rootElem).removeEventListener("click", this,false);            
        ((EventTarget)rootElem).removeEventListener("touchstart", this,false);        
        ((EventTarget)rootElem).removeEventListener("touchend", this,false);        
          
        itsNatDoc.addEventListener((EventTarget)rootElem, "load", this, false);
        itsNatDoc.addEventListener((EventTarget)rootElem, "unload", this, false);       
        
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
            
            Element rootElem = getDocument().getDocumentElement();            
            itsNatDoc.removeEventListener((EventTarget)rootElem, "load", this, false);  // No sirve para nada pero por si fallara
        }
        else if (type.equals("unload"))
        {
            ClientDocument clientDoc = itsNatDoc.getClientDocumentOwner(); // En el caso de control remoto NO interesa enviar este código
            clientDoc.addCodeToSend("var view = itsNatDoc.findViewByXMLId(\"testUnloadListenerId\"); view.setText(view.getText() + \"OK\");");            
        }        
        else
        {
            itsNatDoc.addCodeToSend("alert(\"Unexpected event\");");
        }
  
    }
    
}
