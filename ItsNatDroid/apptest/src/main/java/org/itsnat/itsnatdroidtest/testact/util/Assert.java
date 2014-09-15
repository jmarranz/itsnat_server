package org.itsnat.itsnatdroidtest.testact.util;

import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

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
        else throw new ItsNatDroidException("Cannot test");
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


}
