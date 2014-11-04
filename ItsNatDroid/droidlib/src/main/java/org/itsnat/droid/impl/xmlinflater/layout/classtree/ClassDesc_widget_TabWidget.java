package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_TabWidget extends ClassDescViewBased
{
    public ClassDesc_widget_TabWidget(ClassDescViewMgr classMgr, ClassDesc_widget_LinearLayout parentClass)
    {
        super(classMgr,"android.widget.TabWidget",parentClass);
    }

    protected void init()
    {
        super.init();

        // android:divider es un atributo definido en LinearLayout
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"tabStripEnabled","setStripEnabled",true));
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"tabStripLeft","setLeftStripDrawable",null)); // existe un Drawable por defecto
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"tabStripRight","setRightStripDrawable",null));
    }
}

