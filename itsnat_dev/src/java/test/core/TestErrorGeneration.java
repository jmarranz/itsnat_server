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
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestErrorGeneration implements EventListener,Serializable
{
    /** Creates a new instance of TestClassAttribute */
    public TestErrorGeneration(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element link = itsNatDoc.getDocument().getElementById("errorTestId");
        ((EventTarget)link).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        throw new RuntimeException("TEST EXCEPTION THROWN");
    }
}
