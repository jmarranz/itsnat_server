package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_Chronometer_format;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_Chronometer extends ClassDescViewBased
{
    public ClassDescView_widget_Chronometer(ClassDescViewMgr classMgr, ClassDescViewBased parentClass)
    {
        super(classMgr,"android.widget.Chronometer",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescView_widget_Chronometer_format(this));
    }
}

