package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;

import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.drawable.ChildElementDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.LayerDrawableItem;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.attr.AttrDescDrawableReflecMethodDimensionFloat;

/**
 * Created by jmarranz on 10/11/14.
 */
public class ClassDescLayerDrawableItem extends ClassDescChildElementDrawable<LayerDrawableItem>
{
    public ClassDescLayerDrawableItem(ClassDescDrawableMgr classMgr)
    {
        super(classMgr,"layer-list:item");
    }

    @Override
    public Class<LayerDrawableItem> getDrawableClass()
    {
        return LayerDrawableItem.class;
    }

    @Override
    public ChildElementDrawable createChildElementDrawable(DOMElement domElement,XMLInflaterDrawable inflaterDrawable,Context ctx)
    {
        return new LayerDrawableItem();
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescDrawableReflecMethodDimensionFloat<LayerDrawableItem>(this,"bottom"));
        //addAttrDesc(new AttrDescDrawableReflecMethodBoolean(this, "dither", true));
    }


}
