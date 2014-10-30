package org.itsnat.droid.impl.browser.serveritsnat;

import android.view.View;

import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.UserData;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.ClickEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.FocusEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.KeyEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.TextChangeEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.TouchEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.DroidEventListener;
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

    protected ItsNatDocImpl itsNatDoc;
    protected MapList<String,DroidEventListener> eventListeners;
    protected String nodeCacheId;
    protected UserDataImpl userData;
    protected MapLight<String,String> onTypeInlineCodeMap;

    public ItsNatViewImpl(ItsNatDocImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public static ItsNatViewImpl getItsNatView(ItsNatDocImpl itsNatDoc,View view)
    {
        if (view == null)
            return itsNatDoc.getItsNatViewNull();
        ItsNatViewNotNullImpl viewData = (ItsNatViewNotNullImpl)view.getTag(ITSNAT_VIEW_KEY);
        if (viewData == null)
        {
            viewData = new ItsNatViewNotNullImpl(itsNatDoc,view);
            view.setTag(ITSNAT_VIEW_KEY,viewData);
        }
        return viewData;
    }

    public ItsNatDocImpl getItsNatDocImpl()
    {
        return itsNatDoc;
    }

    /*
    public PageImpl getPageImpl()
    {
        return itsNatDoc.getPageImpl();
    }
*/

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
