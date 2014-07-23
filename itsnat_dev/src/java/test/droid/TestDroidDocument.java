/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class TestDroidDocument implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element elem;


    public TestDroidDocument(ItsNatDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;

        new TestDroidMiscAutomatic(itsNatDoc);
        new TestDroidEventTimeout(itsNatDoc);        
        new TestDroidViewInsertionAndSetAttributes(itsNatDoc);
        new TestDroidCustomViewInsertion(itsNatDoc);
        new TestDroidViewTreeInsertion(itsNatDoc);
        new TestDroidViewTreeRemoving(itsNatDoc);
        new TestDroidScriptUtil(itsNatDoc);
        new TestDroidTouchEvent(itsNatDoc);

    }

    public void handleEvent(Event evt)
    {
        if (false) throw new RuntimeException("Test Exception from Server");
        
        if (false) itsNatDoc.addCodeToSend("BAD_CODE();");        
    }
}
