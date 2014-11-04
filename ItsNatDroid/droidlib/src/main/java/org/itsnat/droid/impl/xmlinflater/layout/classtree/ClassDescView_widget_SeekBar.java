package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_SeekBar extends ClassDescViewBased
{
    public ClassDescView_widget_SeekBar(ClassDescViewMgr classMgr,ClassDescView_widget_AbsSeekBar parentClass)
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

