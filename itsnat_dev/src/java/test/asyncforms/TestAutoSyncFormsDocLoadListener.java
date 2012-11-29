/*
 * TestCoreLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.asyncforms;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import test.shared.Shared;
import test.shared.TestSerialization;

/**
 *
 * @author jmarranz
 */
public class TestAutoSyncFormsDocLoadListener implements ItsNatServletRequestListener
{

    /**
     * Creates a new instance of TestCoreLoadListener
     */
    public TestAutoSyncFormsDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new TestAutoSyncFormsDocument((ItsNatHTMLDocument)request.getItsNatDocument(),request);

        Shared.setRemoteControlLink(request,response);

        new TestSerialization(request);
    }

}
