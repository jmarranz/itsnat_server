package org.itsnat.droid.impl.browser.clientdoc;

import android.view.MotionEvent;
import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;

import java.util.List;

/**
 * Created by jmarranz on 4/07/14.
 */
public class EventListenerViewAdapter implements View.OnClickListener,View.OnTouchListener
{
    protected ItsNatViewImpl viewData;
    protected View.OnClickListener clickListener;
    protected View.OnTouchListener touchListener;

    public EventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        this.viewData = viewData;
    }

    @Override
    public void onClick(View view)
    {
        if (clickListener != null) clickListener.onClick(viewData.getView());

        dispatch("click",null);
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
            case MotionEvent.ACTION_CANCEL:
                type = "touchcancel";
                break;
        }

        dispatch(type,motionEvent);

        return false; // No lo tengo claro si true o false
    }

    private void dispatch(String type,MotionEvent motionEvent)
    {
        // motionEvent puede ser null

        List<DOMStdEventListener> list = viewData.getEventListeners(type);
        if (list == null) return;
        View view = viewData.getView();
        for(DOMStdEventListener listener : list)
        {
System.out.println("PROVISIONAL: REMOTE EVENT OK " + type);
            listener.dispatchEvent(view,motionEvent);
        }
    }

    public void setOnClickListener(View.OnClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public void setOnTouchListener(View.OnTouchListener touchListener)
    {
        this.touchListener = touchListener;
    }
}
