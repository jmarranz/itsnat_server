package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;
import android.view.View;
import android.view.ViewParent;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.ItsNatDroid;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.util.WeakMapWithValue;

import java.util.List;

/**
 * Created by jmarranz on 16/06/14.
 */
public class InflatedLayoutImpl implements InflatedLayout
{
    protected ItsNatDroidImpl parent;
    protected View rootView;
    protected WeakMapWithValue<String,View> mapIdViewXMLStd;
    protected Context ctx;
    protected AttrCustomInflaterListener inflateListener;
    protected MapLight<String,String> namespacesByPrefix = new MapLight<String,String>();
    protected String androidNSPrefix;

    public InflatedLayoutImpl(ItsNatDroidImpl parent,AttrCustomInflaterListener inflateListener,Context ctx)
    {
        // rootView se define a posteriori
        this.parent = parent;
        this.inflateListener = inflateListener;
        this.ctx = ctx;
    }

    public XMLLayoutInflateService getXMLLayoutInflateService()
    {
        return parent.getXMLLayoutInflateService();
    }

    public String getAndroidNSPrefix()
    {
        return androidNSPrefix;
    }

    public void addNamespace(String prefix,String ns)
    {
        namespacesByPrefix.put(prefix,ns);
        if (XMLLayoutInflateService.XMLNS_ANDROID.equals(ns))
            this.androidNSPrefix = prefix;
    }

    public MapLight<String,String> getNamespacesByPrefix()
    {
        return namespacesByPrefix;
    }

    public String getNamespace(String prefix)
    {
        return namespacesByPrefix.get(prefix);
    }

    @Override
    public ItsNatDroid getItsNatDroid()
    {
        return parent;
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

    private WeakMapWithValue<String,View> getMapIdViewXMLStd()
    {
        if (mapIdViewXMLStd == null) mapIdViewXMLStd = new WeakMapWithValue<String,View>();
        return mapIdViewXMLStd;
    }

    public String unsetElementId(View view)
    {
        return getMapIdViewXMLStd().removeByValue(view);
    }

    public View getElementById(String id)
    {
        View viewFound = getMapIdViewXMLStd().getValueByKey(id);
        if (viewFound == null) return null;
        // Ojo, puede estar desconectado aunque el objeto Java esté "vivo"

        if (viewFound == rootView) return viewFound; // No está desconectado

        ViewParent parent = viewFound.getParent();
        while(parent != null)
        {
            if (parent == rootView) return viewFound;
            parent = parent.getParent();
        }
        // Está registrado pero sin embargo no está en el árbol de Views, podríamos eliminarlo (remove) para que no de la lata
        // pero si se vuelve a insertar perderíamos el elemento pues al reinsertar no podemos capturar la operación y definir el id,
        // tampoco es que sea demasiado importante porque el programador una vez que cambia el árbol de views por su cuenta
        // "rompe" los "contratos" de ItsNatDroid
        return null;
    }

    public String getXMLId(View view)
    {
        return getMapIdViewXMLStd().getKeyByValue(view);
    }

    public void setXMLId(String id, View view)
    {
        getMapIdViewXMLStd().put(id,view);
    }

    public View findViewByXMLId(String id)
    {
        // No llamamos a este método getElementById() porque devuelve un View no un DOM Node
        return getElementById(id);
    }

    public void insertFragment(View parentView,String markup,String[] loadScript,List<String> scriptList,PageImpl page)
    {
        getXMLLayoutInflateService().insertFragment(parentView,markup,loadScript,scriptList,this,page);
    }
}
