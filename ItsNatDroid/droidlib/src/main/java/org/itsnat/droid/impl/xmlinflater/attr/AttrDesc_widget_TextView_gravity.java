package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_gravity extends AttrDesc
{
    public AttrDesc_widget_TextView_gravity(ClassDescViewBased parent)
    {
        super(parent,"gravity");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        int valueInt = parseNameComposition(value,AttrDescGravityUtil.valueMap);

        ((TextView)view).setGravity(valueInt);
    }

    public void removeAttribute(View view)
    {
        ((TextView)view).setGravity(0);
    }
}
