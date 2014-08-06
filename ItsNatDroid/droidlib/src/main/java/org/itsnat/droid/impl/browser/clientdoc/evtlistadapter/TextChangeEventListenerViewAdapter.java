package org.itsnat.droid.impl.browser.clientdoc.evtlistadapter;

import android.text.Editable;
import android.text.TextWatcher;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidTextChangeEventImpl;

/**
 * Created by jmarranz on 24/07/14.
 */
public class TextChangeEventListenerViewAdapter extends DroidEventListenerViewAdapter implements TextWatcher
{
    public TextChangeEventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        super(viewData);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count)
    {
    }

    @Override
    public void afterTextChanged(Editable editable)
    {
        CharSequence nativeEvent = DroidTextChangeEventImpl.createTextChangeEventNative(editable);

        dispatch("change",nativeEvent);
    }
}
