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
public class TestNewAttrListener implements EventListener,Serializable
{

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestNewAttrListener(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element linkToNewAttr = itsNatDoc.getDocument().getElementById("linkToNewAttr");
        ((EventTarget)linkToNewAttr).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        Element linkToNewAttr = (Element)evt.getCurrentTarget();
        linkToNewAttr.setAttribute("style","color:red;");
    }

}
