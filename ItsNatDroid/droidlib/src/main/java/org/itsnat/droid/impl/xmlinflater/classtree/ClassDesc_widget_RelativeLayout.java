package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodId;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.attr.GravityUtil;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_RelativeLayout extends ClassDescViewBased
{
    public ClassDesc_widget_RelativeLayout(ClassDescViewMgr classMgr,ClassDesc_widget_ViewGroup parentClass)
    {
        super(classMgr,"android.widget.RelativeLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"left|top"));
        addAttrDesc(new AttrDescReflecMethodId(this,"ignoreGravity",-1));
    }
}

