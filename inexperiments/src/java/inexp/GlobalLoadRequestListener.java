package inexp;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

public class GlobalLoadRequestListener implements ItsNatServletRequestListener
{
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        if (itsNatDoc == null)
        {
            // Requested with a custom URL, not ItsNat standard format,
            // for instance servlet without params or Google AJAX crawling.
            // Internal redirection specifying the target template page:
            ServletRequest servRequest = request.getServletRequest();
            servRequest.setAttribute("itsnat_doc_name","main");
            ServletResponse servResponse = response.getServletResponse();

            request.getItsNatServlet().processRequest(servRequest,servResponse);  
        }
    }
}
