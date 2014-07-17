/*
 * TestReferrerPushLoadListener.java
 *
 * Created on 9 de agosto de 2007, 17:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.referrerpush;

import java.io.Serializable;
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
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestReferrerPushDocument implements ItsNatServletRequestListener,Serializable
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of TestReferrerPushLoadListener
     */
    public TestReferrerPushDocument(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = request.getItsNatDocument();
        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        Element outElem = doc.getElementById("outId");
        if (!outElem.hasChildNodes()) // La primera vez o algo falla
        {
            Element h = doc.createElement("h3");
            h.appendChild(doc.createTextNode("Markup Transported"));
            outElem.appendChild(h);
        }
        outElem.appendChild(doc.createTextNode(" 1 "));
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
        ItsNatDocument itsNatDocTarget = request.getItsNatDocument();
        Document docTarget = itsNatDocTarget.getDocument();
        String targetName = itsNatDocTarget.getItsNatDocumentTemplate().getName();

        Document doc = itsNatDoc.getDocument();
        Element refElem = doc.getElementById("outId");
        Node contentNode = ItsNatDOMUtil.extractChildren(refElem);

        contentNode = docTarget.importNode(contentNode,true);
        Element outTargetElem = docTarget.getElementById("outId");
        outTargetElem.appendChild(contentNode);

        if (targetName.equals("test_referrer_push_next"))
        {
            Element link = docTarget.getElementById("testReloadId");
            link.setAttribute("style",""); // Visible
        }
        else if (targetName.equals("test_referrer_push"))
        {
            // Puede ser un reload antes de ir a la página "next" o bien
            // un "falso" reload (debido a window.location.reload(true)) provocado al volver
            // de la página next con un navegador con cacheado.
            Element link = docTarget.getElementById("closeButtonId");
            link.setAttribute("style",""); // Visible
        }
    }
}
