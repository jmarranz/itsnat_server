/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.remres;


import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;


public class TestDroidRemoteAnimationsDocLoadListener implements ItsNatServletRequestListener
{
    @Override
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new TestDroidRemoteAnimationsDocument((ItsNatDocument)request.getItsNatDocument(),request);
    }
}
