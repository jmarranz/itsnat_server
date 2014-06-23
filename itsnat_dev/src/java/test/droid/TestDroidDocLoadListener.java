/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid;


import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;


public class TestDroidDocLoadListener implements ItsNatServletRequestListener
{
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new TestDroidDocument((ItsNatDocument)request.getItsNatDocument(),request);
    }
}
