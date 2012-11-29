
package itsnatex;

import itsnatspring.ItsNatDocumentInitialize;
import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class CoreExampleDocument implements EventListener,Serializable,ItsNatDocumentInitialize
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element clickElem1;
    protected Element clickElem2;

    public CoreExampleDocument()
    {
    }

    @Override
    public void load(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();
        load();
    }

    public void load()
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        this.clickElem1 = doc.getElementById("clickableId1");
        this.clickElem2 = doc.getElementById("clickableId2");

        clickElem1.setAttribute("style","color:red;");
        Text text1 = (Text)clickElem1.getFirstChild();
        text1.setData("Click Me!");

        Text text2 = (Text)clickElem2.getFirstChild();
        text2.setData("Cannot be clicked");

        Element noteElem = doc.createElement("p");
        noteElem.appendChild(doc.createTextNode("Ready to receive clicks..."));
        doc.getBody().appendChild(noteElem);

        ((EventTarget)clickElem1).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == clickElem1)
        {
            removeClickable(clickElem1);
            setAsClickable(clickElem2);
        }
        else
        {
            setAsClickable(clickElem1);
            removeClickable(clickElem2);
        }

        ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
        ItsNatServletRequest itsNatReq = itsNatEvt.getItsNatServletRequest();
        ItsNatDocument itsNatDoc = itsNatReq.getItsNatDocument();

        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element noteElem = doc.createElement("p");
        noteElem.appendChild(
                doc.createTextNode("Clicked " + ((Element)currTarget).getAttribute("id")));
        doc.getBody().appendChild(noteElem);
    }

    public void setAsClickable(Element elem)
    {
        elem.setAttribute("style","color:red;");
        Text text = (Text)elem.getFirstChild();
        text.setData("Click Me!");
        ((EventTarget)elem).addEventListener("click",this,false);
    }

    public void removeClickable(Element elem)
    {
        elem.removeAttribute("style");
        Text text = (Text)elem.getFirstChild();
        text.setData("Cannot be clicked");
        ((EventTarget)elem).removeEventListener("click",this,false);
    }

}
