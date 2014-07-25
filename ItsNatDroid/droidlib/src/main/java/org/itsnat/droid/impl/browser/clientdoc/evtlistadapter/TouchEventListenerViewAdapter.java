package org.itsnat.droid.impl.browser.clientdoc.evtlistadapter;

import android.view.MotionEvent;
import android.view.View;

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

        boolean res = false; // Conviene que sea false porque de otra podemos bloquear un scrollview padre. Sabemos que si es false y hay un View contenedor no se procesan los eventos touchmove etc, si es false apenas el down. El listener del usuario puede forzar un true si quiere
        if (touchListener != null) res = touchListener.onTouch(viewData.getView(), motionEvent);

        return res;
    }

    public void setOnTouchListener(View.OnTouchListener touchListener)
    {
        this.touchListener = touchListener;
    }
}
