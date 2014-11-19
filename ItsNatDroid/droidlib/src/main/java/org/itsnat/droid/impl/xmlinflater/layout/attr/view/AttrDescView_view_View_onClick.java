package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_onClick extends AttrDescView
{
    public AttrDescView_view_View_onClick(ClassDescViewBased parent)
    {
        super(parent,"onClick");
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout,final Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final String handlerName = attr.getValue();

        if (!ValueUtil.isEmpty(handlerName))
        {
            View.OnClickListener listener = new View.OnClickListener()
            {
                private Method mHandler;

                public void onClick(View v)
                {
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

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view,"",xmlInflaterLayout,ctx,null,null);
    }
}
