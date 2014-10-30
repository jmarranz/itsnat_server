package org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter;

import android.view.KeyEvent;
import android.view.View;

import org.itsnat.droid.impl.browser.serveritsnat.ItsNatViewImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidKeyEventImpl;

/**
 * Created by jmarranz on 24/07/14.
 */
public class KeyEventListenerViewAdapter extends DroidEventListenerViewAdapter implements View.OnKeyListener
{
    protected View.OnKeyListener keyboardListener;

    public KeyEventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        super(viewData);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent)
    {
        String type = DroidKeyEventImpl.getTypeFromAction(keyEvent);

        dispatch(type,keyEvent);

        boolean res = false;
        if (keyboardListener != null) res = keyboardListener.onKey(viewData.getView(), i, keyEvent);

        return res;
    }

    public void setOnKeyListener(View.OnKeyListener keyboardListener)
    {
        this.keyboardListener = keyboardListener;
    }
}
