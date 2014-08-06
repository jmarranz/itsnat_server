package org.itsnat.droid.impl.browser.clientdoc.evtlistadapter;

import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidFocusEventImpl;

/**
 * Created by jmarranz on 24/07/14.
 */
public class FocusEventListenerViewAdapter extends DroidEventListenerViewAdapter implements View.OnFocusChangeListener
{
    protected View.OnFocusChangeListener focusListener;

    public FocusEventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        super(viewData);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus)
    {
        String type = hasFocus ? "focus" : "blur";

        Boolean nativeEvent = DroidFocusEventImpl.createFocusEventNative(hasFocus);

        dispatch(type,nativeEvent);

        if (focusListener != null) focusListener.onFocusChange(view,hasFocus);
    }

    public void setOnFocusChangeListener(View.OnFocusChangeListener focusListener)
    {
        this.focusListener = focusListener;
    }
}
