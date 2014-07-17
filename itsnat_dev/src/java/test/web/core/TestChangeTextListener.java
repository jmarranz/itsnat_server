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
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestChangeTextListener implements EventListener,Serializable
{

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestChangeTextListener(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element textToChangeElem = itsNatDoc.getDocument().getElementById("textToChange");
        ((EventTarget)textToChangeElem).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        Element textToChangeElem = (Element)evt.getCurrentTarget();
        Text textToChange = (Text)textToChangeElem.getFirstChild();
        textToChange.setData(textToChange.getData() + "=>Text Changed");
     }

}
