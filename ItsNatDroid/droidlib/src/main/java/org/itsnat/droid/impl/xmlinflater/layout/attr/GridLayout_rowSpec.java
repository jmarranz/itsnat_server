package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.widget.GridLayout;

/**
 * Created by jmarranz on 7/09/14.
 */
public class GridLayout_rowSpec extends GridLayout_columnAndRowSpec
{
    public int layout_row = Integer.MIN_VALUE;
    public int layout_rowSpan = 1;

    public void setSpec(GridLayout.LayoutParams params,GridLayout.Spec spec)
    {
        params.rowSpec = spec;
    }

    @Override
    public boolean isHorizontal()
    {
        return false;
    }

    @Override
    public int getStart()
    {
        return layout_row;
    }

    @Override
    public int getSpan()
    {
        return layout_rowSpan;
    }
}
