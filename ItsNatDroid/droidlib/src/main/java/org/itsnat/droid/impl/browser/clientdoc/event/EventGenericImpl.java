package org.itsnat.droid.impl.browser.clientdoc.event;

import org.apache.http.NameValuePair;
import org.itsnat.droid.event.Event;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventGenericListener;

import java.util.List;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class EventGenericImpl implements Event
{
    protected EventGenericListener listener;
    protected boolean mustBeSent = true;

    public EventGenericImpl(EventGenericListener listener)
    {
        this.listener = listener;
    }

    public void setMustBeSent(boolean value) { this.mustBeSent = value; }
    public void sendEvent()
    {
        if (this.mustBeSent) listener.getItsNatDocImpl().getEventManager().sendEvent(this);
    }

    public boolean isIgnoreHold()
    {
        return false;
    }

    public String getType()
    {
        return null; // Se redefine
    }

    public void onEventReturned()
    {
        // Redefinir en derivada si se quiere hacer algo
    }

    public abstract void saveEvent();

    public EventGenericListener getEventGenericListener()
    {
        return listener;
    }

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = listener.getItsNatDocImpl().genParamURL();
        listener.genParamURL(this,params);
        return params;
    }


}
