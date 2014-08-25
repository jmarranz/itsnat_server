/*
 * Shared.java
 *
 * Created on 7 de noviembre de 2006, 22:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.shared;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.CommMode;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.w3c.dom.Document;

/**
 *
 * @author jmarranz
 */
public class Shared
{

    /** Creates a new instance of Shared */
    public Shared()
    {
    }

    public static void setRemoteControlLink(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatHttpServletRequest httpRequest = (ItsNatHttpServletRequest)request;
        String servletURL = getServletURL(httpRequest);
        String sessionId = httpRequest.getItsNatHttpSession().getId();
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        String docId = itsNatDoc.getId();

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

        ServletRequest natRequest = request.getServletRequest();
        natRequest.setAttribute("servletURL",servletURL);
        natRequest.setAttribute("sessionId",sessionId);
        natRequest.setAttribute("docId",docId);
        natRequest.setAttribute("commModeRemCtrl",Integer.toString(commMode)); 
        natRequest.setAttribute("eventTimeout",Integer.toString(-1));
        natRequest.setAttribute("waitDocTimeout",Integer.toString(10000));

        // Sabemos que únicamente hay variables en un link pero lo hacemos para probar el proceso de todo el documento
        Document doc = itsNatDoc.getDocument();
        request.createItsNatVariableResolver().resolve(doc);
    }

    public static String getServletURL(ItsNatHttpServletRequest httpRequest)
    {
        HttpServletRequest request = httpRequest.getHttpServletRequest();
        return request.getRequestURL().toString();
    }
}
