package org.itsnat.droid.impl.xmlinflater.drawable;

import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.xmlinflater.ClassDescMgr;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawableUnknown;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescDrawableMgr extends ClassDescMgr<ClassDescDrawable,Drawable>
{
    public ClassDescDrawableMgr(XMLInflateRegistry parent)
    {
        super(parent);
        initClassDesc();
    }

    @Override
    protected ClassDescDrawable createClassDescUnknown(String className, ClassDescDrawable parentClass)
    {
        return new ClassDescDrawableUnknown(this, className, parentClass);
    }

    @Override
    protected void initClassDesc()
    {
        /*
        ClassDescView_view_View view_View = new ClassDescView_view_View(this);
        addClassDesc(view_View);

            ClassDescView_widget_AnalogClock widget_AnalogClock = new ClassDescView_widget_AnalogClock(this,view_View);
            addClassDesc(widget_AnalogClock);
*/

    }


}
