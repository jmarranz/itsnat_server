package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;

import java.util.List;

/**
 * Created by jmarranz on 4/07/14.
 */
public class EventListenerViewAdapter implements View.OnClickListener
{
    protected ItsNatViewImpl viewData;
    protected View.OnClickListener clickListener;

    public EventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        this.viewData = viewData;
    }

    @Override
    public void onClick(View view)
    {
        dispatch("click");
    }


    private void dispatch(String type)
    {
        if ("click".equals(type))
        {
            if (clickListener != null) clickListener.onClick(viewData.getView());
        }

        List<DOMStdEventListener> list = viewData.getEventListeners(type);
        if (list == null) return;
        for(DOMStdEventListener listener : list)
        {
System.out.println("PROVISIONAL: REMOTE CLICK OK");
            listener.dispatchEvent();
        }
    }

    public void setOnClickListener(View.OnClickListener clickListener)
    {
        this.clickListener = clickListener;
    }
}
