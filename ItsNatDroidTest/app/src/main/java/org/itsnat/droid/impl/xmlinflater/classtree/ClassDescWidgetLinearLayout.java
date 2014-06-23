package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescWidgetLinearLayoutOrientation;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescWidgetTextViewTextAppearance;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescWidgetLinearLayout extends ClassDescViewBase
{
    public ClassDescWidgetLinearLayout(ClassDescViewBase parent)
    {
        super("android.widget.LinearLayout",parent);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescWidgetLinearLayoutOrientation(this)); // "orientation"
    }
}

