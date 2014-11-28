package org.itsnat.droid.impl.xmlinflater.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

/**
 * Created by jmarranz on 28/11/14.
 */
public class DrawableUtil
{
    public static Bitmap createBitmap(byte[] byteArray)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length,options);
    }

    public static Drawable createImageBasedDrawable(byte[] byteArray,Resources res)
    {
        Bitmap bitmap = createBitmap(byteArray);

        byte[] chunk = bitmap.getNinePatchChunk();
        boolean result = NinePatch.isNinePatchChunk(chunk);
        if (result)
            return new NinePatchDrawable(res,bitmap,chunk,new Rect(),"XML 9 patch");
        else
            return new BitmapDrawable(res,bitmap);
    }
}
