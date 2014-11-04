package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.widget.GridLayout;

/**
 * Created by jmarranz on 7/09/14.
 */
public class GridLayout_columnSpec extends GridLayout_columnAndRowSpec
{
    public int layout_column = Integer.MIN_VALUE;
    public int layout_columnSpan = 1;

    public void setSpec(GridLayout.LayoutParams params,GridLayout.Spec spec)
    {
        params.columnSpec = spec;
    }

    @Override
    public boolean isHorizontal()
    {
        return true;
    }

    @Override
    public int getStart()
    {
        return layout_column;
    }

    @Override
    public int getSpan()
    {
        return layout_columnSpan;
    }
}
