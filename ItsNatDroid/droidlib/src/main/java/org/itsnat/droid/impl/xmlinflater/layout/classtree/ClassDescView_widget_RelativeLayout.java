package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodId;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GravityUtil;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_RelativeLayout extends ClassDescViewBased
{
    public ClassDescView_widget_RelativeLayout(ClassDescViewMgr classMgr,ClassDescView_view_ViewGroup parentClass)
    {
        super(classMgr,"android.widget.RelativeLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"left|top"));
        addAttrDesc(new AttrDescViewReflecMethodId(this,"ignoreGravity",-1));
    }
}

