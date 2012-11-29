/*
 * TestAddRowListener.java
 *
 * Created on 2 de octubre de 2006, 21:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 *
 * @author jmarranz
 */
public class TestPlainHTMLTextArea implements EventListener,Serializable
{
    protected HTMLTextAreaElement textArea;
    protected Element link;

    /**
     * Creates a new instance of TestAddRowListener
     */
    public TestPlainHTMLTextArea(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        this.textArea = (HTMLTextAreaElement)itsNatDoc.getDocument().getElementById("textAreaId");
        this.link = itsNatDoc.getDocument().getElementById("changeTextAreaId");
        ((EventTarget)link).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        textArea.setValue("A new remote view must see \nthis NEW Text");
    }

}
