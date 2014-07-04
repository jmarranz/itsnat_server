package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;

import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.EventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;
import org.itsnat.droid.impl.util.MapList;

import java.util.List;

/**
 * Created by jmarranz on 4/07/14.
 */
public class ItsNatViewImpl implements ItsNatView
{
    public static final int ITSNAT_VIEW_DATA = 1111111111;

    protected View view;
    protected MapList<String,DOMStdEventListener> eventListeners;
    protected EventListenerViewAdapter evtListenerViewAdapter;

    public ItsNatViewImpl(View view)
    {
        this.view = view;
    }

    public static ItsNatViewImpl getItsNatView(View view)
    {
        ItsNatViewImpl viewData = (ItsNatViewImpl)view.getTag(ITSNAT_VIEW_DATA);
        if (viewData == null)
        {
            viewData = new ItsNatViewImpl(view);
            view.setTag(ITSNAT_VIEW_DATA,viewData);
        }
        return viewData;
    }

    public View getView()
    {
        return view;
    }

    public MapList<String,DOMStdEventListener> getEventListeners()
    {
        if (eventListeners == null) eventListeners = new MapList<String,DOMStdEventListener>();
        return eventListeners;
    }

    public List<DOMStdEventListener> getEventListeners(String type)
    {
        return getEventListeners().getValueList(type);
    }

    public EventListenerViewAdapter getEventListenerViewAdapter()
    {
        if (evtListenerViewAdapter == null) this.evtListenerViewAdapter = new EventListenerViewAdapter(this);
        return evtListenerViewAdapter;
    }

    public void setOnClickListener(View.OnClickListener l)
    {
        getEventListenerViewAdapter().setOnClickListener(l);
    }
}
