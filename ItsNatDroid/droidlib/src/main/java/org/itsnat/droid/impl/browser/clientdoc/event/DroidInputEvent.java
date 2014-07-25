package org.itsnat.droid.impl.browser.clientdoc.event;

import android.os.Parcel;
import android.view.InputEvent;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

import java.util.List;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class DroidInputEvent extends NormalEvent
{
    /**
     * The current event phase is the capturing phase.
     */
    public static final short CAPTURING_PHASE           = 1;
    /**
     * The event is currently being evaluated at the target
     * <code>EventTarget</code>.
     */
    public static final short AT_TARGET                 = 2;
    /**
     * The current event phase is the bubbling phase.
     */
    public static final short BUBBLING_PHASE            = 3;

    protected InputEvent evtNative;
    protected int eventPhase;
    protected View viewTarget;

    public DroidInputEvent(DroidEventListener listener, InputEvent evtNative)
    {
        super(listener);
        this.evtNative = evtNative;
        this.eventPhase = AT_TARGET;
    }

    public DroidEventListener getDroidEventListener()
    {
        return (DroidEventListener)listener;
    }

    @Override
    public Object getNativeEvent()
    {
        return evtNative;
    }

    @Override
    public String getType()
    {
        return getDroidEventListener().getType();
    }

    public int getEventPhase()
    {
        return eventPhase;
    }

    public void setEventPhase(int eventPhase)
    {
        this.eventPhase = eventPhase;
    }

    public void setViewTarget(View view)
    {
        this.viewTarget = view;
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
        parcelOut.recycle();

        Parcel parcelIn = Parcel.obtain();
        parcelIn.unmarshall(data, 0, data.length);
        parcelIn.setDataPosition(0);
        this.evtNative = (InputEvent) parcelIn.readValue(evtNative.getClass().getClassLoader());
        parcelIn.recycle();
    }

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = super.genParamURL();
        params.add(new BasicNameValuePair("itsnat_evt_eventPhase", "" + eventPhase));

        View view = getDroidEventListener().getView();
        String viewTargetStr = viewTarget != null && viewTarget != view? listener.getItsNatDocImpl().getStringPathFromView(viewTarget) : "null";
        params.add(new BasicNameValuePair("itsnat_evt_target",viewTargetStr));

        return params;
    }
}
