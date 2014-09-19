package org.itsnat.itsnatdroidtest.testact.util;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;

import org.itsnat.droid.ItsNatDroidException;

/**
 * Created by jmarranz on 19/06/14.
 */
public class Assert
{
    public static void assertPositive(int a)
    {
        if (a <= 0) throw new ItsNatDroidException("Failed " + a);
    }

    public static void assertPositive(float a)
    {
        if (a <= 0) throw new ItsNatDroidException("Failed " + a);
    }

    public static void assertNotNull(Object a)
    {
        if (a == null) throw new ItsNatDroidException("Failed " + a);
    }

    public static void assertFalse(boolean a)
    {
        if (a) throw new ItsNatDroidException("Failed " + a);
    }

    public static void assertTrue(boolean a)
    {
        if (!a) throw new ItsNatDroidException("Failed " + a);
    }

    public static void assertEquals(boolean a,boolean b)
    {
        if (a != b) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public static void assertEquals(int a,int b)
    {
        if (a != b) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public static void assertEquals(float a,float b)
    {
        if (a != b) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }


    public static void assertEquals(CharSequence a,CharSequence b)
    {
        assertEqualsInternal(a,b);
    }

    public static void assertEquals(Rect a,Rect b)
    {
        assertEqualsInternal(a,b);
    }

    public final static void assertEqualsInternal(Object a,Object b)
    {
        if (a == b) return;
        if (a != null && !a.equals(b) || b != null && !b.equals(a)) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public static void assertEquals(Drawable a,Drawable b)
    {
        if (!a.getClass().equals(b.getClass())) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");

        //assertEquals(a.getBounds(),b.getBounds());

        if (a instanceof ColorDrawable)
        {
            assertEquals(((ColorDrawable) a).getColor(), ((ColorDrawable) b).getColor());
        }
        else if (a instanceof GradientDrawable)
        {
            Drawable.ConstantState sa = ((GradientDrawable) a).getConstantState();
            Drawable.ConstantState sb = ((GradientDrawable) b).getConstantState();

            assertEquals((Integer)TestUtil.getField(sa,"mStrokeWidth"),(Integer)TestUtil.getField(sb,"mStrokeWidth"));
        }
        else if (a instanceof BitmapDrawable)
        {
            BitmapDrawable a_bitmap = (BitmapDrawable)a;
            BitmapDrawable b_bitmap = (BitmapDrawable)b;
            assertEquals(a_bitmap.getBitmap().getByteCount(),b_bitmap.getBitmap().getByteCount());
            assertEquals(a_bitmap.getBitmap().getWidth(),b_bitmap.getBitmap().getWidth());
            assertEquals(a_bitmap.getBitmap().getHeight(),b_bitmap.getBitmap().getHeight());
        }
        else if (a instanceof LayerDrawable)
        {
            LayerDrawable a_layer = (LayerDrawable)a;
            LayerDrawable b_layer = (LayerDrawable)b;

            assertEquals(a_layer.getIntrinsicWidth(), b_layer.getIntrinsicWidth());
            assertEquals(a_layer.getIntrinsicHeight(),b_layer.getIntrinsicHeight());
            assertEquals(a_layer.getNumberOfLayers(),a_layer.getNumberOfLayers());
            for(int i = 0; i < a_layer.getNumberOfLayers(); i++)
            {
                assertEquals(a_layer.getDrawable(i), b_layer.getDrawable(i));
            }
        }
        else if (a instanceof RotateDrawable)
        {
            RotateDrawable a_rot = (RotateDrawable)a;
            RotateDrawable b_rot = (RotateDrawable)b;

            assertEquals(a_rot.getIntrinsicWidth(), b_rot.getIntrinsicWidth());
            assertEquals(a_rot.getIntrinsicHeight(),b_rot.getIntrinsicHeight());

            assertEquals(a_rot.getDrawable(), b_rot.getDrawable());
        }
        else if (a instanceof ClipDrawable)
        {
            ClipDrawable a_clip = (ClipDrawable)a;
            ClipDrawable b_clip = (ClipDrawable)b;

            assertEquals(a_clip.getOpacity(), b_clip.getOpacity());
            assertEquals(a_clip.isStateful(), b_clip.isStateful());
            assertEquals(a_clip.getIntrinsicWidth(), b_clip.getIntrinsicWidth());
            assertEquals(a_clip.getIntrinsicHeight(), b_clip.getIntrinsicHeight());

            Drawable.ConstantState sa = a_clip.getConstantState();
            Drawable.ConstantState sb = b_clip.getConstantState();

            Class clasz = TestUtil.resolveClass(ClipDrawable.class.getName() + "$ClipState");
            assertEquals((Drawable)TestUtil.getField(sa,clasz,"mDrawable"),(Drawable)TestUtil.getField(sb,clasz,"mDrawable"));
        }
        else
            throw new ItsNatDroidException("Cannot test " + a);
    }

    public static void assertEqualsStrokeWidth(Drawable a,int b)
    {
        Drawable.ConstantState sa = ((GradientDrawable) a).getConstantState();
        assertEquals((Integer)TestUtil.getField(sa,"mStrokeWidth"),b);
    }

    public static void assertEquals(ColorStateList a,ColorStateList b)
    {
        if (!a.equals(b)) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public static void assertEquals(ObjectAnimator a,ObjectAnimator b)
    {
        // Comparamos unas cuantas propiedades
        assertEquals(a.getPropertyName(),b.getPropertyName());
//        assertEquals(a.getTarget().getClass().getName(),a.getTarget().getClass().getName());
        assertTrue(a.getInterpolator().equals(b.getInterpolator()));

        assertEquals(a.getDuration(), b.getDuration());
        assertEquals(a.getRepeatMode(),b.getRepeatMode());
    }
}
