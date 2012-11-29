
package inexp.hybridcs;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;

public class HybridCSLoadListener implements ItsNatServletRequestListener
{
    public HybridCSLoadListener() { }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new HybridCSDocument((ItsNatHTMLDocument)request.getItsNatDocument());
    }
}
