package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.view.ViewGroup;

import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_margin extends AttrDesc_view_View_layout_margin_Base
{
    public AttrDesc_view_View_layout_margin(ClassDescViewBased parent)
    {
        super(parent,"layout_margin");
    }

    @Override
    protected void setAttribute(ViewGroup.MarginLayoutParams params, int value)
    {
        params.topMargin = value;
        params.leftMargin = value;
        params.bottomMargin = value;
        params.rightMargin = value;
    }

    @Override
    protected void removeAttribute(ViewGroup.MarginLayoutParams params)
    {
        params.topMargin = 0;
        params.leftMargin = 0;
        params.bottomMargin = 0;
        params.rightMargin = 0;
    }
}
