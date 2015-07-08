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

package manual.core.misc.remtmpl;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLImageElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class RemoteTemplateDocument implements EventListener
{
    protected ItsNatDocument itsNatDoc;
    protected HTMLAnchorElement link;
    protected HTMLInputElement inputSearch;

    public RemoteTemplateDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();

        NodeList list = doc.getElementsByTagName("img");
        int len = list.getLength();
        for(int i = 0; i < len; i++)
        {
            HTMLImageElement img = (HTMLImageElement)list.item(i);
            img.setSrc("http://www.google.com/" + img.getSrc());
        }

        Element form = (Element)doc.getElementsByTagName("form").item(0);
        form.setAttribute("action","");
        String onsubmit = "var elem = document.getElementById('q'); " +
                "if (elem.value == '') { alert('Empty? Sure?'); return false; }" +
                "else { alert('No, you are not going to the real Google :)'); return true; }";
        form.setAttribute("onsubmit",onsubmit);

        list = doc.getElementsByTagName("input");
        len = list.getLength();
        for(int i = 0; i < len; i++)
        {
            HTMLInputElement input = (HTMLInputElement)list.item(i);
            String name = input.getName();
            if ("q".equals(name))
            {
                input.setAttribute("id","q");
                inputSearch = input;
            }
            else
                input.removeAttribute("name"); // makes it useless
        }

        HTMLInputElement template = (HTMLInputElement)doc.createElement("input");
        template.setAttribute("type","hidden");
        template.setName("itsnat_doc_name");
        template.setValue("remoteTemplateExampleResult");
        form.appendChild(template);

        list = doc.getElementsByTagName("a");
        len = list.getLength();
        for(int i = 0; i < len; i++)
        {
            HTMLAnchorElement elem = (HTMLAnchorElement)list.item(i);
            elem.setHref("javascript:alert('Hey!! This is not interesting! :)');");
        }

        link = (HTMLAnchorElement)doc.createElement("a");
        link.setHref("javascript:;");
        link.setAttribute("style","font-size:25px;");
        link.appendChild(doc.createTextNode("ItsNat says Hello to Google... CLICK ME!"));
        form.getParentNode().insertBefore(link, form);

        ((EventTarget)link).addEventListener("click", this,false);
    }

    public void handleEvent(Event evt)
    {
        Document doc = itsNatDoc.getDocument();

        inputSearch.setValue("ItsNat");

        ((EventTarget)link).removeEventListener("click", this,false);
        Element elem = doc.createElement("div");
        elem.setAttribute("style","font-size:25px;");
        link.getParentNode().insertBefore(elem, link);
        link.getParentNode().removeChild(link);
        ((Text)link.getFirstChild()).setData("Have a Good Search!!");
        elem.appendChild(link.getFirstChild());
    }
}
