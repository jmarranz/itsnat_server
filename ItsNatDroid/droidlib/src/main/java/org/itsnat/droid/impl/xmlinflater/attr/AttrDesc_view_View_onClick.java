package org.itsnat.droid.impl.xmlinflater.attr;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingAttrTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_onClick extends AttrDesc
{
    public AttrDesc_view_View_onClick(ClassDescViewBased parent)
    {
        super(parent,"onClick");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingAttrTasks pending)
    {
        final String handlerName = value;

        if (!ValueUtil.isEmpty(handlerName))
        {
            View.OnClickListener listener = new View.OnClickListener()
            {
                private Method mHandler;

                public void onClick(View v)
                {
                    Context ctx = v.getContext();
                    if (mHandler == null)
                    {
                        try
                        {
                            mHandler = ctx.getClass().getMethod(handlerName, View.class);
                        }
                        catch (NoSuchMethodException e)
                        {
                            int id = v.getId();
                            String idText = id == View.NO_ID ? "" : " with id '" + ctx.getResources().getResourceEntryName(id) + "'";
                            throw new IllegalStateException("Could not find a method " +
                                    handlerName + "(View) in the activity " + ctx.getClass() + " for onClick handler" + " on view " + v.getClass() + idText, e);
                        }
                    }

                    try
                    {
                        mHandler.invoke(ctx, v);
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new IllegalStateException("Could not execute non " + "public method of the activity", e);
                    }
                    catch (InvocationTargetException e)
                    {
                        throw new IllegalStateException("Could not execute " + "method of the activity", e);
                    }
                }

            };

            view.setOnClickListener(listener); // Obviamente esto interviene en la capacidad de procesar eventos click ItsNat en esta View
        }
        else
        {
            view.setOnClickListener(null);
        }
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"",null,null);
    }
}
