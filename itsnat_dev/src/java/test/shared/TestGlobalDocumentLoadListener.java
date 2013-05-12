/*
 * TestGlobalDocumentLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.shared;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import test.stateless.TestGlobalStatelessDocumentLoadListener;

/**
 *
 * @author jmarranz
 */
public class TestGlobalDocumentLoadListener implements ItsNatServletRequestListener
{

    /**
     * Creates a new instance of TestGlobalDocumentLoadListener
     */
    public TestGlobalDocumentLoadListener()
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
                TestGlobalStatelessDocumentLoadListener.processRequest(request, response);            
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
                    // Pretty URL
                    HttpServletRequest servRequest = (HttpServletRequest)request.getServletRequest();
                    String pathInfo = servRequest.getPathInfo();
                    if (pathInfo == null)
                        throw new RuntimeException("Unexpected URL");

                    // Formato esperado de docName: "/docname"
                    docName = pathInfo.substring(1);
                    docName = docName.replace('/','_');
                    request.getServletRequest().setAttribute("itsnat_doc_name",docName);

                    ServletResponse servResponse = response.getServletResponse();
                    request.getItsNatServlet().processRequest(servRequest,servResponse);
                }
            }
        }
    }

}
