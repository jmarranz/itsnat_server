package org.itsnat.droid.impl.browser.clientdoc.event;

import android.os.Parcel;
import android.view.InputEvent;

import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventStfulListener;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class DOMStdEvent extends DOMEvent
{
    protected InputEvent evtNative;
    protected Parcel parcel;

    public DOMStdEvent(DOMStdEventListener listener,InputEvent evtNative)
    {
        super(listener);
        this.evtNative = evtNative;
    }

    @Override
    public void saveEvent()
    {
        // Para evitar el problema de acceder en modo ASYNC_HOLD al evento original tras haberse encolado y terminado el proceso del evento por el navegador (da error en MSIE y otros)
        if (evtNative == null) return;

        Parcel parcel = Parcel.obtain();
        evtNative.writeToParcel(parcel, 0);
        this.evtNative = InputEvent.CREATOR.createFromParcel(parcel);
        parcel.recycle();
    }

}
