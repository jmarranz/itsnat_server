package org.itsnat.droid.impl.xmlinflater.layout;

import android.view.View;
import android.view.ViewParent;

import org.itsnat.droid.impl.util.WeakMapWithValue;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;

/**
 * Created by jmarranz on 25/08/14.
 */
public class ViewMapByXMLId
{
    protected InflatedLayoutImpl layout;
    protected WeakMapWithValue<String,View> mapIdViewXMLStd;

    public ViewMapByXMLId(InflatedLayoutImpl layout)
    {
        this.layout = layout;
    }

    private WeakMapWithValue<String,View> getMapIdViewXMLStd()
    {
        if (mapIdViewXMLStd == null) mapIdViewXMLStd = new WeakMapWithValue<String,View>();
        return mapIdViewXMLStd;
    }

    public String getXMLId(View view)
    {
        // Quiero el id de este View, no es relevante si está dentro del árbol de Views o no
        ViewId viewId = ViewId.getViewId(layout, view);
        return viewId.getXMLId(); // Puede ser null el resultado obviamente
        //return getMapIdViewXMLStd().getKeyByValue(view);
    }

    public void setXMLId(String id, View view)
    {
        ViewId viewId = ViewId.getViewId(layout, view);
        viewId.setXMLId(id);
        getMapIdViewXMLStd().put(id,view);
    }

    public String unsetXMLId(View view)
    {
        ViewId viewId = ViewId.getViewId(layout, view);
        viewId.unsetXMLId();
        viewId.disconnect(); // Así liberamos memoria y el objeto ViewId se pierde
        return getMapIdViewXMLStd().removeByValue(view);
    }

    public View findViewByXMLId(String id)
    {
        // Busca sólo DENTRO del árbol de Views, si está desconectado devolverá null
        View viewFound = getMapIdViewXMLStd().getValueByKey(id);
        if (viewFound == null) return null;
        // Ojo, puede estar desconectado aunque el objeto Java esté "vivo"

        View rootView = layout.getRootView();
        if (viewFound == rootView) return viewFound; // No está desconectado

        ViewParent parent = viewFound.getParent();
        while(parent != null)
        {
            if (parent == rootView)
            {
                return viewFound;
            }
            parent = parent.getParent();
        }
        // Está registrado pero sin embargo no está en el árbol de Views, podríamos eliminarlo (remove) para que no de la lata
        // pero si se vuelve a insertar perderíamos el elemento pues al reinsertar no podemos capturar la operación y definir el id,
        // tampoco es que sea demasiado importante porque el programador una vez que cambia el árbol de views por su cuenta
        // "rompe" los "contratos" de ItsNatDroid
        return null;
    }




}
