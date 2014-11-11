package org.itsnat.droid.impl.xmlinflater.drawable;

import org.itsnat.droid.impl.xmlinflater.ClassDescMgr;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescNinePatchDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescDrawableMgr extends ClassDescMgr<ClassDescDrawable>
{
    public ClassDescDrawableMgr(XMLInflateRegistry parent)
    {
        super(parent);
        initClassDesc();
    }

    public ClassDescDrawable get(String className)
    {
        return classes.get(className);
    }

    @Override
    protected void initClassDesc()
    {
        ClassDescNinePatchDrawable ninePatch = new ClassDescNinePatchDrawable(this,null);
        addClassDesc(ninePatch);

        /*
        ClassDescView_view_View view_View = new ClassDescView_view_View(this);
        addClassDesc(view_View);

            ClassDescView_widget_AnalogClock widget_AnalogClock = new ClassDescView_widget_AnalogClock(this,view_View);
            addClassDesc(widget_AnalogClock);
*/

    }


}
