package org.itsnat.droid.impl.xmlinflater.drawable.attr;

import android.content.Context;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescDrawable_BitmapDrawable_tileMode extends AttrDescDrawable<BitmapDrawable>
{
    public static final Map<String,Integer> valueMap = new HashMap<String,Integer>( 4 );
    static
    {
        valueMap.put("disabled", -1);
        valueMap.put("clamp", 0);
        valueMap.put("repeat",1);
        valueMap.put("mirror",2);
    }

    public AttrDescDrawable_BitmapDrawable_tileMode(ClassDescDrawable parent, String name)
    {
        super(parent,name);
    }

    @Override
    public void setAttribute(BitmapDrawable draw, DOMAttr attr,XMLInflaterDrawable xmlInflaterDrawable,Context ctx)
    {
        int tileMode = this.<Integer>parseSingleName(attr.getValue(), valueMap);

        Shader.TileMode tileModeObj;
        if (tileMode != -1)
        {
            switch (tileMode)
            {
                case 0:
                    tileModeObj = Shader.TileMode.CLAMP;
                    break;
                case 1:
                    tileModeObj = Shader.TileMode.REPEAT;
                    break;
                case 2:
                    tileModeObj = Shader.TileMode.MIRROR;
                    break;
                default: // Imposible este caso
                    tileModeObj = null;
                    break;
            }
        }
        else tileModeObj = null;

        String name = getName();
        if ("tileMode".equals(name))
            draw.setTileModeXY(tileModeObj,tileModeObj);
        else if ("tileModeX".equals(name))
            draw.setTileModeX(tileModeObj);
        else if ("tileModeY".equals(name))
            draw.setTileModeY(tileModeObj);
    }
}
