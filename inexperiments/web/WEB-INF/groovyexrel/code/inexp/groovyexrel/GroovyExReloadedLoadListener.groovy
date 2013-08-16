
package inexp.groovyexrel;

import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;

class GroovyExReloadedLoadListener implements ItsNatServletRequestListener
{
long i = 15;

    void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    { 


System.out.println("GroovyExReloadedLoadListener " + i);
        new inexp.groovyexrel.GroovyExReloadedDocument(request.getItsNatDocument());
    }
}
