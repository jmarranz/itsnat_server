package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;

/**
 * Created by jmarranz on 8/10/14.
 */
public class ClassDesc_widget_CheckedTextView extends ClassDescViewBased
{
    public ClassDesc_widget_CheckedTextView(ClassDescViewMgr classMgr,ClassDesc_widget_TextView parentClass)
    {
        super(classMgr,"android.widget.CheckedTextView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodDrawable(this,"checkMark","setCheckMarkDrawable",null));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"checked",false));
    }
}
