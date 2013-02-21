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
 * (C) Innowhere Software Services S.L., Spanish company, year 2007
 */

package manual.core.misc.remctrl;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequest;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatAttachedClientCometEvent;
import org.itsnat.core.event.ItsNatAttachedClientEvent;
import org.itsnat.core.event.ItsNatAttachedClientEventListener;
import org.itsnat.core.event.ItsNatAttachedClientTimerEvent;


public class RemoteControlSupervision implements ItsNatAttachedClientEventListener
{
    public RemoteControlSupervision()
    {
    }

    public void handleEvent(ItsNatAttachedClientEvent event)
    {
        if (event.getItsNatDocument() != null)
            processWithDocument(event);
        else
            processNotDocument(event);
    }

    public void processWithDocument(ItsNatAttachedClientEvent event)
    {
        int phase = event.getPhase();
        if (phase == ItsNatAttachedClientEvent.REQUEST)
        {
            String[] msg = new String[1];
            if (event instanceof ItsNatAttachedClientTimerEvent)
            {
                ItsNatAttachedClientTimerEvent timerEvent = (ItsNatAttachedClientTimerEvent)event;
                boolean accepted = (timerEvent.getRefreshInterval() >= 3000);
                event.setAccepted(accepted);
                if (!accepted) msg[0] = "Refresh interval too short: " + timerEvent.getRefreshInterval();
            }
            else if (event instanceof ItsNatAttachedClientCometEvent)
            {
                event.setAccepted(true);
            }
            else // "none" refresh mode
            {
                event.setAccepted(true);
            }

            if (event.getWaitDocTimeout() > 30000)
            {
                event.setAccepted(false);
                msg[0] = "Too much time waiting for iframe/object/embed: " + event.getWaitDocTimeout();
            }

            ItsNatServletRequest request = event.getItsNatServletRequest();
            ServletRequest servRequest = request.getServletRequest();

            if (!event.isAccepted())
            {
                ItsNatServletResponse response = event.getItsNatServletResponse();
                ItsNatServlet servlet = response.getItsNatServlet();
                @SuppressWarnings("unchecked")
                Map<String,String[]> newParams = new HashMap<String,String[]>(servRequest.getParameterMap());
                newParams.remove("itsnat_action"); // Removes: itsnat_action=attach_doc
                newParams.put("itsnat_doc_name",new String[]{"feashow.ext.core.misc.remCtrlReqRejected"});
                newParams.put("reason", msg); // submitted as array
                servRequest = servlet.createServletRequest(servRequest, newParams);
                servlet.processRequest(servRequest,response.getServletResponse());
            }
        }
        else if (phase == ItsNatAttachedClientEvent.REFRESH)
        {
            ClientDocument observer = event.getClientDocument();
            if (observer.getItsNatDocument().isInvalid())
            {
                observer.addCodeToSend("alert('Observed document was destroyed');");
            }
            else
            {
                long initTime = observer.getCreationTime();
                long currentTime = System.currentTimeMillis();
                long limitMilisec = 15*60*1000;
                // 15 minutes (to avoid a long monitoring session)
                if (currentTime - initTime > limitMilisec)
                {
                    event.setAccepted(false);
                    observer.addCodeToSend("alert('Remote Control Timeout');\n");
                }
            }
        }
        // ItsNatAttachedClientEvent.LOAD & UNLOAD : nothing to do

        if (!event.isAccepted())
            event.getItsNatEventListenerChain().stop(); // Not really necessary
    }

    public void processNotDocument(ItsNatAttachedClientEvent event)
    {
        ItsNatServletResponse response = event.getItsNatServletResponse();
        if (event.getPhase() == ItsNatAttachedClientEvent.REQUEST)
        {
            ItsNatServlet servlet = response.getItsNatServlet();
            ServletRequest servRequest = event.getItsNatServletRequest().getServletRequest();
            @SuppressWarnings("unchecked")
            Map<String,String[]> newParams = new HashMap<String,String[]>(servRequest.getParameterMap());
            newParams.remove("itsnat_action"); // Removes: itsnat_action=attach_doc
            newParams.put("itsnat_doc_name",new String[]{"feashow.ext.core.misc.remCtrlDocNotFound"});
            servRequest = servlet.createServletRequest(servRequest, newParams);
            servlet.processRequest(servRequest,response.getServletResponse());
        }
        else // ItsNatAttachedClientEvent.REFRESH
        {
            response.addCodeToSend("if (confirm('Session is expired. Close Window?')) window.close();");
        }

        /*
        if (event.getPhase() == ItsNatAttachedClientEvent.REQUEST)
        {
            ServletRequest request =
			event.getItsNatServletRequest().getServletRequest();
            String sessionId = request.getParameter("itsnat_session_id");
            String docId = request.getParameter("itsnat_doc_id");
            try
            {
              Writer out = response.getServletResponse().getWriter();
              out.write("<html><body><h1>Session/document not found with id:");
              out.write(sessionId + "/" + docId);
              out.write("</h1></body></html>");
            }
            catch(IOException ex) { new RuntimeException(ex); }
        }
        */
    }
}
