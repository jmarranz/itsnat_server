package org.itsnat.droid.impl.browser.clientdoc.evtlistadapter;

import android.view.MotionEvent;
import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidMotionEventImpl;

/**
 * Created by jmarranz on 24/07/14.
 */
public class ClickEventListenerViewAdapter extends DroidEventListenerViewAdapter implements View.OnClickListener
{
    protected View.OnClickListener clickListener;


    public ClickEventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        super(viewData);
    }

    @Override
    public void onClick(View view)
    {
        MotionEvent motionEvent = DroidMotionEventImpl.createMotionEventNative("click", 0, 0);

        dispatch("click",motionEvent);

        if (clickListener != null) clickListener.onClick(view);
    }

    public void setOnClickListener(View.OnClickListener clickListener)
    {
        this.clickListener = clickListener;
    }


}
