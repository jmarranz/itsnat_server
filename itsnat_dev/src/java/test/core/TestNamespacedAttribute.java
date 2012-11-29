/*
 * TestClassAttribute.java
 *
 * Created on 9 de marzo de 2007, 13:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;


import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestNamespacedAttribute implements EventListener,Serializable
{
    /** Creates a new instance of TestClassAttribute */
    public TestNamespacedAttribute(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element elem = itsNatDoc.getDocument().getElementById("testNamespacedAttrId");
        ((EventTarget)elem).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        // Testeamos el método ItsNat JavaScript setTextNode
        Element elem = (Element)evt.getCurrentTarget();
        elem.setAttributeNS("http://prefix.org/prefix","prefix:myattr","OK");

        ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
        ItsNatDocument itsNatDoc = itsNatEvt.getItsNatDocument();
        StringBuffer code = new StringBuffer();
        code.append( "var elem = document.getElementById('testNamespacedAttrId');" );
        code.append( "if (elem.getAttributeNS) alert(elem.getAttributeNS('http://prefix.org/prefix','myattr'));" );
        code.append( "else alert('NOT SUPPORTED');");
        itsNatDoc.addCodeToSend(code.toString()); // Deberá mostrar un "OK"
    }
}
