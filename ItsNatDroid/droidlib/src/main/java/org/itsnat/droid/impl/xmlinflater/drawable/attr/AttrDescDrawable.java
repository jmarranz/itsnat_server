package org.itsnat.droid.impl.xmlinflater.drawable.attr;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMAttrRemote;
import org.itsnat.droid.impl.xmlinflater.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescDrawable extends AttrDesc
{
    public AttrDescDrawable(ClassDescDrawable parent, String name)
    {
        super(parent,name);
    }

    public static Bitmap getBitmap(DOMAttr attr,Context ctx,XMLInflateRegistry xmlInflateRegistry)
    {
        if (attr instanceof DOMAttrRemote)
        {
            // http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.3_r1/android/graphics/drawable/Drawable.java#Drawable.createFromXmlInner%28android.content.res.Resources%2Corg.xmlpull.v1.XmlPullParser%2Candroid.util.AttributeSet%29
            DOMAttrRemote attrRem = (DOMAttrRemote)attr;
            byte[] byteArray = (byte[])attrRem.getRemoteResource();
            BitmapFactory.Options options = new BitmapFactory.Options();
            return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length,options);
        }
        else
        {
            String attrValue = attr.getValue();
            if (isResource(attrValue))
            {
                // http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.3_r1/android/graphics/drawable/NinePatchDrawable.java#240
                int resId = xmlInflateRegistry.getIdentifier(attrValue,ctx);
                if (resId <= 0) return null;
                Resources res = ctx.getResources();
                final TypedValue value = new TypedValue();
                final Rect padding = new Rect();
                final BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap;
                InputStream is = res.openRawResource(resId, value);
                try
                {
                    bitmap = BitmapFactory.decodeResourceStream(res, value, is, padding, options);
                }
                finally
                {
                    try { is.close(); }
                    catch (IOException e) { throw new ItsNatDroidException(e); }
                }

                return bitmap;
            }

            throw new ItsNatDroidException("Cannot process " + attrValue);
        }
    }

    public abstract void setAttribute(Drawable draw, DOMAttr attr,XMLInflaterDrawable xmlInflaterDrawable,Context ctx);

    public abstract void removeAttribute(Drawable draw,XMLInflaterDrawable xmlInflaterDrawable, Context ctx);
}


