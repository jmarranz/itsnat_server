package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcessChildGridLayout;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_gravity extends AttrDesc
{
    public AttrDesc_view_View_layout_gravity(ClassDescViewBased parent)
    {
        super(parent,"layout_gravity");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int valueInt = parseMultipleName(value, AttrDescGravityUtil.valueMap);

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

        if (oneTimeAttrProcess != null) oneTimeAttrProcess.setNeededSetLayoutParams();
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
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
