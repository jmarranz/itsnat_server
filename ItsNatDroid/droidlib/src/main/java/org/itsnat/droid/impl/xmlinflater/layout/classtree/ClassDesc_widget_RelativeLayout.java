package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodId;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GravityUtil;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_RelativeLayout extends ClassDescViewBased
{
    public ClassDesc_widget_RelativeLayout(ClassDescViewMgr classMgr,ClassDesc_view_ViewGroup parentClass)
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

