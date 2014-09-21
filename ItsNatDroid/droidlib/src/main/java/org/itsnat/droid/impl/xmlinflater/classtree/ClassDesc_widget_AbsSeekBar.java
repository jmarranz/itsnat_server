package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_AbsSeekBar extends ClassDescViewBased
{
    public ClassDesc_widget_AbsSeekBar(ClassDescViewBased parentClass)
    {
        super("android.widget.AbsSeekBar",parentClass);
    }

    protected void init()
    {
        super.init();

        // El atributo android:thumb está documentado en SeekBar pero implementado realmente en AbsSeekBar
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"thumb",null)); // Android tiene un drawable por defecto
    }
}

