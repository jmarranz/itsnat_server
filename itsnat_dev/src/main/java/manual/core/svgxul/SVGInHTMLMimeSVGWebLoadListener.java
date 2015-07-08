
package manual.core.svgxul;


import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

public class SVGInHTMLMimeSVGWebLoadListener implements ItsNatServletRequestListener
{
    public SVGInHTMLMimeSVGWebLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        new SVGInHTMLMimeSVGWebDocument(itsNatDoc);
    }
}
