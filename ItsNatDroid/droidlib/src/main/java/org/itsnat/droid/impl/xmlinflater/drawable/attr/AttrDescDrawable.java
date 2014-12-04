package org.itsnat.droid.impl.xmlinflater.drawable.attr;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.TypedValue;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMAttrDynamic;
import org.itsnat.droid.impl.dom.DOMAttrLocalResource;
import org.itsnat.droid.impl.xmlinflater.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.itsnat.droid.impl.xmlinflater.drawable.DrawableUtil;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;

import java.io.InputStream;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescDrawable<Tdrawable> extends AttrDesc
{
    public AttrDescDrawable(ClassDescDrawable parent, String name)
    {
        super(parent,name);
    }

    public static Bitmap getBitmapNoScale(DOMAttr attr,Context ctx,XMLInflateRegistry xmlInflateRegistry)
    {
        return getBitmap(attr,false,-1,ctx,xmlInflateRegistry);
    }

    public static Bitmap getBitmap(DOMAttr attr,boolean scale,int bitmapDensityReference,Context ctx,XMLInflateRegistry xmlInflateRegistry)
    {
        if (attr instanceof DOMAttrDynamic)
        {
            // http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.3_r1/android/graphics/drawable/Drawable.java#Drawable.createFromXmlInner%28android.content.res.Resources%2Corg.xmlpull.v1.XmlPullParser%2Candroid.util.AttributeSet%29
            DOMAttrDynamic attrDyn = (DOMAttrDynamic)attr;
            byte[] byteArray = (byte[])attrDyn.getResource();
            Resources res = ctx.getResources();
            return DrawableUtil.createBitmap(byteArray,scale,bitmapDensityReference,res);
        }
        else if (attr instanceof DOMAttrLocalResource)
        {
            String attrValue = attr.getValue();
            if (XMLInflateRegistry.isResource(attrValue))
            {
                // http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.3_r1/android/graphics/drawable/NinePatchDrawable.java#240
                int resId = xmlInflateRegistry.getIdentifier(attrValue,ctx);
                if (resId <= 0) return null;
                final TypedValue value = new TypedValue();
                Resources res = ctx.getResources();
                InputStream is = res.openRawResource(resId, value);
                return DrawableUtil.createBitmap(is,value,res);
            }

            throw new ItsNatDroidException("Cannot process " + attrValue);
        }
        else throw new ItsNatDroidException("Internal Error");
    }


    public abstract void setAttribute(Tdrawable draw, DOMAttr attr,XMLInflaterDrawable xmlInflaterDrawable,Context ctx);

    public void removeAttribute(Tdrawable draw,XMLInflaterDrawable xmlInflaterDrawable, Context ctx)
    {
        throw new ItsNatDroidException("Internal error");
    }


}


