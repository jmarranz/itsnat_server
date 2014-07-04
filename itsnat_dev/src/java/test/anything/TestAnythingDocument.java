/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.anything;

import java.io.Serializable;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class TestAnythingDocument implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element elem;
    protected Element logElem;

    public TestAnythingDocument(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;

        this.elem = itsNatDoc.getDocument().getElementById("elemId");
        elem.setAttributeNS("http://schemas.android.com/apk/res/android", "android:background", "#dddddd");        
        ((EventTarget)elem).addEventListener("click", this,false);

        this.logElem = itsNatDoc.getDocument().getElementById("logId");
    }

    public void handleEvent(Event evt)
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        Element infoElem = doc.createElement("div");
        infoElem.appendChild(doc.createTextNode("clicked"));
        
//org.w3c.dom.Node parent = elem.getParentNode();
//parent.getParentNode().removeChild(parent);
        
        logElem.appendChild(infoElem);
    }
}
