/*
 * TestRemoteCtrlLauncherDocLoadListener.java
 *
 * Created on 8 de noviembre de 2006, 17:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.remotectrl;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.ItsNatSession;
import org.itsnat.core.ItsNatSessionCallback;
import org.itsnat.core.ItsNatVariableResolver;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import java.util.LinkedList;
import java.util.List;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.CommMode;
import org.itsnat.core.domutil.ElementGroupManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpSession;
import test.web.shared.Shared;

/**
 *
 * @author jmarranz
 */
public class TestRemoteCtrlLauncherDocLoadListener implements ItsNatServletRequestListener
{

    /** Creates a new instance of TestRemoteCtrlLauncherDocLoadListener */
    public TestRemoteCtrlLauncherDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatServletContext appCtx = request.getItsNatServlet().getItsNatServletConfig().getItsNatServletContext();

        final LinkedList<ItsNatSession> sessions = new LinkedList<ItsNatSession>();
        ItsNatSessionCallback cb = new ItsNatSessionCallback()
        {
            public boolean handleSession(ItsNatSession session)
            {
                sessions.add(session);
                return true;
            }
        };
        appCtx.enumerateSessions(cb);
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        ItsNatHttpServletRequest httpRequest = (ItsNatHttpServletRequest)request;

        String servletURL = Shared.getServletURL(httpRequest);

        int commMode;
        switch(itsNatDoc.getCommMode())
        {
            case CommMode.XHR_SYNC:
            case CommMode.XHR_ASYNC:
            case CommMode.XHR_ASYNC_HOLD: commMode = CommMode.XHR_ASYNC; break;
            case CommMode.SCRIPT:
            case CommMode.SCRIPT_HOLD: commMode = CommMode.SCRIPT; break;
            default: throw new RuntimeException("Unexpected Error");
        }
        
        request.getServletRequest().setAttribute("servletURL",servletURL);
        request.getServletRequest().setAttribute("commModeRemCtrl",Integer.toString(commMode));
        request.getServletRequest().setAttribute("eventTimeout","-1");
        request.getServletRequest().setAttribute("waitDocTimeout","10000");
        
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList sessionNodeList = factory.createElementList(doc.getElementById("sessions"),true);

        for(int i = 0; i < sessions.size(); i++)
        {
            Element sessionTitleElem = (Element)sessionNodeList.addElement();

            ItsNatHttpSession itsNatSession = (ItsNatHttpSession)sessions.get(i);
            String sessionId = itsNatSession.getId();

            ItsNatVariableResolver level1 = request.createItsNatVariableResolver();
            level1.setLocalVariable("sessionId",sessionId);
            level1.setLocalVariable("agentInfo",itsNatSession.getUserAgent());
            level1.resolve(sessionTitleElem);

            ItsNatDocument[] remDocs = itsNatSession.getItsNatDocuments();
            Element docsParent = ItsNatDOMUtil.getElementById("docs",sessionTitleElem); // Pues estará duplicado
            ElementList docNodeList = factory.createElementList(docsParent,false);

            if (remDocs.length > 0)
            {
                docNodeList.removeAllElements(); // Elimina el patrón y el "No Documents Loaded" iniciando la lista

                for(int j = 0; j < remDocs.length; j++)
                {
                    Element docInfoElem = (Element)docNodeList.addElement();

                    ItsNatDocument remDoc = remDocs[j];
                    ItsNatVariableResolver level2 = level1.createItsNatVariableResolver();
                    level2.setLocalVariable("docId",remDoc.getId());
                    level2.setLocalVariable("docName",remDoc.getItsNatDocumentTemplate().getName());
                    level2.resolve(docInfoElem);
                }
            }
            else
            {
                docNodeList.removeElementAt(0); // Elimina el patrón (el primero) pero no el "No Documents Loaded"
            }

        }

    }

}
