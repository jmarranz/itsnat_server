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
import android.graphics.drawable.StateListDrawable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import org.itsnat.droid.ItsNatDroidException;

import java.util.Iterator;

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

    public static void assertEquals(long a,long b)
    {
        if (a != b) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public static void assertEquals(float a,float b)
    {
        if (a != b) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public static void assertEquals(String a,String b)
    {
        assertEqualsInternal(a,b);
    }

    public static void assertEquals(CharSequence a,String b)
    {
        // El CharSequence puede ser por ejemplo Spannable pero en este caso queremos comparar "el valor textual"
        assertEqualsInternal(a.toString(), b);
    }

    public static void assertEquals(SpannableStringBuilder a,SpannableStringBuilder b)
    {
        // En teoría se puede hacer un test más completo
        assertEqualsInternal(a.toString(),b.toString());
    }

    public static void assertEquals(CharSequence a,CharSequence b)
    {
        // El CharSequence puede ser por ejemplo Spannable
        assertEqualsInternal(a.getClass(),b.getClass());
        assertEqualsInternal(a.toString(),b.toString());
    }

    public static void assertEquals(Rect a,Rect b)
    {
        assertEqualsInternal(a,b);
    }

    public static void assertEquals(InputFilter.LengthFilter a,InputFilter.LengthFilter b)
    {
        int a_int = (Integer)TestUtil.getField(a,"mMax");
        int b_int = (Integer)TestUtil.getField(b,"mMax");
        assertEquals(a_int,b_int);
    }


    public final static void assertEquals(Object a,Object b)
    {
        assertEqualsInternal(a,b);
    }

    public final static void assertEqualsInternal(Object a,Object b)
    {
        if (a == b) return;
        if (a != null && !a.equals(b) || b != null && !b.equals(a)) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public final static void assertEquals(Boolean a,Boolean b)
    {
        assertEquals((boolean)a,(boolean)b);
    }

    public final static void assertEquals(Integer a,int b)
    {
        assertEquals((int)a,b);
    }

    public final static void assertEquals(Integer a,Integer b)
    {
        assertEquals((int)a,(int)b);
    }

    public final static void assertEquals(Long a,Long b)
    {
        assertEquals((long)a,(long)b);
    }

    public final static void assertEquals(Float a,float b)
    {
        assertEquals((float)a,b);
    }

    public final static void assertEquals(Float a,Float b)
    {
        assertEquals((float)a,(float)b);
    }

    public final static void assertEquals(int[][] a,int[][] b)
    {
        if (a.length != b.length) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");

        for(int i = 0; i < a.length; i++)
            assertEquals(a[i],b[i]);
    }

    public final static void assertEquals(int[] a,int[] b)
    {
        if (a.length != b.length) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");

        for(int i = 0; i < a.length; i++)
            assertEquals(a[i],b[i]);
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

            assertEquals((Integer) TestUtil.getField(sa, "mStrokeWidth"), (Integer) TestUtil.getField(sb, "mStrokeWidth"));
            // mSolidColor ya no existe en level 21: assertEquals((Integer)TestUtil.getField(sa,"mSolidColor"),(Integer)TestUtil.getField(sb,"mSolidColor"));
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
        else if (a instanceof StateListDrawable)
        {
            StateListDrawable a_state = (StateListDrawable) a;
            StateListDrawable b_state = (StateListDrawable) b;

/*
            mStateListState/mStateSets
            StateListState a_stList = a_state.getStateListState();
*/
            int[] a_stateArr = a_state.getState();
            int[] b_stateArr = b_state.getState();
            assertEquals(a_stateArr.length,b_stateArr.length);
            for(int i = 0; i < a_stateArr.length; i++)
            {
                assertEquals(a_stateArr[i],b_stateArr[i]);
            }
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
        assertEquals((int[][])TestUtil.getField(a,"mStateSpecs"),(int[][])TestUtil.getField(b,"mStateSpecs"));
        assertEquals((int[])TestUtil.getField(a,"mColors"),(int[])TestUtil.getField(b,"mColors"));
        assertEquals((Integer)TestUtil.getField(a,"mDefaultColor"),(Integer)TestUtil.getField(b,"mDefaultColor"));

//        if (!a.equals(b)) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
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

    public static void assertEquals(AnimationSet a,AnimationSet b)
    {
        // Comparamos unas cuantas propiedades

        assertEquals(a.getDuration(),b.getDuration());
        //assertEquals(a.getStartTime(),b.getStartTime());
        //assertEquals(a.getStartOffset(),b.getStartOffset());

        assertEquals(a.getAnimations().size(),b.getAnimations().size());

        Iterator<Animation> a_it = a.getAnimations().iterator();
        Iterator<Animation> b_it = b.getAnimations().iterator();
        while(a_it.hasNext())
        {
            Animation a_anim = a_it.next();
            Animation b_anim = b_it.next();
            assertEquals(a_anim,b_anim);
        }
    }

    public static void assertEquals(Animation a,Animation b)
    {
        if (a instanceof TranslateAnimation)
        {
            TranslateAnimation a_trans = (TranslateAnimation)a;
            TranslateAnimation b_trans = (TranslateAnimation)b;

            // Comparamos unas cuantas propiedades
            assertEquals(a_trans.getDuration(),b_trans.getDuration());
//            assertEquals(a_trans.getStartOffset(),b_trans.getStartOffset());
//            assertEquals(a_trans.getStartTime(),b_trans.getStartTime());
            assertEquals(a_trans.getInterpolator(),b_trans.getInterpolator());
        }
        else
            throw new ItsNatDroidException("Cannot test " + a);
    }

    public static void assertEquals(android.view.animation.Interpolator a,android.view.animation.Interpolator b)
    {
        assertEquals(a.getClass(),b.getClass());
        assertEquals(a.getInterpolation(5),a.getInterpolation(5));
    }

}
