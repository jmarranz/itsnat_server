/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.svg;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatKeyEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestSVGKeyEvents implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element elem;
    protected boolean cleared = false;

    public TestSVGKeyEvents(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        Document doc = itsNatDoc.getDocument();
        this.elem = doc.getElementById("testKeyEventsId");

        Element root = doc.getDocumentElement();
        ((EventTarget)root).addEventListener("keypress",this,false);
        ((EventTarget)root).addEventListener("keyup",this,false);
        ((EventTarget)root).addEventListener("keydown",this,false);
    }

    public void handleEvent(Event evt)
    {
        ItsNatKeyEvent keyEvt = (ItsNatKeyEvent)evt;
        String code;
        if (keyEvt.getType().equals("keypress"))
            code = keyEvt.getType() + ": charCode " + keyEvt.getCharCode();
        else // keydown/up
            code = keyEvt.getType() + ": keyCode " + keyEvt.getKeyCode();

        Text text = (Text)elem.getFirstChild();
        if (!cleared) { text.setData(""); cleared = true; }
        text.setData(text.getData() + code + ", ");
    }
}
