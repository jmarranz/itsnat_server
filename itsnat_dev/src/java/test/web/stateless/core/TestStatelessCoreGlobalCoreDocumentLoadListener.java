/*
 * TestGlobalDocumentLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.stateless.core;

import javax.servlet.ServletRequest;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;

/**
 *
 * @author jmarranz
 */
public class TestStatelessCoreGlobalCoreDocumentLoadListener 
{
    public static void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ServletRequest servReq = request.getServletRequest();        
        String docName = servReq.getParameter("itsnat_doc_name_second_opportunity");                
        if (docName != null)
        {                  
            servReq.setAttribute("itsnat_doc_name", docName);
        }        
    }

}
