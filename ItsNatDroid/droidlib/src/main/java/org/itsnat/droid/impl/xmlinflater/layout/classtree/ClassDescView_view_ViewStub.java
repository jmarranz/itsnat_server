package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodId;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_view_ViewStub extends ClassDescViewBased
{
    public ClassDescView_view_ViewStub(ClassDescViewMgr classMgr, ClassDescView_view_View parentClass)
    {
        super(classMgr,"android.view.ViewStub",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodId(this,"inflatedId","setInflatedId",-1));
        addAttrDesc(new AttrDescViewReflecMethodId(this,"layout","setLayoutResource",0));

    }
}

