package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.drawable.ChildElementDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.LayerDrawableItem;
import org.itsnat.droid.impl.xmlinflated.drawable.LayerDrawableItemBitmap;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;

/**
 * Created by jmarranz on 10/11/14.
 */
public class ClassDescLayerDrawableItemBitmap extends ClassDescChildElementDrawable<LayerDrawableItemBitmap>
{
    protected ClassDescBitmapDrawable bitmapClassDesc;

    public ClassDescLayerDrawableItemBitmap(ClassDescDrawableMgr classMgr)
    {
        super(classMgr,"layer-list:item:bitmap");

        this.bitmapClassDesc = new ClassDescBitmapDrawable(classMgr);
    }

    @Override
    public Class<LayerDrawableItemBitmap> getDrawableClass()
    {
        return LayerDrawableItemBitmap.class;
    }

    @Override
    public ChildElementDrawable createChildElementDrawable(DOMElement domElement,XMLInflaterDrawable inflaterDrawable,ChildElementDrawable parentChildDrawable,Context ctx)
    {
        BitmapDrawable bitmapDrawable = bitmapClassDesc.createRootDrawable(domElement,inflaterDrawable,ctx);

        return new LayerDrawableItemBitmap((LayerDrawableItem)parentChildDrawable,bitmapDrawable);
    }

    @Override
    protected boolean isAttributeIgnored(String namespaceURI,String name)
    {
        if (super.isAttributeIgnored(namespaceURI,name))
            return true;
        return bitmapClassDesc.isAttributeIgnored(namespaceURI,name);
    }

    public boolean setAttribute(LayerDrawableItemBitmap draw,DOMAttr attr,XMLInflaterDrawable xmlInflaterDrawable, Context ctx)
    {
        BitmapDrawable bitMapDraw = draw.getBitmapDrawable();
        return bitmapClassDesc.setAttribute(bitMapDraw,attr,xmlInflaterDrawable,ctx);
    }

    protected void init()
    {
        super.init();

        bitmapClassDesc.init();
    }

}
