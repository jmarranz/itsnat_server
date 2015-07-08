
package org.itsnat.feashow.features.core.otherns;


import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

public class XULPureLoadListener implements ItsNatServletRequestListener
{
    public XULPureLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();

        new XULPureDocument(itsNatDoc);
    }

}
