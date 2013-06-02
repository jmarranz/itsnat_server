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

package manual.stateless;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletRequest;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;


/**
 *
 * @author jmarranz
 */
public class StatelessGlobalDocumentLoadListener implements ItsNatServletRequestListener
{
    public StatelessGlobalDocumentLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();

        if (itsNatDoc != null)
        {
            String docName = itsNatDoc.getItsNatDocumentTemplate().getName();
            System.out.println("Loading " + docName);
        }
        else
        {
            ServletRequest servReq = request.getServletRequest();
            String itsNatAction = servReq.getParameter("itsnat_action");  
            if ("event_stateless".equals(itsNatAction))
            {      
                String docName = servReq.getParameter("itsnat_doc_name_second_opportunity");                
                if (docName != null)    
                    servReq.setAttribute("itsnat_doc_name", docName);  // Second opportunity
            }
            else
            {
                String docName = servReq.getParameter("itsnat_doc_name");
                if (docName == null) docName = (String)servReq.getAttribute("itsnat_doc_name");
                if (docName != null)
                {
                    System.out.println("Page not found " + docName);
                    try
                    {
                        Writer out = response.getServletResponse().getWriter();
                        out.write("<html><body><h1>Page not found: \"" + docName + "\"");
                        out.write("</h1></body></html>");
                    }
                    catch(IOException ex) { throw new RuntimeException(ex); }
                }
                else
                {
                    throw new RuntimeException("Unexpected");
                }
            }
        }
    }

}
