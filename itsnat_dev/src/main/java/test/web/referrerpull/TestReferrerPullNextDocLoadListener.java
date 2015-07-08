/*
 * TestReferrerPullLoadListener.java
 *
 * Created on 9 de agosto de 2007, 17:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.referrerpull;

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
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestReferrerPullNextDocLoadListener implements ItsNatServletRequestListener
{

    /**
     * Creates a new instance of TestReferrerPullLoadListener
     */
    public TestReferrerPullNextDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        processReferrer(request,response);

        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element outElem = doc.getElementById("outId");
        outElem.appendChild(doc.createTextNode(" 2 "));

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
        Document doc = itsNatDoc.getDocument();
        Element outElem = doc.getElementById("outId");
        ItsNatDocument itsNatDocRef = request.getItsNatDocumentReferrer();
        if (itsNatDocRef == null)
        {
            String msg = "There is no referrer";

            Element div = doc.createElement("div");
            Text text = doc.createTextNode(msg);
            div.appendChild(text);
            outElem.appendChild(div);
        }
        else
        {
            synchronized(itsNatDocRef)
            {
                Document docRef = itsNatDocRef.getDocument();

                Element outRefElem = docRef.getElementById("outId");
                Node contentNode = ItsNatDOMUtil.extractChildren(outRefElem);
                contentNode = doc.importNode(contentNode,true);
                outElem.appendChild(contentNode);
            }
        }
    }

}
