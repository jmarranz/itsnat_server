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
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequest;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.event.ItsNatAttachedClientCometEvent;
import org.itsnat.core.event.ItsNatAttachedClientEvent;
import org.itsnat.core.event.ItsNatAttachedClientEventListener;
import org.itsnat.core.event.ItsNatAttachedClientTimerEvent;
import org.itsnat.core.event.ItsNatTimerEvent;
import org.itsnat.core.event.ItsNatTimerHandle;
import org.itsnat.feashow.BrowserUtil;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

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
            if (event.isAccepted()) // by now
            {
                boolean askUser = Boolean.valueOf(servRequest.getParameter("ask_user")).booleanValue();
                if (askUser)
                {
                    boolean accepted = askToUser(event,msg);
                    event.setAccepted(accepted);
                    if (accepted && !event.isReadOnly())
                    {
                        ItsNatDocument itsNatDoc = event.getItsNatDocument();
                        final ClientDocument clientOwner = itsNatDoc.getClientDocumentOwner();
                        StringBuilder note = new StringBuilder();
                        note.append("alert(\"Dear user, you have accepted that another user can modify the document in server ");
                        note.append("in the same way you can. To help you almost instantaneously see ");
                        note.append("what the other user is doing, a timer has been added to update your page ");
                        note.append("each 3 seconds for 30 minutes\");");
                        clientOwner.addCodeToSend(note);
                        
                        EventListener listener = new EventListener()
                        {
                            public void handleEvent(Event evt)
                            {
                                ItsNatTimerEvent timerEvt = (ItsNatTimerEvent)evt;
                                ItsNatTimerHandle handle = timerEvt.getItsNatTimerHandle();
                                long firstTime = handle.getFirstTime();
                                if ((new Date().getTime() - firstTime) > 30*60*1000) // 30 minutes
                                {
                                    handle.cancel();
                                    clientOwner.addCodeToSend("The timer for updating the client has finished");
                                }
                            }
                        };
                        ItsNatTimer timer = clientOwner.createItsNatTimer();
                        timer.schedule(null,listener,1000,3000);
                    }
                }
            }

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


    public boolean askToUser(ItsNatAttachedClientEvent event,String[] msg)
    {
        final boolean[] ready = new boolean[] { false };
        final boolean[] answer = new boolean[] { false };

        ClientDocument observer = event.getClientDocument();
        ItsNatDocument itsNatDoc = observer.getItsNatDocument();

        RemoteControlUserRequestEventListener listener =
                new RemoteControlUserRequestEventListener(event.isReadOnly(),ready,answer);

        synchronized(itsNatDoc)
        {
            itsNatDoc.addEventListener(listener);
        }

        synchronized(ready)
        {
            int timeout = 5000 + RemoteControlTimerMgrGlobalEventListener.PERIOD;
            try { ready.wait(timeout); }
            catch(InterruptedException ex) { throw new RuntimeException(ex); }
        }

        synchronized(itsNatDoc)
        {
            // If code to ask user was not sent to the user
            // then is not sent.
            itsNatDoc.removeEventListener(listener);
        }

        boolean accepted = answer[0];
        if (!accepted)
        {
            if (itsNatDoc.isInvalid())
                msg[0] = "The user closed the page before answering.";
            else if (ready[0])
                msg[0] = "The user rejected monitoring.";
            else
                msg[0] = "Timeout (no user answer).";
        }

        return accepted;
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
    }
}
