package org.itsnat.droid.impl.browser.clientdoc;

import android.os.StrictMode;

import org.apache.http.NameValuePair;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.impl.browser.clientdoc.event.EventGeneric;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventGenericListener;

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

        EventGenericListener evtListener = evt.getEventGenericListener();
        String servletPath = parent.getServletPath();
        int commMode = evtListener.getCommMode();
        long timeout = evtListener.getTimeout();
        List<NameValuePair> params = evt.genParamURL();

        if ((commMode == CommMode.SCRIPT) || (commMode == CommMode.SCRIPT_HOLD)) throw new ItsNatDroidException("SCRIPT and SCRIPT_HOLD communication modes are not supported");

if (true)
{
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

    EventSender sender = new EventSender(this);
    String response = sender.requestSyncText(evt,servletPath, params, timeout);
    return;
}

        if (commMode == CommMode.XHR_SYNC)
        {
            EventSender sender = new EventSender(this);
            String response = sender.requestSyncText(evt,servletPath, params, timeout);
            //processResult(evt,false,response);


        }
        else // XHR_ASYNC y XHR_ASYNC_HOLD
        {
            if (commMode == CommMode.XHR_ASYNC_HOLD) this.holdEvt = evt;

            //sender.requestAsyncText(method, servletPath, paramURL, evt, timeout);
        }

    }



/*
    private void processResult(EventGeneric evt,boolean async,String response)
    {
        listener.processRespBegin();

        if (typeof status == "number") // Es undefined ocasionalmente en S60WebKit por ejemplo al enviar el unload
        {
            if (status == 200) listener.processRespValid(this.xhr.responseText); // "OK"
            else if (status != 0)
            {
                // Normalmente: status == 500 => Error interno del servidor, el servidor ha lanzado una excepcion
                // "responseText" contiene el texto de la excepcion del servidor (en Opera esta vacio), xhr.statusText nos da apenas la frase "Error Interno del Servidor"
                var errMsg = "status: " + status + "\n" + this.xhr.statusText + "\n\n" + this.xhr.responseText;
                listener.processRespError(errMsg);
            }
        }

        listener.processRespEnd(async);
    }
    */
}
