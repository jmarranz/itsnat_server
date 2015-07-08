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
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestRemoveAttrListener implements EventListener,Serializable
{

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestRemoveAttrListener(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element linkToRemoveAttr = itsNatDoc.getDocument().getElementById("linkToRemoveAttr");
        ((EventTarget)linkToRemoveAttr).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        Element linkToRemoveAttr = (Element)evt.getCurrentTarget();
        linkToRemoveAttr.removeAttribute("style");

        // Quitamos el listener para que no se pueda llamar de nuevo
        evt.getCurrentTarget().removeEventListener(evt.getType(),this,false);
    }

}
