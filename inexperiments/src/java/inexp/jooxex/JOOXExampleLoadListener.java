
package inexp.jooxex;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;

public class JOOXExampleLoadListener implements ItsNatServletRequestListener
{
    public JOOXExampleLoadListener() { }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new JOOXExampleDocument((ItsNatHTMLDocument)request.getItsNatDocument());
    }
}
