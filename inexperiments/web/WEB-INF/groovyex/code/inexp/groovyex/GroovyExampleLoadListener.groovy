
package inexp.groovyex;

import org.itsnat.core.event.ItsNatServletRequestListener;
import inexp.jreloadex.FalseDB
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import inexp.groovyex.GroovyExampleDocument;

class GroovyExampleLoadListener implements ItsNatServletRequestListener
{
    def db

    GroovyExampleLoadListener() 
    { 
    }
    
    GroovyExampleLoadListener(FalseDB db) // Explicit type tells Groovy to reload FalseDB class when changed
    {
        this.db = db;
    }

    void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {       
        new GroovyExampleDocument(request.getItsNatDocument(),db);        
    }
}
