/*
 * TestDroidStatelessCoreInitialDocument.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.droid.stateless.core;

import java.io.Serializable;
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
public class TestDroidStatelessCoreInitialDocument implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element testEventCustomElem;
    
    /**
     * Creates a new instance of TestDroidStatelessCoreInitialDocument
     */
    public TestDroidStatelessCoreInitialDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        Document doc = itsNatDoc.getDocument();
        this.testEventCustomElem = doc.getElementById("testEventCustomId");
        ((EventTarget)testEventCustomElem).addEventListener("click", this, false);

    }

    public void handleEvent(Event evt)
    {
        EventTarget target = evt.getCurrentTarget();
        if (target == testEventCustomElem)
        {
            itsNatDoc.addCodeToSend("itsNatDoc.alert(\"BIEN\");");
        }

    }


}
