package org.itsnat.droid.impl.browser.clientdoc;

import org.itsnat.droid.impl.browser.clientdoc.event.EventGeneric;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jmarranz on 8/07/14.
 */
public class EventManager
{
    protected ItsNatDocImpl parent;
    protected EventGeneric holdEvt = null;
    protected List<EventGeneric> queue = new LinkedList<EventGeneric>();

    public EventManager(ItsNatDocImpl parent)
    {
        this.parent = parent;
    }

    private void processEvents(boolean notHold)
    {
        this.holdEvt = null;
        while(!queue.isEmpty())
        {
            EventGeneric evt = queue.remove(0);
            sendEventEffective(evt);
            if (notHold) continue;
            if (holdEvt != null) break; // El evento enviado ordena bloquear
        }
        if (notHold) this.holdEvt = null;
    }


    public void sendEvent(EventGeneric evt)
    {
        if (parent.isDisabledEvents()) return;
        if (evt.isIgnoreHold())
        {
            this.processEvents(true); // liberamos la cola, recordar que es monohilo
            this.sendEventEffective(evt);
        }
        else if (holdEvt != null)
        {
            evt.saveEvent();
            queue.add(evt);
        }
        else this.sendEventEffective(evt);
    }

    private void sendEventEffective(EventGeneric evt)
    {
        /*
        if (parent.isDisabledEvents()) return; // pudo ser definido desde el servidor en el anterior evento

        List<GlobalEventListener> globalListeners = parent.globalEventListeners;
        if (globalListeners != null && !globalListeners.isEmpty())
        {
            GlobalEventListener[] array = globalListeners.toArray(new GlobalEventListener[0]); // asi permitimos que se añadan mientras se procesan
            int len = array.length;
            for(int i = 0; i < len; i++)
            {
                GlobalEventListener listener = array[i];
                boolean res = listener.process(evt);
                if (!res) return; // no enviar
            }
        }

        parent.fireEventMonitors(true,false,evt);
        String method = "POST";
        String servletPath = parent.getServletPath();
        int commMode = evt.getEventGenericListener().getCommMode();
        String paramURL = evt.genParamURL();


        if (commMode == 1) // XHR_SYNC
        {
            var ajax = new itsnat.AJAX(this.itsNatDoc,win);
            ajax.requestSyncText(method,servletPath,paramURL);
            ajax.processResult(evt,false);
        }
        else
        {
            if ((commMode == 3)||(commMode == 5)) this.holdEvt = evt; // XHR_ASYNC_HOLD y SCRIPT_HOLD

            var timeout = evt.getListenerWrapper().getTimeout();
            if ((commMode == 2)||(commMode == 3)) // XHR_ASYNC y XHR_ASYNC_HOLD
            {
                var ajax = new itsnat.AJAX(this.itsNatDoc,win);
                ajax.requestAsyncText(method,servletPath,paramURL,evt,timeout);
            }
            else // SCRIPT (4) y SCRIPT_HOLD (5)
            {
                this.itsNatDoc.sendEventByScript(servletPath,paramURL,evt,timeout);
            }
        }
*/
    }
}
