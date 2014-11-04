package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecFieldSetDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_AnalogClock extends ClassDescViewBased
{
    public ClassDesc_widget_AnalogClock(ClassDescViewMgr classMgr,ClassDesc_view_View parentClass)
    {
        super(classMgr,"android.widget.AnalogClock",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecFieldSetDrawable(this,"dial","mDial",null));
        addAttrDesc(new AttrDescReflecFieldSetDrawable(this,"hand_hour","mHourHand",null));
        addAttrDesc(new AttrDescReflecFieldSetDrawable(this,"hand_minute","mMinuteHand",null));

    }
}

