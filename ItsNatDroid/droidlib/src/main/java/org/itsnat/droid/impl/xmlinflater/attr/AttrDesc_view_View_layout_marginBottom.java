package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_marginBottom extends AttrDesc_view_View_layout_margin_Base
{
    public AttrDesc_view_View_layout_marginBottom(ClassDescViewBased parent)
    {
        super(parent,"layout_marginBottom");
    }

    @Override
    protected void setAttribute(ViewGroup.MarginLayoutParams params, int value)
    {
        params.bottomMargin = value;
    }

    @Override
    protected void removeAttribute(ViewGroup.MarginLayoutParams params)
    {
        params.bottomMargin = 0;
    }
}
