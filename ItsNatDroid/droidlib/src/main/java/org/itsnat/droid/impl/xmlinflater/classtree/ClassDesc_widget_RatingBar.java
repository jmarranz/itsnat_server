package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodInt;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_RatingBar extends ClassDescViewBased
{
    public ClassDesc_widget_RatingBar(ClassDescViewMgr classMgr,ClassDescViewBased parentClass)
    {
        super(classMgr,"android.widget.RatingBar",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodBoolean(this,"isIndicator",false));
        addAttrDesc(new AttrDescReflecMethodInt(this,"numStars",5));
        addAttrDesc(new AttrDescReflecMethodFloat(this,"rating",0f));
        addAttrDesc(new AttrDescReflecMethodFloat(this,"stepSize",0.5f));
    }
}

