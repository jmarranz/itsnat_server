package org.itsnat.droid.impl.xmlinflated.layout;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.ItsNatDroid;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.model.layout.ViewParsed;
import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.ViewMapByXMLId;
import org.itsnat.droid.impl.xmlinflater.layout.XMLLayoutInflater;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.List;

/**
 * Created by jmarranz on 16/06/14.
 */
public abstract class InflatedLayoutImpl implements InflatedLayout
{
    protected ItsNatDroidImpl itsNatDroid;
    protected XMLLayoutInflater inflater;
    protected LayoutParsed layoutParsed;
    protected View rootView;
    protected ViewMapByXMLId viewMapByXMLId;
    protected Context ctx;
    protected AttrCustomInflaterListener inflateListener;


    public InflatedLayoutImpl(ItsNatDroidImpl itsNatDroid,LayoutParsed layoutParsed,AttrCustomInflaterListener inflateListener,Context ctx)
    {
        // rootView se define a posteriori
        this.itsNatDroid = itsNatDroid;
        this.layoutParsed = layoutParsed;
        this.inflateListener = inflateListener;
        this.ctx = ctx;
    }

    public LayoutParsed getLayoutParsed()
    {
        return layoutParsed;
    }

    public XMLLayoutInflateService getXMLLayoutInflateService()
    {
        return itsNatDroid.getXMLLayoutInflateService();
    }

    public String getAndroidNSPrefix()
    {
        return layoutParsed.getAndroidNSPrefix();
    }

    public MapLight<String,String> getNamespacesByPrefix()
    {
        return layoutParsed.getNamespacesByPrefix();
    }

    public String getNamespace(String prefix)
    {
        return getNamespacesByPrefix().get(prefix);
    }

    @Override
    public ItsNatDroid getItsNatDroid()
    {
        return getItsNatDroidImpl();
    }

    public ItsNatDroidImpl getItsNatDroidImpl()
    {
        return itsNatDroid;
    }

    @Override
    public View getRootView()
    {
        return rootView;
    }

    public void setRootView(View rootView)
    {
        this.rootView = rootView;
    }

    public AttrCustomInflaterListener getAttrCustomInflaterListener()
    {
        return inflateListener;
    }

    public Context getContext()
    {
        return ctx;
    }

    private ViewMapByXMLId getViewMapByXMLId()
    {
        if (viewMapByXMLId == null) viewMapByXMLId = new ViewMapByXMLId(this);
        return viewMapByXMLId;
    }

    public String unsetXMLId(View view)
    {
        return getViewMapByXMLId().unsetXMLId(view);
    }

    public String getXMLId(View view)
    {
        return getViewMapByXMLId().getXMLId(view);
    }

    public void setXMLId(String id, View view)
    {
        getViewMapByXMLId().setXMLId(id, view);
    }

    public View findViewByXMLId(String id)
    {
        if (viewMapByXMLId == null) return null;
        return viewMapByXMLId.findViewByXMLId(id);
    }

    public View inflateLayout(String[] loadScript, List<String> scriptList)
    {
        this.inflater = createXMLLayoutInflater();
        return inflater.inflateLayout(loadScript, scriptList);
    }

    public View insertFragment(ViewParsed rootViewFragmentParsed)
    {
        return inflater.insertFragment(rootViewFragmentParsed);
    }

    public boolean setAttribute(ClassDescViewBased classDesc,View view,String namespaceURI,String name,String value,
                                OneTimeAttrProcess oneTimeAttrProcess,PendingPostInsertChildrenTasks pending)
    {
        return inflater.setAttribute(classDesc,view,namespaceURI,name,value,oneTimeAttrProcess,pending);
    }

    public abstract XMLLayoutInflater createXMLLayoutInflater();
}
