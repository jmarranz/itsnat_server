package org.itsnat.droid.impl.xmlinflater.drawable.attr;

import android.content.Context;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.TileModeUtil;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescDrawable_BitmapDrawable_tileMode extends AttrDescDrawable<BitmapDrawable>
{
    public AttrDescDrawable_BitmapDrawable_tileMode(ClassDescDrawable parent)
    {
        super(parent,"tileMode");
    }

    @Override
    public void setAttribute(BitmapDrawable draw, DOMAttr attr,XMLInflaterDrawable xmlInflaterDrawable,Context ctx)
    {
        int tileMode = this.<Integer>parseSingleName(attr.getValue(), TileModeUtil.valueMap);

        BitmapDrawable drawBitmap = (BitmapDrawable)draw;
        if (tileMode != -1)
        {
            switch (tileMode)
            {
                case 0:
                    drawBitmap.setTileModeXY(Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    break;
                case 1:
                    drawBitmap.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                    break;
                case 2:
                    drawBitmap.setTileModeXY(Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                    break;
            }
        }
        else drawBitmap.setTileModeXY(null,null);
    }
}
