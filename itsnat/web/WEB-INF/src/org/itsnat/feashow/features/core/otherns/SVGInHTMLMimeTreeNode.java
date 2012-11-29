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

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.tmpl.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLStyleElement;

public class SVGInHTMLMimeTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element buttonChangeColor;
    protected Element buttonBigger;
    protected Element buttonAdd;
    protected Element buttonRemove;
    protected Element circleElem;
    protected ElementList circleList;
    protected HTMLStyleElement styleElem;


    public SVGInHTMLMimeTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();

        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        ItsNatHTMLDocFragmentTemplate docFragTemplate =
                (ItsNatHTMLDocFragmentTemplate)servlet.getItsNatDocFragmentTemplate("feashow.core.otherns.svgInHTMLMime.ex");
        DocumentFragment headFrag = docFragTemplate.loadDocumentFragmentHead(itsNatDoc);
        this.styleElem = (HTMLStyleElement)ItsNatTreeWalker.getFirstChildElement(headFrag);
        HTMLHeadElement headElem =
                (HTMLHeadElement)ItsNatTreeWalker.getFirstChildElement(doc.getDocumentElement());
        headElem.appendChild(styleElem);

        Element listParentElem = doc.getElementById("circleListId");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        this.circleList = factory.createElementList(listParentElem,true);

        this.buttonChangeColor = doc.getElementById("changeColorId");
        ((EventTarget)buttonChangeColor).addEventListener("click",this,false);

        this.buttonBigger = doc.getElementById("biggerCircleId");
        ((EventTarget)buttonBigger).addEventListener("click",this,false);

        this.buttonAdd = doc.getElementById("addCircleId");
        ((EventTarget)buttonAdd).addEventListener("click",this,false);

        this.buttonRemove = doc.getElementById("removeCircleId");
        ((EventTarget)buttonRemove).addEventListener("click",this,false);

        this.circleElem = doc.getElementById("circleId");
        ((EventTarget)circleElem).addEventListener("click",this,false);
    }

    public void endExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        doc.getBody().removeAttribute("class");

        styleElem.getParentNode().removeChild(styleElem);
        this.styleElem = null;

        this.circleList = null;

        ((EventTarget)buttonChangeColor).removeEventListener("click",this,false);
        this.buttonChangeColor = null;

        ((EventTarget)buttonBigger).removeEventListener("click",this,false);
        this.buttonBigger = null;

        ((EventTarget)buttonAdd).removeEventListener("click",this,false);
        this.buttonAdd = null;

        ((EventTarget)buttonRemove).removeEventListener("click",this,false);
        this.buttonRemove = null;

        ((EventTarget)circleElem).removeEventListener("click",this,false);
        this.circleElem = null;
    }

    public void handleEvent(Event evt)
    {
        ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
        Document doc = itsNatEvt.getItsNatDocument().getDocument();

        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == buttonChangeColor)
        {
            ((HTMLDocument)doc).getBody().setAttribute("class","invalid");
        }
        else if (currTarget == buttonBigger)
        {
            int radio = Integer.parseInt(circleElem.getAttribute("r"));
            radio += 20;
            circleElem.setAttribute("r",Integer.toString(radio));
        }
        else if (currTarget == buttonAdd)
        {
            Element circleElem = circleList.addElement();
            int len = circleList.getLength();
            if (len > 1)
            {
                Element prevCircle = circleList.getElementAt(len - 2);
                int cx = Integer.parseInt(prevCircle.getAttribute("cx"));
                cx += 70;
                circleElem.setAttribute("cx",Integer.toString(cx));
            }
        }
        else if (currTarget == buttonRemove)
        {
            int len = circleList.getLength();
            if (len > 0)
                circleList.removeElementAt(len - 1);
        }
        else if (currTarget == circleElem)
        {
            circleElem.setAttribute("fill","red");
        }
    }

}
