package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_Switch_switchTextAppearance;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_Switch extends ClassDescViewBased
{
    public ClassDesc_widget_Switch(ClassDescViewMgr classMgr,ClassDesc_widget_CompoundButton parentClass)
    {
        super(classMgr,"android.widget.Switch",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"switchMinWidth","mSwitchMinWidth",0));
        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"switchPadding","mSwitchPadding",0));

        addAttrDesc(new AttrDesc_widget_Switch_switchTextAppearance(this));

    }
}

