package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodInt;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_RatingBar extends ClassDescViewBased
{
    public ClassDescView_widget_RatingBar(ClassDescViewMgr classMgr,ClassDescViewBased parentClass)
    {
        super(classMgr,"android.widget.RatingBar",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"isIndicator",false));
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"numStars",5));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"rating",0f));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"stepSize",0.5f));
    }
}

