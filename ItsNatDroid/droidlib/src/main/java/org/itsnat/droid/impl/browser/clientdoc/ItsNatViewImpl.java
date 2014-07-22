package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;

import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;
import org.itsnat.droid.impl.util.MapLightList;
import org.itsnat.droid.impl.util.MapList;

import java.util.List;

/**
 * Created by jmarranz on 4/07/14.
 */
public class ItsNatViewImpl implements ItsNatView
{
    public static final int ITSNAT_VIEW_DATA = 1111111111;

    protected PageImpl page;
    protected View view;
    protected MapList<String,DroidEventListener> eventListeners;
    protected EventListenerViewAdapter evtListenerViewAdapter;
    protected String nodeCacheId;

    public ItsNatViewImpl(PageImpl page,View view)
    {
        this.page = page;
        this.view = view;
    }

    public static ItsNatViewImpl getItsNatView(PageImpl page,View view)
    {
        ItsNatViewImpl viewData = (ItsNatViewImpl)view.getTag(ITSNAT_VIEW_DATA);
        if (viewData == null)
        {
            viewData = new ItsNatViewImpl(page,view);
            view.setTag(ITSNAT_VIEW_DATA,viewData);
        }
        return viewData;
    }

    public PageImpl getPageImpl()
    {
        return page;
    }

    public View getView()
    {
        return view;
    }

    public String getXMLId()
    {
        return page.getInflatedLayoutImpl().getXMLId(view);
    }

    public MapList<String,DroidEventListener> getEventListeners()
    {
        if (eventListeners == null) eventListeners = new MapLightList<String,DroidEventListener>();
        return eventListeners;
    }

    public List<DroidEventListener> getEventListeners(String type)
    {
        return getEventListeners().get(type);
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

    public void setOnTouchListener(View.OnTouchListener l)
    {
        getEventListenerViewAdapter().setOnTouchListener(l);
    }

    public void setOnKeyListener(View.OnKeyListener l)
    {
        getEventListenerViewAdapter().setOnKeyListener(l);
    }

    public String getNodeCacheId()
    {
        return nodeCacheId;
    }

    public void setNodeCacheId(String nodeCacheId)
    {
        this.nodeCacheId = nodeCacheId;
    }
}
