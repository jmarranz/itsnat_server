package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescGravityUtil;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecId;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMultipleName;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_RelativeLayout extends ClassDescViewBased
{
    public ClassDesc_widget_RelativeLayout(ClassDesc_widget_ViewGroup parentClass)
    {
        super("android.widget.RelativeLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMultipleName(this,"gravity", AttrDescGravityUtil.valueMap,"left|top"));
        addAttrDesc(new AttrDescReflecId(this,"ignoreGravity"));
    }
}

