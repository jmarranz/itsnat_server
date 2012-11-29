/*
 * TestSVGDocLoadListener.java
 *
 * Created on 11 de febrero de 2008, 18:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.svg;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

/**
 *
 * @author jmarranz
 */
public class TestSVGDocLoadListener implements ItsNatServletRequestListener
{

    /** Creates a new instance of TestSVGDocLoadListener */
    public TestSVGDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();

        new TestSVGDocument(itsNatDoc,request,response);
    }
}
