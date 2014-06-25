package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBase;

/**
 * Esta clase NO sirve para procesar atributos "@+id/nombre" porque "nombre" no se manifiesta en el R.id porque no ha podido compilarse,
 * sino más bien sirve para "@id/nombre" lo cual supone que "nombre" esté definido como un resource de tipo id para que esté compilado
 * (podría ser incluso "@android:id/nombre" un id predefinido de Android).
 *
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescViewViewId extends AttrDescReflection
{
    public AttrDescViewViewId(ClassDescViewBase parent)
    {
        super(parent, "id", "setId");
    }

    protected Class<?> getClassParam()
    {
        return int.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        int resId = getIdentifier(value,view.getContext());
        setAttribute(view,resId);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,0);
    }
}
