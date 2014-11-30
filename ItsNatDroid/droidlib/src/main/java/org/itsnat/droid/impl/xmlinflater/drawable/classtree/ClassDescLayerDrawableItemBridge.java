package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.drawable.ChildElementDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.LayerDrawableItem;
import org.itsnat.droid.impl.xmlinflated.drawable.LayerDrawableItemBridge;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;

/**
 * Created by jmarranz on 10/11/14.
 */
public class ClassDescLayerDrawableItemBridge extends ClassDescChildElementDrawable<LayerDrawableItemBridge>
{
    public ClassDescLayerDrawableItemBridge(ClassDescDrawableMgr classMgr)
    {
        super(classMgr,"layer-list:item:*");
        // En la documentación se sugiere que sólo se puede poner un <bitmap> pero no es así, se puede poner cualquier drawable
    }

    @Override
    public Class<LayerDrawableItemBridge> getDrawableClass()
    {
        return LayerDrawableItemBridge.class;
    }

    @Override
    public ChildElementDrawable createChildElementDrawable(DOMElement domElement,XMLInflaterDrawable inflaterDrawable,ChildElementDrawable parentChildDrawable,Context ctx)
    {
        String name = domElement.getName();
        ClassDescRootElementDrawable classDescBridge = (ClassDescRootElementDrawable)getClassDescDrawableMgr().get(name);

        Drawable childDrawable = classDescBridge.createRootDrawable(domElement,inflaterDrawable,ctx);

        return new LayerDrawableItemBridge(classDescBridge,(LayerDrawableItem)parentChildDrawable,childDrawable);
    }

    @Override
    protected boolean isAttributeIgnored(LayerDrawableItemBridge draw,String namespaceURI,String name)
    {
        if (super.isAttributeIgnored(draw,namespaceURI,name))
            return true;

        ClassDescRootElementDrawable classDescBridge = draw.getClassDescRootDrawableBridge();
        return classDescBridge.isAttributeIgnored(draw,namespaceURI,name);
    }

    public boolean setAttribute(LayerDrawableItemBridge draw,DOMAttr attr,XMLInflaterDrawable xmlInflaterDrawable, Context ctx)
    {
        ClassDescRootElementDrawable classDescBridge = draw.getClassDescRootDrawableBridge();
        Drawable drawable = draw.getDrawable();
        return classDescBridge.setAttribute(drawable,attr,xmlInflaterDrawable,ctx);
    }

}
