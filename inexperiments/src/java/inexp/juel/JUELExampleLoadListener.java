
package inexp.juel;

import javax.el.ExpressionFactory;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;

public class JUELExampleLoadListener implements ItsNatServletRequestListener
{
    protected ExpressionFactory factory;
    
    public JUELExampleLoadListener() 
    {
        // Note: JUEL is based on EL 2.2 able to call arbitrary methods but Tomcat 6 uses 2.1 with missing some public EL methods used by JUEL. 
        // When used Tomcat 6 do not use JSP, by this way EL classes are not loaded, otherwise classload incompatibilities happen or JUEL fails when calling arbitrary methods
        // because the missing public API methods.
        // http://stackoverflow.com/questions/7793069/java-lang-nosuchmethoderror-javax-el-elresolver-invokeljavax-el-elcontextljav

        // ExpressionFactory is mutltithread
        this.factory = new de.odysseus.el.ExpressionFactoryImpl();  // javax.el.methodInvocations is enabled by default
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new JUELExampleDocument((ItsNatHTMLDocument)request.getItsNatDocument(),factory);
    }
}
