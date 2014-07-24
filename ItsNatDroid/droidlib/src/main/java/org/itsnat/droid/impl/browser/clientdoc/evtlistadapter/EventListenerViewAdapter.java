package org.itsnat.droid.impl.browser.clientdoc.evtlistadapter;

import android.os.SystemClock;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.NormalEvent;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

import java.util.List;

/**
 * Created by jmarranz on 4/07/14.
 */
public abstract class EventListenerViewAdapter
{
    protected ItsNatViewImpl viewData;

    public EventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        this.viewData = viewData;
    }

    protected void dispatch(String type,InputEvent nativeEvt)
    {
        List<DroidEventListener> list = viewData.getEventListeners(type);
        if (list == null) return;

        View view = viewData.getView();
        for (DroidEventListener listener : list)
        {
            NormalEvent evtWrapper = listener.createEventWrapper(nativeEvt);
            try
            {
                listener.dispatchEvent(view, evtWrapper);
            }
            catch(Exception ex)
            {
                // Desde aquí capturamos todos los fallos del proceso de eventos, el código anterior a dispatch(String,InputEvent) nunca debería
                // fallar, o bien porque es muy simple o porque hay llamadas al código del usuario que él mismo puede controlar sus fallos
                OnEventErrorListener errorListener = viewData.getPageImpl().getOnEventErrorListener();
                if (errorListener != null)
                {
                    errorListener.onError(ex, evtWrapper);
                    continue;
                }
                else
                {
                    if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException) ex;
                    else throw new ItsNatDroidException(ex);
                }
            }
        }
    }

}
