package org.itsnat.droid.impl.browser.clientdoc.evtlistadapter;

import android.view.MotionEvent;
import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.EventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;

/**
 * Created by jmarranz on 24/07/14.
 */
public class TouchEventListenerViewAdapter extends EventListenerViewAdapter implements View.OnTouchListener
{
    protected View.OnTouchListener touchListener;

    public TouchEventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        super(viewData);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if (touchListener != null) touchListener.onTouch(viewData.getView(), motionEvent);

        String type = "";
        int action = motionEvent.getAction();
        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
                type = "touchstart";
                break;
            case MotionEvent.ACTION_UP:
                type = "touchend";
                break;
            case MotionEvent.ACTION_MOVE:
                type = "touchmove";
                break;
            case MotionEvent.ACTION_CANCEL:
                type = "touchcancel";
                break;
        }

        dispatch(type,motionEvent);

        return false; // No lo tengo claro si true o false
    }

    public void setOnTouchListener(View.OnTouchListener touchListener)
    {
        this.touchListener = touchListener;
    }
}
