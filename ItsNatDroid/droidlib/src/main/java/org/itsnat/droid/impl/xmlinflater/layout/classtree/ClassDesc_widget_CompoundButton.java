package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_CompoundButton extends ClassDescViewBased
{
    public ClassDesc_widget_CompoundButton(ClassDescViewMgr classMgr,ClassDescViewBased parentClass)
    {
        super(classMgr,"android.widget.CompoundButton",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodDrawable(this,"button","setButtonDrawable",null)); // Android suele tener un drawble por defecto
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"checked",false));
    }
}

