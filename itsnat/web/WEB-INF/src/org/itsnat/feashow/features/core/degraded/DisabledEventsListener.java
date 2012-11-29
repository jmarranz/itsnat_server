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

package org.itsnat.feashow.features.core.degraded;


import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.http.ItsNatHttpSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLFormElement;

public class DisabledEventsListener
{
    protected ItsNatServletRequest itsNatRequest;
    protected ElementList list;

    public DisabledEventsListener(ItsNatServletRequest itsNatRequest)
    {
        this.itsNatRequest = itsNatRequest;

        try
        {
            String action = itsNatRequest.getServletRequest().getParameter("action");
            if (action == null)
                firstTime();
            else
                processAction(action);

            ItsNatDocument itsNatDoc = itsNatRequest.getItsNatDocument();
            Document doc = itsNatDoc.getDocument();
            HTMLFormElement formElem = (HTMLFormElement)doc.getElementById("formId");
            String code = "if (this.elements['message'].value == '') \n" +
                          "  { alert('Empty message'); return false; } \n" +
                          "else return true;";
            formElem.setAttribute("onsubmit",code);
        }
        finally
        {
            saveDocumentToSession();
        }
    }

    public void firstTime()
    {
        ItsNatDocument itsNatDoc = itsNatRequest.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        Element listParentElem = doc.getElementById("messageListId");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList list = factory.createElementList(listParentElem,true);

        list.addElement("I'm happy today");
        list.addElement("The sky is blue");
    }

    public void processAction(String action)
    {
        ItsNatDocument itsNatDoc = itsNatRequest.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        ItsNatDocument itsNatDocPrev = loadDocumentFromSession();
        if (itsNatDocPrev == null)
        {
            itsNatDoc.addCodeToSend("alert('Session lost');");
            return;
        }

        Element parentPrev = itsNatDocPrev.getDocument().getElementById("messageListParentId");
        Element tablePrev = ItsNatTreeWalker.getFirstChildElement(parentPrev);

        Element parent = doc.getElementById("messageListParentId");
        ItsNatDOMUtil.removeAllChildren(parent);
        Element table = (Element)doc.importNode(tablePrev,true);
        parent.appendChild(table);

        Element listParentElem = doc.getElementById("messageListId");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList list = factory.createElementList(listParentElem,false);

        ServletRequest request = itsNatRequest.getServletRequest();
        String msg = request.getParameter("message");

        list.addElement(msg);
    }

    public void saveDocumentToSession()
    {
        ItsNatDocument itsNatDoc = itsNatRequest.getItsNatDocument();
        ItsNatHttpSession itsNatSession = (ItsNatHttpSession)itsNatRequest.getItsNatSession();
        HttpSession session = itsNatSession.getHttpSession();
        session.setAttribute("previous_doc",itsNatDoc);
    }

    public ItsNatDocument loadDocumentFromSession()
    {
        ItsNatHttpSession itsNatSession = (ItsNatHttpSession)itsNatRequest.getItsNatSession();
        HttpSession session = itsNatSession.getHttpSession();
        ItsNatDocument itsNatDocPrev = (ItsNatDocument)session.getAttribute("previous_doc");
        session.removeAttribute("previous_doc"); // No longer available
        return itsNatDocPrev;
    }
}
