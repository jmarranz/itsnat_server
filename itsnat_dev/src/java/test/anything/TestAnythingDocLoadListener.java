/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.anything;


import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;


public class TestAnythingDocLoadListener implements ItsNatServletRequestListener
{
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new TestAnythingDocument((ItsNatHTMLDocument)request.getItsNatDocument(),request);
    }
}
