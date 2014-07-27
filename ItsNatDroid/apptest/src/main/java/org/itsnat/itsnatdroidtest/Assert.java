package org.itsnat.itsnatdroidtest;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.itsnat.droid.ItsNatDroidException;

/**
 * Created by jmarranz on 19/06/14.
 */
public class Assert
{
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

    public static void assertEqualsRelativeLayoutLayoutParams(View a,View b)
    {
        RelativeLayout.LayoutParams a_params = (RelativeLayout.LayoutParams)a.getLayoutParams();
        RelativeLayout.LayoutParams b_params = (RelativeLayout.LayoutParams)a.getLayoutParams();
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
        LinearLayout.LayoutParams b_params = (LinearLayout.LayoutParams)a.getLayoutParams();

        assertEquals(a_params.gravity,b_params.gravity);
    }
}
