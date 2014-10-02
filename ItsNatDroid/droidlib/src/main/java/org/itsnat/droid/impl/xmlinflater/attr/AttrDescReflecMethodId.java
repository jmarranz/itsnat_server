package org.itsnat.droid.impl.xmlinflater.attr;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 *
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescReflecMethodId extends AttrDescReflecMethod
{
    public AttrDescReflecMethodId(ClassDescViewBased parent, String name, String methodName)
    {
        super(parent,name,methodName,getClassParam());
    }

    public AttrDescReflecMethodId(ClassDescViewBased parent, String name)
    {
        super(parent,name,getClassParam());
    }

    protected static Class<?> getClassParam()
    {
        return int.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        // Curiosamente procesamos aquí los casos de "@+id/...", la razón es que cualquier atributo que referencie un id (más allá
        // de android:id) puede registrar un nuevo atributo lo cual es útil si el android:id como tal está después,
        // después en android:id ya no hace falta que sea "@+id/...".
        // http://stackoverflow.com/questions/11029635/android-radiogroup-checkedbutton-property

        Context ctx = view.getContext();
        int id = 0;
        if (value.startsWith("@+id/") || value.startsWith("@id/")) // Si fuera el caso de "@+mypackage:id/name" ese caso no lo soportamos, no lo he visto nunca aunque en teoría está sintácticamente permitido
        {
            id = getIdentifier(value, ctx,false); // Tiene prioridad el recurso de Android, pues para qué generar un id nuevo si ya existe o bien ya fue registrado dinámicamente
            if (id <= 0)
            {
                int pos = value.indexOf('/');
                String idName = value.substring(pos + 1);
                XMLLayoutInflateService inflateService = parent.getClassDescViewMgr().getXMLLayoutInflateService();
                if (value.startsWith("@+id/"))
                    id = inflateService.findViewIdAddIfNecessary(idName);
                else
                    id = inflateService.findViewId(idName);
                if (id <= 0) throw new ItsNatDroidException("Not found resource with id \"" + value + "\"");
            }
        }
        else id = getIdentifier(value, ctx);

        callMethod(view, id);
    }

    public void removeAttribute(View view)
    {
        callMethod(view, -1);
    }

}
