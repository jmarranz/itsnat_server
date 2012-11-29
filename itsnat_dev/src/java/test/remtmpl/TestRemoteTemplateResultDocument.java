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

package test.remtmpl;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLImageElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class TestRemoteTemplateResultDocument implements Serializable
{
    protected ItsNatDocument itsNatDoc;
    
    public TestRemoteTemplateResultDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();

        HTMLInputElement inputSearch = null;
        NodeList list = doc.getElementsByTagName("input");
        int len = list.getLength();
        for(int i = 0; i < len; i++)
        {
            HTMLInputElement input = (HTMLInputElement)list.item(i);
            String type = input.getType();
            if ("text".equals(type)) { inputSearch = input; break; }
        }

        Element div = doc.createElement("div");
        div.setAttribute("style","font-size:25px;");
        div.appendChild(doc.createTextNode("End of demo. Yes I know, is a clone far of perfect :)"));
        inputSearch.getParentNode().insertBefore(div, inputSearch);

        list = doc.getElementsByTagName("a");
        len = list.getLength();
        for(int i = 0; i < len; i++)
        {
            HTMLAnchorElement elem = (HTMLAnchorElement)list.item(i);
            String href = elem.getHref();
            if (!href.startsWith("http:"))
                elem.setHref("http://www.google.com" + href);
        }

        list = doc.getElementsByTagName("img");
        len = list.getLength();
        for(int i = 0; i < len; i++)
        {
            HTMLImageElement elem = (HTMLImageElement)list.item(i);
            String src = elem.getSrc();
            if (!src.startsWith("http:"))
                elem.setSrc("http://www.google.com" + src);
        }
    }
}
