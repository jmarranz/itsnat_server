package org.itsnat.droid.impl.browser.clientdoc.evtlistadapter;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;

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
        MotionEvent motionEvent = MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(),MotionEvent.ACTION_UP,0,0,0);

        dispatch("click",motionEvent);

        if (clickListener != null) clickListener.onClick(viewData.getView());
    }

    public void setOnClickListener(View.OnClickListener clickListener)
    {
        this.clickListener = clickListener;
    }
}
