package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_DatePicker_endYear_startYear;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_DatePicker_maxDate_minDate;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_DatePicker extends ClassDescViewBased
{
    public ClassDesc_widget_DatePicker(ClassDesc_widget_FrameLayout parentClass)
    {
        super("android.widget.DatePicker",parentClass);
    }

    protected void init()
    {
        super.init();


        addAttrDesc(new AttrDescReflecMethodBoolean(this,"calendarViewShown",true));
        addAttrDesc(new AttrDesc_widget_DatePicker_endYear_startYear(this,"endYear"));
        addAttrDesc(new AttrDesc_widget_DatePicker_maxDate_minDate(this,"maxDate"));
        addAttrDesc(new AttrDesc_widget_DatePicker_maxDate_minDate(this,"minDate"));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"spinnersShown",true));
        addAttrDesc(new AttrDesc_widget_DatePicker_endYear_startYear(this,"startYear"));
    }
}

