package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;

import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.drawable.ChildElementDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.LayerDrawableItem;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.attr.AttrDescDrawableReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.drawable.attr.AttrDescDrawableReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.attr.AttrDescDrawableReflecMethodId;

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
    public ChildElementDrawable createChildElementDrawable(DOMElement domElement,XMLInflaterDrawable inflaterDrawable,ChildElementDrawable parentChildDrawable,Context ctx)
    {
        return new LayerDrawableItem();
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescDrawableReflecMethodDimensionInt<LayerDrawableItem>(this,"bottom"));
        addAttrDesc(new AttrDescDrawableReflecMethodDrawable(this,"drawable"));
        addAttrDesc(new AttrDescDrawableReflecMethodId(this,"id"));
        addAttrDesc(new AttrDescDrawableReflecMethodDimensionInt(this,"left"));
        // android:paddingMode es level 21
        addAttrDesc(new AttrDescDrawableReflecMethodDimensionInt(this,"right"));
        addAttrDesc(new AttrDescDrawableReflecMethodDimensionInt(this,"top"));
    }

}
