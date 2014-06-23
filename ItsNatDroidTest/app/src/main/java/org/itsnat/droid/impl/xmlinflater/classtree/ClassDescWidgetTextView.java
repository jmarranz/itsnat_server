package org.itsnat.droid.impl.xmlinflater.classtree;

import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescWidgetTextViewTextAppearance;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescWidgetTextViewTextSize;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescWidgetTextView extends ClassDescViewBase
{
    public ClassDescWidgetTextView(ClassDescViewBase parent)
    {
        super("android.widget.TextView",parent);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescCharSequence(this, "text"));
        addAttrDesc(new AttrDescWidgetTextViewTextSize(this)); // textSize
        addAttrDesc(new AttrDescWidgetTextViewTextAppearance(this)); // "textAppearance"

    }
}

