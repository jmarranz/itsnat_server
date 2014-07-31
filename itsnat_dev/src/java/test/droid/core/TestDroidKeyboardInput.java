/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.droid.DroidEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidKeyboardInput extends TestDroidBase implements EventListener
{
    protected Element editElement;
    protected Element processElement;
    protected Element outElem;
    
    public TestDroidKeyboardInput(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        this.editElement = getDocument().getElementById("testKeyboardInputEditId");        
        //((EventTarget)editElement).addEventListener("keydown", this, false);
        //((EventTarget)editElement).addEventListener("keyup", this, false);       
        
        this.processElement = getDocument().getElementById("testKeyboardInputProcessId");
        ((EventTarget)processElement).addEventListener("click", this, false);        
        
        this.outElem = getDocument().getElementById("testKeyboardInput_text_Id");        
    }
    
    public void handleEvent(Event evt)
    {     
        DroidEvent evt2 = (DroidEvent)evt;        
        
        Document doc = getDocument();
 
        logToTextView(outElem,"Event " + evt2.getType());

  
    }
    
}
