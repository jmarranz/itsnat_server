package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcessChildGridLayout;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GridLayout_columnSpec;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GridLayout_rowSpec;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_layout_gravity extends AttrDescView
{
    public AttrDescView_view_View_layout_gravity(ClassDescViewBased parent)
    {
        super(parent,"layout_gravity");
    }

    public void setAttribute(final View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, final OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final int valueInt = parseMultipleName(attr.getValue(), GravityUtil.valueMap);

        Runnable task = new Runnable(){
            @Override
            public void run()
            {
                // Objetos LayoutParams diferentes pero mismos valores: http://developer.android.com/reference/android/R.attr.html#layout_gravity
                ViewGroup.LayoutParams params = view.getLayoutParams();
                if (params instanceof LinearLayout.LayoutParams)
                    ((LinearLayout.LayoutParams)params).gravity = valueInt;
                else if (params instanceof FrameLayout.LayoutParams)
                    ((FrameLayout.LayoutParams)params).gravity = valueInt;
                else if (params instanceof GridLayout.LayoutParams)
                {
                    if (oneTimeAttrProcess != null)
                    {
                        OneTimeAttrProcessChildGridLayout oneTimeAttrProcessGrid = (OneTimeAttrProcessChildGridLayout) oneTimeAttrProcess;
                        if (oneTimeAttrProcessGrid.gridLayout_columnSpec == null) oneTimeAttrProcessGrid.gridLayout_columnSpec = new GridLayout_columnSpec();
                        if (oneTimeAttrProcessGrid.gridLayout_rowSpec == null)    oneTimeAttrProcessGrid.gridLayout_rowSpec = new GridLayout_rowSpec();

                        oneTimeAttrProcessGrid.gridLayout_columnSpec.layout_gravity = valueInt;
                        oneTimeAttrProcessGrid.gridLayout_rowSpec.layout_gravity = valueInt;
                    }
                    else
                    {
                        ((GridLayout.LayoutParams)params).setGravity(valueInt);
                    }
                }
            }};

        if (oneTimeAttrProcess != null)
        {
            oneTimeAttrProcess.addLayoutParamsTask(task);
        }
        else
        {
            task.run();
            view.setLayoutParams(view.getLayoutParams());
        }

    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params instanceof LinearLayout.LayoutParams)
            ((LinearLayout.LayoutParams)params).gravity = Gravity.LEFT;
        else if (params instanceof FrameLayout.LayoutParams)
            ((FrameLayout.LayoutParams)params).gravity = Gravity.LEFT;
        else if (params instanceof GridLayout.LayoutParams)
            ((GridLayout.LayoutParams)params).setGravity(Gravity.LEFT); // La doc habla también de un BASELINE pero no lo encuentro como Gravity

        view.setLayoutParams(params);
    }
}
