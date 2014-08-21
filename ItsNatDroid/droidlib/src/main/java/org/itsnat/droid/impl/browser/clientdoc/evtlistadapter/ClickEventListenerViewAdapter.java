package org.itsnat.droid.impl.browser.clientdoc.evtlistadapter;

import android.view.MotionEvent;
import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidMotionEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

/**
 * Created by jmarranz on 24/07/14.
 */
public class ClickEventListenerViewAdapter extends DroidEventListenerViewAdapter implements View.OnClickListener
{
    protected View.OnClickListener clickListener;
    protected String inlineCode;

    public ClickEventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        super(viewData);
    }

    @Override
    public void onClick(View view)
    {
        MotionEvent motionEvent = DroidMotionEventImpl.createMotionEventNative("click", 0, 0);

        String inlineCode = viewData.getClickEventListenerViewAdapter().getInlineCode();
        if (inlineCode != null)
        {
            executeInlineEventHandler(inlineCode, "click", DroidEventListener.MOTION_EVENT, motionEvent);
        }

        dispatch("click",motionEvent);

        if (clickListener != null) clickListener.onClick(view);
    }

    public void setOnClickListener(View.OnClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public String getInlineCode()
    {
        return inlineCode;
    }

    public void setInlineCode(String inlineCode)
    {
        this.inlineCode = inlineCode;
    }
}
