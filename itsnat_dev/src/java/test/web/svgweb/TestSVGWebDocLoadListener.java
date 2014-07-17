/*
 * TestSVGWebDocLoadListener.java
 *
 * Created on 11 de febrero de 2008, 18:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.svgweb;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

/**
 *
 * @author jmarranz
 */
public class TestSVGWebDocLoadListener implements ItsNatServletRequestListener
{

    /** Creates a new instance of TestSVGWebDocLoadListener */
    public TestSVGWebDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new TestSVGWebDocument(request,response);
    }
}
