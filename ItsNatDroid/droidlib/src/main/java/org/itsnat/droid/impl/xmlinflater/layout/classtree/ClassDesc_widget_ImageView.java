package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodColor;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecFieldSetBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_ImageView_scaleType;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ImageView extends ClassDescViewBased
{
    public ClassDesc_widget_ImageView(ClassDescViewMgr classMgr,ClassDesc_view_View parentClass)
    {
        super(classMgr,"android.widget.ImageView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodBoolean(this,"adjustViewBounds",false));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"baseline",-1f));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"baselineAlignBottom",false));
        addAttrDesc(new AttrDescReflecFieldSetBoolean(this,"cropToPadding","mCropToPadding",false));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"maxHeight",(float)Integer.MAX_VALUE));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"maxWidth",(float)Integer.MAX_VALUE));
        addAttrDesc(new AttrDesc_widget_ImageView_scaleType(this));
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"src","setImageDrawable","@null"));
        addAttrDesc(new AttrDescReflecMethodColor(this,"tint","setColorFilter","#000000"));


    }
}

