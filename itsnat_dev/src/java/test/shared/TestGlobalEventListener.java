/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.shared;

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatEventStateless;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class TestGlobalEventListener implements EventListener,Serializable
{
    protected Object parent;

    public TestGlobalEventListener(Object parent)
    {
        this.parent = parent;
    }

    public void handleEvent(Event evt)
    {
        ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
        if (itsNatEvt instanceof ItsNatEventStateless)
        {
            if (itsNatEvt.getItsNatDocument() == null)
            {
                ClientDocument clientDoc = itsNatEvt.getClientDocument();                
                //ServletRequest request = itsNatEvt.getItsNatServletRequest().getServletRequest();
                String docName = (String)itsNatEvt.getExtraParam("itsnat_doc_name");
                if (docName != null)
                    clientDoc.addCodeToSend("alert('Stateless event OK with not found itsnat_doc_name: " + docName + " and title " + itsNatEvt.getExtraParam("title") + "');"); 
                else
                    clientDoc.addCodeToSend("alert('Custom stateless event OK and title " + itsNatEvt.getExtraParam("title") + "');"); 
            }
        }
        else
        {
            if (itsNatEvt.getItsNatDocument() == null)
            {
                // Hemos perdido la sesi�n o la p�gina
                // El ClientDocument es especial para el caso de documento no encontrado
                // a�n as� algunas cosas funcionan.
                ClientDocument clientDoc = itsNatEvt.getClientDocument();
                clientDoc.addCodeToSend("if (confirm('Session or page is lost. Reloading...'))");
                clientDoc.addCodeToSend("  window.location.reload(true);");

                itsNatEvt.getItsNatEventListenerChain().stop();
            }
        }
    }

}
