/*
 * TestRemoteControlListener.java
 *
 * Created on 7 de noviembre de 2006, 17:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.shared;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletResponse;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatAttachedClientCometEvent;
import org.itsnat.core.event.ItsNatAttachedClientEvent;
import org.itsnat.core.event.ItsNatAttachedClientEventListener;
import org.itsnat.core.event.ItsNatAttachedClientTimerEvent;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestRemoteControlListener implements ItsNatAttachedClientEventListener
{
    protected boolean refreshMsg;

    /**
     * Creates a new instance of TestRemoteControlListener
     */
    public TestRemoteControlListener(boolean refreshMsg)
    {
        this.refreshMsg = refreshMsg;
    }

    public void handleEvent(ItsNatAttachedClientEvent event)
    {
        //ItsNatDocument itsNatDoc = event.getItsNatDocument();

        int phase = event.getPhase();

        ClientDocument observer = event.getClientDocument();
        ItsNatDocument itsNatDoc = event.getItsNatDocument();
        ServletResponse response = event.getItsNatServletResponse().getServletResponse();

        switch(phase)
        {
            case ItsNatAttachedClientEvent.REQUEST:
                if (event instanceof ItsNatAttachedClientTimerEvent)
                {
                    ItsNatAttachedClientTimerEvent timerEvent = (ItsNatAttachedClientTimerEvent)event;
                    boolean accepted = (timerEvent.getRefreshInterval() >= 1000);                
                    if (!accepted)
                    {
                        try
                        {
                            Writer out = response.getWriter();
                            out.write("<html><body><h1>Remote control request rejected. Interval too short: ");
                            out.write(Integer.toString(timerEvent.getRefreshInterval()));
                            out.write("</h1></body></html>");
                        }
                        catch(IOException ex) { throw new RuntimeException(ex); }
                    }
                    event.setAccepted(accepted);
                }
                else if (event instanceof ItsNatAttachedClientCometEvent)
                {
                    event.setAccepted(true); // Nothing to check
                }
                else // "None" refresh mode
                {
                    event.setAccepted(true); // Nothing to check
                }

                if (event.getWaitDocTimeout() > 30000) // Demasiado tiempo de espera
                    event.setAccepted(false);
                
                if (!event.isAccepted())
                    return;

                break;

            case ItsNatAttachedClientEvent.LOAD:
                 //event.setAccepted(false);
                break;
            case ItsNatAttachedClientEvent.REFRESH:
                if (itsNatDoc.isInvalid())
                {
                    observer.addCodeToSend("alert('Observed document was destroyed');");
                }
                else if (event instanceof ItsNatAttachedClientTimerEvent)
                {
                    // Esto no es lo normal (modificar a través de un visor el documento)
                    // pero es para probar.
                    // Lo normal es a partir del estado del DOM hacer algo en consecuencia
                    // junto con la sessión del "observado" que tenemos acceso a ella via ItsNatDocument
                    ItsNatAttachedClientTimerEvent timerEvent = (ItsNatAttachedClientTimerEvent)event;

                    long initTime = timerEvent.getItsNatServletRequest().getClientDocument().getCreationTime();
                    long currentTime = System.currentTimeMillis();
                    int limitMilisec = 15*60*1000; // 15 minutos, para evitar que esté indefinidamente
                    if (currentTime - initTime > limitMilisec)
                    {
                        event.setAccepted(false);
                        observer.addCodeToSend("alert('Remote Control Timeout');");
                    }
                    else if (refreshMsg)
                    {
                        Document doc = itsNatDoc.getDocument();
                        if (doc instanceof HTMLDocument) // En el ejemplo SVG no funciona obviamente
                        {
                            String phaseStr;
                            if (phase == ItsNatAttachedClientEvent.LOAD)
                                phaseStr = "load";
                            else // if (phase == ItsNatAttachedClientEvent.REFRESH)
                                phaseStr = "refresh";
                            ((HTMLDocument)doc).getBody().appendChild(doc.createTextNode("OK remote ctrl " + phaseStr + " "));
                        }
                    }
                }
                break;
            case ItsNatAttachedClientEvent.UNLOAD:
                // Nada que hacer
                break;
        }

        if (!event.isAccepted())
            event.getItsNatEventListenerChain().stop();
    }

}
