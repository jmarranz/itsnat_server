package org.itsnat.droid.impl.xmlinflated.layout;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.ItsNatDroid;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.model.layout.ViewParsed;
import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.ViewMapByXMLId;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 16/06/14.
 */
public abstract class InflatedLayoutImpl extends InflatedXML implements InflatedLayout
{
    protected View rootView;
    protected ViewMapByXMLId viewMapByXMLId;
    protected XMLInflaterLayout xmlInflater; // El InflatedLayoutImpl está "vivo" tras el inflado, pueden haber cambios de atributos e inserción de fragments, por eso necesitamos el XML inflater que se usó para inflar en tiempo de carga

    public InflatedLayoutImpl(ItsNatDroidImpl itsNatDroid,LayoutParsed layoutParsed,Context ctx)
    {
        super(itsNatDroid,layoutParsed,ctx);
        // rootView se define a posteriori
    }

    public LayoutParsed getLayoutParsed()
    {
        return (LayoutParsed)xmlParsed;
    }

    public XMLInflaterLayout getXMLInflaterLayout()
    {
        return xmlInflater;
    }

    public void setXMLInflaterLayout(XMLInflaterLayout xmlInflater)
    {
        this.xmlInflater = xmlInflater;
    }

    public String getAndroidNSPrefix()
    {
        return getLayoutParsed().getAndroidNSPrefix();
    }

    public MapLight<String,String> getNamespacesByPrefix()
    {
        return getLayoutParsed().getNamespacesByPrefix();
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


    @Override
    public View getRootView()
    {
        return rootView;
    }

    public void setRootView(View rootView)
    {
        this.rootView = rootView;
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

    public View insertFragment(ViewParsed rootViewFragmentParsed)
    {
        return getXMLInflaterLayout().insertFragment(rootViewFragmentParsed);
    }

    public boolean setAttribute(ClassDescViewBased classDesc,View view,AttrParsed attr,
                                OneTimeAttrProcess oneTimeAttrProcess,PendingPostInsertChildrenTasks pending)
    {
        return getXMLInflaterLayout().setAttribute(classDesc, view, attr, oneTimeAttrProcess, pending);
    }

    public abstract AttrLayoutInflaterListener getAttrLayoutInflaterListener();
}
