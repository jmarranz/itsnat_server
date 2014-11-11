package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.attr.AttrDescDrawableReflecMethodBoolean;

/**
 * Created by jmarranz on 10/11/14.
 */
public class ClassDescNinePatchDrawable extends ClassDescDrawable
{
    public ClassDescNinePatchDrawable(ClassDescDrawableMgr classMgr, ClassDescDrawable parentClass)
    {
        super(classMgr,NinePatchDrawable.class.getName(), parentClass);
    }

    protected void init()
    {
        super.init();

        // Atributos analizados para Android 4.4 (API Level: 19) pero teniendo en cuenta que sólo soportamos Level 15 (Android 4.0.3)

        addAttrDesc(new AttrDescDrawableReflecMethodBoolean(this, "dither", false));
    }

    @Override
    public Drawable createRootDrawable(InflatedDrawable inflatedDrawable, Resources res)
    {
        // Ver: http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.3_r1/android/graphics/drawable/Drawable.java#Drawable.createFromXmlInner%28android.content.res.Resources%2Corg.xmlpull.v1.XmlPullParser%2Candroid.util.AttributeSet%29
        NinePatchDrawable drawable = (NinePatchDrawable)super.createRootDrawable(inflatedDrawable, res);
        drawable.setTargetDensity(res.getDisplayMetrics());
        return drawable;
    }
}
