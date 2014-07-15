package org.itsnat.droid.impl.browser.clientdoc.event;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
        // Para evitar el problema de acceder en modo ASYNC_HOLD al evento original tras haberse encolado y terminado el proceso del evento por el navegador
        // En Android lo normal es reutilizar el objeto evento para siguientes eventos por ello tenemos que hacer una copia
        if (evtNative == null) return;

        // http://stackoverflow.com/questions/1626667/how-to-use-parcel-in-android

        Parcel parcelOut = Parcel.obtain();
        parcelOut.writeValue(evtNative);
        byte[] data = parcelOut.marshall();

        Parcel parcelIn = Parcel.obtain();
        parcelIn.unmarshall(data, 0, data.length);
        this.evtNative = (InputEvent) parcelIn.readValue(evtNative.getClass().getClassLoader());
        parcelIn.recycle();
        parcelOut.recycle();
    }

}
