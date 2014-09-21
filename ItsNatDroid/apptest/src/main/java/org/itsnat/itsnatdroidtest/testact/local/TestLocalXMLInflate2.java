package org.itsnat.itsnatdroidtest.testact.local;

import android.content.res.Resources;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AbsSeekBar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

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

        // Test TextView
        {
            final TextView compLayout = (TextView) comp.getChildAt(childCount);
            final TextView parsedLayout = (TextView) parsed.getChildAt(childCount);


        }




//         System.out.println("\n\n\nDEFAULT VALUE: " + compLayout.getColumnCount() + " " + parsedLayout.getColumnCount());
        //System.out.println("\n\n\n");

    }
}