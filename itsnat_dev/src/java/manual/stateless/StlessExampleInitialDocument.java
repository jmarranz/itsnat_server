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
package manual.stateless;


import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLDocument;

public class StlessExampleInitialDocument 
{
    protected ItsNatHTMLDocument itsNatDoc;

    public StlessExampleInitialDocument(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        
        // This is just an excuse to show how the initial page is a conventional ItsNat page
        // (but stateless)
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        Text node = (Text)doc.createTextNode("This the initial stateless page (not kept in server)");
        Element presentationElem = doc.getElementById("presentationId");
        presentationElem.appendChild(node);       
    }


}
