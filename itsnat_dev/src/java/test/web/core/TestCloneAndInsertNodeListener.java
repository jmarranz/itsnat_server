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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestCloneAndInsertNodeListener implements EventListener,Serializable
{
    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestCloneAndInsertNodeListener(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element linkToCloneAndInsert = itsNatDoc.getDocument().getElementById("linkToCloneAndInsert");
        ((EventTarget)linkToCloneAndInsert).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        Element link = (Element)evt.getCurrentTarget();
        Node child = link.getFirstChild();
        Node cloned = child.cloneNode(true);
        link.getParentNode().insertBefore(cloned,link);
    }

}
