package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.view.ViewGroup;

import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_layout_marginRight extends AttrDescView_view_View_layout_margin_Base
{
    public AttrDescView_view_View_layout_marginRight(ClassDescViewBased parent)
    {
        super(parent,"layout_marginRight");
    }

    @Override
    protected void setAttribute(ViewGroup.MarginLayoutParams params, int value)
    {
        params.rightMargin = value;
    }

    @Override
    protected void removeAttribute(ViewGroup.MarginLayoutParams params)
    {
        params.rightMargin = 0;
    }
}
