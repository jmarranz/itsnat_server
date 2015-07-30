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

package test.web.remtmpl;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
        // This is the result, Google detects something is "weird":
        
        /*
        <html><head><meta http-equiv="content-type" content="text/html;charset=utf-8"><title>302 Moved</title></head><body>
        <h1>302 Moved</h1>
        The document has moved
        <a HREF="https://www.google.es/search?q=ItsNat&gws_rd=cr,ssl&ei=Cl66VaXSForvUNHeu-AE">here</a>.

        </body></html>        
        */
        
        Document doc = itsNatDoc.getDocument();

        Element body = (Element)doc.getElementsByTagName("body").item(0);        
        
        Element div = doc.createElement("div");
        div.setAttribute("style","font-size:20px; margin-top:20px");
        div.appendChild(doc.createTextNode("This is the answer of Google, really. Yes I know, is a clone of a search far of perfect, Google tries to make things hard :) "));
        body.appendChild(div);
        div = doc.createElement("div");
        div.setAttribute("style","font-size:20px; margin-top:20px");        
        div.appendChild(doc.createTextNode("End of ItsNat Remote Template demo."));        
        body.appendChild(div);         
    }
}
