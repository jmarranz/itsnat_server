/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.mobile;


import java.io.Serializable;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public class TestMobileEventLoad implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;

    public TestMobileEventLoad(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();
        ((EventTarget)view).addEventListener("load",this,false);
    }

    public void handleEvent(Event evt)
    {
        String type = evt.getType();

        log("Event: " + type);
    }

    public void log(String msg)
    {
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element parent = doc.getElementById("logId");
        parent.appendChild(doc.createTextNode(msg + ".")); // Para que se vea
    }
}
