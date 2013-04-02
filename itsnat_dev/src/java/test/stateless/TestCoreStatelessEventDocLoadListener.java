/*
 * TestCoreDocLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.stateless;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestCoreStatelessEventDocLoadListener implements ItsNatServletRequestListener
{
    /**
     * Creates a new instance of TestCoreDocLoadListener
     */
    public TestCoreStatelessEventDocLoadListener()
    {
    }
   
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new TestCoreStatelessEventDocument((ItsNatHTMLDocument)request.getItsNatDocument(),request,response);
    }

}
