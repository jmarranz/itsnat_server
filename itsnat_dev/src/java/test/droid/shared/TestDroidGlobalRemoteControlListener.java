/*
 * TestRemoteControlListener.java
 *
 * Created on 7 de noviembre de 2006, 17:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.droid.shared;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatAttachedClientEvent;
import org.itsnat.core.event.ItsNatAttachedClientEventListener;

/**
 *
 * @author jmarranz
 */
public class TestDroidGlobalRemoteControlListener implements ItsNatAttachedClientEventListener
{
    /**
     * Creates a new instance of TestRemoteControlListener
     */
    public TestDroidGlobalRemoteControlListener()
    {
    }

    public void handleEvent(ItsNatAttachedClientEvent event)
    {
        // Este sólo lo usamos para procesar casos de error
        if (event.getItsNatDocument() != null) return;

        ItsNatServletResponse response = event.getItsNatServletResponse();
        if (event.getPhase() == ItsNatAttachedClientEvent.REQUEST)
        {
            ServletRequest request = event.getItsNatServletRequest().getServletRequest();
            String sessionId = request.getParameter("itsnat_session_id");
            String docId = request.getParameter("itsnat_doc_id");
            try
            {
                Writer out = response.getServletResponse().getWriter();
                out.write("<html><body><h1>Session/document not found with id:");
                out.write(sessionId + "/" + docId);
                out.write("</h1></body></html>");
            }
            catch(IOException ex) { throw new RuntimeException(ex); }
        }
        else // ItsNatAttachedClientEvent.REFRESH
        {
           response.addCodeToSend("if (confirm('Session is expired or observed doc lost. Close?')) window.close();");
        }

        event.getItsNatEventListenerChain().stop();
    }
}
