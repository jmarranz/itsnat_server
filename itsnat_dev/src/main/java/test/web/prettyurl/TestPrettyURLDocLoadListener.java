/*
 * TestPrettyURLDocLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.prettyurl;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import test.web.shared.Shared;
import test.web.shared.TestSerialization;

/**
 *
 * @author jmarranz
 */
public class TestPrettyURLDocLoadListener implements ItsNatServletRequestListener
{

    /**
     * Creates a new instance of TestPrettyURLDocLoadListener
     */
    public TestPrettyURLDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();

        new TestPrettyURLDocument(itsNatDoc);

        Shared.setRemoteControlLink(request,response);

        new TestSerialization(request);
    }

}
