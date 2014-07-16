package org.itsnat.droid.impl.util;

import android.content.res.Resources;
import android.util.TypedValue;

import org.itsnat.droid.ItsNatDroidException;

import java.io.UnsupportedEncodingException;

/**
 * Se debería usar TypedValue.complexToDimensionPixelOffset y complexToDimensionPixelSize
 * en el caso de necesitar enteros pero es un follón
 *
 * Created by jmarranz on 30/04/14.
 */
public class ValueUtil
{
    public static float dpToPixel(float value,Resources res)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, res.getDisplayMetrics());
    }

    public static float spToPixel(float value,Resources res)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, res.getDisplayMetrics());
    }

    public static float inToPixel(float value,Resources res)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, value, res.getDisplayMetrics());
    }

    public static float mmToPixel(float value,Resources res)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, value, res.getDisplayMetrics());
    }

    public static boolean isEmpty(String str)
    {
        return str == null || str.isEmpty();
    }

    public static String toString(byte[] data,String encoding)
    {
        try { return new String(data,encoding); }
        catch (UnsupportedEncodingException ex) { throw new ItsNatDroidException(ex); }
    }
}
