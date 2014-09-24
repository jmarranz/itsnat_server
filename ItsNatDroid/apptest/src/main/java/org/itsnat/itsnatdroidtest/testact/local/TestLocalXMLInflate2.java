package org.itsnat.itsnatdroidtest.testact.local;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsSeekBar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.itsnatdroidtest.testact.util.TestUtil;

import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertEquals;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertFalse;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertNotNull;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertTrue;


/**
 * Created by jmarranz on 19/06/14.
 */
public class TestLocalXMLInflate2
{
    public static void test(ScrollView compRoot, ScrollView parsedRoot)
    {
        Resources res = compRoot.getContext().getResources();

        // comp = "Layout compiled"
        // parsed = "Layout dynamically parsed"
        // No podemos testear layout_width/height en el ScrollView root porque un View está desconectado y al desconectar el width y el height se ponen a 0
        // assertEquals(comp.getWidth(),parsed.getWidth());
        // assertEquals(comp.getHeight(),parsed.getHeight());

        LinearLayout comp = (LinearLayout) compRoot.getChildAt(0);
        LinearLayout parsed = (LinearLayout) parsedRoot.getChildAt(0);

        assertEquals(comp.getOrientation(), parsed.getOrientation());

        int childCount = 0;

        // buttonBack
        {
            Button compButton = (Button) comp.getChildAt(childCount);
            Button parsedButton = (Button) parsed.getChildAt(childCount);
            assertEquals(compButton.getId(), parsedButton.getId());
            assertEquals(compButton.getText(), parsedButton.getText());
        }

        childCount++;

        // buttonReload
        {
            Button compButton = (Button) comp.getChildAt(childCount);
            Button parsedButton = (Button) parsed.getChildAt(childCount);
            assertEquals(compButton.getId(), parsedButton.getId());
            assertEquals(compButton.getText(), parsedButton.getText());
        }

        childCount++;

        // Test ImageView
        {
            final ImageView compLayout = (ImageView) comp.getChildAt(childCount);
            final ImageView parsedLayout = (ImageView) parsed.getChildAt(childCount);

            // android:adjustViewBounds (método get es Level 16)
            assertTrue((Boolean) TestUtil.getField(compLayout, "mAdjustViewBounds"));
            assertEquals((Boolean) TestUtil.getField(compLayout, "mAdjustViewBounds"), (Boolean) TestUtil.getField(parsedLayout, "mAdjustViewBounds"));

            assertEquals(compLayout.getBaseline(), ValueUtil.dpToPixelInt(40, res));
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    assertEquals(compLayout.getBaseline(), parsedLayout.getBaseline());
                }
            });

            assertTrue(compLayout.getBaselineAlignBottom());
            assertEquals(compLayout.getBaselineAlignBottom(), parsedLayout.getBaselineAlignBottom());

            assertTrue((Boolean) TestUtil.getField(compLayout, "mCropToPadding"));
            assertEquals((Boolean) TestUtil.getField(compLayout, "mCropToPadding"), (Boolean) TestUtil.getField(parsedLayout, "mCropToPadding"));

            assertEquals((Integer) TestUtil.getField(compLayout, "mMaxHeight"), ValueUtil.dpToPixelInt(1000, res));
            assertEquals((Integer) TestUtil.getField(compLayout, "mMaxHeight"), (Integer) TestUtil.getField(parsedLayout, "mMaxHeight"));

            assertEquals((Integer) TestUtil.getField(compLayout, "mMaxWidth"),ValueUtil.dpToPixelInt(1000, res));
            assertEquals((Integer) TestUtil.getField(compLayout, "mMaxWidth"), (Integer) TestUtil.getField(parsedLayout, "mMaxWidth"));

            assertEquals(compLayout.getScaleType().ordinal(), ImageView.ScaleType.CENTER_INSIDE.ordinal());
            assertEquals(compLayout.getScaleType().ordinal(), parsedLayout.getScaleType().ordinal());

            // android:src (no tiene método get)
            assertNotNull((Drawable) TestUtil.getField(compLayout, "mDrawable"));
            assertEquals((Drawable) TestUtil.getField(compLayout, "mDrawable"), (Drawable) TestUtil.getField(parsedLayout, "mDrawable"));

            // android:hint (no tiene método get)
            // No hay manera de comparar dos PorterDuffColorFilter, si no define el hint devuelve null por lo que algo es algo
            assertNotNull(((PorterDuffColorFilter) TestUtil.getField(compLayout, "mColorFilter"))); // 0x55eeee55
            assertNotNull(((PorterDuffColorFilter) TestUtil.getField(parsedLayout, "mColorFilter")));

        }

        childCount++;

        // Test ProgressBar (indeterminate)
        {
            final ProgressBar compLayout = (ProgressBar) comp.getChildAt(childCount);
            final ProgressBar parsedLayout = (ProgressBar) parsed.getChildAt(childCount);

            assertTrue(compLayout.isIndeterminate());
            assertEquals(compLayout.isIndeterminate(), parsedLayout.isIndeterminate());

            // android:indeterminateBehavior
            assertEquals((Integer) TestUtil.getField(compLayout, "mBehavior"), 2);
            assertEquals((Integer) TestUtil.getField(compLayout, "mBehavior"), (Integer) TestUtil.getField(parsedLayout, "mBehavior"));

            assertNotNull((LayerDrawable) compLayout.getIndeterminateDrawable());
            assertEquals((LayerDrawable)compLayout.getIndeterminateDrawable(),(LayerDrawable)parsedLayout.getIndeterminateDrawable());

            // android:indeterminateDuration
            assertEquals((Integer) TestUtil.getField(compLayout, "mDuration"), 6000);
            assertEquals((Integer) TestUtil.getField(compLayout, "mDuration"), (Integer) TestUtil.getField(parsedLayout, "mDuration"));

            // android:indeterminateOnly
            assertTrue((Boolean) TestUtil.getField(compLayout, "mOnlyIndeterminate"));
            assertEquals((Boolean) TestUtil.getField(compLayout,"mOnlyIndeterminate"), (Boolean) TestUtil.getField(parsedLayout, "mOnlyIndeterminate"));

            // android:interpolator
            // LinearInterpolator no tiene atributos, simplemente el valor suministrado es devuelto como tal por lo que
            // todos los objetos LinearInterpolator son iguales funcionalmente aunque no sean iguales como instancia
            // testear la no nulidad y el tipo es suficiente
            // http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.3_r1/android/view/animation/LinearInterpolator.java?av=f
            assertNotNull((LinearInterpolator)compLayout.getInterpolator());
            assertNotNull((LinearInterpolator)parsedLayout.getInterpolator());
        }

        childCount++;

        // Test ProgressBar (determinate)
        {
            final ProgressBar compLayout = (ProgressBar) comp.getChildAt(childCount);
            final ProgressBar parsedLayout = (ProgressBar) parsed.getChildAt(childCount);

            assertEquals(compLayout.getMax(),90);
            assertEquals(compLayout.getMax(),parsedLayout.getMax());

            assertEquals((Integer) TestUtil.getField(compLayout,"mMaxHeight"),ValueUtil.dpToPixelInt(30, res));
            assertEquals((Integer) TestUtil.getField(compLayout,"mMaxHeight"), (Integer) TestUtil.getField(parsedLayout, "mMaxHeight"));

            assertEquals((Integer) TestUtil.getField(compLayout,"mMaxWidth"),ValueUtil.dpToPixelInt(30, res));
            assertEquals((Integer) TestUtil.getField(compLayout,"mMaxWidth"), (Integer) TestUtil.getField(parsedLayout, "mMaxWidth"));

            assertEquals((Integer) TestUtil.getField(compLayout,"mMinHeight"),ValueUtil.dpToPixelInt(20, res));
            assertEquals((Integer) TestUtil.getField(compLayout,"mMinHeight"), (Integer) TestUtil.getField(parsedLayout, "mMinHeight"));

            assertEquals((Integer) TestUtil.getField(compLayout,"mMinWidth"),ValueUtil.dpToPixelInt(20, res));
            assertEquals((Integer) TestUtil.getField(compLayout,"mMinWidth"), (Integer) TestUtil.getField(parsedLayout, "mMinWidth"));

            assertEquals(compLayout.getProgress(),30);
            assertEquals(compLayout.getProgress(),parsedLayout.getProgress());

            assertNotNull((LayerDrawable)compLayout.getProgressDrawable());
            assertEquals((LayerDrawable)compLayout.getProgressDrawable(),(LayerDrawable)parsedLayout.getProgressDrawable());

            assertEquals(compLayout.getSecondaryProgress(),50);
            assertEquals(compLayout.getSecondaryProgress(),parsedLayout.getSecondaryProgress());
        }

        childCount++;

        // Test RatingBar
        {
            final RatingBar compLayout = (RatingBar) comp.getChildAt(childCount);
            final RatingBar parsedLayout = (RatingBar) parsed.getChildAt(childCount);

            assertFalse(compLayout.isIndicator());
            assertEquals(compLayout.isIndicator(),parsedLayout.isIndicator());

            assertEquals(compLayout.getNumStars(),6);
            assertEquals(compLayout.getNumStars(),parsedLayout.getNumStars());

            assertEquals(compLayout.getRating(),5.25f);
            assertEquals(compLayout.getRating(),parsedLayout.getRating());

            assertEquals(compLayout.getStepSize(),0.75f);
            assertEquals(compLayout.getStepSize(),parsedLayout.getStepSize());
        }

        childCount++;

        // Test SeekBar
        {
            final SeekBar compLayout = (SeekBar) comp.getChildAt(childCount);
            final SeekBar parsedLayout = (SeekBar) parsed.getChildAt(childCount);

            // Test android:thumb
            assertNotNull((StateListDrawable) TestUtil.getField(compLayout, AbsSeekBar.class, "mThumb"));
            assertNotNull((StateListDrawable) TestUtil.getField(parsedLayout, AbsSeekBar.class, "mThumb"));
        }

        childCount++;

        // Test TextView (single line)
        {
            final TextView compLayout = (TextView) comp.getChildAt(childCount);
            final TextView parsedLayout = (TextView) parsed.getChildAt(childCount);

            // Test android:autoLink
            assertEquals(compLayout.getAutoLinkMask() & 0x01,0x01); // web
            assertEquals(compLayout.getAutoLinkMask() & 0x02,0x02); // email
            assertEquals(compLayout.getAutoLinkMask(),0x03); // web|email
            assertEquals(compLayout.getAutoLinkMask(),parsedLayout.getAutoLinkMask());

            // Test android:bufferType
            assertEquals((TextView.BufferType)TestUtil.getField(compLayout,"mBufferType"),TextView.BufferType.EDITABLE);
            assertEquals((TextView.BufferType)TestUtil.getField(compLayout,"mBufferType"),(TextView.BufferType)TestUtil.getField(parsedLayout,"mBufferType"));

            // Test android:cursorVisible
            // Android 4.0.3 (Level 15) tiene un atributo llamado mCursorVisible, dicho atributo cambia en una versión superior pero no se cual
            // pero ya a partir de Level 16 existe el método isCursorVisible
            try
            {
                assertTrue((Boolean) TestUtil.getField(compLayout, "mCursorVisible"));
                assertEquals((Boolean) TestUtil.getField(compLayout, "mCursorVisible"), (Boolean) TestUtil.getField(parsedLayout, "mCursorVisible"));
            }
            catch(ItsNatDroidException ex)
            {
                if (!(ex.getCause() instanceof NoSuchFieldException))
                    throw ex;

                assertTrue((Boolean) TestUtil.callMethod(compLayout,null,"isCursorVisible",null));
                assertEquals((Boolean)TestUtil.callMethod(compLayout,null,"isCursorVisible",null),(Boolean)TestUtil.callMethod(parsedLayout,null,"isCursorVisible",null));
            }

            // Test android:drawableBottom,android:drawableLeft,android:drawableRight,android:drawableTop
            assertEquals(compLayout.getCompoundDrawables().length, 4);
            Drawable[] compDrawArr = compLayout.getCompoundDrawables();
            Drawable[] parsedDrawArr = parsedLayout.getCompoundDrawables();
            for(int i = 0; i < 4; i++)
            {
                assertEquals(compDrawArr[i],parsedDrawArr[i]);
            }

            // Test android:drawablePadding
            assertEquals(compLayout.getCompoundDrawablePadding(),ValueUtil.dpToPixelInt(10,res));
            assertEquals(compLayout.getCompoundDrawablePadding(),parsedLayout.getCompoundDrawablePadding());

            assertEquals(compLayout.getEllipsize(), TextUtils.TruncateAt.MARQUEE);
            assertEquals(compLayout.getEllipsize(),parsedLayout.getEllipsize());

            // Test android:ems  Cuando se define llamando setEms(int) se definen también con el mismo valor minEms y maxEms
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinWidth"), 50);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinWidth"),(Integer)TestUtil.getField(parsedLayout, "mMinWidth"));
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaxWidth"), 50);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaxWidth"),(Integer)TestUtil.getField(parsedLayout, "mMaxWidth"));

            assertTrue(compLayout.getFreezesText());
            assertEquals(compLayout.getFreezesText(),parsedLayout.getFreezesText());

            // Tests android:gravity (no get en Level 15)
            assertEquals(compLayout.getGravity(),Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            assertEquals(compLayout.getGravity(),parsedLayout.getGravity());

            /* No testeamos android:height porque se pisa con android:ems
            assertEquals(compLayout.getHeight(),ValueUtil.dpToPixelInt(45,res));
            assertEquals(compLayout.getHeight(),parsedLayout.getHeight());
            */

            assertEquals(compLayout.getHint(),"Hint Text Test");
            assertEquals(compLayout.getHint(),parsedLayout.getHint());

            assertEquals(compLayout.getImeActionId(),0x00000002);
            assertEquals(compLayout.getImeActionId(),parsedLayout.getImeActionId());

            assertEquals(compLayout.getImeActionLabel(),"Gojm");
            assertEquals(compLayout.getImeActionLabel(),parsedLayout.getImeActionLabel());

            assertEquals(compLayout.getImeOptions(),EditorInfo.IME_ACTION_GO|EditorInfo.IME_ACTION_SEARCH);
            assertEquals(compLayout.getImeOptions(),parsedLayout.getImeOptions());

            // Test android:includeFontPadding
            assertFalse((Boolean) TestUtil.getField(compLayout, "mIncludePad"));
            assertEquals((Boolean)TestUtil.getField(compLayout, "mIncludePad"),(Boolean)TestUtil.getField(parsedLayout, "mIncludePad"));

            assertEquals(compLayout.getInputType(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
            assertEquals(compLayout.getInputType(), parsedLayout.getInputType());

            // Test android:lineSpacingExtra
            assertEquals((Float) TestUtil.getField(compLayout, "mSpacingAdd"), ValueUtil.dpToPixel(5, res));
            assertEquals((Float)TestUtil.getField(compLayout, "mSpacingAdd"),(Float)TestUtil.getField(parsedLayout, "mSpacingAdd"));

            // Test android:lineSpacingMultiplier
            assertEquals((Float)TestUtil.getField(compLayout, "mSpacingMult"),1.2f);
            assertEquals((Float)TestUtil.getField(compLayout, "mSpacingMult"),(Float)TestUtil.getField(parsedLayout, "mSpacingMult"));

            // Test android:lines
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaximum"),1);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaximum"),(Integer)TestUtil.getField(parsedLayout, "mMaximum"));
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinimum"),1);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinimum"),(Integer)TestUtil.getField(parsedLayout, "mMinimum"));
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaxMode"),1); // modo LINES = 1
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaxMode"),(Integer)TestUtil.getField(parsedLayout, "mMaxMode"));
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinMode"),1);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinMode"),(Integer)TestUtil.getField(parsedLayout, "mMinMode"));

            assertFalse(compLayout.getLinksClickable());
            assertEquals(compLayout.getLinksClickable(), parsedLayout.getLinksClickable());

            assertEquals((Integer)TestUtil.getField(compLayout, "mMarqueeRepeatLimit"),-1);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMarqueeRepeatLimit"),(Integer)TestUtil.getField(parsedLayout, "mMarqueeRepeatLimit"));

            // Test android:maxEms
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaxWidth"), 50);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaxWidth"),(Integer)TestUtil.getField(parsedLayout, "mMaxWidth"));
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaxWidthMode"),1);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaxWidthMode"),(Integer)TestUtil.getField(parsedLayout, "mMaxWidthMode"));

            // Test android:maxLength
            assertEquals((InputFilter.LengthFilter)compLayout.getFilters()[0],new InputFilter.LengthFilter(1000));
            assertEquals((InputFilter.LengthFilter)compLayout.getFilters()[0],(InputFilter.LengthFilter)parsedLayout.getFilters()[0]);

            // Test android:maxLines
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaximum"),1);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaximum"),(Integer)TestUtil.getField(parsedLayout, "mMaximum"));
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaxMode"),1); // modo LINES = 1
            assertEquals((Integer)TestUtil.getField(compLayout, "mMaxMode"),(Integer)TestUtil.getField(parsedLayout, "mMaxMode"));

            // Test android:minEms
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinWidth"), 50);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinWidth"),(Integer)TestUtil.getField(parsedLayout, "mMinWidth"));
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinWidthMode"),1);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinWidthMode"),(Integer)TestUtil.getField(parsedLayout, "mMinWidthMode"));

            // Test android:minLines
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinimum"),1);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinimum"),(Integer)TestUtil.getField(parsedLayout, "mMinimum"));
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinMode"),1);
            assertEquals((Integer)TestUtil.getField(compLayout, "mMinMode"),(Integer)TestUtil.getField(parsedLayout, "mMinMode"));

            assertEquals(compLayout.getPrivateImeOptions(), "com.example.myapp.JustToWriteSomething=3");
            assertEquals(compLayout.getPrivateImeOptions(), parsedLayout.getPrivateImeOptions());

            // android:scrollHorizontally
            assertTrue((Boolean) TestUtil.getField(compLayout, "mHorizontallyScrolling"));
            assertEquals((Boolean)TestUtil.getField(compLayout,"mHorizontallyScrolling"),(Boolean)TestUtil.getField(parsedLayout, "mHorizontallyScrolling"));

            // Test android:selectAllOnFocus
            // Android 4.0.3 (Level 15) tiene un atributo llamado mSelectAllOnFocus, dicho atributo cambia en una versión superior
            // estando dentro ahora del atributo mEditor (android.widget.Editor)
            try
            {
                assertTrue((Boolean) TestUtil.getField(compLayout, "mSelectAllOnFocus"));
                assertEquals((Boolean) TestUtil.getField(compLayout, "mSelectAllOnFocus"), (Boolean) TestUtil.getField(parsedLayout, "mSelectAllOnFocus"));
            }
            catch(ItsNatDroidException ex)
            {
                if (!(ex.getCause() instanceof NoSuchFieldException))
                    throw ex;

                Object compEditor = TestUtil.getField(compLayout,"mEditor");
                Object parsedEditor = TestUtil.getField(parsedLayout,"mEditor");

                assertTrue((Boolean) TestUtil.getField(compEditor, "mSelectAllOnFocus"));
                assertEquals((Boolean) TestUtil.getField(compEditor, "mSelectAllOnFocus"), (Boolean) TestUtil.getField(parsedEditor, "mSelectAllOnFocus"));
            }

            // Test android:shadowColor
            assertEquals((Integer) TestUtil.getField(compLayout.getPaint(), Paint.class, "shadowColor"),0xffff0000);
            assertEquals((Integer) TestUtil.getField(compLayout.getPaint(), Paint.class, "shadowColor"),(Integer) TestUtil.getField(parsedLayout.getPaint(), Paint.class, "shadowColor"));

            // Test android:shadowDx
            assertEquals((Float)TestUtil.getField(compLayout,"mShadowDx"),1.1f);
            assertEquals((Float)TestUtil.getField(compLayout,"mShadowDx"),(Float)TestUtil.getField(parsedLayout,"mShadowDx"));

            // Test android:shadowDy
            assertEquals((Float)TestUtil.getField(compLayout,"mShadowDy"),1.2f);
            assertEquals((Float)TestUtil.getField(compLayout,"mShadowDy"),(Float)TestUtil.getField(parsedLayout,"mShadowDy"));

            // Test android:shadowRadius
            assertEquals((Float)TestUtil.getField(compLayout,"mShadowRadius"),1.3f);
            assertEquals((Float)TestUtil.getField(compLayout,"mShadowRadius"),(Float)TestUtil.getField(parsedLayout,"mShadowRadius"));

            // Test android:text
            // El inputType influye en el tipo de objeto de texto
            assertEquals((SpannableStringBuilder)compLayout.getText(),new SpannableStringBuilder("TextView Tests (this text is cut on the right with ellipsis points)"));
            assertEquals(compLayout.getText(),parsedLayout.getText());

            // Test: android:textAllCaps no puedo testear porque no he conseguido que funcione ni en modo compilado
            // por otra parte los TransformationMethod resultantes son diferentes :(
            TransformationMethod a_trans = compLayout.getTransformationMethod();
            TransformationMethod b_trans = parsedLayout.getTransformationMethod();

            // Test: android:textColor
            assertEquals(compLayout.getTextColors().getDefaultColor(),0xff00ff00);
            assertEquals(compLayout.getTextColors(), parsedLayout.getTextColors());

            // Test android:textColorHighlight
            assertEquals((Integer)TestUtil.getField(compLayout,"mHighlightColor"),0xff0000ff);
            assertEquals((Integer)TestUtil.getField(compLayout,"mHighlightColor"),(Integer)TestUtil.getField(parsedLayout,"mHighlightColor"));

            // Test android:textColorHint
            assertEquals(compLayout.getHintTextColors().getDefaultColor(),0xff00ff00);
            assertEquals(compLayout.getHintTextColors(), parsedLayout.getHintTextColors());

            assertFalse(compLayout.isTextSelectable());
            assertEquals(compLayout.isTextSelectable(), parsedLayout.isTextSelectable());

            assertEquals(compLayout.getTextScaleX(),1.2f);
            assertEquals(compLayout.getTextScaleX(), parsedLayout.getTextScaleX());

            assertEquals(compLayout.getTextSize(),ValueUtil.dpToPixelInt(15,res));
            assertEquals(compLayout.getTextSize(), parsedLayout.getTextSize());

            // Test android:textStyle y typeface
            int NORMAL = 0;
            int BOLD = 1;
            int ITALIC = 2;

            Typeface compTf = compLayout.getTypeface();
            Typeface parsedTf = parsedLayout.getTypeface();

            assertEquals(compTf.getStyle(),BOLD | ITALIC);
            assertEquals(compTf.getStyle(),parsedTf.getStyle());



//            System.out.println("PARAR");
        }




//         System.out.println("\n\n\nDEFAULT VALUE: " + compLayout.getColumnCount() + " " + parsedLayout.getColumnCount());
        //System.out.println("\n\n\n");

    }
}