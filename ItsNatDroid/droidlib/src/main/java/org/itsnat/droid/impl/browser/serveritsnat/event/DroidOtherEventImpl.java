package org.itsnat.droid.impl.browser.serveritsnat.event;

import org.itsnat.droid.event.DroidEvent;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.DroidEventListener;

/**
 * Created by jmarranz on 7/07/14.
 */
public class DroidOtherEventImpl extends DroidEventImpl implements DroidEvent
{
    protected Object evtNative; // Por poner algo

    public DroidOtherEventImpl(DroidEventListener listener, Object evtNative)
    {
        super(listener);
        this.evtNative = evtNative;
    }

    public static Object createOtherEventNative()
    {
        return new Object();
    }

    public DroidEventListener getDroidEventListener()
    {
        return (DroidEventListener)listener;
    }

    public Object getNativeEvent()
    {
        return evtNative;
    }

    @Override
    public void saveEvent()
    {
    }

    public boolean isIgnoreHold()
    {
        // Hemos quitado el caso "unload" como ignoreHold porque esto NO es web, en web el evento unload no llega al servidor
        // en Android no hay problema controlamos totalmente la destrucción, el ignoreHold provoca que se envíen
        // a la vez los eventos lo que hace que un unload listener del usuario en el servidor pueda no ejecutarse porque
        // el evento unload estándar de destrucción del documento ha llegado antes.
        // Así aseguramos que los listener unload se ejecuten deterministicamente

        return false; // "unload".equals(getDroidEventListener().getType()); // Si es un unload
    }

}
