package org.itsnat.droid.impl.browser.clientdoc;

import android.os.StrictMode;

import org.itsnat.droid.ItsNatDroidException;
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

    public ItsNatDocImpl getItsNatDocImpl()
    {
        return parent;
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
        //String method = "POST";
        String servletPath = parent.getServletPath();
        int commMode = evt.getEventGenericListener().getCommMode();
        StringBuilder paramURL = evt.genParamURL();

        if ((commMode == CommMode.SCRIPT) || (commMode == CommMode.SCRIPT_HOLD)) throw new ItsNatDroidException("SCRIPT and SCRIPT_HOLD communication modes are not supported");

        if (true)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            EventSender sender = new EventSender(this);
            sender.requestSyncText(servletPath, paramURL.toString());
        }

        /*
            EventSender sender = new EventSender(this);
            if (commMode == CommMode.XHR_SYNC) // XHR_SYNC
            {
                sender.requestSyncText(servletPath, paramURL.toString());
                sender.processResult(evt, false);
            } else // XHR_ASYNC y XHR_ASYNC_HOLD
            {
                if (commMode == CommMode.XHR_ASYNC_HOLD) this.holdEvt = evt;

                long timeout = evt.getEventGenericListener().getTimeout();
                sender.requestAsyncText(method, servletPath, paramURL, evt, timeout);
            }
        */
    }
}
