package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_LinearLayout_gravity extends AttrDesc
{
    public AttrDesc_widget_LinearLayout_gravity(ClassDescViewBased parent)
    {
        super(parent,"gravity");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        int valueInt = parseNameComposition(value,AttrDescGravityUtil.valueMap);

        ((LinearLayout)view).setGravity(valueInt);
    }

    public void removeAttribute(View view)
    {
        ((LinearLayout)view).setGravity(0);
    }
}
