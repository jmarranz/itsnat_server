/*
 * TestReferrerPullDocLoadListener.java
 *
 * Created on 9 de agosto de 2007, 17:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.referrerpull;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import test.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestReferrerPullDocLoadListener implements ItsNatServletRequestListener,Serializable
{

    /**
     * Creates a new instance of TestReferrerPullDocLoadListener
     */
    public TestReferrerPullDocLoadListener()
    {

    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        processReferrer(request,response);

        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element outElem = doc.getElementById("outId");
        outElem.appendChild(doc.createTextNode(" 1 "));

        EventListener listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt) { }
        };
        Element checkElem = doc.getElementById("checkServerId");
        ((EventTarget)checkElem).addEventListener("click", listener, false);
    }

    public void processReferrer(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element outElem = doc.getElementById("outId");
        ItsNatDocument itsNatDocRef = request.getItsNatDocumentReferrer();
        if (itsNatDocRef == null)
        {
            String msg = "There is no referrer. Adding markup to transport";

            Element div = doc.createElement("div");
            Text text = doc.createTextNode(msg);
            div.appendChild(text);
            doc.getBody().appendChild(div);
            //outElem.appendChild(div);

            Element h = doc.createElement("h3");
            h.appendChild(doc.createTextNode("Markup Transported"));
            outElem.appendChild(h);

            Element link = doc.getElementById("testReloadId");
            link.setAttribute("style",""); // Visible
        }
        else
        {
            synchronized(itsNatDocRef)
            {
                Document docRef = itsNatDocRef.getDocument();

                String docNameRef = itsNatDocRef.getItsNatDocumentTemplate().getName();
                if (docNameRef.equals("test_referrer_pull") || // Reload
                         docNameRef.equals("test_referrer_pull_next")) // Back button
                {
                    Element outRefElem = docRef.getElementById("outId");
                    Node contentNode = ItsNatDOMUtil.extractChildren(outRefElem);
                    contentNode = doc.importNode(contentNode,true);
                    outElem.appendChild(contentNode);

                    if (docNameRef.equals("test_referrer_pull"))
                    {
                        // Reload
                        Element link1 = doc.getElementById("nextPageToTestBackButtonId");
                        link1.setAttribute("style",""); // Visible
                        Element link2 = doc.getElementById("closeButtonId");
                        link2.setAttribute("style",""); // Visible
                    }
                    else if (docNameRef.equals("test_referrer_pull_next"))
                    {
                        // Back button
                        Element link = doc.getElementById("closeButtonId");
                        link.setAttribute("style",""); // Visible
                    }
                }
            }
        }
    }
}
