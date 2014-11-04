package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.view.ViewGroup;

import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_marginLeft extends AttrDesc_view_View_layout_margin_Base
{
    public AttrDesc_view_View_layout_marginLeft(ClassDescViewBased parent)
    {
        super(parent,"layout_marginLeft");
    }

    @Override
    protected void setAttribute(ViewGroup.MarginLayoutParams params, int value)
    {
        params.leftMargin = value;
    }

    @Override
    protected void removeAttribute(ViewGroup.MarginLayoutParams params)
    {
        params.leftMargin = 0;
    }
}
