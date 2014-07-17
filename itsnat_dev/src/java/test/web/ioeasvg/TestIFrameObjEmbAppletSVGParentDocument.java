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

package test.web.ioeasvg;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatNode;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLAppletElement;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.html.ItsNatHTMLEmbedElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAppletElement;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLObjectElement;
import org.w3c.dom.html.HTMLParamElement;
import test.web.shared.Shared;
import test.web.shared.TestSerialization;

/**
 *
 * @author jmarranz
 */
public class TestIFrameObjEmbAppletSVGParentDocument implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected HTMLElement containerElem;
    protected HTMLElement circleSelected;
    protected HTMLInputElement inputRadio;
    protected HTMLInputElement updateButton;
    protected HTMLButtonElement reinsertButton;
    protected Element changeURLElem;

    /** Creates a new instance of TestIFrameObjEmbAppletSVGParentDocument */
    public TestIFrameObjEmbAppletSVGParentDocument(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();
        load(request,response);
    }

    public ItsNatDocument getItsNatDocument()
    {
        return itsNatDoc;
    }

    public void load(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        itsNatDoc.setUserValue("parent_user_object_svg",this);

        Document doc = itsNatDoc.getDocument();
        this.containerElem = (HTMLElement)doc.getElementById("containerSVGId");
        this.circleSelected = (HTMLElement)doc.getElementById("circleSelectedId");

        this.inputRadio = (HTMLInputElement)doc.getElementById("radioId");
        itsNatDoc.addEventListener((EventTarget)inputRadio,"change",this,false,new NodePropertyTransport("value"));

        this.updateButton = (HTMLInputElement)doc.getElementById("updateId");
        ((EventTarget)updateButton).addEventListener("click",this,false);

        itsNatDoc.addUserEventListener(null,"update_svg",this);

        this.reinsertButton = (HTMLButtonElement)doc.getElementById("reinsertId");
        ((EventTarget)reinsertButton).addEventListener("click",this,false);

        this.changeURLElem = (HTMLButtonElement)doc.getElementById("changeURLId");
        ((EventTarget)changeURLElem).addEventListener("click",this,false);

        Shared.setRemoteControlLink(request,response);

        new TestSerialization(request);
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
        if (evt instanceof ItsNatUserEvent)
        {
            // Nothing specific to do, the client will
            // be automatically updated as response of this event.
        }
        else if (evt.getCurrentTarget() == updateButton)
        {
            updateCircle();
        }
        else if (evt.getCurrentTarget() == reinsertButton)
        {
            reinsert();
        }
        else if (evt.getCurrentTarget() == changeURLElem)
        {
            changeURL();
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
            if (containerElem instanceof HTMLIFrameElement)
                childDoc = ((HTMLIFrameElement)containerElem).getContentDocument();
            else if (containerElem instanceof HTMLObjectElement)
                childDoc = ((HTMLObjectElement)containerElem).getContentDocument();
            else if (containerElem instanceof ItsNatHTMLEmbedElement)
                childDoc = ((ItsNatHTMLEmbedElement)containerElem).getContentDocument();
            else // ItsNatHTMLAppletElement
                childDoc = ((ItsNatHTMLAppletElement)containerElem).getContentDocument();
        }
        catch(NoSuchMethodError ex)
        {
            // Cause: Xerces compatibility package of Tomcat 5.5
            // misses this standard DOM method in HTMLIFrameElement and
            // HTMLObjectElement interfaces
            // Don't worry, our required method is there.
            try
            {
                Method method = containerElem.getClass().getMethod("getContentDocument",(Class[])null); // El cast es para evitar un warnning en Java 1.5+
                childDoc = (Document)method.invoke(containerElem,(Object[])null);
            }
            catch(Exception ex2) { throw new RuntimeException(ex2); }
        }

        ItsNatDocument childItsNatDoc = ((ItsNatNode)childDoc).getItsNatDocument(); // This method is multithread
        if (childItsNatDoc == null)
        {
            itsNatDoc.addCodeToSend("alert('Not loaded yet');");
            return;
        }

        synchronized(childItsNatDoc) // NEEDED!!!
        {
            TestSVGBoundDocument childUserDoc =
                 (TestSVGBoundDocument)childItsNatDoc.getUserValue("user_object_svg");
            Element circle = childUserDoc.getSelectedCircle();
            if (circle == null)
            {
                itsNatDoc.addCodeToSend("alert('No selected circle');");
                return;
            }
            circle.setAttribute("r",Integer.toString(newRadio));

            // Notify containerElem child document
            String ref = itsNatDoc.getScriptUtil().getNodeReference(containerElem);
            StringBuilder code = new StringBuilder();
            code.append("var elem = " + ref + "; ");
 
            code.append("var childDoc = (typeof elem.getSVGDocument != \"undefined\") ? elem.getSVGDocument() : elem.contentDocument; "); // getSVGDocument in MSIE or with Batik . Hay un errorcillo en este código en el caso de abrir una ventana nueva Timer o Comet y caso IFRAME, no se arreglarlo pero parece como si no se hubiera iniciado el getSVGDocument, con OBJECT no pasa

            code.append("childDoc.getItsNatDoc().fireUserEvent(null,'update_svg');");
            itsNatDoc.addCodeToSend(code.toString());
        }
    }

    public void reinsert()
    {
        HTMLElement newContainer = (HTMLElement)containerElem.cloneNode(true);

        Node parent = containerElem.getParentNode();
        parent.insertBefore(newContainer,containerElem);

        parent.removeChild(containerElem);

        this.containerElem = newContainer;
    }

    public void changeURL()
    {
        String url = getURL();
        setURL("foo"); // Carga errónea para que el atributo cambie
        setURL(url); // Restauramos
    }

    public String getURL()
    {
        if (containerElem instanceof HTMLIFrameElement)
            return ((HTMLIFrameElement)containerElem).getSrc();
        else if (containerElem instanceof HTMLObjectElement)
        {
            String type = containerElem.getAttribute("type");
            if (type.equals("application/x-java-applet"))
            {
                HTMLParamElement param = (HTMLParamElement)ItsNatTreeWalker.getFirstChildElement((HTMLObjectElement)containerElem);
                return param.getValue(); // <param name="src" value="url" />
            }
            else
                return ((HTMLObjectElement)containerElem).getData();
        }
        else if (containerElem instanceof ItsNatHTMLEmbedElement)
            return ((ItsNatHTMLEmbedElement)containerElem).getAttribute("src");
        else // HTMLAppletElement
        {
            HTMLParamElement param = (HTMLParamElement)ItsNatTreeWalker.getFirstChildElement((HTMLAppletElement)containerElem);
            return param.getValue(); // <param name="src" value="url" />
        }
    }

    public void setURL(String url)
    {
        if (containerElem instanceof HTMLIFrameElement)
            ((HTMLIFrameElement)containerElem).setSrc(url);
        else if (containerElem instanceof HTMLObjectElement)
        {
            // <param name="src" value="url" />
            HTMLParamElement param = (HTMLParamElement)ItsNatTreeWalker.getFirstChildElement((HTMLObjectElement)containerElem);
            String type = containerElem.getAttribute("type");
            if (type.equals("application/x-java-applet"))
            {
                param.setValue(url); // <param name="src" value="url" />
            }
            else
            {
                // Para MSIE
                ((HTMLObjectElement)containerElem).setAttribute("src",url);
                param.setValue(url); // <param name="src" value="url" />

                ((HTMLObjectElement)containerElem).setData(url);
            }
        }
        else if (containerElem instanceof ItsNatHTMLEmbedElement)
        {
            ((ItsNatHTMLEmbedElement)containerElem).setAttribute("src",url);  // No funciona en MSIE
        }
        else // HTMLAppletElement
        {
            HTMLParamElement param = (HTMLParamElement)ItsNatTreeWalker.getFirstChildElement((HTMLAppletElement)containerElem);
            param.setValue(url); // <param name="src" value="url" />
        }
    }
}
