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

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequest;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatAttachedClientEvent;
import org.itsnat.core.event.ItsNatAttachedClientEventListener;

public class GlobalRemoteControlSupervision implements ItsNatAttachedClientEventListener
{
    public GlobalRemoteControlSupervision()
    {
    }

    public void handleEvent(ItsNatAttachedClientEvent event)
    {
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

        if (event.getPhase() == ItsNatAttachedClientEvent.REQUEST)
        {
            ItsNatServlet servlet = response.getItsNatServlet();
            ServletRequest servRequest = event.getItsNatServletRequest().getServletRequest();
            Map<String,String[]> newParams = new HashMap<String,String[]>(servRequest.getParameterMap());
            newParams.remove("itsnat_action");
            newParams.put("itsnat_doc_name",new String[]{"feashow.ext.core.misc.remCtrlDocNotFound"});
            servRequest = servlet.createServletRequest(servRequest, newParams);
            servlet.processRequest(servRequest,response.getServletResponse());
        }

        if (event.getPhase() == ItsNatAttachedClientEvent.REQUEST)
        {
            // ...
        }
        else // ItsNatAttachedClientEvent.REFRESH
        {
            response.addCodeToSend("if (confirm('Session is expired. Close Window?')) window.close();");
        }
    }
}
