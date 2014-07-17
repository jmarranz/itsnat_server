/*
 * OnClickAddRowListenerExample.java
 *
 * Created on 2 de octubre de 2006, 21:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

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
public class TestInsertBeforeNodeListener implements EventListener,Serializable
{

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestInsertBeforeNodeListener(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element linkToInsertBefore = itsNatDoc.getDocument().getElementById("linkToInsertBefore");
        ((EventTarget)linkToInsertBefore).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        Element linkToInsertBefore = (Element)evt.getCurrentTarget();
        Document doc = linkToInsertBefore.getOwnerDocument();
        Element newNode = doc.createElement("span");
        Element newNode2 = doc.createElement("span");
        newNode2.appendChild(doc.createTextNode("New Node "));
        newNode.appendChild(newNode2);
        linkToInsertBefore.insertBefore(newNode,linkToInsertBefore.getFirstChild());
    }

}
