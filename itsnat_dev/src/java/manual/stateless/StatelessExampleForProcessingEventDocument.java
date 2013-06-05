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

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatEventDOMStateless;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.core.tmpl.ItsNatHTMLDocFragmentTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author jmarranz
 */
public class StatelessExampleForProcessingEventDocument implements Serializable,EventListener
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element counterElem;
    
    public StatelessExampleForProcessingEventDocument(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = itsNatDoc;
        
        if (!itsNatDoc.isCreatedByStatelessEvent())
            throw new RuntimeException("Only to test stateless, must be loaded by a stateless event");

        // Counter node with same value (state) than in client:
        String currCountStr = request.getServletRequest().getParameter("counter");
        int counter = Integer.parseInt(currCountStr);
        
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        this.counterElem = doc.getElementById("counterId");
        ((Text)counterElem.getFirstChild()).setData(String.valueOf(counter));
        
        itsNatDoc.addEventListener(this);
    }

    public void handleEvent(Event evt)
    {
        ItsNatEventDOMStateless itsNatEvt = (ItsNatEventDOMStateless)evt;
        
        Text counterText = (Text)counterElem.getFirstChild();
        String currCountStr = counterText.getData();
        int counter = Integer.parseInt(currCountStr);
        counter++;
        counterText.setData(String.valueOf(counter));        
        
        Document doc = itsNatDoc.getDocument();        
        
        Element elemParent = doc.getElementById("insertHereId");
        ScriptUtil scriptGen = itsNatDoc.getScriptUtil();
        String elemRef = scriptGen.getNodeReference(elemParent);
        ClientDocument clientDoc = itsNatEvt.getClientDocument();
        clientDoc.addCodeToSend(elemRef + ".innerHTML = '';");        
        clientDoc.addCodeToSend("alert('Currently inserted fragment removed before');");        
                
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();  
        ItsNatHTMLDocFragmentTemplate docFragTemplate = (ItsNatHTMLDocFragmentTemplate)servlet.getItsNatDocFragmentTemplate("manual.stless.example.fragment");  

        DocumentFragment docFrag = docFragTemplate.loadDocumentFragmentBody(itsNatDoc);  
  
        elemParent.appendChild(docFrag); // docFrag is empty now  
        
        // Umm we have to celebrate/highlight this insertion 
        Element child1 = ItsNatTreeWalker.getFirstChildElement(elemParent);
        Element child2 = ItsNatTreeWalker.getNextElement(child1);
        Text textChild2 = (Text)child2.getFirstChild();
        Element bold = doc.createElement("i");
        bold.appendChild(textChild2); // is removed from child2
        child2.appendChild(bold);  
        child2.setAttribute("style","color:red"); // <h3 style="color:red"><i>Inserted!</i></h3>  
    }    
}
