package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.NinePatchDrawable;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawableRoot;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.DrawableUtil;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.attr.AttrDescDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.attr.AttrDescDrawableReflecMethodBoolean;

/**
 * Created by jmarranz on 10/11/14.
 */
public class ClassDescNinePatchDrawable extends ClassDescRootElementDrawable<NinePatchDrawable>
{
    public ClassDescNinePatchDrawable(ClassDescDrawableMgr classMgr)
    {
        super(classMgr,"nine-patch");
    }

    @Override
    public ElementDrawableRoot createRootElementDrawable(DOMElement rootElem, XMLInflaterDrawable inflaterDrawable, Context ctx)
    {
        // http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.3_r1/android/graphics/drawable/Drawable.java#Drawable.createFromXmlInner%28android.content.res.Resources%2Corg.xmlpull.v1.XmlPullParser%2Candroid.util.AttributeSet%29
        // http://stackoverflow.com/questions/5079868/create-a-ninepatch-ninepatchdrawable-in-runtime

        DOMAttr attrSrc = rootElem.findDOMAttribute(InflatedXML.XMLNS_ANDROID, "src");
        if (attrSrc == null) throw new ItsNatDroidException("Missing src attribute in element " + rootElem.getName());

        // No necesita escalar pues por definición es "flexible"
        Bitmap bitmap = AttrDescDrawable.getBitmap(attrSrc,true,inflaterDrawable.getBitmapDensityReference(),ctx,classMgr.getXMLInflateRegistry());

        NinePatchDrawable drawable = DrawableUtil.createNinePatchDrawable(bitmap,ctx.getResources());
        return new ElementDrawableRoot(drawable,null);
    }

    @Override
    protected boolean isAttributeIgnored(NinePatchDrawable draw,String namespaceURI,String name)
    {
        if (super.isAttributeIgnored(draw,namespaceURI,name))
            return true;
        return isSrcAttribute(namespaceURI, name); // Se trata de forma especial en otro lugar
    }

    private static boolean isSrcAttribute(String namespaceURI,String name)
    {
        return InflatedXML.XMLNS_ANDROID.equals(namespaceURI) && name.equals("src");
    }

    @Override
    public Class<NinePatchDrawable> getDrawableClass()
    {
        return NinePatchDrawable.class;
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescDrawableReflecMethodBoolean(this, "dither"));
    }


}
