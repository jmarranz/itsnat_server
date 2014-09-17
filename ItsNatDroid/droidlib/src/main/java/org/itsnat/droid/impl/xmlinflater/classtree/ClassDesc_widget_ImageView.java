package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecColor;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_ImageView_scaleType;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ImageView extends ClassDescViewBased
{
    public ClassDesc_widget_ImageView(ClassDesc_view_View parentClass)
    {
        super("android.widget.ImageView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecBoolean(this,"adjustViewBounds",false));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"baseline",-1f));
        addAttrDesc(new AttrDescReflecBoolean(this,"baselineAlignBottom",false));
        addAttrDesc(new AttrDescReflecFieldSetBoolean(this,"cropToPadding","mCropToPadding",false));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"maxHeight",(float)Integer.MAX_VALUE));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"maxWidth",(float)Integer.MAX_VALUE));
        addAttrDesc(new AttrDesc_widget_ImageView_scaleType(this));
        addAttrDesc(new AttrDescReflecDrawable(this,"src","setImageDrawable"));
        addAttrDesc(new AttrDescReflecColor(this,"tint","setColorFilter","#000000"));


    }
}

