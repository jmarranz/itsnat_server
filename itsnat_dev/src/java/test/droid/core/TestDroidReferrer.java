/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.ItsNatEvent;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidReferrer extends TestDroidBase implements EventListener
{
    protected Element outElem;
    protected String referrerId;
    
    public TestDroidReferrer(ItsNatDocument itsNatDoc,ItsNatServletRequest request)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testReferrerId");    
        ((EventTarget)testLauncher).addEventListener("click", this, false);        
        
        this.outElem = getDocument().getElementById("testReferrer_text_Id");         
        
        ItsNatDocument itsNatDocReferrer = request.getItsNatDocumentReferrer();        
        this.referrerId = (itsNatDocReferrer != null ? itsNatDocReferrer.getId() : "(no ref)");
    }
    
    public void handleEvent(Event evt)
    {            
        logToTextView(outElem,"Referrer Doc Id: " + referrerId);         
        logToTextView(outElem,"\nCurrent Doc Id: " + itsNatDoc.getId());          
        logToTextView(outElem,"\nNote: id numbers are not strictly consecutives");  // El generador de ids de los documentos es a nivel de sesión y se usa para otros tipos de objetos también 
        
        // itsNatDoc.addCodeToSend("alert(\"Does nothing\");");
    }
    
}
