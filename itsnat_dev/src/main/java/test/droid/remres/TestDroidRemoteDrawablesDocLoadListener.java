/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.remres;


import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;


public class TestDroidRemoteDrawablesDocLoadListener implements ItsNatServletRequestListener
{
    @Override
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new TestDroidRemoteDrawablesDocument((ItsNatDocument)request.getItsNatDocument(),request);
    }
}
