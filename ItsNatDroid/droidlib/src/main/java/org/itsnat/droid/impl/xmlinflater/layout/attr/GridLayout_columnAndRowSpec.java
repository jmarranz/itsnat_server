package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;

/**
 * Created by jmarranz on 7/09/14.
 */
public abstract class GridLayout_columnAndRowSpec
{
    public static MethodContainer<GridLayout.Alignment> method_getAlignment =
            new MethodContainer<GridLayout.Alignment>(GridLayout.class,"getAlignment", new Class[]{int.class, boolean.class});

    public int layout_gravity = Gravity.NO_GRAVITY;

    public GridLayout_columnAndRowSpec() {}

    public void setAttributes(View view)
    {
        GridLayout viewParent = (GridLayout)view.getParent();
        GridLayout.Spec spec = GridLayout.spec(getStart(), getSpan(), getAlignment(viewParent, layout_gravity, isHorizontal()));

        GridLayout.LayoutParams params = (GridLayout.LayoutParams)view.getLayoutParams();
        setSpec(params,spec);
    }

    public abstract void setSpec(GridLayout.LayoutParams params,GridLayout.Spec spec);
    public abstract boolean isHorizontal();
    public abstract int getStart();
    public abstract int getSpan();

    public static GridLayout.Alignment getAlignment(GridLayout view,int gravity,boolean horizontal)
    {
        return method_getAlignment.invoke(view,gravity,horizontal);
    }
}
