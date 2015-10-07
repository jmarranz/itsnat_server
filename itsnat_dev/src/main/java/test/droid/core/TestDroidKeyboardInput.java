/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.event.droid.DroidEvent;
import org.itsnat.core.event.droid.DroidTextChangeEvent;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.droid.shared.TestDroidBase;

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
        
        itsNatDoc.addEventListener((EventTarget)editElement, "change", this, false);         
        
        this.processElement = getDocument().getElementById("testKeyboardInputProcessId");
        ((EventTarget)processElement).addEventListener("click", this, false);        
        
        
        this.outElem = getDocument().getElementById("testKeyboardInputLogId");        
    }
    
    @Override
    public void handleEvent(Event evt)
    {     
        EventTarget currTarget = evt.getCurrentTarget();
        DroidEvent evt2 = (DroidEvent)evt;        
        
        ClientDocument clientDoc = evt2.getClientDocument();   
        // Usamos ClientDocument.getScriptUtil() y addCodeToSend en vez de ItsNatDocument para que en control remoto no se dupliquen las operaciones (se vuelve loco el ejemplo)
        
        logToTextView(outElem,"Event " + evt2.getType() + " ");
        
        if (currTarget == editElement)
        {
            String type = evt2.getType();
            if (type.equals("focus"))
            {
                // Hacemos el botón focusable para que al pulsarse le quite el foco al EditText y se ejecute el evento blur
                
                String processRef = clientDoc.getScriptUtil().getNodeReference(processElement);
                clientDoc.addCodeToSend("var elem = " + processRef + ";");            
                clientDoc.addCodeToSend("elem.setFocusable(true);");        
                clientDoc.addCodeToSend("elem.setFocusableInTouchMode(true);");                
                
                
                // No hace falta hacer elem.setFocusable(false) para conseguir quitar el azul feo cuando el botón processElement tiene el foco, pues
                // Android tiende a que una vez que un control ha tomado el foco ha de existir un control con el foco y siempre podemos cambiarlo al EditText
            }
            else if (type.equals("blur"))
            {
                String text = (String)evt2.getExtraParam("getText()");
                logToTextView(outElem,"\nTEXT: " + text + "\n");
                               
                String editRef = clientDoc.getScriptUtil().getNodeReference(editElement);                
                clientDoc.addCodeToSend("var elem = " + editRef + ";");                
                clientDoc.addCodeToSend("elem.setText(\"\");");
                clientDoc.addCodeToSend("elem.requestFocus(); ");  // Esto es para que el botón processElement no se quede con el foco con un azul muy feo                 
            }
            else if (type.equals("change"))
            {
                DroidTextChangeEvent evt3 = (DroidTextChangeEvent)evt; 
                logToTextView(outElem,"\nchange: " + evt3.getNewText() + "\n");               
             }            
        }
        else if (currTarget == processElement)
        {        
            String processRef = clientDoc.getScriptUtil().getNodeReference(processElement);
            clientDoc.addCodeToSend("var elem = " + processRef + ";");            
            clientDoc.addCodeToSend("elem.requestFocus(); ");  // Para provocar el blur en el EditText si tuviera el foco                  
        }
    }
    
}
