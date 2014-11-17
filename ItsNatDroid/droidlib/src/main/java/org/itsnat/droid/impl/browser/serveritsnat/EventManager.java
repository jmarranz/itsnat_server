package org.itsnat.droid.impl.browser.serveritsnat;

import org.apache.http.NameValuePair;
import org.itsnat.droid.CommMode;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.serveritsnat.event.EventGenericImpl;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.EventGenericListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jmarranz on 8/07/14.
 */
public class EventManager
{
    protected ItsNatDocImpl itsNatDoc;
    protected EventGenericImpl holdEvt = null;
    protected List<EventGenericImpl> queue = new LinkedList<EventGenericImpl>();

    public EventManager(ItsNatDocImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public ItsNatDocImpl getItsNatDocImpl()
    {
        return itsNatDoc;
    }

    public void returnedEvent(EventGenericImpl evt)
    {
        evt.onEventReturned();
        if (this.holdEvt == evt) processEvents(false);
    }

    private void processEvents(boolean notHold)
    {
        this.holdEvt = null;
        while (!queue.isEmpty())
        {
            EventGenericImpl evt = queue.remove(0);
            sendEventEffective(evt);
            if (notHold) continue;
            if (holdEvt != null) break; // El evento enviado ordena bloquear
        }
        if (notHold) this.holdEvt = null;
    }


    public void sendEvent(EventGenericImpl evt)
    {
        if (itsNatDoc.isDisabledEvents()) return;
        if (evt.isIgnoreHold())
        {
            processEvents(true); // liberamos la cola, recordar que es monohilo
            sendEventEffective(evt);
        }
        else if (holdEvt != null)
        {
            evt.saveEvent();
            queue.add(evt);
        }
        else sendEventEffective(evt);
    }

    private void sendEventEffective(EventGenericImpl evt)
    {
        if (itsNatDoc.isDisabledEvents()) return; // pudo ser definido desde el servidor en el anterior evento

        List<GlobalEventListener> globalListeners = itsNatDoc.globalEventListeners;
        if (globalListeners != null && !globalListeners.isEmpty())
        {
            GlobalEventListener[] array = globalListeners.toArray(new GlobalEventListener[0]); // asi permitimos que se añadan mientras se procesan
            int len = array.length;
            for (int i = 0; i < len; i++)
            {
                GlobalEventListener listener = array[i];
                boolean res = listener.process(evt);
                if (!res) return; // no enviar
            }
        }

        itsNatDoc.fireEventMonitors(true, false, evt);

        EventGenericListener evtListener = evt.getEventGenericListener();
        String servletPath = itsNatDoc.getItsNatServletPath();
        int commMode = evtListener.getCommMode();
        long timeout = evtListener.getTimeout();
        List<NameValuePair> params = evt.genParamURL();


        if (commMode == CommMode.XHR_SYNC)
        {
            EventSender sender = new EventSender(this);
            sender.requestSync(evt, servletPath, params, timeout);
        }
        else if (commMode == CommMode.XHR_ASYNC || commMode == CommMode.XHR_ASYNC_HOLD)
        {
            if (commMode == CommMode.XHR_ASYNC_HOLD)
                this.holdEvt = evt;

            EventSender sender = new EventSender(this);
            sender.requestAsync(evt, servletPath, params, timeout);
        }
        else  // if ((commMode == 4) /*CommMode.SCRIPT*/ || (commMode == 5) /*CommMode.SCRIPT_HOLD*/)
            throw new ItsNatDroidException("SCRIPT and SCRIPT_HOLD communication modes are not supported");

    }

}
