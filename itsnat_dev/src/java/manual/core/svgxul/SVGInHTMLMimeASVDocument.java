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
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ParamTransport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class SVGInHTMLMimeASVDocument implements EventListener
{
    protected ItsNatDocument itsNatDoc;
    protected Element svgElem;
    protected ElementList circleList;
    protected Element selectedCircle;
    protected Element textElem;
    protected Element addCircleElem;

    public SVGInHTMLMimeASVDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();

        this.svgElem = doc.getElementById("svgId");
        ParamTransport[] params = new ParamTransport[]
            { new CustomParamTransport("offsetX","event.getNativeEvent().offsetX"),
              new CustomParamTransport("offsetY","event.getNativeEvent().offsetY") };
        itsNatDoc.addEventListener(((EventTarget)svgElem),"click",this,false,params);

        ElementGroupManager egm = itsNatDoc.getElementGroupManager();
        Element circleListElem = doc.getElementById("circleListId");
        this.circleList = egm.createElementList(circleListElem,false);

        this.addCircleElem = doc.getElementById("addCircleId");
        ((EventTarget)addCircleElem).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        EventTarget target = evt.getTarget();
        if (target == svgElem) // MSIE or outside of circles
        {
            ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
            if ("undefined".equals(itsNatEvt.getExtraParam("offsetX")))
                return; // Not MSIE and outside of circles

            String offsetXStr = (String)itsNatEvt.getExtraParam("offsetX");
            int x = Integer.parseInt(offsetXStr);
            String offsetYStr = (String)itsNatEvt.getExtraParam("offsetY");
            int y = Integer.parseInt(offsetYStr);
            itsNatDoc.addCodeToSend("alert('Clicked: ' + " + x + " + ',' + " + y + ");");

            for(int i = circleList.getLength() - 1; i >= 0; i--)
            {
                // The last circle has a greater SVG "z-index"
                Element circleElem = circleList.getElementAt(i);
                if (clickedCircle(x,y,circleElem))
                {
                    selectCircle(circleElem);
                    break;
                }
            }
        }
        else if ("circle".equals(((Node)target).getLocalName()))
        {
            selectCircle((Element)target);
        }
        else if (target == addCircleElem)
        {
            Element lastElem = circleList.getLastElement();
            if (lastElem == null)
            {
                circleList.addElement(); // Will based on the pattern
            }
            else
            {
                Element newCircle = circleList.addElement();
                int cx = Integer.parseInt(lastElem.getAttribute("cx"));
                cx += 100;
                newCircle.setAttribute("cx",Integer.toString(cx));
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
