package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;

import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.UserData;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.ClickEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.FocusEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.KeyEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.TextChangeEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.TouchEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;
import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.util.MapLightList;
import org.itsnat.droid.impl.util.MapList;
import org.itsnat.droid.impl.util.UserDataImpl;

import java.util.List;

/**
 * Created by jmarranz on 4/07/14.
 */
public abstract class ItsNatViewImpl implements ItsNatView
{
    public static final int ITSNAT_VIEW_KEY = 1111111111;

    protected PageImpl page;
    protected MapList<String,DroidEventListener> eventListeners;
    protected String nodeCacheId;
    protected UserDataImpl userData;
    protected MapLight<String,String> onTypeInlineCodeMap;

    public ItsNatViewImpl(PageImpl page)
    {
        this.page = page;
    }

    public static ItsNatViewImpl getItsNatView(PageImpl page,View view)
    {
        if (view == null)
            return page.getItsNatDocImpl().getItsNatViewNull();
        ItsNatViewNotNullImpl viewData = (ItsNatViewNotNullImpl)view.getTag(ITSNAT_VIEW_KEY);
        if (viewData == null)
        {
            viewData = new ItsNatViewNotNullImpl(page,view);
            view.setTag(ITSNAT_VIEW_KEY,viewData);
        }
        return viewData;
    }

    public PageImpl getPageImpl()
    {
        return page;
    }

    public abstract View getView();

    public abstract String getXMLId();

    public abstract void setXMLId(String id);

    public MapLight<String,String> getOnTypeInlineCodeMap()
    {
        if (onTypeInlineCodeMap == null) this.onTypeInlineCodeMap = new MapLight<String,String>();
        return onTypeInlineCodeMap;
    }

    public MapList<String,DroidEventListener> getEventListeners()
    {
        if (eventListeners == null) this.eventListeners = new MapLightList<String,DroidEventListener>();
        return eventListeners;
    }

    public List<DroidEventListener> getEventListeners(String type)
    {
        return getEventListeners().get(type);
    }

    public abstract ClickEventListenerViewAdapter getClickEventListenerViewAdapter();

    public abstract TouchEventListenerViewAdapter getTouchEventListenerViewAdapter();

    public abstract KeyEventListenerViewAdapter getKeyEventListenerViewAdapter();

    public abstract FocusEventListenerViewAdapter getFocusEventListenerViewAdapter();

    public abstract TextChangeEventListenerViewAdapter getTextChangeEventListenerViewAdapter();

    public abstract void setTextChangeEventListenerViewAdapter(TextChangeEventListenerViewAdapter textChangeEvtListenerViewAdapter);

    public abstract void setOnClickListener(View.OnClickListener l);

    public abstract void setOnTouchListener(View.OnTouchListener l);

    public abstract void setOnKeyListener(View.OnKeyListener l);

    public abstract void setOnFocusChangeListener(View.OnFocusChangeListener l);

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

    public String getOnTypeInlineCode(String attrName)
    {
        return getOnTypeInlineCodeMap().get(attrName);
    }

    public void setOnTypeInlineCode(String attrName,String code)
    {
        getOnTypeInlineCodeMap().put(attrName,code);
    }

    public void removeOnTypeInlineCode(String type)
    {
        getOnTypeInlineCodeMap().remove(type);
    }
}
