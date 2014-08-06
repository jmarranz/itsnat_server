package org.itsnat.droid.impl.browser.clientdoc.event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.event.DroidTextChangeEvent;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

import java.util.List;

/**
 * Created by jmarranz on 7/07/14.
 */
public class DroidTextChangeEventImpl extends DroidEventImpl implements DroidTextChangeEvent
{
    protected CharSequence evtNative;

    public DroidTextChangeEventImpl(DroidEventListener listener, CharSequence evtNative)
    {
        super(listener);
        this.evtNative = evtNative;
    }

    public static CharSequence createTextChangeEventNative(CharSequence newText)
    {
        return newText;
    }

    public DroidEventListener getDroidEventListener()
    {
        return (DroidEventListener)listener;
    }

    public CharSequence getNativeEvent()
    {
        return evtNative;
    }

    public CharSequence getNewText()
    {
        return evtNative;
    }

    @Override
    public void saveEvent()
    {
    }

    public List<NameValuePair> genParamURL()
    {
        CharSequence newText = getNewText();

        List<NameValuePair> params = super.genParamURL();
        params.add(new BasicNameValuePair("itsnat_evt_newText",newText.toString()));
        return params;
    }

}
