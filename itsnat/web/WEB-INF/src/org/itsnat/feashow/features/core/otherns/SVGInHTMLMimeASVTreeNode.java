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

import java.util.Date;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.http.ItsNatHttpSession;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class SVGInHTMLMimeASVTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element svgElem;
    protected ElementList circleList;
    protected Element selectedCircle;
    protected Element textElem;
    protected Element addCircleElem;
    protected Element removeCircleElem;
    protected Element biggerCircleElem;
    protected Element smallerCircleElem;

    public SVGInHTMLMimeASVTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

       ItsNatHttpSession session = (ItsNatHttpSession)itsNatDoc.getClientDocumentOwner().getItsNatSession();
        if (itsNatDoc.isLoading() &&
           itsNatDoc.getItsNatDocumentTemplate().isFastLoadMode() &&
           session.getUserAgent().indexOf("MSIE") != -1)
        {
            // Removing <!DOCTYPE> sets Internet Explorer in Quirks Mode.
            // Quirks mode is mandatory with SVG inline and ASV
            DocumentType docType = doc.getDoctype();
            doc.removeChild(docType);
        }

        this.svgElem = doc.getElementById("svgId");
        ParamTransport[] params = new ParamTransport[]
            { new CustomParamTransport("offsetX","event.getNativeEvent().offsetX"),
              new CustomParamTransport("offsetY","event.getNativeEvent().offsetY") };
        itsNatDoc.addEventListener(((EventTarget)svgElem),"click",this,false,params);

        ElementGroupManager egm = itsNatDoc.getElementGroupManager();
        Element circleListElem = doc.getElementById("circleListId");
        this.circleList = egm.createElementList(circleListElem,false);

        this.textElem = doc.getElementById("textId");
        ((Text)textElem.getFirstChild()).setData(new Date().toString());

        this.addCircleElem = doc.getElementById("addCircleId");
        ((EventTarget)addCircleElem).addEventListener("click",this,false);

        this.removeCircleElem = doc.getElementById("removeCircleId");
        ((EventTarget)removeCircleElem).addEventListener("click",this,false);

        this.biggerCircleElem = doc.getElementById("biggerCircleId");
        ((EventTarget)biggerCircleElem).addEventListener("click",this,false);

        this.smallerCircleElem = doc.getElementById("smallerCircleId");
        ((EventTarget)smallerCircleElem).addEventListener("click",this,false);
    }

    public void endExamplePanel()
    {
        ((EventTarget)svgElem).removeEventListener("click",this,false);
        this.svgElem = null;

        this.circleList = null;
        this.selectedCircle = null;
        ((EventTarget)addCircleElem).removeEventListener("click",this,false);
        this.addCircleElem = null;
        ((EventTarget)removeCircleElem).removeEventListener("click",this,false);
        this.removeCircleElem = null;
        ((EventTarget)biggerCircleElem).removeEventListener("click",this,false);
        this.biggerCircleElem = null;
        ((EventTarget)smallerCircleElem).removeEventListener("click",this,false);
        this.smallerCircleElem = null;
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
            log("Clicked: " + x + "," + y);
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
        else if ("circle".equals(((Node)target).getLocalName())) // Not MSIE
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
        else if (target == removeCircleElem)
        {
            if (!circleList.isEmpty())
            {
                Element circle = circleList.removeElementAt(circleList.getLength() - 1);
                if (selectedCircle == circle) this.selectedCircle = null;
            }
        }
        else if ((target == biggerCircleElem)||
                 (target == smallerCircleElem))
        {
            if (selectedCircle != null)
            {
                boolean bigger = (target == biggerCircleElem);
                int r = Integer.parseInt(selectedCircle.getAttribute("r"));
                if (bigger) r += 20;
                else { r -= 20; if (r < 0) r = 10; }
                selectedCircle.setAttribute("r",Integer.toString(r));
            }
            else
            {
                ItsNatDocument itsNatDoc = getItsNatDocument();
                itsNatDoc.addCodeToSend("alert('Select a circle');");
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
