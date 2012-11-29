/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.waiaria;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class WAIARIALoadListener implements ItsNatServletRequestListener
{
    public WAIARIALoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new WAIARIADocument((ItsNatHTMLDocument)request.getItsNatDocument());
    }

}
