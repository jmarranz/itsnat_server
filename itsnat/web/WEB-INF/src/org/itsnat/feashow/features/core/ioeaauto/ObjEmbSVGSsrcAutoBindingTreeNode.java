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
package org.itsnat.feashow.features.core.ioeaauto;

import java.lang.reflect.Method;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatNode;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLEmbedElement;
import org.itsnat.core.http.ItsNatHttpSession;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLObjectElement;
import org.w3c.dom.html.HTMLParamElement;
import org.w3c.dom.html.HTMLSelectElement;

public class ObjEmbSVGSsrcAutoBindingTreeNode extends FeatureTreeNode implements EventListener
{
    protected HTMLElement container;
    protected HTMLElement circleSelected;
    protected HTMLInputElement inputRadio;
    protected HTMLInputElement updateButton;
    protected HTMLSelectElement selectContainer;
    protected String url;

    public ObjEmbSVGSsrcAutoBindingTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        itsNatDoc.setUserValue("parent_user_object",this);

        Document doc = itsNatDoc.getDocument();
        this.container = (HTMLElement)doc.getElementById("containerId");

        // Initially container is an <object>
        this.url = container.getAttribute("data");

        ItsNatHttpSession session = (ItsNatHttpSession)itsNatDoc.getClientDocumentOwner().getItsNatSession();
        if (session.getUserAgent().indexOf("MSIE") != -1)
        {
            // URL must be absolute when used with Savarese Ssrc
            this.url = getRequestURLOfDocument() + this.url + "#p=n,c=n"; // For communication parent/child (p=parent, c=child, n=no, y=yes pending update)

            // In Internet Explorer the <object> element is weird.
            // If the element is already in tree and we need to change
            // the URL and use auto-binding in ItsNat, the problems are bigger.
            // The best technique is to remove the element, set up, and reinsert.
            Element containerFoo = doc.createElement("object");
            container.getParentNode().replaceChild(containerFoo,container);

            container.setAttribute("data",url);
            // Needed in Internet Explorer only:
            container.setAttribute("src",url);
            Element paramSrc = ItsNatTreeWalker.getFirstChildElement(container);
            paramSrc.setAttribute("value",url);  // <param name="src" value="?...">
            // Reinserting again:
            containerFoo.getParentNode().replaceChild(container,containerFoo);
        }

        this.circleSelected = (HTMLElement)doc.getElementById("circleSelectedId");

        this.inputRadio = (HTMLInputElement)doc.getElementById("radioId");
        itsNatDoc.addEventListener((EventTarget)inputRadio,"change",this,false,new NodePropertyTransport("value"));

        this.updateButton = (HTMLInputElement)doc.getElementById("updateId");
        ((EventTarget)updateButton).addEventListener("click",this,false);

        itsNatDoc.addUserEventListener(null,"update",this);

        this.selectContainer = (HTMLSelectElement)doc.getElementById("selectContId");
        itsNatDoc.addEventListener((EventTarget)selectContainer,"change",this,false,new NodePropertyTransport("selectedIndex"));
    }

    public void endExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        itsNatDoc.removeUserValue("parent_user_object");

        this.container = null;
        this.circleSelected = null;

        ((EventTarget)inputRadio).removeEventListener("change",this,false);
        this.inputRadio = null;

        ((EventTarget)updateButton).removeEventListener("click",this,false);
        this.updateButton = null;

        itsNatDoc.removeUserEventListener(null,"update",this);
    }

    public Element getCircleSelected()
    {
        return circleSelected;
    }

    public Element getInputRadio()
    {
        return inputRadio;
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (evt instanceof ItsNatUserEvent)
        {
            // Nothing specific to do, the client will
            // be automatically updated as response of this event.
        }
        else if (currTarget == updateButton)
        {
            updateCircle();
        }
        else if (currTarget == selectContainer)
        {
            ItsNatDocument itsNatDoc = getItsNatDocument();
            Document doc = itsNatDoc.getDocument();
            String selIndexStr = (String)((ItsNatEvent)evt).getExtraParam("selectedIndex");
            int index = Integer.parseInt(selIndexStr);
            Element oldContainer = container;
            switch(index)
            {
                case 0:
                    this.container = (HTMLElement)doc.createElement("object");
                    container.setAttribute("type","image/svg+xml");
                    container.setAttribute("data",url);
                    // Useful for Internet Explorer:
                    container.setAttribute("src",url);
                    Element param = doc.createElement("param");
                    param.setAttribute("name","src");
                    param.setAttribute("value",url);
                    container.appendChild(param);
                    break;
                case 1:
                    this.container = (HTMLElement)doc.createElement("embed");
                    container.setAttribute("type","image/svg+xml");
                    container.setAttribute("src",url);
                    break;
            }
            container.setAttribute("id",oldContainer.getAttribute("id"));
            container.setAttribute("style",oldContainer.getAttribute("style"));
            oldContainer.getParentNode().replaceChild(container,oldContainer);
        }
    }

    public void updateCircle()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        int newRadio = -1;
        String valueStr = inputRadio.getAttribute("value");
        try
        {
            newRadio = Integer.parseInt(valueStr);
        }
        catch(NumberFormatException ex)
        {
            itsNatDoc.addCodeToSend("alert('Not an integer number');");
            return;
        }

        Document childDoc;
        try
        {
            if (container instanceof HTMLObjectElement)
                childDoc = ((HTMLObjectElement)container).getContentDocument();
            else
                childDoc = ((ItsNatHTMLEmbedElement)container).getContentDocument();
        }
        catch(NoSuchMethodError ex)
        {
            // Cause: Xerces compatibility package of Tomcat 5.5
            // misses this standard DOM method in HTMLIFrameElement
            // and HTMLObjectElement interfaces
            // Don't worry, our required method is there.
            try
            {
                Method method = container.getClass().getMethod("getContentDocument",null);
                childDoc = (Document)method.invoke(container,(Object[])null);
            }
            catch(Exception ex2) { throw new RuntimeException(ex2); }
        }

        if (childDoc == null)
        {
            itsNatDoc.addCodeToSend("alert('Not loaded yet');");
            return;
        }
        ItsNatDocument childItsNatDoc = ((ItsNatNode)childDoc).getItsNatDocument(); // This method is multithread

        synchronized(childItsNatDoc) // NEEDED!!!
        {
            ChildSVGSsrcAutoBindingDocument childUserDoc =
                 (ChildSVGSsrcAutoBindingDocument)childItsNatDoc.getUserValue("user_object");
            Element circle = childUserDoc.getSelectedCircle();
            if (circle == null)
            {
                itsNatDoc.addCodeToSend("alert('No selected circle');");
                return;
            }
            circle.setAttribute("r",Integer.toString(newRadio));

            // Notify container child document
            String ref = itsNatDoc.getScriptUtil().getNodeReference(container);
            StringBuilder code = new StringBuilder();
            code.append("var elem = " + ref + ";");
            code.append("var url = elem.LocationURL;");
            code.append("if (url)"); // MSIE + Ssrc
            code.append("{");
            code.append("  var pos = url.indexOf('#');"); // #p=n,c=n
            code.append("  url = url.substring(0,pos + 7) + 'y' + url.substring(pos + 7);");
            code.append("  elem.Navigate(url);");
            code.append("}");
            code.append("else"); // Not loaded yet or other browsers.
            code.append("{");
            code.append("  var childDoc = (typeof elem.getSVGDocument != \"undefined\") ? elem.getSVGDocument() : elem.contentDocument;"); // getSVGDocument useful in FireFox and <embed>
            code.append("  if (childDoc) childDoc.getItsNatDoc().fireUserEvent(null,'update');");
            code.append("}");
            itsNatDoc.addCodeToSend(code.toString());
        }
    }

}
