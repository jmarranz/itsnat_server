/*
 * TestReferrerPushNextDocument.java
 *
 * Created on 9 de agosto de 2007, 17:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.referrerpush;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestReferrerPushNextDocument implements ItsNatServletRequestListener
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of TestReferrerPushNextDocument
     */
    public TestReferrerPushNextDocument(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = request.getItsNatDocument();
        load();
    }

    public void load()
    {
System.out.println("Load 2");
        Document doc = itsNatDoc.getDocument();
        Element refElem = doc.getElementById("outId");
        refElem.appendChild(doc.createTextNode(" 2 "));
        itsNatDoc.addReferrerItsNatServletRequestListener(this);

        EventListener listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt) { }
        };
        Element checkElem = doc.getElementById("checkServerId");
        ((EventTarget)checkElem).addEventListener("click", listener, false);
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        // Caso Reload y Back button
        ItsNatDocument itsNatDocTarget = request.getItsNatDocument();
        Document docTarget = itsNatDocTarget.getDocument();
        String targetName = itsNatDocTarget.getItsNatDocumentTemplate().getName();
System.out.println("desde 2 para " + targetName);

        Document doc = itsNatDoc.getDocument();
        Element refElem = doc.getElementById("outId");
        Node contentNode = ItsNatDOMUtil.extractChildren(refElem);
        contentNode = docTarget.importNode(contentNode,true);
        Element refTargetElem = docTarget.getElementById("outId");
        refTargetElem.appendChild(contentNode);

        if (targetName.equals("test_referrer_push_next"))
        {
            // Reload
            Element link = docTarget.getElementById("backButtonId");
            link.setAttribute("style",""); // Visible
        }
        else if (targetName.equals("test_referrer_push"))
        {
            // Back button
            Element link = docTarget.getElementById("nextPageToTestBackButtonId");
            link.setAttribute("style","display:none"); // Hidden

            link = docTarget.getElementById("closeButtonId");
            link.setAttribute("style",""); // Visible
        }
    }
}
