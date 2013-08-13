
package inexp.groovyex;

import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;

class GroovyExampleLoadListener implements ItsNatServletRequestListener
{
    void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new inexp.groovyex.GroovyExampleDocument(request.getItsNatDocument());
    }
}
