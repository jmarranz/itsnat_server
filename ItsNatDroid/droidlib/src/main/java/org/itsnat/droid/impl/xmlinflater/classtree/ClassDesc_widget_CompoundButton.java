package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_CompoundButton extends ClassDescViewBased
{
    public ClassDesc_widget_CompoundButton(ClassDescViewBased parentClass)
    {
        super("android.widget.CompoundButton",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodDrawable(this,"button","setButtonDrawable",null)); // Android suele tener un drawble por defecto
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"checked",false));
    }
}

