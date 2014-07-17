/*
 * DocumentLoadListenerTest.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.clientmut;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

/**
 *
 * @author jmarranz
 */
public class TestClientMutationDocLoadListener implements ItsNatServletRequestListener
{

    /**
     * Creates a new instance of DocumentLoadListenerTest
     */
    public TestClientMutationDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        new TestClientMutationDocument(itsNatDoc,request,response);
    }

}
