package org.itsnat.itsnatdroidtest.testact.local;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsSeekBar;
import android.widget.AdapterViewAnimator;
import android.widget.AdapterViewFlipper;
import android.widget.AnalogClock;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.itsnatdroidtest.R;
import org.itsnat.itsnatdroidtest.testact.util.TestUtil;

import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertEquals;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertEqualsStrokeWidth;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertFalse;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertNotNull;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertPositive;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertTrue;


/**
 * Created by jmarranz on 19/06/14.
 */
public class TestLocalXMLInflate1
{

    public static void test(ScrollView compRoot,ScrollView parsedRoot,InflatedLayout layout)
    {
        Context ctx = compRoot.getContext();
        final Resources res = ctx.getResources();

        // comp = "Layout compiled"
        // parsed = "Layout dynamically parsed"

        LinearLayout comp = (LinearLayout)compRoot.getChildAt(0);
        LinearLayout parsed = (LinearLayout)parsedRoot.getChildAt(0);

        assertEquals(comp.getOrientation(),parsed.getOrientation());

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

        // Testing misc attribs
        {
            RelativeLayout compLayout = (RelativeLayout) comp.getChildAt(childCount);
            RelativeLayout parsedLayout = (RelativeLayout) parsed.getChildAt(childCount);
            {

                final TextView compTextView1 = (TextView) compLayout.getChildAt(0);
                final TextView parsedTextView1 = (TextView) parsedLayout.getChildAt(0);

                // Test id ya definido como recurso compilado
                assertEquals(compTextView1.getId(),R.id.textViewTest1);
                assertEquals(compTextView1.getId(), parsedTextView1.getId());

                // Test findViewByXMLId
                if (compTextView1 != compLayout.findViewById(R.id.textViewTest1)) throw new RuntimeException("FAIL");
                if (parsedTextView1 != layout.findViewByXMLId("textViewTest1")) throw new RuntimeException("FAIL");


                assertEquals(compTextView1.getText(), parsedTextView1.getText());

                assertEquals(compTextView1.getTextSize(),ValueUtil.dpToPixelInt(15, res));
                assertEquals(compTextView1.getTextSize(), parsedTextView1.getTextSize());

                // Test style
                assertEquals(compTextView1.getPaddingLeft(),ValueUtil.dpToPixelInt(21, res));
                assertEquals(compTextView1.getPaddingLeft(),parsedTextView1.getPaddingLeft());
                assertEquals(compTextView1.getPaddingRight(),ValueUtil.dpToPixelInt(21, res));
                assertEquals(compTextView1.getPaddingRight(),parsedTextView1.getPaddingRight());

                assertEquals(compTextView1.getPaddingTop(),ValueUtil.dpToPixelInt(10, res));
                assertEquals(compTextView1.getPaddingTop(),parsedTextView1.getPaddingTop());

                assertEquals(compTextView1.getPaddingBottom(),ValueUtil.dpToPixelInt(10, res));
                assertEquals(compTextView1.getPaddingBottom(),parsedTextView1.getPaddingBottom());

                assertEquals(compTextView1.getTextColors().getDefaultColor(),0xff0000ff);
                assertEquals(compTextView1.getTextColors(), parsedTextView1.getTextColors());

                assertEquals(((ColorDrawable)compTextView1.getBackground()).getColor(), res.getColor(res.getIdentifier("@android:color/holo_green_light",null,null)));
                assertEquals(compTextView1.getBackground(), parsedTextView1.getBackground());

                final TextView compTextView2 = (TextView) compLayout.getChildAt(1);
                final TextView parsedTextView2 = (TextView) parsedLayout.getChildAt(1);

                // Test id añadido dinámicamente "@+id/..."
                // En este caso el valor del id compilado (que existe) no es igual al añadido dinámicamente
                assertEquals(((TextView)compLayout.findViewById(R.id.textViewTest2)),compTextView2);
                assertEquals(((TextView)parsedLayout.findViewById(parsedTextView2.getId())),parsedTextView2);
                assertEquals(compTextView2.getId(),parsedTextView2.getId()); // Porque existe el id compilado y tiene prioridad en el caso dinámico

                assertEquals(compTextView2.getText(), parsedTextView2.getText());
                assertEquals(compTextView2.getBackground(), parsedTextView2.getBackground());

                // Test atributo style
                // No tenemos una forma de testear "textAppearanceMedium" de forma directa, una forma es testear una de las propiedades que impone, ej el tamaño del texto
                assertEquals(compTextView2.getTextSize(), parsedTextView2.getTextSize());

                final TextView compTextView3 = (TextView) compLayout.getChildAt(2);
                final TextView parsedTextView3 = (TextView) parsedLayout.getChildAt(2);


                RelativeLayout.LayoutParams compTextParams3 = (RelativeLayout.LayoutParams)compTextView3.getLayoutParams();
                RelativeLayout.LayoutParams parsedTextParams3 = (RelativeLayout.LayoutParams)parsedTextView3.getLayoutParams();
                int[] compTextRules3 = compTextParams3.getRules();
                int[] parsedTextRules3 = parsedTextParams3.getRules();
                assertEquals(compTextRules3.length, parsedTextRules3.length); // Por si acaso pero son todas las posibles rules
                assertPositive(compTextRules3[RelativeLayout.BELOW]);
                assertEquals(compTextRules3[RelativeLayout.BELOW],compTextView2.getId());
                assertPositive(parsedTextRules3[RelativeLayout.BELOW]);
                assertEquals(parsedTextRules3[RelativeLayout.BELOW],parsedTextView2.getId());

            }
        }

        childCount++;

        // Testing custom class
        {
            final TextView compCustomTextView = (TextView) comp.getChildAt(childCount);
            final TextView parsedCustomTextView = (TextView) parsed.getChildAt(childCount);
            assertEquals(compCustomTextView.getText(), parsedCustomTextView.getText());
            assertEquals(compCustomTextView.getBackground(), parsedCustomTextView.getBackground());
        }


        childCount++;

        // Test View Attribs
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);
            {
                {
                    final TextView compTextView = (TextView) compLinLayout.getChildAt(0);
                    final TextView parsedTextView = (TextView) parsedLinLayout.getChildAt(0);
                    assertEquals(compTextView.getAlpha(), 0.7f);
                    assertEquals(compTextView.getAlpha(), parsedTextView.getAlpha());
                    assertEquals(((ColorDrawable) compTextView.getBackground()).getColor(), 0xffdddddd);
                    assertEquals(compTextView.getBackground(), parsedTextView.getBackground());

                    assertFalse(compTextView.isClickable());
                    assertEquals(compTextView.isClickable(), parsedTextView.isClickable());
                    assertEquals(compTextView.getContentDescription(), "For Testing View Attribs");
                    assertEquals(compTextView.getContentDescription(), parsedTextView.getContentDescription());
                    assertEquals(compTextView.getDrawingCacheQuality(), View.DRAWING_CACHE_QUALITY_HIGH);
                    assertEquals(compTextView.getDrawingCacheQuality(), parsedTextView.getDrawingCacheQuality());
                    assertTrue(compTextView.isDuplicateParentStateEnabled());
                    assertEquals(compTextView.isDuplicateParentStateEnabled(), parsedTextView.isDuplicateParentStateEnabled());
                }

                {
                    final ScrollView compScrollView = (ScrollView) compLinLayout.getChildAt(1);
                    final ScrollView parsedScrollView = (ScrollView) parsedLinLayout.getChildAt(1);

                    assertTrue(compScrollView.isScrollbarFadingEnabled()); // Correspondiente a requiresFadingEdge
                    assertEquals(compScrollView.isScrollbarFadingEnabled(), parsedScrollView.isScrollbarFadingEnabled());
                    // Test android:fadingEdgeLength
                    assertEquals(compScrollView.getVerticalFadingEdgeLength(), ValueUtil.dpToPixelInt(10, res));
                    assertEquals(compScrollView.getVerticalFadingEdgeLength(), parsedScrollView.getVerticalFadingEdgeLength());
                    assertEquals(compScrollView.getHorizontalFadingEdgeLength(), ValueUtil.dpToPixelInt(10, res));
                    assertEquals(compScrollView.getHorizontalFadingEdgeLength(), parsedScrollView.getHorizontalFadingEdgeLength());

                    // Test android:scrollbarAlwaysDrawHorizontalTrack

                    final Class[] scrollCacheClasses = new Class[]{View.class, TestUtil.resolveClass("android.view.View$ScrollabilityCache"), TestUtil.resolveClass("android.widget.ScrollBarDrawable")};

                    assertTrue((Boolean) TestUtil.getField(compScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mAlwaysDrawHorizontalTrack"}));
                    assertEquals((Boolean) TestUtil.getField(compScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mAlwaysDrawHorizontalTrack"}), (Boolean) TestUtil.getField(parsedScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mAlwaysDrawHorizontalTrack"}));

                    // Test android:scrollbarAlwaysDrawVerticalTrack
                    assertTrue((Boolean) TestUtil.getField(compScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mAlwaysDrawVerticalTrack"}));
                    assertEquals((Boolean) TestUtil.getField(compScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mAlwaysDrawVerticalTrack"}), (Boolean) TestUtil.getField(parsedScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mAlwaysDrawVerticalTrack"}));

                    // Test android:scrollbarThumbHorizontal
                    assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mHorizontalThumb"}), ValueUtil.dpToPixelInt(0.9f, res));
                    assertEquals((GradientDrawable) TestUtil.getField(compScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mHorizontalThumb"}), (GradientDrawable) TestUtil.getField(parsedScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mHorizontalThumb"}));

                    // Test android:scrollbarThumbVertical
                    assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mVerticalThumb"}), ValueUtil.dpToPixelInt(0.9f, res));
                    assertEquals((GradientDrawable) TestUtil.getField(compScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mVerticalThumb"}), (GradientDrawable) TestUtil.getField(parsedScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mVerticalThumb"}));

                    // Test android:scrollbarTrackHorizontal
                    assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mHorizontalTrack"}), ValueUtil.dpToPixelInt(0.9f, res));
                    assertEquals((GradientDrawable) TestUtil.getField(compScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mHorizontalTrack"}), (GradientDrawable) TestUtil.getField(parsedScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mHorizontalTrack"}));

                    // Test android:scrollbarTrackVertical
                    assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mVerticalTrack"}), ValueUtil.dpToPixelInt(0.9f, res));
                    assertEquals((GradientDrawable) TestUtil.getField(compScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mVerticalTrack"}), (GradientDrawable) TestUtil.getField(parsedScrollView, scrollCacheClasses, new String[]{"mScrollCache", "scrollBar", "mVerticalTrack"}));

                    // Test android:scrollbars
                    int scrollbars = (Integer)TestUtil.getField(compScrollView,View.class,"mViewFlags");
                    int SCROLLBARS_MASK = 0x00000300;
                    scrollbars = scrollbars & SCROLLBARS_MASK;
                    assertEquals(scrollbars & 0x00000100,0x00000100); // Horizontal
                    assertEquals(scrollbars & 0x00000200,0x00000200); // Vertical
                }

                {
                    // No usamos aquí TextView porque minHeight/minWidth se definen también en TextView y no podríamos testear para View (testearíamos los de TextView)
                    final View compTextView2 = compLinLayout.getChildAt(2);
                    final View parsedTextView2 = parsedLinLayout.getChildAt(2);

                    //assertEquals(compTextView2.getText(), parsedTextView2.getText());
                    // Test android:filterTouchesWhenObscured
                    assertTrue(compTextView2.getFilterTouchesWhenObscured());
                    // En el emulador 4.0.4 el setFilterTouchesWhenObscured() parece como si hiciera un NOT al parámetro, sin embargo en el Nexus 4 perfecto
                    // por ello mostramos un alertDialog no lanzamos una excepción
                    if (compTextView2.getFilterTouchesWhenObscured() != parsedTextView2.getFilterTouchesWhenObscured())
                        TestUtil.alertDialog(compTextView2.getContext(), "Test failed in filterTouchesWhenObscured, don't worry it seems an Android emulator bug (running on 4.0.4 emulator?)");
                    assertTrue(compTextView2.isFocusable());
                    assertEquals(compTextView2.isFocusable(), parsedTextView2.isFocusable());
                    assertTrue(compTextView2.isFocusableInTouchMode());
                    assertEquals(compTextView2.isFocusableInTouchMode(), parsedTextView2.isFocusableInTouchMode());
                    assertFalse(compTextView2.isHapticFeedbackEnabled());
                    assertEquals(compTextView2.isHapticFeedbackEnabled(), parsedTextView2.isHapticFeedbackEnabled());
                    assertPositive(compTextView2.getId());
                    assertEquals(compTextView2.getId(), parsedTextView2.getId());
                    // No puedo testear android:isScrollContainer porque  isScrollContainer() se define en un Level superior
                    //assertTrue( (((int)((Integer)getField(compTextView2,View.class,"mPrivateFlags"))) & 0x00100000) != 0); // PFLAG_SCROLL_CONTAINER_ADDED 0x00100000

                    assertTrue(compTextView2.getKeepScreenOn());
                    assertEquals(compTextView2.getKeepScreenOn(), parsedTextView2.getKeepScreenOn());
                    assertEquals(compTextView2.getLayerType(), View.LAYER_TYPE_HARDWARE);
                    assertEquals(compTextView2.getLayerType(), parsedTextView2.getLayerType());
                    assertTrue(compTextView2.isLongClickable());
                    assertEquals(compTextView2.isLongClickable(), parsedTextView2.isLongClickable());
                    // Test android:minHeight
                    assertEquals((Integer) TestUtil.getField(compTextView2, View.class, "mMinHeight"), ValueUtil.dpToPixelInt(30, res));
                    assertEquals((Integer) TestUtil.getField(compTextView2, View.class, "mMinHeight"), (Integer) TestUtil.getField(parsedTextView2, View.class, "mMinHeight"));
                    assertEquals((Integer) TestUtil.getField(compTextView2, View.class, "mMinWidth"), ValueUtil.dpToPixelInt(30, res));
                    assertEquals((Integer) TestUtil.getField(compTextView2, View.class, "mMinWidth"), (Integer) TestUtil.getField(parsedTextView2, View.class, "mMinWidth"));
                    assertPositive(compTextView2.getNextFocusDownId());
                    assertEquals(compTextView2.getNextFocusDownId(), parsedTextView2.getNextFocusDownId());
                    assertPositive(compTextView2.getNextFocusForwardId());
                    assertEquals(compTextView2.getNextFocusForwardId(), parsedTextView2.getNextFocusForwardId());
                    assertPositive(compTextView2.getNextFocusLeftId());
                    assertEquals(compTextView2.getNextFocusLeftId(), parsedTextView2.getNextFocusLeftId());
                    assertPositive(compTextView2.getNextFocusRightId());
                    assertEquals(compTextView2.getNextFocusRightId(), parsedTextView2.getNextFocusRightId());
                    assertPositive(compTextView2.getNextFocusUpId());
                    assertEquals(compTextView2.getNextFocusUpId(), parsedTextView2.getNextFocusUpId());
                    // No puedo testear android:onClick porque no hay get nativo asociado
                    assertEquals(compTextView2.getPaddingLeft(), ValueUtil.dpToPixelInt(10, res));
                    assertEquals(compTextView2.getPaddingLeft(), parsedTextView2.getPaddingLeft());
                    assertEquals(compTextView2.getPaddingRight(), ValueUtil.dpToPixelInt(11, res));
                    assertEquals(compTextView2.getPaddingRight(), parsedTextView2.getPaddingRight());
                    assertEquals(compTextView2.getPaddingTop(), ValueUtil.dpToPixelInt(12, res));
                    assertEquals(compTextView2.getPaddingTop(), parsedTextView2.getPaddingTop());
                    assertEquals(compTextView2.getPaddingBottom(), ValueUtil.dpToPixelInt(13, res));
                    assertEquals(compTextView2.getPaddingBottom(), parsedTextView2.getPaddingBottom());
                    assertEquals(compTextView2.getRotation(), 10.5f);
                    assertEquals(compTextView2.getRotation(), parsedTextView2.getRotation());
                    assertEquals(compTextView2.getRotationX(), 45.5f);
                    assertEquals(compTextView2.getRotationX(), parsedTextView2.getRotationX());
                    assertEquals(compTextView2.getRotationY(), 10.5f);
                    assertEquals(compTextView2.getRotationY(), parsedTextView2.getRotationY());
                    assertFalse(compTextView2.isSaveEnabled());
                    assertEquals(compTextView2.isSaveEnabled(), parsedTextView2.isSaveEnabled());
                    assertEquals(compTextView2.getScaleX(), 1.2f);
                    assertEquals(compTextView2.getScaleX(), parsedTextView2.getScaleX());
                    assertEquals(compTextView2.getScaleY(), 1.2f);
                    assertEquals(compTextView2.getScaleY(), parsedTextView2.getScaleY());
                    // No testeamos android:scrollX y android:scrollY (con getScrollX() y getScrollY()) porque después de definirse correctamente
                    // algo hace poner a cero los valores, quizás al insertar la View
                    assertPositive(compTextView2.getScrollBarStyle());
                    assertEquals(compTextView2.getScrollBarStyle(), parsedTextView2.getScrollBarStyle());

                    assertFalse(compTextView2.isSoundEffectsEnabled());
                    assertEquals(compTextView2.isSoundEffectsEnabled(), parsedTextView2.isSoundEffectsEnabled());
                    assertEquals((String) compTextView2.getTag(), "theTag");
                    assertEquals((String) compTextView2.getTag(), (String) parsedTextView2.getTag());
                    assertEquals(compTextView2.getPivotX(), ValueUtil.dpToPixelInt(70, res));
                    assertEquals(compTextView2.getPivotX(), parsedTextView2.getPivotX());
                    assertEquals(compTextView2.getPivotY(), ValueUtil.dpToPixelInt(10, res));
                    assertEquals(compTextView2.getPivotY(), parsedTextView2.getPivotY());
                    assertEquals(compTextView2.getTranslationX(), ValueUtil.dpToPixelInt(10, res));
                    assertEquals(compTextView2.getTranslationX(), parsedTextView2.getTranslationX());
                    assertEquals(compTextView2.getTranslationY(), ValueUtil.dpToPixelInt(10, res));
                    assertEquals(compTextView2.getTranslationY(), parsedTextView2.getTranslationY());
                }
            }

        }

        childCount++;

        // Test AnalogClock
        {
            final AnalogClock compLayout = (AnalogClock) comp.getChildAt(childCount);
            final AnalogClock parsedLayout = (AnalogClock) parsed.getChildAt(childCount);

            // android:dial
            assertNotNull((Drawable)TestUtil.getField(compLayout, "mDial"));
            assertEquals((Drawable)TestUtil.getField(compLayout,"mDial"),(Drawable)TestUtil.getField(parsedLayout,"mDial"));

            // android:hand_hour
            assertNotNull((Drawable) TestUtil.getField(compLayout, "mHourHand"));
            assertEquals((Drawable)TestUtil.getField(compLayout,"mHourHand"),(Drawable)TestUtil.getField(parsedLayout,"mHourHand"));

            // android:hand_minute
            assertNotNull((Drawable)TestUtil.getField(compLayout,"mMinuteHand"));
            assertEquals((Drawable)TestUtil.getField(compLayout,"mMinuteHand"),(Drawable)TestUtil.getField(parsedLayout,"mMinuteHand"));
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

            // android:tint (no tiene método get)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) // LOLLIPOP = 21
            {
                // No hay manera de comparar dos PorterDuffColorFilter, si no define el hint devuelve null por lo que algo es algo
                assertNotNull(((PorterDuffColorFilter) TestUtil.getField(compLayout, "mColorFilter"))); // 0x55eeee55
                assertNotNull(((PorterDuffColorFilter) TestUtil.getField(parsedLayout, "mColorFilter")));
            }
            else
            {
                // A partir de Lollipop via XML no se define el tint con setColorFilter() sino de otra forma
                assertEquals((ColorStateList) TestUtil.callGetMethod(compLayout,"getImageTintList"), (ColorStateList) TestUtil.callGetMethod(parsedLayout,"getImageTintList"));

            }
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
            assertEquals((StateListDrawable) TestUtil.getField(parsedLayout, AbsSeekBar.class, "mThumb"),(StateListDrawable) TestUtil.getField(parsedLayout, AbsSeekBar.class, "mThumb"));
        }

        childCount++;

        // Test TextView 1
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

                assertTrue((Boolean) TestUtil.callGetMethod(compLayout,"isCursorVisible"));
                assertEquals((Boolean)TestUtil.callGetMethod(compLayout,"isCursorVisible"),(Boolean)TestUtil.callGetMethod(parsedLayout,"isCursorVisible"));
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

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) // 4.0.3 Level 15
            {
                assertEquals((Integer) TestUtil.getField(compLayout.getPaint(), Paint.class, "shadowColor"),0xffff0000);
                assertEquals((Integer) TestUtil.getField(compLayout.getPaint(), Paint.class, "shadowColor"),(Integer) TestUtil.getField(parsedLayout.getPaint(), Paint.class, "shadowColor"));
            }
            else // Partir de la versión siguiente (level 16) hay un método getShadowColor(), en teoría se podría seguir usando el atributo interno shadowColor de Paint pero en Level 21 (Lollipop) desaparece, usar el método desde level 16 es la mejor opción
            {
                assertEquals((Integer)TestUtil.callGetMethod(compLayout,"getShadowColor"),0xffff0000);
                assertEquals((Integer)TestUtil.callGetMethod(compLayout,"getShadowColor"),(Integer) TestUtil.callGetMethod(parsedLayout,"getShadowColor"));
            }


            // Test android:shadowDx
            assertEquals((Float)TestUtil.getField(compLayout,"mShadowDx"),1.1f);
            assertEquals((Float)TestUtil.getField(compLayout,"mShadowDx"),(Float)TestUtil.getField(parsedLayout,"mShadowDx"));

            // Test android:shadowDy
            assertEquals((Float)TestUtil.getField(compLayout,"mShadowDy"),1.2f);
            assertEquals((Float)TestUtil.getField(compLayout,"mShadowDy"),(Float)TestUtil.getField(parsedLayout,"mShadowDy"));

            // Test android:shadowRadius
            assertEquals((Float)TestUtil.getField(compLayout,"mShadowRadius"),1.3f);
            assertEquals((Float)TestUtil.getField(compLayout,"mShadowRadius"),(Float)TestUtil.getField(parsedLayout,"mShadowRadius"));

            // Test android:singleLine
            assertTrue((Boolean)TestUtil.getField(compLayout,"mSingleLine"));
            assertEquals((Boolean)TestUtil.getField(compLayout,"mSingleLine"),(Boolean)TestUtil.getField(parsedLayout,"mSingleLine"));


            // Test android:text
            // El inputType influye en el tipo de objeto de texto
            assertEquals((SpannableStringBuilder)compLayout.getText(),new SpannableStringBuilder("TextView Tests 1 (this text is cut on the right)"));
            assertEquals(compLayout.getText(),parsedLayout.getText());

            // Test: android:textAllCaps no he conseguido que funcione ni en modo compilado en este test
            // pero los TransformationMethod parecen correctos
            TransformationMethod comp_trans = compLayout.getTransformationMethod();
            TransformationMethod parsed_trans = parsedLayout.getTransformationMethod();
            assertEquals(comp_trans.getClass().getName(),"android.text.method.AllCapsTransformationMethod");
            assertEquals(comp_trans.getClass().getName(),parsed_trans.getClass().getName());


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
            int NORMAL = 0, BOLD = 1, ITALIC = 2;

            Typeface compTf = compLayout.getTypeface();
            Typeface parsedTf = parsedLayout.getTypeface();
            assertEquals(compTf.getStyle(),BOLD | ITALIC);
            assertEquals(compTf.getStyle(),parsedTf.getStyle());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                assertEquals((Integer)TestUtil.getField(compTf,"native_instance"),(Integer)TestUtil.getField(parsedTf,"native_instance"));
            else // A partir de Lollipop (level 21) es un long
                assertEquals((Long)TestUtil.getField(compTf,"native_instance"),(Long)TestUtil.getField(parsedTf,"native_instance"));
        }

        childCount++;

        // Test TextView 2
        // Se testean de nuevo algunos atributos y otros que no podían testearse antes
        {
            final TextView compLayout = (TextView) comp.getChildAt(childCount);
            final TextView parsedLayout = (TextView) parsed.getChildAt(childCount);

            assertEquals((SpannableString)compLayout.getText(),new SpannableString("TextView Tests 2 (this text is much more longer and is cut on the right with ellipsis points)"));
            assertEquals(compLayout.getText(),parsedLayout.getText());

            // Test android:bufferType
            // Repetimos este test con SPANNABLE porque en el anterior por alguna razón se cambiaba a EDITABLE
            assertEquals((TextView.BufferType)TestUtil.getField(compLayout,"mBufferType"),TextView.BufferType.SPANNABLE);
            assertEquals((TextView.BufferType)TestUtil.getField(compLayout,"mBufferType"),(TextView.BufferType)TestUtil.getField(parsedLayout,"mBufferType"));

            assertEquals(compLayout.getEllipsize(), TextUtils.TruncateAt.END);
            assertEquals(compLayout.getEllipsize(),parsedLayout.getEllipsize());

            // Test android:height, android:maxHeight
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // getHeight() no se corresponde exactamente con setHeight(int) pero el resultado es coherente
                    assertEquals(compLayout.getHeight(),ValueUtil.dpToPixelInt(30,res));
                    assertEquals(compLayout.getHeight(),parsedLayout.getHeight());

                    // Estos tests no funcionan porque mi impresión es que layout_height="30dp" define la altura por su cuenta pero
                    // no como PIXELS sino como LINES en el layout compilado
                    //assertEquals((Integer)TestUtil.getField(compLayout, "mMaxMode"),2); // modo PIXELS = 2
                    //assertEquals((Integer)TestUtil.getField(compLayout, "mMaxMode"),(Integer)TestUtil.getField(parsedLayout, "mMaxMode"));
                    //assertEquals((Integer)TestUtil.getField(compLayout, "mMaximum"),ValueUtil.dpToPixelInt(30,res));
                    //assertEquals((Integer)TestUtil.getField(compLayout, "mMaximum"),(Integer)TestUtil.getField(parsedLayout, "mMaximum"));
                }
            });

            // Test: android:textAllCaps en este test si conseguimos que funcione porque isTextSelectable="false"
            // y no es "editable"
            TransformationMethod comp_trans = compLayout.getTransformationMethod();
            TransformationMethod parsed_trans = parsedLayout.getTransformationMethod();
            assertEquals(comp_trans.getClass().getName(),"android.text.method.AllCapsTransformationMethod");
            assertEquals(comp_trans.getClass().getName(),parsed_trans.getClass().getName());

            assertFalse(compLayout.isTextSelectable());
            assertEquals(compLayout.isTextSelectable(),parsedLayout.isTextSelectable());


            // Test android:singleLine
            assertTrue((Boolean)TestUtil.getField(compLayout,"mSingleLine"));
            assertEquals((Boolean)TestUtil.getField(compLayout,"mSingleLine"),(Boolean)TestUtil.getField(parsedLayout,"mSingleLine"));
        }

        childCount++;

        // Test TextView 3 (textAppearance y hint)
        {
            final TextView compLayout = (TextView) comp.getChildAt(childCount);
            final TextView parsedLayout = (TextView) parsed.getChildAt(childCount);

            assertEquals(compLayout.getHint(),"Hint Text (TextView Tests 3)");
            assertEquals(compLayout.getHint(),parsedLayout.getHint());

            assertEquals(compLayout.getTextSize(),ValueUtil.dpToPixelInt(21,res));
            assertEquals(compLayout.getTextSize(), parsedLayout.getTextSize());
        }

        childCount++;

        // CompoundButton Tests (a través de CheckBox)
        {
            final CheckBox compLayout = (CheckBox) comp.getChildAt(childCount);
            final CheckBox parsedLayout = (CheckBox) parsed.getChildAt(childCount);

            assertEquals(compLayout.getText(),"CompoundButton Tests");
            assertEquals(compLayout.getText(),parsedLayout.getText());

            assertNotNull((StateListDrawable) TestUtil.getField(compLayout, CompoundButton.class, "mButtonDrawable"));
            assertEquals((StateListDrawable)TestUtil.getField(compLayout,CompoundButton.class,"mButtonDrawable"),(StateListDrawable)TestUtil.getField(parsedLayout,CompoundButton.class,"mButtonDrawable"));

            assertTrue(compLayout.isChecked());
            assertEquals(compLayout.isChecked(),parsedLayout.isChecked());

        }

        childCount++;

        // Switch Tests
        {
            final Switch compLayout = (Switch) comp.getChildAt(childCount);
            final Switch parsedLayout = (Switch) parsed.getChildAt(childCount);

            assertEquals(compLayout.getText(),"Switch Tests");
            assertEquals(compLayout.getText(),parsedLayout.getText());

            // android:switchMinWidth
            assertEquals((Integer) TestUtil.getField(compLayout, "mSwitchMinWidth"), ValueUtil.dpToPixelInt(150, res));
            assertEquals((Integer)TestUtil.getField(compLayout,"mSwitchMinWidth"),(Integer)TestUtil.getField(parsedLayout,"mSwitchMinWidth"));

            // android:switchPadding
            assertEquals((Integer)TestUtil.getField(compLayout,"mSwitchPadding"),ValueUtil.dpToPixelInt(30,res));
            assertEquals((Integer)TestUtil.getField(compLayout,"mSwitchPadding"),(Integer)TestUtil.getField(parsedLayout,"mSwitchPadding"));

            // android:switchTextAppearance
            // No tenemos una forma de testear "switchTextAppearanceLarge" de forma directa, una forma es testear una de las propiedades que impone, ej el tamaño del texto
            Paint compTextPaint = (Paint)TestUtil.getField(compLayout,"mTextPaint");
            Paint parsedTextPaint = (Paint)TestUtil.getField(parsedLayout,"mTextPaint");
            assertEquals((Float)TestUtil.callMethod(compTextPaint,null,Paint.class,"getTextSize",null),(Float)TestUtil.callMethod(parsedTextPaint,null,Paint.class,"getTextSize",null));

            assertEquals(compLayout.getTextOff(),"NORL");
            assertEquals(compLayout.getTextOff(),parsedLayout.getTextOff());

            assertEquals(compLayout.getTextOn(),"YESRL");
            assertEquals(compLayout.getTextOn(),parsedLayout.getTextOn());

            // Test android:textStyle y android:typeface

            int NORMAL = 0, BOLD = 1, ITALIC = 2;

            Typeface compTf = compLayout.getTypeface();
            Typeface parsedTf = parsedLayout.getTypeface();
            assertEquals(compTf.getStyle(),BOLD | ITALIC);
            assertEquals(compTf.getStyle(),parsedTf.getStyle());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                assertEquals((Integer)TestUtil.getField(compTf,"native_instance"),(Integer)TestUtil.getField(parsedTf,"native_instance"));
            else // A partir de Lollipop (level 21) es un long
                assertEquals((Long)TestUtil.getField(compTf,"native_instance"),(Long)TestUtil.getField(parsedTf,"native_instance"));

            // Test android:thumb
            assertNotNull((StateListDrawable) TestUtil.getField(compLayout,"mThumbDrawable"));
            assertEquals((StateListDrawable) TestUtil.getField(parsedLayout,"mThumbDrawable"),(StateListDrawable) TestUtil.getField(parsedLayout,"mThumbDrawable"));

            // Test android:mThumbTextPadding
            assertEquals((Integer) TestUtil.getField(compLayout, "mThumbTextPadding"), ValueUtil.dpToPixelInt(20, res));
            assertEquals((Integer)TestUtil.getField(compLayout,"mThumbTextPadding"),(Integer)TestUtil.getField(parsedLayout,"mThumbTextPadding"));

            // Test android:track
            assertNotNull((StateListDrawable) TestUtil.getField(compLayout,"mTrackDrawable"));
            assertEquals((StateListDrawable) TestUtil.getField(parsedLayout,"mTrackDrawable"),(StateListDrawable) TestUtil.getField(parsedLayout,"mTrackDrawable"));

        }

        childCount++;

        // ToggleButton Tests
        // Nota: ToggleButton ha sido reemplazado totalmente por Switch, lo implementamos para los despistados
        {
            final ToggleButton compLayout = (ToggleButton) comp.getChildAt(childCount);
            final ToggleButton parsedLayout = (ToggleButton) parsed.getChildAt(childCount);

            assertEquals((Float)TestUtil.getField(compLayout,"mDisabledAlpha"),0.6f);
            assertEquals((Float)TestUtil.getField(compLayout,"mDisabledAlpha"),(Float)TestUtil.getField(parsedLayout,"mDisabledAlpha"));

            assertEquals(compLayout.getTextOff(),"NORL");
            assertEquals(compLayout.getTextOff(),parsedLayout.getTextOff());

            assertEquals(compLayout.getTextOn(),"YESRL");
            assertEquals(compLayout.getTextOn(),parsedLayout.getTextOn());
        }

        childCount++;

        // CheckedTextView Tests
        {
            final CheckedTextView compLayout = (CheckedTextView) comp.getChildAt(childCount);
            final CheckedTextView parsedLayout = (CheckedTextView) parsed.getChildAt(childCount);

            assertNotNull((StateListDrawable) TestUtil.getField(compLayout,"mCheckMarkDrawable"));
            assertEquals((StateListDrawable) TestUtil.getField(parsedLayout,"mCheckMarkDrawable"),(StateListDrawable) TestUtil.getField(parsedLayout,"mCheckMarkDrawable"));

            assertTrue(compLayout.isChecked());
            assertEquals(compLayout.isChecked(),parsedLayout.isChecked());
        }

        childCount++;

        // Chronometer Tests
        {
            final Chronometer compLayout = (Chronometer) comp.getChildAt(childCount);
            final Chronometer parsedLayout = (Chronometer) parsed.getChildAt(childCount);

            assertEquals(compLayout.getFormat(),"Time: %s");
            assertEquals(compLayout.getFormat(),parsedLayout.getFormat());
        }

        childCount++;

        // EditText Tests
        // No tiene atributos propios pero nos interesa probar si funciona visualmente inputType
        {
            final EditText compLayout = (EditText) comp.getChildAt(childCount);
            final EditText parsedLayout = (EditText) parsed.getChildAt(childCount);

            assertEquals(compLayout.getImeActionId(),0x00000002);
            assertEquals(compLayout.getImeActionId(),parsedLayout.getImeActionId());

            assertEquals(compLayout.getImeActionLabel(),"Go Next");
            assertEquals(compLayout.getImeActionLabel(),parsedLayout.getImeActionLabel());

            assertEquals(compLayout.getImeOptions(),EditorInfo.IME_ACTION_NEXT);
            assertEquals(compLayout.getImeOptions(),parsedLayout.getImeOptions());

            assertEquals(compLayout.getInputType(), InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            assertEquals(compLayout.getInputType(), parsedLayout.getInputType());

            assertFalse(compLayout.isTextSelectable());
            assertEquals(compLayout.isTextSelectable(), parsedLayout.isTextSelectable());
        }


        childCount++;

        // AutoCompleteTextView Tests
        {
            final AutoCompleteTextView compLayout = (AutoCompleteTextView) comp.getChildAt(childCount);
            final AutoCompleteTextView parsedLayout = (AutoCompleteTextView) parsed.getChildAt(childCount);

            // android:completionHint
            assertEquals((CharSequence)TestUtil.getField(compLayout,"mHintText"),"Sports suggested");
            assertEquals((CharSequence) TestUtil.getField(parsedLayout,"mHintText"),(CharSequence) TestUtil.getField(parsedLayout,"mHintText"));

            // android:completionHintView
            assertPositive((Integer) TestUtil.getField(compLayout,"mHintResource"));
            assertEquals((Integer) TestUtil.getField(parsedLayout,"mHintResource"),(Integer) TestUtil.getField(parsedLayout,"mHintResource"));

            assertEquals(compLayout.getDropDownAnchor(), res.getIdentifier("id/anchorOfAutoCompleteTextViewDropDownId",null,ctx.getPackageName()));
            assertEquals(compLayout.getDropDownAnchor(),parsedLayout.getDropDownAnchor());

            assertEquals(compLayout.getDropDownHeight(),ValueUtil.dpToPixelInt(150,res));
            assertEquals(compLayout.getDropDownHeight(),parsedLayout.getDropDownHeight());

            assertEquals(compLayout.getDropDownHorizontalOffset(),ValueUtil.dpToPixelInt(10,res));
            assertEquals(compLayout.getDropDownHorizontalOffset(),parsedLayout.getDropDownHorizontalOffset());

            assertNotNull((StateListDrawable)TestUtil.getField(compLayout, new Class[]{AutoCompleteTextView.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownListHighlight"}));
            assertEquals((StateListDrawable)TestUtil.getField(compLayout, new Class[]{AutoCompleteTextView.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownListHighlight"}), (StateListDrawable)TestUtil.getField(parsedLayout, new Class[]{AutoCompleteTextView.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownListHighlight"}));

            assertEquals(compLayout.getDropDownVerticalOffset(),ValueUtil.dpToPixelInt(5,res));
            assertEquals(compLayout.getDropDownVerticalOffset(),parsedLayout.getDropDownVerticalOffset());

            assertEquals(compLayout.getDropDownWidth(),ValueUtil.dpToPixelInt(300,res));
            assertEquals(compLayout.getDropDownWidth(),parsedLayout.getDropDownWidth());
        }

        childCount++;

        // TextView used as anchor of AutoCompleteTextView Suggest Drop Down (upper View)
        {
            final TextView compLayout = (TextView) comp.getChildAt(childCount);
            final TextView parsedLayout = (TextView) parsed.getChildAt(childCount);

            assertEquals(compLayout.getText(),"Anchor of AutoCompleteTextView Suggest Drop Down");
            assertEquals(compLayout.getText(),parsedLayout.getText());
        }


        childCount++;

        // Test AdapterViewAnimator y AdapterViewFlipper
        {
            final AdapterViewFlipper compLayout = (AdapterViewFlipper) comp.getChildAt(childCount);
            final AdapterViewFlipper parsedLayout = (AdapterViewFlipper) parsed.getChildAt(childCount);

            // AdapterViewAnimator

            assertTrue((Boolean)TestUtil.getField(compLayout,AdapterViewAnimator.class,"mAnimateFirstTime"));
            assertEquals((Boolean) TestUtil.getField(compLayout,AdapterViewAnimator.class,"mAnimateFirstTime"), (Boolean) TestUtil.getField(parsedLayout,AdapterViewAnimator.class,"mAnimateFirstTime"));

            assertNotNull(compLayout.getInAnimation());
            assertEquals(compLayout.getInAnimation(),parsedLayout.getInAnimation());

            assertTrue((Boolean)TestUtil.getField(compLayout,AdapterViewAnimator.class,"mLoopViews"));
            assertEquals((Boolean)TestUtil.getField(compLayout,AdapterViewAnimator.class,"mLoopViews"),(Boolean)TestUtil.getField(parsedLayout,AdapterViewAnimator.class,"mLoopViews"));

            assertNotNull(compLayout.getOutAnimation());
            assertEquals(compLayout.getOutAnimation(),parsedLayout.getOutAnimation());

            // AdapterViewFlipper
            assertTrue(compLayout.isAutoStart());
            assertEquals(compLayout.isAutoStart(),parsedLayout.isAutoStart());

            // android:flipInterval  (getFlipInterval es Level 16)
            assertEquals((Integer)TestUtil.getField(compLayout,"mFlipInterval"),2000);
            assertEquals((Integer)TestUtil.getField(compLayout,"mFlipInterval"),(Integer)TestUtil.getField(parsedLayout,"mFlipInterval"));
        }

        childCount++;

        // Test ViewGroup Attribs
        {
            final LinearLayout compLayout = (LinearLayout) comp.getChildAt(childCount);
            final LinearLayout parsedLayout = (LinearLayout) parsed.getChildAt(childCount);

            assertTrue(compLayout.addStatesFromChildren());
            assertEquals(compLayout.addStatesFromChildren(), parsedLayout.addStatesFromChildren());
            assertFalse(compLayout.isAlwaysDrawnWithCacheEnabled());
            assertEquals(compLayout.isAlwaysDrawnWithCacheEnabled(), parsedLayout.isAlwaysDrawnWithCacheEnabled());
            // Test de android:animateLayoutChanges
            // Si animateLayoutChanges="false" getLayoutTransition() devuelve null por lo que el chequear a null es suficiente test
            assertNotNull(compLayout.getLayoutTransition());
            assertNotNull(parsedLayout.getLayoutTransition());

            assertFalse(compLayout.isAnimationCacheEnabled());
            assertEquals(compLayout.isAnimationCacheEnabled(), parsedLayout.isAnimationCacheEnabled());
            // Tests de android:clipChildren (el método get es Level 18)
            assertFalse(((Integer) TestUtil.getField(compLayout, ViewGroup.class, "mGroupFlags") & 0x1) == 0x1); // FLAG_CLIP_CHILDREN = 0x1
            assertEquals( ((int)(Integer)TestUtil.getField(compLayout, ViewGroup.class, "mGroupFlags") & 0x1) == 0x1, ((int)(Integer)TestUtil.getField(parsedLayout, ViewGroup.class, "mGroupFlags") & 0x1) == 0x1 );
            // Tests de android:clipToPadding
            assertFalse(((Integer)TestUtil.getField(compLayout, ViewGroup.class, "mGroupFlags") & 0x2) == 0x2); // FLAG_CLIP_TO_PADDING = 0x2
            assertEquals(((int) (Integer)TestUtil.getField(compLayout, ViewGroup.class, "mGroupFlags") & 0x2) == 0x2, ((int) (Integer)TestUtil.getField(parsedLayout, ViewGroup.class, "mGroupFlags") & 0x2) == 0x2);
            assertEquals(compLayout.getDescendantFocusability(), ViewGroup.FOCUS_AFTER_DESCENDANTS);
            assertEquals(compLayout.getDescendantFocusability(), parsedLayout.getDescendantFocusability());
            assertTrue((compLayout.getLayoutAnimation().getDelay() - 1.0f * 10 / 100) < 0.00001); // Testeamos el delay porque testear la igualdad del LayoutAnimationController es un rollo
            assertEquals(compLayout.getLayoutAnimation().getDelay(), parsedLayout.getLayoutAnimation().getDelay());
            assertEquals(compLayout.getPersistentDrawingCache(), parsedLayout.getPersistentDrawingCache());
            assertTrue(compLayout.isMotionEventSplittingEnabled());
            assertEquals(compLayout.isMotionEventSplittingEnabled(), parsedLayout.isMotionEventSplittingEnabled());

            {
                TextView compTextView = (TextView) compLayout.getChildAt(0);
                TextView parsedTextView = (TextView) parsedLayout.getChildAt(0);

                assertEquals(compTextView.getText(),"Test ViewGroup Attribs");
                assertEquals(compTextView.getText(),parsedTextView.getText());
            }

            // Testing ViewGroup.LayoutParams
            ViewGroup.LayoutParams a_params = compLayout.getLayoutParams();
            ViewGroup.LayoutParams b_params = parsedLayout.getLayoutParams();

            assertEquals(a_params.height, ViewGroup.LayoutParams.WRAP_CONTENT);
            assertEquals(a_params.height, b_params.height);
            assertEquals(a_params.width, ViewGroup.LayoutParams.MATCH_PARENT);
            assertEquals(a_params.width, b_params.width);

            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    assertEquals(compLayout.getWidth(), parsedLayout.getWidth());
                    assertEquals(compLayout.getHeight(), parsedLayout.getHeight());
                }
            });

        }

        childCount++;

        // Test ViewGroup.MarginLayoutParams
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);
            for(int i = 0; i < 2; i++)
            {
                final TextView compTextView = (TextView) compLinLayout.getChildAt(0);
                final TextView parsedTextView = (TextView) parsedLinLayout.getChildAt(0);

                assertEquals(compTextView.getText(), parsedTextView.getText());
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());

                ViewGroup.MarginLayoutParams a_params = (ViewGroup.MarginLayoutParams)compTextView.getLayoutParams();
                ViewGroup.MarginLayoutParams b_params = (ViewGroup.MarginLayoutParams)parsedTextView.getLayoutParams();

                assertEquals(a_params.topMargin,ValueUtil.dpToPixelInt(15, res));
                assertEquals(a_params.topMargin,b_params.topMargin);
                assertEquals(a_params.leftMargin,ValueUtil.dpToPixelInt(10, res));
                assertEquals(a_params.leftMargin,b_params.leftMargin);
                assertEquals(a_params.bottomMargin,ValueUtil.dpToPixelInt(5, res));
                assertEquals(a_params.bottomMargin,b_params.bottomMargin);
                assertEquals(a_params.rightMargin,ValueUtil.dpToPixelInt(1, res));
                assertEquals(a_params.rightMargin,b_params.rightMargin);
            }
        }

        childCount++;

        // Test AbsListView
        {
            ListView compLayout = (ListView) comp.getChildAt(childCount);
            ListView parsedLayout = (ListView) parsed.getChildAt(childCount);

            assertEquals(compLayout.getCacheColorHint(),0xffff0000);
            assertEquals(compLayout.getCacheColorHint(), parsedLayout.getCacheColorHint());
            assertEquals(compLayout.getChoiceMode(), AbsListView.CHOICE_MODE_MULTIPLE);
            assertEquals(compLayout.getChoiceMode(), parsedLayout.getChoiceMode());
            // No podemos testear android:drawSelectorOnTop porque no hay un isDrawSelectorOnTop
            assertFalse(compLayout.isFastScrollEnabled()); // Preferiría testear el true pero no se porqué razón se ignora el true
            assertEquals(compLayout.isFastScrollEnabled(), parsedLayout.isFastScrollEnabled());
            // android:listSelector
            assertEquals(((ColorDrawable)compLayout.getSelector()).getColor(), 0x6600ff00);
            assertEquals(compLayout.getSelector(), parsedLayout.getSelector());
            assertFalse(compLayout.isScrollingCacheEnabled());
            assertEquals(compLayout.isScrollingCacheEnabled(), parsedLayout.isScrollingCacheEnabled());
            assertFalse(compLayout.isSmoothScrollbarEnabled());
            assertEquals(compLayout.isSmoothScrollbarEnabled(), parsedLayout.isSmoothScrollbarEnabled());
            assertTrue(compLayout.isStackFromBottom());
            assertEquals(compLayout.isStackFromBottom(), parsedLayout.isStackFromBottom());
            assertTrue(compLayout.isTextFilterEnabled());
            assertEquals(compLayout.isTextFilterEnabled(), parsedLayout.isTextFilterEnabled());
            assertEquals(compLayout.getTranscriptMode(), AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            assertEquals(compLayout.getTranscriptMode(), parsedLayout.getTranscriptMode());

        }

        childCount++;

        // Test GridView
        {
            final GridView compLayout = (GridView) comp.getChildAt(childCount);
            final GridView parsedLayout = (GridView) parsed.getChildAt(childCount);

            // Tests android:columnWidth (getColumnWidth es Level 16):
            // En teoría existe el atributo mColumnWidth pero el valor final puede no coincidir con el columnWidth especificado
            // porque es corregido dinámicamente
            assertEquals((Integer)TestUtil.getField(compLayout, "mRequestedColumnWidth"),ValueUtil.dpToPixelInt(30, res));
            assertEquals((Integer)TestUtil.getField(compLayout, "mRequestedColumnWidth"),(Integer)TestUtil.getField(parsedLayout, "mRequestedColumnWidth"));

            // Tests android:gravity (getGravity es Level 16)
            assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"), Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"),(Integer)TestUtil.getField(parsedLayout, "mGravity"));
                }
            });

            // Tests android:horizontalSpacing (getHorizontalSpacing es Level 16):
            assertEquals((Integer)TestUtil.getField(compLayout, "mHorizontalSpacing"),ValueUtil.dpToPixelInt(5, res));
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, "mHorizontalSpacing"),(Integer)TestUtil.getField(parsedLayout, "mHorizontalSpacing"));
                }
            });
            assertEquals(compLayout.getNumColumns(), 3);
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // Usamos un OnLayoutChangeListener porque el setNumColumns define un atributo "request" que no es el atributo
                    // donde va el numColumns definitivo el cual se calcula al hacer el layout
                    assertEquals(compLayout.getNumColumns(),parsedLayout.getNumColumns());
                }
            });
            assertEquals(compLayout.getStretchMode(),GridView.STRETCH_COLUMN_WIDTH); // Es el modo por defecto pero los demás modos en nuestro test se ven muy mal
            assertEquals(compLayout.getStretchMode(), parsedLayout.getStretchMode());

            // Tests android:verticalSpacing (getVerticalSpacing es Level 16):
            assertEquals((Integer)TestUtil.getField(compLayout, "mVerticalSpacing"),ValueUtil.dpToPixelInt(5, res));
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, "mVerticalSpacing"),(Integer)TestUtil.getField(parsedLayout, "mVerticalSpacing"));
                }
            });
        }

        childCount++;

        // Space for page scrolling
        {
            final TextView compTextView = (TextView) comp.getChildAt(childCount);
            final TextView parsedTextView = (TextView) parsed.getChildAt(childCount);
            assertEquals(compTextView.getText(), "(space for page scrolling)");
            assertEquals(compTextView.getText(), parsedTextView.getText());
        }


        childCount++;

        // Test ListView
        {
            final ListView compLayout = (ListView) comp.getChildAt(childCount);
            final ListView parsedLayout = (ListView) parsed.getChildAt(childCount);

            // Test android:divider
            // Test visual: líneas rojas separadoras de items
            assertEqualsStrokeWidth((GradientDrawable) compLayout.getDivider(), ValueUtil.dpToPixelInt(0.9f, res));
            assertEquals((GradientDrawable) compLayout.getDivider(),(GradientDrawable) parsedLayout.getDivider());

            assertEquals(compLayout.getDividerHeight(),ValueUtil.dpToPixelInt(2, res));
            assertEquals(compLayout.getDividerHeight(),parsedLayout.getDividerHeight());
            // Test android:footerDividersEnabled (areFooterDividersEnabled es Level 19)
            assertFalse((Boolean)TestUtil.getField(compLayout, "mFooterDividersEnabled"));
            assertEquals((Boolean)TestUtil.getField(compLayout, "mFooterDividersEnabled"), (Boolean)TestUtil.getField(parsedLayout, "mFooterDividersEnabled"));
            // Test android:headerDividersEnabled (areHeaderDividersEnabled es Level 19)
            assertFalse((Boolean)TestUtil.getField(compLayout, "mHeaderDividersEnabled"));
            assertEquals((Boolean)TestUtil.getField(compLayout, "mHeaderDividersEnabled"),(Boolean)TestUtil.getField(parsedLayout, "mHeaderDividersEnabled"));

        }

        childCount++;

        // Test ExpandableListView
        {
            final ExpandableListView compLayout = (ExpandableListView) comp.getChildAt(childCount);
            final ExpandableListView parsedLayout = (ExpandableListView) parsed.getChildAt(childCount);

            // Test android:childDivider, no hay método get
            // Test visual: líneas rojas separadoras de items
            assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compLayout, "mChildDivider"), ValueUtil.dpToPixelInt(0.9f, res));
            assertEquals((GradientDrawable) TestUtil.getField(compLayout, "mChildDivider"), (GradientDrawable) TestUtil.getField(parsedLayout, "mChildDivider"));

            // Test android:childIndicator, no hay método get, si no se define devuelve null
            assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compLayout, "mChildIndicator"), ValueUtil.dpToPixelInt(2f, res));
            assertEquals((GradientDrawable) TestUtil.getField(compLayout, "mChildIndicator"), (GradientDrawable) TestUtil.getField(parsedLayout, "mChildIndicator"));

            // Test android:childIndicatorLeft, no hay método get
            // No podemos testearlo porque por ej en 4.4.1 tras definir mChildIndicatorLeft y mChildIndicatorright
            // se llama a un método que los cambia de nuevo de una forma inexplicable, en 4.0.3 no hay tal llamada
            //assertPositive((Integer)TestUtil.getField(compLayout,"mChildIndicatorLeft"));
            //assertEquals((Integer)getField(compLayout,"mChildIndicatorLeft"),(Integer)getField(parsedLayout,"mChildIndicatorLeft"));
            // No testeamos android:childIndicatorRight pues tenemos idéntico problema que childIndicatorLeft

            // Test android:groupIndicator, no hay método get
            assertNotNull((StateListDrawable)TestUtil.getField(compLayout, "mGroupIndicator"));
            assertEquals((StateListDrawable) TestUtil.getField(parsedLayout, "mGroupIndicator"), (StateListDrawable) TestUtil.getField(parsedLayout, "mGroupIndicator"));

            // No testeamos android:indicatorLeft ni indicatorRight porque les pasa igual que a childIndicatorLeft
            //assertPositive((Integer)getField(compLayout,"mIndicatorLeft"));
        }

        childCount++;

        // Test AbsSpinner (entries sólo) y Gallery
        {
            final Gallery compLayout = (Gallery) comp.getChildAt(childCount);
            final Gallery parsedLayout = (Gallery) parsed.getChildAt(childCount);
            assertEquals((Integer)TestUtil.getField(compLayout, "mAnimationDuration"), 100);
            assertEquals((Integer)TestUtil.getField(compLayout, "mAnimationDuration"),(Integer)TestUtil.getField(parsedLayout, "mAnimationDuration"));
            assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"), Gravity.CENTER_VERTICAL);
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"),(Integer)TestUtil.getField(parsedLayout, "mGravity"));
                }
            });
            assertEquals((Integer)TestUtil.getField(compLayout, "mSpacing"),ValueUtil.dpToPixelInt(50, res));
            assertEquals((Integer)TestUtil.getField(compLayout, "mSpacing"),(Integer)TestUtil.getField(parsedLayout, "mSpacing"));
            assertEquals((Float)TestUtil.getField(compLayout, "mUnselectedAlpha"), 0.6f);
            assertEquals((Float)TestUtil.getField(compLayout, "mUnselectedAlpha"),(Float)TestUtil.getField(parsedLayout, "mUnselectedAlpha"));
        }

        childCount++;

        // Test Spinner (dropdown)
        {
            final Spinner compLayout = (Spinner) comp.getChildAt(childCount);
            final Spinner parsedLayout = (Spinner) parsed.getChildAt(childCount);

            // Tests android:dropDownHorizontalOffset
            // Este atributo es difícil de testear pues se solapa con paddingLeft (definido en el style en este ejemplo) el cual suele imponer su valor
            // http://stackoverflow.com/questions/21503142/android-spinner-dropdownhorizontaloffset-not-functioning-but-dropdownverticleoff
            assertEquals((Integer)TestUtil.getField(compLayout, new Class[]{Spinner.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownHorizontalOffset"}), ValueUtil.dpToPixelInt(21,res));
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, new Class[]{Spinner.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownHorizontalOffset"}), (Integer)TestUtil.getField(parsedLayout, new Class[]{Spinner.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownHorizontalOffset"}));
                }
            });

            // Tests android:dropDownVerticalOffset
            assertPositive((Integer)TestUtil.getField(compLayout, new Class[]{Spinner.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownVerticalOffset"}));
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, new Class[]{Spinner.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownVerticalOffset"}), (Integer)TestUtil.getField(parsedLayout, new Class[]{Spinner.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownVerticalOffset"}));
                }
            });

            // Tests android:dropDownWidth ( getDropDownWidth() es Level 16)
            assertEquals((Integer)TestUtil.getField(compLayout, "mDropDownWidth"),ValueUtil.dpToPixelInt(200, res));
            assertEquals((Integer)TestUtil.getField(compLayout, "mDropDownWidth"),(Integer)TestUtil.getField(parsedLayout, "mDropDownWidth"));

            // Tests android:gravity (no get en Level 15)
            assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"), Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"),(Integer)TestUtil.getField(parsedLayout, "mGravity"));
                }
            });

            // Tests android:popupBackground
            assertEquals(((ColorDrawable)TestUtil.getField(compLayout, new Class[]{Spinner.class, ListPopupWindow.class, PopupWindow.class}, new String[]{"mPopup", "mPopup", "mBackground"})).getColor(), 0xffeeee55);
            assertEquals((ColorDrawable)TestUtil.getField(compLayout, new Class[]{Spinner.class, ListPopupWindow.class, PopupWindow.class}, new String[]{"mPopup", "mPopup", "mBackground"}),(ColorDrawable)TestUtil.getField(parsedLayout, new Class[]{Spinner.class, ListPopupWindow.class, PopupWindow.class}, new String[]{"mPopup", "mPopup", "mBackground"}));

            // Test style (necesario testear porque se construye de forma especial)

            assertEquals(compLayout.getPaddingLeft(),ValueUtil.dpToPixelInt(21, res));
            assertEquals(compLayout.getPaddingLeft(),parsedLayout.getPaddingLeft());
            assertEquals(compLayout.getPaddingRight(),ValueUtil.dpToPixelInt(21, res));
            assertEquals(compLayout.getPaddingRight(),parsedLayout.getPaddingRight());

        }

        childCount++;

        // Test Spinner (dialog)
        {
            final Spinner compLayout = (Spinner) comp.getChildAt(childCount);
            final Spinner parsedLayout = (Spinner) parsed.getChildAt(childCount);

            assertEquals(compLayout.getPrompt(), "Sport List");
            assertEquals(compLayout.getPrompt(), parsedLayout.getPrompt());
        }

        childCount++;

        // Space for page scrolling
        {
            final TextView compTextView = (TextView) comp.getChildAt(childCount);
            final TextView parsedTextView = (TextView) parsed.getChildAt(childCount);
            assertEquals(compTextView.getText(), "(space for page scrolling)");
            assertEquals(compTextView.getText(), parsedTextView.getText());
        }




//         System.out.println("\n\n\nDEFAULT VALUE: " + compLayout.getColumnCount() + " " + parsedLayout.getColumnCount());
        //System.out.println("\n\n\n");
    }


}
