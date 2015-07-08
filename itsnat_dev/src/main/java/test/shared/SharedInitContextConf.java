/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.shared;

import java.util.Date;
import javax.servlet.ServletContext;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.http.ItsNatHttpServlet;

/**
 *
 * @author jmarranz
 */
public class SharedInitContextConf
{

    public static void init(ServletContext context,ItsNatHttpServlet itsNatServlet)
    {
        int maxOpenDocumentsBySession = 6; // Hay que tener en cuenta los ejemplos con iframes
        boolean sessionReplicationGAE = true; // En GAE
        boolean sessionReplicationNotGAE = false; // No GAE
        boolean sessionSerializeCompressed = false;
        boolean sessionExplicitSerialize = false;
        //long sessionExplicitSerializeFragmentSize = 0; /*100*1024;*/ // 0 = un sólo fragmento, X = número de bytes fragmento, setSessionReplicationXXX(boolean) debe estar a true también


        ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();

        ItsNatServletContext itsNatCtx = itsNatConfig.getItsNatServletContext();
        itsNatCtx.setMaxOpenDocumentsBySession(maxOpenDocumentsBySession);
        // http://radomirmladenovic.info/2009/06/15/detecting-code-execution-on-google-app-engine
        boolean gae = context.getServerInfo().startsWith("Google App Engine");
        itsNatCtx.setSessionReplicationCapable(gae ? sessionReplicationGAE : sessionReplicationNotGAE);
        if (gae) System.out.println("Session Replication: " + itsNatCtx.isSessionReplicationCapable());

        itsNatCtx.setSessionSerializeCompressed(sessionSerializeCompressed);
        itsNatCtx.setSessionExplicitSerialize(sessionExplicitSerialize);
        // itsNatCtx.setSessionExplicitSerializeFragmentSize(sessionExplicitSerializeFragmentSize);
    }

}
