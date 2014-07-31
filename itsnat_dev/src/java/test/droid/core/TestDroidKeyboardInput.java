/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.event.ParamTransport;
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
        ((EventTarget)editElement).addEventListener("focus", this, false);
        
        ParamTransport textParam = new NodePropertyTransport("getText()",false);
        itsNatDoc.addEventListener((EventTarget)editElement, "blur", this, false, new ParamTransport[]{textParam});      
        
        this.processElement = getDocument().getElementById("testKeyboardInputProcessId");
        ((EventTarget)processElement).addEventListener("click", this, false);        
        
        {
            String processRef = itsNatDoc.getScriptUtil().getNodeReference(processElement);
            itsNatDoc.addCodeToSend("var elem = " + processRef + ";");            
            itsNatDoc.addCodeToSend("elem.setFocusable(true);");        
            itsNatDoc.addCodeToSend("elem.setFocusableInTouchMode(true);");            
        }
        
        this.outElem = getDocument().getElementById("testKeyboardInput_text_Id");        
    }
    
    public void handleEvent(Event evt)
    {     
        EventTarget currTarget = evt.getCurrentTarget();
        DroidEvent evt2 = (DroidEvent)evt;        
        
        //Document doc = getDocument();
 
        logToTextView(outElem,"Event " + evt2.getType() + " ");
        
        if (currTarget == editElement && evt2.getType().equals("blur"))
        {
            
            if (evt2.getType().equals("focus"))
            {
                 
            }
            else if (evt2.getType().equals("blur"))
            {
                String text = (String)evt2.getExtraParam("getText()");
                logToTextView(outElem,"\nTEXT: " + text + "\n");
                
                String editRef = itsNatDoc.getScriptUtil().getNodeReference(editElement);                
                itsNatDoc.addCodeToSend(editRef + ".setText(\"\");");                
            }
        }
        else if (currTarget == processElement)
        {
            /*
            String editRef = itsNatDoc.getScriptUtil().getNodeReference(editElement);
            itsNatDoc.addCodeToSend("var elem = " + editRef + ";");            
            itsNatDoc.addCodeToSend("elem.clearFocus();"); // Si el EditText es el primer componente "focusable" volverá a tener el foco automáticamente           
            */   
            
            String processRef = itsNatDoc.getScriptUtil().getNodeReference(processElement);
            itsNatDoc.addCodeToSend("var elem = " + processRef + ";");            
            itsNatDoc.addCodeToSend("elem.requestFocus(); ");             
            
            String editRef = itsNatDoc.getScriptUtil().getNodeReference(editElement);                
            itsNatDoc.addCodeToSend("var elem = " + editRef + ";");            
            itsNatDoc.addCodeToSend("elem.requestFocus(); ");                 
            
            
            //itsNatDoc.addCodeToSend("elem.requestFocus(2); ");     // FOCUS_FORWARD == 2       
             
            //itsNatDoc.addCodeToSend("itsNatDoc.postDelayed(new Runnable(){ void run() {elem.clearFocus(); itsNatDoc.alert(true); }},5000);");            
        }
    }
    
}
