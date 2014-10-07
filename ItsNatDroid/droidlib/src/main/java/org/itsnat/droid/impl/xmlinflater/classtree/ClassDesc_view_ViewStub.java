package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodId;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_view_ViewStub extends ClassDescViewBased
{
    public ClassDesc_view_ViewStub(ClassDescViewMgr classMgr, ClassDesc_view_View parentClass)
    {
        super(classMgr,"android.view.ViewStub",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodId(this,"inflatedId","setInflatedId",-1));
        addAttrDesc(new AttrDescReflecMethodId(this,"layout","setLayoutResource",0));

    }
}

