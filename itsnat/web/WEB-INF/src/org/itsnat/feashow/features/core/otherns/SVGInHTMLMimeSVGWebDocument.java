/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.core.otherns;


import java.io.Serializable;
import java.util.Date;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.event.NodePropertyTransport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

public class SVGInHTMLMimeSVGWebDocument implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element svgElem;
    protected Element circleListElem;
    protected ElementList circleList;
    protected Element selectedCircle;
    protected Element textElem;
    protected Element addCircleElem;
    protected Element removeCircleElem;
    protected Element biggerCircleElem;
    protected Element smallerCircleElem;
    protected Element reinsertElem;
    protected HTMLInputElement useSVGLoadElem;

    public SVGInHTMLMimeSVGWebDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();

        // When the document is loading we cannot access the SVGWeb DOM nodes
        // in client with JavaScript (there is still no SVG DOM),
        // we must wait to the load event.
        // If fastLoad mode is true we can change the SVGWeb DOM in server because no JavaScript
        // code is generated, this is not valid to the attachment of event listeners
        // (JavaScript code is generated and SVG DOM nodes are needed in client).
        // Anyway the best practice is to delegate to the load event.
        // This is not needed of course with native (non-Flash) SVG.

        EventListener listener = new EventListenerSerial() // EventListenerSerial instead of EventListener because this example runs on GAE
        {
            public void handleEvent(Event evt)
            {
                loadSVG();
            }
        };
        AbstractView view = ((DocumentView)doc).getDefaultView();
        ((EventTarget)view).addEventListener("SVGLoad",listener,false);

        this.addCircleElem = doc.getElementById("addCircleId");
        ((EventTarget)addCircleElem).addEventListener("click",this,false);

        this.removeCircleElem = doc.getElementById("removeCircleId");
        ((EventTarget)removeCircleElem).addEventListener("click",this,false);

        this.biggerCircleElem = doc.getElementById("biggerCircleId");
        ((EventTarget)biggerCircleElem).addEventListener("click",this,false);

        this.smallerCircleElem = doc.getElementById("smallerCircleId");
        ((EventTarget)smallerCircleElem).addEventListener("click",this,false);

        this.reinsertElem = doc.getElementById("reinsertId");
        ((EventTarget)reinsertElem).addEventListener("click",this,false);

        this.useSVGLoadElem = (HTMLInputElement)doc.getElementById("useSVGLoadId");
        itsNatDoc.addEventListener((EventTarget)useSVGLoadElem,"click",this,false,new NodePropertyTransport("checked",boolean.class));
    }

    public void loadSVG()
    {
        Document doc = itsNatDoc.getDocument();

        this.svgElem = doc.getElementById("svgId");

        this.circleListElem = doc.getElementById("circleListId");
        ElementGroupManager egm = itsNatDoc.getElementGroupManager();
        this.circleList = egm.createElementList(circleListElem,true);
        circleList.addElement();
        itsNatDoc.addEventListener(((EventTarget)circleListElem),"click",this,false);

        this.textElem = doc.getElementById("textId");
        ((Text)textElem.getFirstChild()).setData(new Date().toString());
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == circleListElem)
        {
            EventTarget target = evt.getTarget();
            if ((target instanceof Element) &&
                ((Element)target).getLocalName().equals("circle"))
            {
                selectCircle((Element)target);
            }
        }
        else if (currTarget == addCircleElem)
        {
            Element lastElem = circleList.getLastElement();
            if (lastElem == null)
            {
                circleList.addElement(); // Will be based on the pattern
            }
            else
            {
                Element newCircle = circleList.addElement();
                int cx = Integer.parseInt(lastElem.getAttribute("cx"));
                cx += 100;
                newCircle.setAttribute("cx",Integer.toString(cx));
            }
        }
        else if (currTarget == removeCircleElem)
        {
            if (!circleList.isEmpty())
            {
                Element circle = circleList.removeElementAt(circleList.getLength() - 1);
                if (selectedCircle == circle) this.selectedCircle = null;
            }
        }
        else if ((currTarget == biggerCircleElem)||
                 (currTarget == smallerCircleElem))
        {
            if (selectedCircle != null)
            {
                boolean bigger = (currTarget == biggerCircleElem);
                int r = Integer.parseInt(selectedCircle.getAttribute("r"));
                if (bigger) r += 20;
                else { r -= 20; if (r < 0) r = 10; }
                selectedCircle.setAttribute("r",Integer.toString(r));
            }
            else
            {
                itsNatDoc.addCodeToSend("alert('Select a circle');");
            }
        }
        else if (currTarget == reinsertElem)
        {
            selectCircle(null);

            HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
            Element newSVGElem = (Element)svgElem.cloneNode(true);
            Node parent = doc.getBody();
            Node sibling = svgElem.getNextSibling();
            parent.removeChild(svgElem);
            parent.insertBefore(newSVGElem,sibling);
            // Note: In this case we remove FIRST the already inserted
            // SVG node because in this example the new SVG node and the current
            // SVG node in document have the same id attribute and SVGWeb FAILS when two or more
            // <svg> elements into the client document have the same id attrib.
            // Changing temporally the id is not a good idea, SVGWeb is not
            // tolerant to an id change before the SVG is rendered (be careful with ids in SVGWeb nodes
            // SVGWeb internally uses your id for identification purposes).

            this.svgElem = newSVGElem;

            // In theory when new SVG nodes are dynamically inserted to be rendered by SVGWeb,
            // in client these nodes are provisional and will be replaced,
            // after insertion into the document any change to this provisional
            // DOM is not valid, changing attributes of the <svg> element is almost
            // the only action valid, so we cannot change the SVG DOM or add event listeners
            // after insertion. The SVGWeb root node automatically fires a SVGLoad event
            // when rendering is done, this is the moment to change the SVG DOM as is suggested
            // by SVGWeb.

            // In ItsNat a SVGLoad listener is not necessary, that is, you CAN modify
            // and/or add listeners to the SVG DOM immediatelly after insertion,
            // ItsNat automatically queues any JavaScript generated code to be
            // executed when rendering is done.

            // Anyway you also can the SVGLoad approach in server as promoted by SVGWeb.
            // In non-Flash mode (non-MSIE browsers) SVGLoad event is simulated by ItsNat,
            // this avoids us to detect if SVG nodes are Flash or native, the same server code
            // is valid in both cases.
            // In this example both approaches are used.
            // Finally remote view/control WORKS with SVGWeb and dynamic insertion/removing.

            // IMPORTANT: do not mix both approaches, that is do NOT modify SVG DOM
            // immediately after insertion AND attach a SVGLoad listener.

            if (useSVGLoadElem.getChecked())
            {
                EventListener listener = new EventListenerSerial() // EventListenerSerial instead of EventListener because this example runs on GAE
                {
                    public void handleEvent(Event evt)
                    {
                        loadSVG();
                    }
                };
                ((EventTarget)svgElem).addEventListener("SVGLoad",listener,false);

                return; // To avoid the next textElem's setData call
            }
            else
            {
                loadSVG();
            }

        }

        ((Text)textElem.getFirstChild()).setData(new Date().toString());
    }

    public void selectCircle(Element circleElem)
    {
        if (selectedCircle != null)
            selectedCircle.setAttribute("fill","#FF0000");

        if (circleElem != null)
            circleElem.setAttribute("fill","#00FF00");
        this.selectedCircle = circleElem;
    }

    public boolean clickedCircle(int x,int y,Element circle)
    {
        int xc = Integer.parseInt(circle.getAttribute("cx"));
        int yc = Integer.parseInt(circle.getAttribute("cy"));
        int r = Integer.parseInt(circle.getAttribute("r"));
        return (xc - r <= x)&&(x <= xc + r)&&(yc - r <= y)&&(y <= yc + r);
    }
}
