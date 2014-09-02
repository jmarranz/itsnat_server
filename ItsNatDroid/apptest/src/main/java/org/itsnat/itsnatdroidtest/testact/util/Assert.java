package org.itsnat.itsnatdroidtest.testact.util;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
        else throw new ItsNatDroidException("Cannot test");
    }

    public static void assertEquals(ColorStateList a,ColorStateList b)
    {
        if (!a.equals(b)) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }



    public static void assertEqualsRelativeLayoutLayoutParams(View a,View b)
    {
        RelativeLayout.LayoutParams a_params = (RelativeLayout.LayoutParams)a.getLayoutParams();
        RelativeLayout.LayoutParams b_params = (RelativeLayout.LayoutParams)b.getLayoutParams();
        int[] a_rules = a_params.getRules();
        int[] b_rules = a_params.getRules();
        assertEquals(a_rules.length,b_rules.length);
        for(int i = 0; i < a_rules.length; i++)
        {
            assertEquals(a_rules[i],b_rules[i]);
        }
    }

    public static void assertEqualsLinearLayoutLayoutParams(View a,View b)
    {
        LinearLayout.LayoutParams a_params = (LinearLayout.LayoutParams)a.getLayoutParams();
        LinearLayout.LayoutParams b_params = (LinearLayout.LayoutParams)b.getLayoutParams();

        assertEquals(a_params.gravity,b_params.gravity);
        assertEquals(a_params.weight,b_params.weight);
    }

    public static void assertEqualsViewGroupMarginLayoutParams(View a, View b)
    {
        ViewGroup.MarginLayoutParams a_params = (ViewGroup.MarginLayoutParams)a.getLayoutParams();
        ViewGroup.MarginLayoutParams b_params = (ViewGroup.MarginLayoutParams)b.getLayoutParams();

        assertEquals(a_params.topMargin,b_params.topMargin);
        assertEquals(a_params.leftMargin,b_params.leftMargin);
        assertEquals(a_params.bottomMargin,b_params.bottomMargin);
        assertEquals(a_params.rightMargin,b_params.rightMargin);
    }
}
