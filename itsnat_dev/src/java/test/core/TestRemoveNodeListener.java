/*
 * OnClickAddRowListenerExample.java
 *
 * Created on 2 de octubre de 2006, 21:10
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
public class TestRemoveNodeListener implements EventListener,Serializable
{

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestRemoveNodeListener(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element linkToRemove = itsNatDoc.getDocument().getElementById("linkToRemove");
        ((EventTarget)linkToRemove).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        Element linkToRemove = (Element)evt.getCurrentTarget();
        linkToRemove.getParentNode().removeChild(linkToRemove);
    }

}
