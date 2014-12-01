package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawableChild;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawableChildBridge;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawableRoot;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;

/**
 * Created by jmarranz on 10/11/14.
 */
public class ClassDescElementDrawableChildBridge extends ClassDescElementDrawableChild<ElementDrawableChildBridge>
{
    public static final String NAME = "*";

    public ClassDescElementDrawableChildBridge(ClassDescDrawableMgr classMgr)
    {
        super(classMgr,NAME);
    }

    @Override
    public Class<ElementDrawableChildBridge> getDrawableClass()
    {
        return ElementDrawableChildBridge.class;
    }

    @Override
    public ElementDrawableChild createChildElementDrawable(DOMElement domElement,XMLInflaterDrawable inflaterDrawable,ElementDrawable parentChildDrawable,Context ctx)
    {
        String name = domElement.getName();
        ClassDescRootElementDrawable classDescBridge = (ClassDescRootElementDrawable)getClassDescDrawableMgr().get(name);
        ElementDrawableRoot childDrawable = classDescBridge.createRootElementDrawable(domElement, inflaterDrawable, ctx);
        Drawable drawable = childDrawable.getDrawable();

        return new ElementDrawableChildBridge(classDescBridge,parentChildDrawable,drawable);
    }

    @Override
    protected boolean isAttributeIgnored(ElementDrawableChildBridge draw,String namespaceURI,String name)
    {
        if (super.isAttributeIgnored(draw,namespaceURI,name))
            return true;

        ClassDescRootElementDrawable classDescBridge = draw.getClassDescRootDrawableBridge();
        return classDescBridge.isAttributeIgnored(draw,namespaceURI,name);
    }

    public boolean setAttribute(ElementDrawableChildBridge draw,DOMAttr attr,XMLInflaterDrawable xmlInflaterDrawable, Context ctx)
    {
        ClassDescRootElementDrawable classDescBridge = draw.getClassDescRootDrawableBridge();
        Drawable drawable = draw.getDrawable();
        return classDescBridge.setAttribute(drawable,attr,xmlInflaterDrawable,ctx);
    }

}
