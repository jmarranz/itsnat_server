
package inexp.xpathex;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;

public class XPathExampleLoadListener implements ItsNatServletRequestListener
{
    public XPathExampleLoadListener() { }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new XPathExampleDocument((ItsNatHTMLDocument)request.getItsNatDocument());
    }
}
