package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_DatePicker_endYear_startYear;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_DatePicker_maxDate_minDate;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_DatePicker extends ClassDescViewBased
{
    public ClassDescView_widget_DatePicker(ClassDescViewMgr classMgr,ClassDescView_widget_FrameLayout parentClass)
    {
        super(classMgr,"android.widget.DatePicker",parentClass);
    }

    protected void init()
    {
        super.init();


        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"calendarViewShown",true));
        addAttrDesc(new AttrDescView_widget_DatePicker_endYear_startYear(this,"endYear"));
        addAttrDesc(new AttrDescView_widget_DatePicker_maxDate_minDate(this,"maxDate"));
        addAttrDesc(new AttrDescView_widget_DatePicker_maxDate_minDate(this,"minDate"));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"spinnersShown",true));
        addAttrDesc(new AttrDescView_widget_DatePicker_endYear_startYear(this,"startYear"));
    }
}

