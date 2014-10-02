package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_SeekBar extends ClassDescViewBased
{
    public ClassDesc_widget_SeekBar(ClassDescViewMgr classMgr,ClassDesc_widget_AbsSeekBar parentClass)
    {
        super(classMgr,"android.widget.SeekBar",parentClass);
    }

    protected void init()
    {
        super.init();

        // El atributo android:thumb está documentado en SeekBar pero implementado realmente en AbsSeekBar
        // El poner este comentario aquí es la única razón de haber creado esta clase, en teoría no es necesaria
    }
}

