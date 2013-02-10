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

package test.ioeasvg;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatNode;
import org.itsnat.core.event.ItsNatUserEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestSVGBoundSavareseDocument implements EventListener
{
    protected ItsNatDocument itsNatDoc;
    protected Element[] circles = new Element[3];
    protected Element selectedCircle;

    public TestSVGBoundSavareseDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        itsNatDoc.setUserValue("user_object_svg",this);

        Document doc = itsNatDoc.getDocument();
        for(int i = 0; i < circles.length; i++)
        {
            this.circles[i] = doc.getElementById("circle" + (i + 1));
            ((EventTarget)circles[i]).addEventListener("click",this,false);
        }

        itsNatDoc.addUserEventListener(null,"update_svg",this);
    }

    public Element getSelectedCircle()
    {
        return selectedCircle;
    }

    public void handleEvent(Event evt)
    {
        if (evt instanceof ItsNatUserEvent) 
        {
            // Radio changed notification
            // Nothing specific to do, the client will
            // be automatically updated as response of this event.
        }
        else // Clicked Circle
        {
            Element circle = (Element)evt.getCurrentTarget();
            selectCircle(circle);
        }
    }

    public void selectCircle(final Element circle)
    {
        int selected = -1;
        for(int i = 0; i < circles.length; i++)
        {
            if (circle != circles[i])
                circles[i].setAttribute("fill","#0000ff");
            else
            {
                this.selectedCircle = circles[i];
                selected = i;
            }
        }

        circle.setAttribute("fill","#ff0000");

        // Synchronization not needed, parent document is ever synchronized

        Element iframeElem = (Element)itsNatDoc.getContainerNode();
        if (iframeElem == null)
        {
            itsNatDoc.addCodeToSend("alert('Disconnected from parent!');");
            return;
        }
        ItsNatDocument parentItsNatDoc = ((ItsNatNode)iframeElem).getItsNatDocument();

        TestSVGSavareseParentDocument parentNode =
                  (TestSVGSavareseParentDocument)parentItsNatDoc.getUserValue("parent_user_object_svg");
        Element circleSelectedNum = parentNode.getCircleSelected();
        Text numNode = (Text)circleSelectedNum.getFirstChild();
        numNode.setData(Integer.toString(selected + 1));

        Element inputRadio = parentNode.getInputRadio();
        inputRadio.setAttribute("value",circle.getAttribute("r"));

        // Notify the client parent document
        StringBuilder code = new StringBuilder();
        code.append("var url = window.location.href;");
        code.append("var pos = url.indexOf('#');"); // #p=n,c=n
        code.append("url = url.substring(0,pos + 3) + 'y' + url.substring(pos + 4);");
        code.append("window.location.href = url;");
        itsNatDoc.addCodeToSend(code.toString());
    }
}
