package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.ViewGroup;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_marginRight extends AttrDesc_view_View_layout_margin_Base
{
    public AttrDesc_view_View_layout_marginRight(ClassDescViewBased parent)
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
