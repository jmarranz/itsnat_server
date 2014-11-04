package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_AbsSeekBar extends ClassDescViewBased
{
    public ClassDesc_widget_AbsSeekBar(ClassDescViewMgr classMgr,ClassDescViewBased parentClass)
    {
        super(classMgr,"android.widget.AbsSeekBar",parentClass);
    }

    protected void init()
    {
        super.init();

        // El atributo android:thumb está documentado en SeekBar pero implementado realmente en AbsSeekBar
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"thumb",null)); // Android tiene un drawable por defecto
    }
}

