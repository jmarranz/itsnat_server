/*
 * TestNoScriptDocLoadListener.java
 *
 * Created on 9 de agosto de 2007, 17:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.noscript;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

/**
 *
 * @author jmarranz
 */
public class TestNoScriptDocLoadListener implements ItsNatServletRequestListener
{

    /**
     * Creates a new instance of TestNoScriptDocLoadListener
     */
    public TestNoScriptDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new TestNoScriptDocument(request);
    }
}
