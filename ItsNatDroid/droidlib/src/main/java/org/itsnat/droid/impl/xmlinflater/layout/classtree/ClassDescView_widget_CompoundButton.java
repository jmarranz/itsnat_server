package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_CompoundButton extends ClassDescViewBased
{
    public ClassDescView_widget_CompoundButton(ClassDescViewMgr classMgr,ClassDescViewBased parentClass)
    {
        super(classMgr,"android.widget.CompoundButton",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodDrawable(this,"button","setButtonDrawable",null)); // Android suele tener un drawble por defecto
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"checked",false));
    }
}

