package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodId;
import org.itsnat.droid.impl.xmlinflater.attr.GravityUtil;
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

        addAttrDesc(new AttrDescReflecMultipleName(this,"gravity", GravityUtil.valueMap,"left|top"));
        addAttrDesc(new AttrDescReflecMethodId(this,"ignoreGravity"));
    }
}

