/*
 * CoreExampleLoadListener.java
 *
 * Created on 2 de mayo de 2007, 16:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comptut;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;

public class CompExampleLoadListener implements ItsNatServletRequestListener
{
    public CompExampleLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();
        new CompExampleDocument(itsNatDoc);
    }
}
