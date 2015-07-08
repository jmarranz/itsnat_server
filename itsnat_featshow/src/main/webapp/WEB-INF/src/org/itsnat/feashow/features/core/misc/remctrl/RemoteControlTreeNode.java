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
package org.itsnat.feashow.features.core.misc.remctrl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.CommMode;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.ItsNatSession;
import org.itsnat.core.ItsNatSessionCallback;
import org.itsnat.core.ItsNatVariableResolver;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.http.ItsNatHttpSession;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class RemoteControlTreeNode extends FeatureTreeNode implements EventListener
{
    protected ElementList sessionNodeList;
    protected Element refreshElem;

    public RemoteControlTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element otherSessionsElem = doc.getElementById("otherSessionsId");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        this.sessionNodeList = factory.createElementList(otherSessionsElem,true);

        this.refreshElem = doc.getElementById("refreshId");
        ((EventTarget)refreshElem).addEventListener("click",this,false);

        thisDocument();

        otherDocumentsAndSessions();
    }

    public void endExamplePanel()
    {
        ((EventTarget)refreshElem).removeEventListener("click",this,false);

        this.sessionNodeList = null;
        this.refreshElem = null;
    }

    public void thisDocument()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver();
        ClientDocument owner = itsNatDoc.getClientDocumentOwner();
        ItsNatHttpSession itsNatSession = (ItsNatHttpSession)owner.getItsNatSession();
        HttpSession session = itsNatSession.getHttpSession();
        session.setAttribute("sessionId",itsNatSession.getId());
        itsNatDoc.setAttribute("docId",itsNatDoc.getId());
        resolver.setLocalVariable("refreshInterval",new Integer(3000));
        resolver.setLocalVariable("commMode",new Integer(CommMode.XHR_ASYNC));

        Element links = doc.getElementById("remoteCtrlLinksId");
        resolver.resolve(links);
    }

    public void otherDocumentsAndSessions()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatDocumentTemplate thisDocTemplate = itsNatDoc.getItsNatDocumentTemplate();

        ItsNatServlet itsNatServlet = thisDocTemplate.getItsNatServlet();
        ItsNatServletContext appCtx = itsNatServlet.getItsNatServletConfig().getItsNatServletContext();

        final List<ItsNatSession> sessionList = new LinkedList<ItsNatSession>();
        ItsNatSessionCallback cb = new ItsNatSessionCallback()
        {
            public boolean handleSession(ItsNatSession session)
            {
                sessionList.add(session);
                return true; // continue
            }
        };
        appCtx.enumerateSessions(cb);

        ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver(true);
        resolver.setLocalVariable("refreshInterval",new Integer(3000));
        resolver.setLocalVariable("commMode",new Integer(CommMode.XHR_ASYNC));

        for(int i = 0; i < sessionList.size(); i++)
        {
            ItsNatHttpSession otherSession = (ItsNatHttpSession)sessionList.get(i);

            ItsNatDocument[] remDocs = otherSession.getItsNatDocuments();

            for(int j = 0; j < remDocs.length; j++)
            {
                ItsNatDocument currRemDoc = remDocs[j];
                if (itsNatDoc == currRemDoc) continue;

                // currRemDoc should be synchronized, but a dead lock could occur by other process doing the same (docs locked mutually, the parent doc is already locked)
                // there is no problem, the ItsNatDocument.getItsNatDocumentTemplate() is thread safe
                ItsNatDocumentTemplate docTemplate = currRemDoc.getItsNatDocumentTemplate();
                if (docTemplate != thisDocTemplate)
                    continue;

                String docId = currRemDoc.getId(); // No sync is needed
                Element sessionElem = (Element)sessionNodeList.addElement();

                long lastRequest = currRemDoc.getClientDocumentOwner().getLastRequestTime();

                ItsNatVariableResolver resolver2 = resolver.createItsNatVariableResolver();
                resolver2.setLocalVariable("sessionId",otherSession.getId());
                resolver2.setLocalVariable("docId",docId);
                resolver2.setLocalVariable("agentInfo",otherSession.getUserAgent());
                resolver2.setLocalVariable("lastRequest",new Date(lastRequest).toString());

                resolver2.resolve(sessionElem);
            }
        }
    }

    public void updateOtherDocumentsAndSessions()
    {
        sessionNodeList.removeAllElements();

        otherDocumentsAndSessions();
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == refreshElem)
        {
            updateOtherDocumentsAndSessions();
        }
    }

}
