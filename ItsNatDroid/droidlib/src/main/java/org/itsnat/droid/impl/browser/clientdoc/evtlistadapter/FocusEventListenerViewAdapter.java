package org.itsnat.droid.impl.browser.clientdoc.evtlistadapter;

import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;

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

        dispatch(type,new Boolean(hasFocus));

        if (focusListener != null) focusListener.onFocusChange(view,hasFocus);
    }

    public void setOnFocusChangeListener(View.OnFocusChangeListener focusListener)
    {
        this.focusListener = focusListener;
    }
}
