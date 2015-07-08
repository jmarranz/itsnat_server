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
 * Author: Jose Maria Arranz Santamaria
 * (C) Innowhere Software Services S.L., Spanish company
 */

package manual.core.svgxul;


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

public class SVGInHTMLMimeSVGWebDocument implements EventListener
{
    protected ItsNatDocument itsNatDoc;
    protected Element svgElem;
    protected Element circleListElem;
    protected ElementList circleList;
    protected Element selectedCircle;
    protected Element textElem;
    protected Element addCircleElem;
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
        EventListener listener = new EventListener()
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
        else if (currTarget == reinsertElem)
        {
            selectCircle(null);

            HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
            Element newSVGElem = (Element)svgElem.cloneNode(true);
            Node parent = doc.getBody();
            Node sibling = svgElem.getNextSibling();
            parent.removeChild(svgElem);
            parent.insertBefore(newSVGElem,sibling);
            this.svgElem = newSVGElem;

            if (useSVGLoadElem.getChecked())
            {
                EventListener listener = new EventListener()
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
