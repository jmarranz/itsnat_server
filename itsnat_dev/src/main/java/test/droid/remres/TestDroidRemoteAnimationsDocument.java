/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.remres;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class TestDroidRemoteAnimationsDocument implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element rootElem;


    public TestDroidRemoteAnimationsDocument(ItsNatDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;

        Document doc = itsNatDoc.getDocument();
        this.rootElem = doc.getDocumentElement();
        ((EventTarget)rootElem).addEventListener("load", this, false);
    }

    @Override
    public void handleEvent(Event evt)
    {
        Document doc = itsNatDoc.getDocument();
        // Nothing to do
    }

}
