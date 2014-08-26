/*
 * TestCoreDocLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.droid.stateless.core;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

/**
 *
 * @author jmarranz
 */
public class TestDroidStatelessCoreEventDocLoadListener implements ItsNatServletRequestListener
{
    /**
     * Creates a new instance of TestCoreDocLoadListener
     */
    public TestDroidStatelessCoreEventDocLoadListener()
    {
    }
   
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new TestDroidStatelessCoreEventDocument(request.getItsNatDocument(),request,response);
    }

}
