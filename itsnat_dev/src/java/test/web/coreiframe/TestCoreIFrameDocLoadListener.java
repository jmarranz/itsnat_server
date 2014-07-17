/*
 * TestCoreLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.coreiframe;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestCoreIFrameDocLoadListener implements ItsNatServletRequestListener
{

    /**
     * Creates a new instance of TestCoreLoadListener
     */
    public TestCoreIFrameDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new TestCoreIFrameDocument((ItsNatHTMLDocument)request.getItsNatDocument(),request,response);
    }

}
