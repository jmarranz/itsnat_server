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

package org.itsnat.feashow;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

public class GlobalItsNatServletRequestListener implements ItsNatServletRequestListener
{
    public GlobalItsNatServletRequestListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();

        if (itsNatDoc != null)
        {
            //String docName = itsNatDoc.getItsNatDocumentTemplate().getName();
            //System.out.println("Loading " + docName);
        }
        else
        {
            ServletRequest servReq = request.getServletRequest();
            String docName = servReq.getParameter("itsnat_doc_name");
            if (docName == null) docName = (String)servReq.getAttribute("itsnat_doc_name");
            if (docName == null)
            {
                HttpServletRequest servRequest = (HttpServletRequest)request.getServletRequest();
                String pathInfo = servRequest.getPathInfo();
                if (pathInfo == null)
                {
                    // Index page
                    servRequest.setAttribute("itsnat_doc_name","feashow.index");
                }
                else
                {
                    // Pretty URL case
                    docName = pathInfo.substring(1); // "/name/name" => "name/name"
                    docName = docName.replace('/','.');// "name.name"
                    servRequest.setAttribute("itsnat_doc_name",docName);
                }
                
                ServletResponse servResponse = response.getServletResponse();
                request.getItsNatServlet().processRequest(servRequest,servResponse);
            }
            else // Not found
            {
                ItsNatServlet servlet = response.getItsNatServlet();
                ServletRequest servRequest = request.getServletRequest();
                Map newParams = new HashMap(servRequest.getParameterMap());
                newParams.put("itsnat_doc_name",new String[]{"feashow.docNotFound"});
                servRequest = servlet.createServletRequest(servRequest, newParams);
                servlet.processRequest(servRequest,response.getServletResponse());
            }
        }
    }

}
