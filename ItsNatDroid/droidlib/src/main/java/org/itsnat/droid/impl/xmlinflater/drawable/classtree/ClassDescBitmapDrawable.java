package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflater.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.attr.AttrDescDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.attr.AttrDescDrawableReflecMethodMultipleName;

/**
 * Created by jmarranz on 10/11/14.
 */
public class ClassDescBitmapDrawable extends ClassDescRootElementDrawable<BitmapDrawable>
{
    public ClassDescBitmapDrawable(ClassDescDrawableMgr classMgr)
    {
        super(classMgr,"bitmap");
    }

    @Override
    public BitmapDrawable createRootDrawable(DOMElement rootElem,XMLInflaterDrawable inflaterDrawable,Context ctx)
    {
        DOMAttr attrSrc = rootElem.findDOMAttribute(InflatedXML.XMLNS_ANDROID, "src");

        Bitmap bitmap = AttrDescDrawable.getBitmap(attrSrc, ctx, classMgr.getXMLInflateRegistry());
        return new BitmapDrawable(ctx.getResources(),bitmap);
    }

    @Override
    protected boolean isAttributeIgnored(String namespaceURI,String name)
    {
        if (super.isAttributeIgnored(namespaceURI,name))
            return true;
        return isSrcAttribute(namespaceURI, name); // Se trata de forma especial en otro lugar
    }

    private static boolean isSrcAttribute(String namespaceURI,String name)
    {
        return InflatedXML.XMLNS_ANDROID.equals(namespaceURI) && name.equals("src");
    }

    @Override
    public Class<BitmapDrawable> getDrawableClass()
    {
        return BitmapDrawable.class;
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescDrawableReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap));
    }


}
