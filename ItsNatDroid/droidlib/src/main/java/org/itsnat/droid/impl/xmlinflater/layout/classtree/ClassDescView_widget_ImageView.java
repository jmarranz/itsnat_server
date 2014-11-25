package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_ImageView_scaleType;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_ImageView_tint;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_ImageView extends ClassDescViewBased
{
    public ClassDescView_widget_ImageView(ClassDescViewMgr classMgr,ClassDescView_view_View parentClass)
    {
        super(classMgr,"android.widget.ImageView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"adjustViewBounds",false));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"baseline",-1f));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"baselineAlignBottom",false));
        addAttrDesc(new AttrDescViewReflecFieldSetBoolean(this,"cropToPadding","mCropToPadding",false));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"maxHeight",(float)Integer.MAX_VALUE));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"maxWidth",(float)Integer.MAX_VALUE));
        addAttrDesc(new AttrDescView_widget_ImageView_scaleType(this));
        addAttrDesc(new AttrDescViewReflecMethodDrawable(this,"src","setImageDrawable","@null"));
        addAttrDesc(new AttrDescView_widget_ImageView_tint(this));
    }
}

