package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;

import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.UserData;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.ClickEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.FocusEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.KeyEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.TouchEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;
import org.itsnat.droid.impl.util.MapLightList;
import org.itsnat.droid.impl.util.MapList;
import org.itsnat.droid.impl.util.UserDataImpl;

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
    protected ClickEventListenerViewAdapter clickEvtListenerViewAdapter;
    protected TouchEventListenerViewAdapter touchEvtListenerViewAdapter;
    protected KeyEventListenerViewAdapter keyEvtListenerViewAdapter;
    protected FocusEventListenerViewAdapter focusEvtListenerViewAdapter;

    protected String nodeCacheId;
    protected UserDataImpl userData;

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

    public ClickEventListenerViewAdapter getClickEventListenerViewAdapter()
    {
        if (clickEvtListenerViewAdapter == null) this.clickEvtListenerViewAdapter = new ClickEventListenerViewAdapter(this);
        return clickEvtListenerViewAdapter;
    }

    public TouchEventListenerViewAdapter getTouchEventListenerViewAdapter()
    {
        if (touchEvtListenerViewAdapter == null) this.touchEvtListenerViewAdapter = new TouchEventListenerViewAdapter(this);
        return touchEvtListenerViewAdapter;
    }

    public KeyEventListenerViewAdapter getKeyEventListenerViewAdapter()
    {
        if (keyEvtListenerViewAdapter == null) this.keyEvtListenerViewAdapter = new KeyEventListenerViewAdapter(this);
        return keyEvtListenerViewAdapter;
    }

    public FocusEventListenerViewAdapter getFocusEventListenerViewAdapter()
    {
        if (focusEvtListenerViewAdapter == null) this.focusEvtListenerViewAdapter = new FocusEventListenerViewAdapter(this);
        return focusEvtListenerViewAdapter;
    }

    public void setOnClickListener(View.OnClickListener l)
    {
        ClickEventListenerViewAdapter evtListenerViewAdapter = getClickEventListenerViewAdapter();
        view.setOnClickListener(evtListenerViewAdapter);
        evtListenerViewAdapter.setOnClickListener(l);
    }

    public void setOnTouchListener(View.OnTouchListener l)
    {
        TouchEventListenerViewAdapter evtListenerViewAdapter = getTouchEventListenerViewAdapter();
        view.setOnTouchListener(evtListenerViewAdapter);
        evtListenerViewAdapter.setOnTouchListener(l);
    }

    public void setOnKeyListener(View.OnKeyListener l)
    {
        KeyEventListenerViewAdapter evtListenerViewAdapter = getKeyEventListenerViewAdapter();
        view.setOnKeyListener(evtListenerViewAdapter);
        evtListenerViewAdapter.setOnKeyListener(l);
    }

    public void setOnFocusChangeListener(View.OnFocusChangeListener l)
    {
        FocusEventListenerViewAdapter evtListenerViewAdapter = getFocusEventListenerViewAdapter();
        view.setOnFocusChangeListener(evtListenerViewAdapter);
        evtListenerViewAdapter.setOnFocusChangeListener(l);
    }

    public String getNodeCacheId()
    {
        return nodeCacheId;
    }

    public void setNodeCacheId(String nodeCacheId)
    {
        this.nodeCacheId = nodeCacheId;
    }

    public UserData getUserData()
    {
        if (userData == null) this.userData = new UserDataImpl();
        return userData;
    }
}
