/*
 * OnClickFireEventFromServerTest.java
 *
 * Created on 6 de agosto de 2007, 15:19
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
public class TestIFrameInsertion implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element link;
    protected Element div;

    /** Creates a new instance of TestIFrameInsertion */
    public TestIFrameInsertion(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        this.link = doc.getElementById("testIFrameInsertionId");
        ((EventTarget)link).addEventListener("click",this,false);

        this.div = doc.getElementById("testIFrameInsertionRefId");
    }

    public void handleEvent(Event evt)
    {
        if (div.getFirstChild() == null)
        {
            Document doc = itsNatDoc.getDocument();
            Element iframe = doc.createElement("iframe");
            iframe.setAttribute("src","index.html");
            iframe.setAttribute("style","border:solid 1px");
            div.appendChild(iframe);
        }
        else
        {
            div.removeChild(div.getFirstChild());
        }
    }
}
