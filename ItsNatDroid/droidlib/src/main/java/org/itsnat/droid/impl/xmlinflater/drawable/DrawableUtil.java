package org.itsnat.droid.impl.xmlinflater.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.TypedValue;

import org.itsnat.droid.ItsNatDroidException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jmarranz on 28/11/14.
 */
public class DrawableUtil
{
    public static Bitmap createBitmapNoScale(byte[] byteArray,Resources res)
    {
        return createBitmap(byteArray,false,-1,res); // Si scale es false, bitmapDensityReference no se necesita
    }

    public static Bitmap createBitmap(byte[] byteArray,boolean scale,int bitmapDensityReference,Resources res)
    {
        // bitmapDensityReference es necesario para escalar adecuadamente un bitmap (no nine patch)
        // En ItsNat cuando los bitmaps son remotos no hay manera de elegir densidades por lo que
        // las imágenes se definen con una densidad "que valga para todos los dispositivos",
        // los prototipos de layouts por ej se pueden testear poniéndo las imágenes en la carpeta drawable-densidad que se
        // quiera por ejemplo drawable-xhdpi (320 dpi), Android sabe de qué carpeta carga el bitmap y por tanto sabe
        // qué densidad tiene la imagen original de acuerdo a la carpeta (Options.inDensity) y sabe
        // si tiene que escalar o no según la densidad del dispositivo (Options.inTargetDensity)
        // Eso mismo lo tiene que hacer ItsNat para que el resultado sea el mismo que en un layout
        // compilado por ello hay que proporcional la densidad de referencia usada durante el diseño (Options.inDensity)


        /*
        http://developer.android.com/guide/practices/screens_support.html
        ldpi (low) ~120dpi
        mdpi (medium) ~160dpi
        hdpi (high) ~240dpi
        xhdpi (extra-high) ~320dpi
        xxhdpi (extra-extra-high) ~480dpi
        xxxhdpi (extra-extra-extra-high) ~640dpi
        */

        // http://stackoverflow.com/questions/16773604/android-bitmap-scale-using-bitmapfactory-options
        // https://code.google.com/p/android/issues/detail?id=7538
        // http://stackoverflow.com/questions/8048603/android-scaling-of-images-to-screen-density
        // http://stackoverflow.com/questions/13482946/supporting-multiple-screens-using-bitmap-factory-options

        BitmapFactory.Options options = new BitmapFactory.Options();

        if (scale)
        {
            /*
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
            int bmpWidth = options.outWidth;
            int bmpHeight = options.outHeight;
            */
            options.inScaled = true;
            options.inDensity = bitmapDensityReference;
            options.inTargetDensity = res.getDisplayMetrics().densityDpi;
        }
        // decodeByteArray NO escala las imágenes aunque se use BitmapFactory.Options
        return BitmapFactory.decodeStream(new ByteArrayInputStream(byteArray), null, options);
    }

    public static Bitmap createBitmap(InputStream is,TypedValue value,Resources res)
    {
        // No hace falta pasar Options ni escalar, ya lo hace Android pues por ejemplo la densidad de referencia la obtiene de donde obtiene el recurso (xhdpi, mdpi etc)
        final Rect padding = new Rect();
        try
        {
            return BitmapFactory.decodeResourceStream(res, value, is, padding, null);
        }
        finally
        {
            try { is.close(); } catch (IOException e) { throw new ItsNatDroidException(e); }
        }
    }

    public static Drawable createImageBasedDrawable(byte[] byteArray,int bitmapDensityReference,boolean expectedNinePatch,Resources res)
    {
        if (expectedNinePatch)
            return createNinePatchDrawable(byteArray,res);

        Bitmap bitmap = createBitmap(byteArray,true,bitmapDensityReference,res);

        byte[] chunk = bitmap.getNinePatchChunk();
        boolean result = NinePatch.isNinePatchChunk(chunk);
        if (result)
        {
            // Raro pero resulta que es un NinePatch (raro porque lo normal es que se especifique la extensión .9.png)
            bitmap = createBitmapNoScale(byteArray,res); // No es necesario escalar pues por definición es "flexible", Android no escala, si quitamos esta línea fallarán los tests de drawable compilado vs dynamic
            return createNinePatchDrawable(bitmap,res);
        }
        else
        {
            return new BitmapDrawable(res, bitmap);
        }
    }

    public static NinePatchDrawable createNinePatchDrawable(byte[] byteArray,Resources res)
    {
        Bitmap bitmap = createBitmapNoScale(byteArray,res); // No es necesario escalar pues por definición es "flexible", Android no escala, si pasamos scale = true fallarán los tests de drawable compilado vs dynamic
        return createNinePatchDrawable(bitmap,res);
    }

    public static NinePatchDrawable createNinePatchDrawable(Bitmap bitmap,Resources res)
    {
        byte[] chunk = bitmap.getNinePatchChunk();
        boolean result = NinePatch.isNinePatchChunk(chunk);
        if (!result) throw new ItsNatDroidException("Expected a 9 patch png, put a valid 9 patch in /res/drawable folder, generate the .apk (/build/outputs/apk in Android Studio), decompress as a zip and copy the png file");

        return new NinePatchDrawable(res, bitmap, chunk, new Rect(), "XML 9 patch");
    }
}
