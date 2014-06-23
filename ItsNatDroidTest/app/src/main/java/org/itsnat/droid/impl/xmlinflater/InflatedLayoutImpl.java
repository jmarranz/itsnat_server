package org.itsnat.droid.impl.xmlinflater;

import android.view.View;
import android.view.ViewParent;

import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.impl.util.WeakMapWithValue;

/**
 * Created by jmarranz on 16/06/14.
 */
public class InflatedLayoutImpl implements InflatedLayout
{
    protected View rootView;
    protected WeakMapWithValue<String,View> mapIdViewXMLStd;

    public InflatedLayoutImpl()
    {
        this.rootView = rootView;
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

    private WeakMapWithValue<String,View> getMapIdViewXMLStd()
    {
        if (mapIdViewXMLStd == null) mapIdViewXMLStd = new WeakMapWithValue<String,View>();
        return mapIdViewXMLStd;
    }

    public void setElementIdAsDOM(String id, View view)
    {
        getMapIdViewXMLStd().put(id,view);
    }

    public String unsetElementIdAsDOM(View view)
    {
        return getMapIdViewXMLStd().removeByValue(view);
    }

    public View getElementById(String id)
    {
        View viewFound = getMapIdViewXMLStd().get(id);
        if (viewFound == null) return null;
        // Ojo, puede estar desconectado aunque el objeto Java esté "vivo"

        if (viewFound == rootView) return viewFound;

        ViewParent parent = viewFound.getParent();
        while(parent != null)
        {
            if (parent == rootView) return viewFound;
        }
        // Está registrado pero sin embargo no está en el árbol de Views, podríamos eliminarlo (remove) para que no de la lata
        // pero si se vuelve a insertar perderíamos el elemento pues al reinsertar no podemos capturar la operación y definir el id,
        // tampoco es que sea demasiado importante porque el programador una vez que cambia el árbol de views por su cuenta
        // "rompe" los "contratos" de ItsNatDroid
        return null;
    }

    public View findViewByXMLId(String id)
    {
        return getElementById(id);
    }

}