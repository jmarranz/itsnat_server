package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDrawable;

/**
 * Created by jmarranz on 8/10/14.
 */
public class ClassDescView_widget_CheckedTextView extends ClassDescViewBased
{
    public ClassDescView_widget_CheckedTextView(ClassDescViewMgr classMgr,ClassDescView_widget_TextView parentClass)
    {
        super(classMgr,"android.widget.CheckedTextView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodDrawable(this,"checkMark","setCheckMarkDrawable",null));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"checked",false));
    }
}
